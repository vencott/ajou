package com.ajou.android.acma;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private FirebaseAuth firebaseAuth;
    private ListView mycourseListView;
    private MycourseAdapter adapter;
    private List<Lecture> mycourseList = new ArrayList<>();
    private LectureLab lectureLab = LectureLab.get(this);

    Button courseButton;
    Button statisticButton;
    Button scheduleButton;
    Button logoutButton;
    Button delButton;
    Button delcourseButton;
    TextView textViewUserEmail;
    LinearLayout mycourse;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mycourseListView = (ListView)findViewById(R.id.mycourseListview);

        update();

        courseButton = (Button) findViewById(R.id.courseButton);
        statisticButton = (Button) findViewById(R.id.statisticButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        delButton = (Button) findViewById(R.id.delButton);
        delcourseButton = (Button) findViewById(R.id.deleteButton);
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);

        mycourse = (LinearLayout) findViewById(R.id.mycourse);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail.setText("반갑습니다. " + user.getEmail() + " 님!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutButton:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.delButton:
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ProfileActivity.this);
                alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String DeleteID = firebaseAuth.getCurrentUser().getEmail();
                                String result = DeleteID.replaceAll("[.]", "");
                                DatabaseManager.databaseReference.child("users").child(result).removeValue();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProfileActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            }
                                        });
                            }
                        }
                );
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert_confirm.show();
                break;

            case R.id.courseButton:
                mycourse.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                statisticButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new CourseFragment());
                fragmentTransaction.commit();
                break;

            case R.id.statisticButton:
                mycourse.setVisibility(View.GONE);
                statisticButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new StatisticFragment());
                fragmentTransaction.commit();
                break;

            case R.id.scheduleButton:
                mycourse.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                statisticButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ScheduleFragment());
                fragmentTransaction.commit();
                break;

            case R.id.deleteButton: // 신청 목록에서 선택 리스트 아이템 제거
                int position = (Integer) view.getTag();
                mycourseList.remove(position);
                mycourseList.notifyAll();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        Toast.makeText(this,"신청 목록으로 돌아갑니다.",Toast.LENGTH_SHORT).show();
        update();
    }

    public void update() {
        lectureLab = lectureLab.get(this);
        lectureLab.setMyLectures();

        mycourseList = lectureLab.getMyLectures();

        adapter = new MycourseAdapter(getApplicationContext(), mycourseList);
        mycourseListView.setAdapter(adapter);
    }
}
