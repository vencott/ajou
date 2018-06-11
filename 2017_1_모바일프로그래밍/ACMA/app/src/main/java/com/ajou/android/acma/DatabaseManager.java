package com.ajou.android.acma;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static int totalLectureNum = 6;
    public static int majorLectureNum = 3;
    public static int refinementLectureNum = 3;
}
