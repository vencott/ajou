package com.f041.team4;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.f041.team4.data.Account;
import com.f041.team4.data.Message;
import com.f041.team4.data.Session;
import com.f041.team4.data.SessionPair;
import com.f041.team4.data.User;
import com.f041.team4.global.Algorithm;
import com.f041.team4.global.Constants;
import com.f041.team4.global.CryptoManager;
import com.f041.team4.global.FirebaseManager;
import com.f041.team4.global.KeyManager;
import com.f041.team4.global.SessionManager;
import com.f041.team4.ui.RegisterActivity;
import com.f041.team4.ui.SessionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Session> sessionList;
    Adapter adapter;

    RecyclerView recyclerView;
    FloatingActionButton mainFab, refreshFab, addFab;
    Animation fabOpen, fabClose;
    boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionList = new ArrayList<>();
        adapter = new Adapter(sessionList);

        recyclerView = findViewById(R.id.view_recycler);
        recyclerView.setAdapter(adapter);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        mainFab = findViewById(R.id.fab_main);
        refreshFab = findViewById(R.id.fab_refresh);
        addFab = findViewById(R.id.fab_add);
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
                toggleFab();
            }
        });
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                toggleFab();
            }
        });

        register();
    }

    void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent, Constants.REGISTER);
    }

    void showDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("세션 생성");
        ad.setMessage("세션을 연결할 상대방의 이름을 입력해주세요");

        final EditText et = new EditText(MainActivity.this);
        ad.setView(et);
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String with = et.getText().toString();
                startSession(with);
                dialog.dismiss();
            }
        });
        ad.show();
    }

    void startSession(String with) {
        FirebaseManager.getInstance().usersRef.whereEqualTo("name", with).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> users = queryDocumentSnapshots.toObjects(User.class);
                User receiver = users.get(0);
                final Session session = createNewSession(receiver);

                FirebaseManager.getInstance().sessionsRef.add(session).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        sendInitialMessage(session);
                    }
                });
            }
        });
    }

    void sendInitialMessage(Session session) {
        Message message = new Message(session.getInitiator(), session.getReceiver(), session.getEncryptedSessionKey(), Constants.INIT_MESSAGE, System.currentTimeMillis());
        FirebaseManager.getInstance().messagesRef.add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                refresh();
            }
        });
    }

    void openSession(Session session) {
        Intent intent = new Intent(MainActivity.this, SessionActivity.class);
        intent.putExtra("encryptedSessionKey", session.getEncryptedSessionKey());
        intent.putExtra("initiator", session.getInitiator());
        intent.putExtra("receiver", session.getReceiver());
        startActivityForResult(intent, Constants.SESSION);
    }

    Session createNewSession(User receiver) {
        Key sessionKey = KeyManager.createNewSessionKey(Algorithm.AES);
        PublicKey publicKeyOfReceiver = KeyManager.decodePublicKey(receiver.getEncodedPublicKey());
        String encryptedSessionKey = CryptoManager.encryptSessionKey(Algorithm.RSA, sessionKey, publicKeyOfReceiver);

        SessionManager.getInstance().add(new SessionPair(sessionKey, encryptedSessionKey));

        return new Session(Account.getInstance().getName(), receiver.getName(), encryptedSessionKey, false);
    }

    void refresh() {
        if (Account.getInstance().getName() == null)
            return;

        FirebaseManager.getInstance().sessionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    sessionList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Session session = document.toObject(Session.class);
                        if (Account.getInstance().getName().equals(session.getInitiator()) || Account.getInstance().getName().equals(session.getReceiver()))
                            sessionList.add(session);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void toggleFab() {
        if (isFabOpen) {
            mainFab.setImageResource(R.drawable.ic_plus);
            refreshFab.startAnimation(fabClose);
            addFab.startAnimation(fabClose);
            refreshFab.setClickable(false);
            addFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.setImageResource(R.drawable.ic_close);
            refreshFab.startAnimation(fabOpen);
            addFab.startAnimation(fabOpen);
            refreshFab.setClickable(true);
            addFab.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REGISTER:
                case Constants.SESSION:
                    refresh();
                    break;
            }
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Session> sessionList;

        Adapter(List<Session> sessionList) {
            this.sessionList = sessionList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_session, viewGroup, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Session session = sessionList.get(i);
            viewHolder.setItem(session);
        }

        @Override
        public int getItemCount() {
            return sessionList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Session session;

        TextView tvWith;
        TextView tvEnabled;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWith = itemView.findViewById(R.id.tv_with);
            tvEnabled = itemView.findViewById(R.id.tv_enabled);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openSession(session);
                }
            });
        }

        void setItem(Session session) {
            this.session = session;
            setUi();
        }

        void setUi() {
            String with = Account.getInstance().getName().equals(session.getReceiver()) ? session.getInitiator() : session.getReceiver();
            tvWith.setText(with + "님과의 Session");
            tvEnabled.setText(session.isEnabled() ? "세션키 교환 전" : "세션키 교환 완료");
            tvEnabled.setTextColor(session.isEnabled() ? Color.BLUE : Color.RED);
        }
    }
}
