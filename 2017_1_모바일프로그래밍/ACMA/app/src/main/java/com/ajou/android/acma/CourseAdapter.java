package com.ajou.android.acma;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CourseAdapter extends BaseAdapter{

    private Context context;
    private List<Lecture> courseList;
    private Schedule schedule = new Schedule();
    public long num;
    private LectureLab lectureLab;

    public CourseAdapter(Context context, List<Lecture> courseList) {
        lectureLab = LectureLab.get(context);
        this.context = context;
        this.courseList = courseList;
        String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[.]","");
        String UserID = userID.replaceAll("[.]","");
        DatabaseManager.databaseReference.child("users").child(UserID).child("totalCredit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = (Long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup parent) {

        View v = View.inflate(context, R.layout.lecture,null);
        TextView courseGrade = (TextView)v.findViewById(R.id.courseGrade);
        TextView courseName = (TextView)v.findViewById(R.id.courseName);
        TextView courseProfessor = (TextView)v.findViewById(R.id.courseProfessor);
        TextView courseCredit = (TextView)v.findViewById(R.id.courseCredit);
        TextView courseID = (TextView)v.findViewById(R.id.courseID);
        TextView courseLimit = (TextView)v.findViewById(R.id.courseLimit);
        TextView courseRegister = (TextView)v.findViewById(R.id.courseRegister);
        TextView courseTime = (TextView)v.findViewById(R.id.courseTime);
        TextView courseRoom = (TextView)v.findViewById(R.id.courseRoom);
        Button addButton = (Button)v.findViewById(R.id.addButton);

        courseName.setText(courseList.get(i).getCourseName());
        courseProfessor.setText(courseList.get(i).getCourseProfessor());
        courseCredit.setText(courseList.get(i).getCourseCredit() + "학점");
        courseID.setText(courseList.get(i).getCourseID());
        courseLimit.setText("제한: " + courseList.get(i).getCourseLimit());
        courseRegister.setText("신청: " + courseList.get(i).getCourseRegister());
        courseTime.setText(courseList.get(i).getCourseTime() + "");
        courseRoom.setText(courseList.get(i).getCourseRoom() + "");
        courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parent.getContext())
                        .setMessage(courseList.get(i).getCourseName() + " 을(를) 수강신청 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseManager.databaseReference.child("lectures").
                                        child(Long.toString(courseList.get(i).getCount())).
                                        child("courseRegister").
                                        setValue(courseList.get(i).getCourseRegister()+ 1);

                                String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[.]","");
                                String UserID = userID.replaceAll("[.]","");
                                DatabaseManager.databaseReference.child("users").child(UserID).child("totalCredit").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        num = (Long) dataSnapshot.getValue();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                DatabaseManager.databaseReference.child("users").child(UserID).child("totalCredit").setValue(num + courseList.get(i).getCourseCredit());
                                Toast.makeText(parent.getContext(), courseList.get(i).getCourseName() + " 이(가) 수강신청 되었습니다.", Toast.LENGTH_SHORT).show();

                                lectureLab.addMyLecture(courseList.get(i));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
            }

        });

        v.setTag(courseList.get(i).getCount());

        return v;
    }
}