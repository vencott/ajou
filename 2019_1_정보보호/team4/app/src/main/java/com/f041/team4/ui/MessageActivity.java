package com.f041.team4.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.f041.team4.R;
import com.f041.team4.data.Account;
import com.f041.team4.data.Message;
import com.f041.team4.data.SessionPair;
import com.f041.team4.global.Algorithm;
import com.f041.team4.global.Constants;
import com.f041.team4.global.CryptoManager;
import com.f041.team4.global.FirebaseManager;
import com.f041.team4.global.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;

public class MessageActivity extends AppCompatActivity {
    boolean isInitialMessage;
    String encrypted;
    String encryptedSessionKey;
    String from;
    String to;

    TextView tvEncrypted;
    TextView tvDecrypted;
    TextView btnDecrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        isInitialMessage = getIntent().getBooleanExtra("isInitial", false);
        encrypted = getIntent().getStringExtra("message");
        encryptedSessionKey = getIntent().getStringExtra("encryptedSessionKey");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        tvEncrypted = findViewById(R.id.tv_encrypted);
        tvDecrypted = findViewById(R.id.tv_decrypted);
        btnDecrypt = findViewById(R.id.btn_decrypt);

        setUi();
    }

    void setUi() {
        if (isInitialMessage) {
            encrypted += "\n당신의 Private Key로 이를 복호화하려면 아래 복호화 버튼을 눌러주세요.";
            tvDecrypted.setVisibility(View.GONE);
            btnDecrypt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    decryptSessionKey();
                }
            });
        } else {
            tvDecrypted.setVisibility(View.VISIBLE);
            btnDecrypt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    decrypt();
                }
            });
        }

        tvEncrypted.setText(encrypted);
    }

    void decryptSessionKey() {
        Key sessionKey = CryptoManager.decryptSessionKey(Algorithm.RSA, encryptedSessionKey, Account.getInstance().getPrivateKey());
        SessionPair sessionPair = new SessionPair(sessionKey, encryptedSessionKey);
        SessionManager.getInstance().add(sessionPair);
        sendReplyMessage();
    }

    void sendReplyMessage() {
        Message message = new Message(to, from, encryptedSessionKey, Constants.REPLY_MESSAGE, System.currentTimeMillis());
        FirebaseManager.getInstance().messagesRef.add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                updateSessionEnabled();
            }
        });
    }

    void updateSessionEnabled() {
        FirebaseManager.getInstance().sessionsRef.whereEqualTo("encryptedSessionKey", encryptedSessionKey).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                FirebaseManager.getInstance().sessionsRef.document(id).update("enabled", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });
    }

    void decrypt() {
        Key sessionKey = SessionManager.getInstance().searchSessionKey(encryptedSessionKey);
        String decrypted = CryptoManager.decrypt(Algorithm.AES, encrypted, sessionKey);
        tvDecrypted.setText(decrypted);
    }
}
