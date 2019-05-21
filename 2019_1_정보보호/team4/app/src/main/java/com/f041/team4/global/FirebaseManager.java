package com.f041.team4.global;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {
    private static FirebaseManager instance = new FirebaseManager();

    public FirebaseFirestore db;
    public CollectionReference usersRef;
    public CollectionReference sessionsRef;

    private FirebaseManager() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        sessionsRef = db.collection("sessions");
    }

    public static FirebaseManager getInstance() {
        return instance;
    }
}
