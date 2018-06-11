package com.ajou.android.acma;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class User {

    private String Major;
    private String Gender;
    private String Grade;
    private String Name;
    private long totalCredit;

    public User(String major, String gender, String grade, String name, long totalCredit) {
        Major = major;
        Gender = gender;
        Grade = grade;
        Name = name;
        this.totalCredit = totalCredit;
    }

    public long getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(long totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

