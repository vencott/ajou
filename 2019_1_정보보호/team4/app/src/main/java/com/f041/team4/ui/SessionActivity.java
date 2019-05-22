package com.f041.team4.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.f041.team4.R;
import com.f041.team4.data.Account;
import com.f041.team4.data.Message;
import com.f041.team4.global.Algorithm;
import com.f041.team4.global.Constants;
import com.f041.team4.global.CryptoManager;
import com.f041.team4.global.FirebaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class SessionActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String encryptedSessionKey;
    String initiator;
    String receiver;
    boolean amIReceiver;
    Adapter adapter;

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

        refresh();
    }

    void decryptSessionKey() {
        Key sessionKey = CryptoManager.decryptSessionKey(Algorithm.RSA, encryptedSessionKey, Account.getInstance().getPrivateKey());

    }

    boolean isInitMessage(String message) {
        return message.equals(Constants.INIT_MESSAGE);
    }

    boolean isReplyMessage(String message) {
        return message.equals(Constants.REPLY_MESSAGE);
    }

    void refresh() {
        FirebaseManager.getInstance().messagesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    messageList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Message message = document.toObject(Message.class);
                        if (message.getEncryptedSessionKey().equals(encryptedSessionKey))
                            messageList.add(message);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
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
        }

        public void setItem(Message message) {
            this.message = message;
            setUi();
        }

        public void setUi() {
            boolean isMyMessage = message.getFrom().equals(Account.getInstance().getName());

            if (isMyMessage) {
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

