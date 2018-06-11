package com.ajou.android.acma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText userID;
    EditText userPassword;
    TextView registerButton;
    TextView findButton;
    Button loginBt;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        userID = (EditText)findViewById(R.id.idText);
        userPassword = (EditText)findViewById(R.id.passwordText);

        progressDialog = new ProgressDialog(this);

        loginBt = (Button)findViewById(R.id.login);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        registerButton = (TextView)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        findButton = (TextView)findViewById(R.id.findpasswordButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findIntent = new Intent(LoginActivity.this,FindActivity.class);
                LoginActivity.this.startActivity(findIntent);
            }
        });
    }

    private void userLogin(){
        String ID = userID.getText().toString().trim();
        String Password = userPassword.getText().toString().trim();

        if(TextUtils.isEmpty(ID)){
            Toast.makeText(this, "ID를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this, "PASSWORD를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인 중입니다. 잠시만 기다려 주세요...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(ID,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(),"로그인 실패!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private long lastTimeBackPressed;

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        Toast.makeText(this,"뒤로가기 버튼을 한번 더 눌러 종료합니다.",Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

}
