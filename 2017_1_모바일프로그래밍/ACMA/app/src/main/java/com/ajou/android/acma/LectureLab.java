package com.ajou.android.acma;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LectureLab {
    private static LectureLab sLectureLab;
    private List<Lecture> mLectures;
    private List<Lecture> mMajorLectures;
    private List<Lecture> mRefinementLectures;
    private List<Lecture> myLectures;
    public long num;

    private LectureLab(Context context) {
        mLectures = new ArrayList<>();
        mMajorLectures = new ArrayList<>();
        mRefinementLectures = new ArrayList<>();
        myLectures = new ArrayList<>();
        createLectures();
        setMyLectures();
    }

    public List<Lecture> getLectures() {
        return mLectures;
    }

    public void addMyLecture(Lecture lecture) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[.]","");
        String UserID = userID.replaceAll("[.]","");
        myLectures.add(lecture);
        DatabaseManager.databaseReference.child("users").child(UserID).child("myCourse").setValue(myLectures);
    }

    public void setMyLectures() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[.]","");
        String UserID = userID.replaceAll("[.]","");
        DatabaseManager.databaseReference.child("users").child(UserID).child("myCourse").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myLectures.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Lecture post = postSnapshot.getValue(Lecture.class);
                    myLectures.add(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setSchedules() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[.]","");
        String UserID = userID.replaceAll("[.]","");
        DatabaseManager.databaseReference.child("users").child(UserID).child("myCourse").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myLectures.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Lecture post = postSnapshot.getValue(Lecture.class);
                    myLectures.add(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<Lecture> getMyLectures() {
        return myLectures;
    }

    public List<Lecture> getMajorLectures() {
        mMajorLectures.clear();

        for(Lecture lecture : mLectures) {
            if(lecture.getCourseType().equals("major")) {
                mMajorLectures.add(lecture);
            }
        }
        return mMajorLectures;
    }

    public Lecture getLectureByCount(Long count) {
        for(Lecture lecture : mLectures) {
            if(lecture.getCount() == count) {
                return lecture;
            }
        }
        return null;
    }

    public List<Lecture> getRefinementLectures() {
        mRefinementLectures.clear();

        for(Lecture lecture : mLectures) {
            if(lecture.getCourseType().equals("refinement")) {
                mRefinementLectures.add(lecture);
            }
        }
        return mRefinementLectures;
    }

    public List<Lecture> getTermLectures(String term, List<Lecture> lectures) {
        List<Lecture> lectureList = new ArrayList<>();

        for(Lecture lecture : lectures) {
            if(lecture.getCourseTerm().equals(term)) {
                lectureList.add(lecture);
            }
        }
        return lectureList;
    }

    public static LectureLab get(Context context) {
        if(sLectureLab == null) {
            sLectureLab = new LectureLab(context);
        }
        return sLectureLab;
    }

    public void createLectures() {
        for(int i =  0; i < DatabaseManager.totalLectureNum; i++)
            mLectures.add(new Lecture(i));

    }
}

