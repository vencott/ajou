package com.ajou.android.acma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter adapter;
    Spinner spinner;
    FirebaseAuth firebaseAuth;
    Button registerButton;
    ProgressDialog progressDialog;
    EditText idText;
    EditText textPassword;
    EditText nameText;
    RadioGroup genderGroup;
    RadioGroup gradeGroup;
    RadioButton selectedGender;
    RadioButton selectedGrade;

    private String userID, userPassword, userName, userMajor, userGender, userGrade, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        idText = (EditText)findViewById(R.id.idText);
        textPassword = (EditText)findViewById(R.id.passwordText);
        nameText = (EditText)findViewById(R.id.nameText);

        genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        gradeGroup = (RadioGroup)findViewById(R.id.gradeGroup);

        registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        spinner = (Spinner)findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.major,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);

    }

    private void writeNewUser(String userID, String userMajor, String userGender, String userGrade, String userName, long totalCredit){
        User user = new User(userMajor, userGender, userGrade, userName, totalCredit);
        DatabaseManager.databaseReference.child("users").child(userID).setValue(user);
    }

    private void registerUser() {
        userID = idText.getText().toString().trim();
        userPassword = textPassword.getText().toString().trim();
        userName = nameText.getText().toString().trim();

        if(TextUtils.isEmpty(userID)){
            Toast.makeText(this,"ID를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Password를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Name을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("등록 중 입니다. 기다려 주세요...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(userID, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "6자리 이상 비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {

        registerUser();

        int gender = genderGroup.getCheckedRadioButtonId();
        int grade = gradeGroup.getCheckedRadioButtonId();

        selectedGender = (RadioButton)findViewById(gender);
        selectedGrade = (RadioButton)findViewById(grade);

        userID = idText.getText().toString().trim();
        result = userID.replaceAll("[.]","");
        userName = nameText.getText().toString().trim();
        userMajor = spinner.getSelectedItem().toString().trim();
        userGender = selectedGender.getText().toString().trim();
        userGrade = selectedGrade.getText().toString().trim();

        writeNewUser(result, userMajor, userGender, userGrade, userName, 0);
    }

}