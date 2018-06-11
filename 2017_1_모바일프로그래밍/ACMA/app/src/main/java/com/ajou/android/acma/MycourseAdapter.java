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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

/*
 * Created by LDY on 2017-06-07.
 */

public class MycourseAdapter extends BaseAdapter{

    private Context context;
    private List<Lecture> MycourseList;
    public long num;
    private LectureLab lectureLab;

    public MycourseAdapter(Context context, List<Lecture> mycourseList) {
        this.context = context;
        this.MycourseList = mycourseList;
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
        return MycourseList.size();
    }

    @Override
    public Object getItem(int i) {
        return MycourseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup parent) {
        View v = View.inflate(context, R.layout.mycourse,null);
        TextView courseGrade = (TextView)v.findViewById(R.id.courseGrade);
        TextView courseName = (TextView)v.findViewById(R.id.courseName);
        TextView courseLimit = (TextView)v.findViewById(R.id.courseLimit);
        TextView courseRegister = (TextView)v.findViewById(R.id.courseRegister);
        TextView courseRate = (TextView)v.findViewById(R.id.courseRate);

        courseGrade.setText(MycourseList.get(i).getCourseGrade() + "학년");
        courseName.setText(MycourseList.get(i).getCourseName());
        courseLimit.setText("제한: " + MycourseList.get(i).getCourseLimit());
        courseRegister.setText("신청: " + MycourseList.get(i).getCourseRegister());

        Button deleteButton = (Button)v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parent.getContext())
                        .setMessage(MycourseList.get(i).getCourseName() + " 을(를) 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseManager.databaseReference.child("lectures").
                                        child(Long.toString(MycourseList.get(i).getCount())).
                                        child("courseRegister").
                                        setValue(MycourseList.get(i).getCourseRegister()-1);

                                String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[.]","");
                                String UserID = userID.replaceAll("[.]","");
                                String CourseID = String.valueOf(MycourseList.get(i).getCount());
                                DatabaseManager.databaseReference.child("users").child(UserID).child("myCourse").child(Integer.toString(i)).removeValue();
                                DatabaseManager.databaseReference.child("users").child(UserID).child("totalCredit").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        num = (Long) dataSnapshot.getValue();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                DatabaseManager.databaseReference.child("users").child(UserID).child("totalCredit").setValue(num  - MycourseList.get(i).getCourseCredit());
                                Toast.makeText(parent.getContext(), MycourseList.get(i).getCourseName() + " 이(가) 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                               // lectureLab.addMyLecture(MycourseList.get(i));

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

        v.setTag(MycourseList.get(i).getCount());
        return v;
    }

}
