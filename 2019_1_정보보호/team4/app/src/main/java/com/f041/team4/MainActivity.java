package com.f041.team4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Session> sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        FirebaseManager.getInstance().sessionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Session session = document.toObject(Session.class);
                        sessionList.clear();
                        if (Account.getInstance().getName().equals(session.getInitiator()) || Account.getInstance().getName().equals(session.getReceiver()))
                            sessionList.add(session);
                        // notifydatasetchanged
                    }
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
