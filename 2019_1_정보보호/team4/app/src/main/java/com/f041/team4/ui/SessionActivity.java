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

import com.f041.team4.R;
import com.f041.team4.data.Account;
import com.f041.team4.data.Message;
import com.f041.team4.global.Algorithm;
import com.f041.team4.global.Constants;
import com.f041.team4.global.CryptoManager;
import com.f041.team4.global.FirebaseManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;
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

        FirebaseManager.getInstance().messagesRef.whereEqualTo("encryptedSessionKey", encryptedSessionKey).orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    return;
                messageList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Message message = doc.toObject(Message.class);
                    if (isInitMessage(message.getMessage()) && isMyMessage(message.getFrom()))
                        message.setMessage("상대방의 Public Key로 Session Key를 암호화하여 전송하였습니다.");
                    messageList.add(message);
                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

    void decryptSessionKey() {
        Key sessionKey = CryptoManager.decryptSessionKey(Algorithm.RSA, encryptedSessionKey, Account.getInstance().getPrivateKey());

    }

    void writeMessage() {
        Message message = new Message(Account.getInstance().getName(), amIReceiver ? initiator : receiver, encryptedSessionKey, messageList.size() + "", System.currentTimeMillis());
        FirebaseManager.getInstance().messagesRef.add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        });
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

