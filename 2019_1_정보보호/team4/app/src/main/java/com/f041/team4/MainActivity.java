package com.f041.team4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.f041.team4.data.Account;
import com.f041.team4.data.Session;
import com.f041.team4.data.SessionPair;
import com.f041.team4.data.User;
import com.f041.team4.global.Algorithm;
import com.f041.team4.global.CryptoManager;
import com.f041.team4.global.FirebaseManager;
import com.f041.team4.global.KeyManager;
import com.f041.team4.global.SessionManager;
import com.f041.team4.ui.RegisterActivity;
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

    RecyclerView recyclerView;
    Adapter adapter;

    private FloatingActionButton fab_main, fab_refresh, fab_add;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionList = new ArrayList<>();

        recyclerView = findViewById(R.id.view_recycler);
        adapter = new Adapter(sessionList);
        recyclerView.setAdapter(adapter);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        fab_main = findViewById(R.id.fab_main);
        fab_refresh = findViewById(R.id.fab_refresh);
        fab_add = findViewById(R.id.fab_add);


        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
                toggleFab();
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSession("jungmin");
                toggleFab();
            }
        });

        register();
    }

    void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    void startSession(String with) {
        FirebaseManager.getInstance().usersRef.whereEqualTo("name", with).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> users = queryDocumentSnapshots.toObjects(User.class);
                User receiver = users.get(0);

                FirebaseManager.getInstance().sessionsRef.add(createNewSession(receiver)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        refresh();
                    }
                });
            }
        });
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

    void acceptSession() {
        FirebaseManager.getInstance().sessionsRef.whereEqualTo("receiver", Account.getInstance().getName()).whereEqualTo("enabled", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Session> sessions = queryDocumentSnapshots.toObjects(Session.class);
                Session session = sessions.get(0);
                Key sessionKey = CryptoManager.decryptSessionKey(Algorithm.RSA, session.getEncryptedSessionKey(), Account.getInstance().getPrivateKey());
                SessionManager.getInstance().add(new SessionPair(sessionKey, session.getEncryptedSessionKey()));
                FirebaseManager.getInstance().sessionsRef.document(queryDocumentSnapshots.getDocuments().get(0).getId()).update("enabled", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        refresh();
                    }
                });
            }
        });
    }

    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.ic_plus);
            fab_refresh.startAnimation(fab_close);
            fab_add.startAnimation(fab_close);
            fab_refresh.setClickable(false);
            fab_add.setClickable(false);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.ic_close);
            fab_refresh.startAnimation(fab_open);
            fab_add.startAnimation(fab_open);
            fab_refresh.setClickable(true);
            fab_add.setClickable(true);
            isFabOpen = true;
        }
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvWith;
    TextView tvEnabled;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvWith = itemView.findViewById(R.id.tv_with);
        tvEnabled = itemView.findViewById(R.id.tv_enabled);
    }

    public void setUi(String name, boolean enable) {
        tvWith.setText(name + "님과의 Session");
        tvEnabled.setText(enable ? "연결됨" : "연결안됨");
        tvEnabled.setTextColor(enable ? Color.BLUE : Color.RED);
    }
}

class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Session> sessionList;

    public Adapter(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Session session = sessionList.get(i);
        String with = Account.getInstance().getName().equals(session.getReceiver()) ? session.getInitiator() : session.getReceiver();
        viewHolder.setUi(with, session.isEnabled());
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }
}


/*
세션에 들어갔을때
1. enabled
    - 세션의 encryptedSession을 SessionPairs에서 찾아 실제 세션 키 알아냄
2. disabled
    - initiater
        - 아직 상대방이 accept 안함 메시지
    - receiver
        - accept과정
*/
