package com.f041.team4.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.f041.team4.R;
import com.f041.team4.data.Account;
import com.f041.team4.data.Message;
import com.f041.team4.global.Algorithm;
import com.f041.team4.global.Constants;
import com.f041.team4.global.CryptoManager;
import com.f041.team4.global.FirebaseManager;
import com.f041.team4.global.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SessionActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String encryptedSessionKey;
    String initiator;
    String receiver;
    boolean amIReceiver;
    Adapter adapter;
    LinearLayout llInput;
    EditText etMessage;
    TextView btnSend;

    List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        encryptedSessionKey = getIntent().getStringExtra("encryptedSessionKey");
        initiator = getIntent().getStringExtra("initiator");
        receiver = getIntent().getStringExtra("receiver");
        amIReceiver = receiver.equals(Account.getInstance().getName());

        messageList = new ArrayList<>();
        adapter = new Adapter(messageList);

        recyclerView = findViewById(R.id.view_recycler);
        recyclerView.setAdapter(adapter);

        llInput = findViewById(R.id.ll_input);
        etMessage = llInput.findViewById(R.id.et_message);
        btnSend = llInput.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeMessage(etMessage.getText().toString().trim());
            }
        });

        FirebaseManager.getInstance().messagesRef.whereEqualTo("encryptedSessionKey", encryptedSessionKey).orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    return;
                messageList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Message message = doc.toObject(Message.class);
                    messageList.add(message);
                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
                llInput.setVisibility(adapter.getItemCount() >= 2 ? View.VISIBLE : View.GONE);
            }
        });
    }

    void writeMessage(String text) {
        String encrypted = CryptoManager.encrypt(Algorithm.AES, text, SessionManager.getInstance().searchSessionKey(encryptedSessionKey));
        Message message = new Message(Account.getInstance().getName(), amIReceiver ? initiator : receiver, encryptedSessionKey, encrypted, System.currentTimeMillis());
        FirebaseManager.getInstance().messagesRef.add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                etMessage.setText("");
            }
        });
    }

    void viewMessage(Message message) {
        if (isInitMessage(message.getMessage()) && isMyMessage(message.getFrom()))
            return;
        if (isReplyMessage(message.getMessage()))
            return;

        Intent intent = new Intent(SessionActivity.this, MessageActivity.class);
        intent.putExtra("isInitial", !isMyMessage(message.getFrom()) && isInitMessage(message.getMessage()));
        intent.putExtra("message", message.getMessage());
        intent.putExtra("encryptedSessionKey", message.getEncryptedSessionKey());
        intent.putExtra("from", message.getFrom());
        intent.putExtra("to", message.getTo());
        startActivity(intent);
    }

    boolean isMyMessage(String name) {
        return name.equals(Account.getInstance().getName());
    }

    boolean isInitMessage(String message) {
        return message.equals(Constants.INIT_MESSAGE);
    }

    boolean isReplyMessage(String message) {
        return message.equals(Constants.REPLY_MESSAGE);
    }


    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Message> messageList;

        public Adapter(List<Message> messageList) {
            this.messageList = messageList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Message message = messageList.get(i);
            viewHolder.setItem(message);
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Message message;

        TextView tvName;
        TextView tvMessage;
        View leftBlank;
        View rightBlank;
        View messageBlank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMessage = itemView.findViewById(R.id.tv_message);
            leftBlank = itemView.findViewById(R.id.blank_left);
            rightBlank = itemView.findViewById(R.id.blank_right);
            messageBlank = itemView.findViewById(R.id.blank_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (message != null)
                        viewMessage(message);
                }
            });
        }

        public void setItem(Message message) {
            this.message = message;
            setUi();
        }

        public void setUi() {
            if (isMyMessage(message.getFrom())) {
                rightBlank.setVisibility(View.GONE);
                leftBlank.setVisibility(View.VISIBLE);
                tvName.setGravity(Gravity.RIGHT);
                messageBlank.setVisibility(View.VISIBLE);
            } else {
                rightBlank.setVisibility(View.VISIBLE);
                leftBlank.setVisibility(View.GONE);
                tvName.setGravity(Gravity.LEFT);
                messageBlank.setVisibility(View.GONE);
            }

            tvName.setText(message.getFrom());
            tvMessage.setText(message.getMessage());
        }
    }
}

