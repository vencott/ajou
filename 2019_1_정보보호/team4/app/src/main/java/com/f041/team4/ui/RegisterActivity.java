package com.f041.team4.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.f041.team4.R;
import com.f041.team4.data.Account;
import com.f041.team4.data.User;
import com.f041.team4.global.FirebaseManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class RegisterActivity extends AppCompatActivity {
    EditText etName;
    TextView btnRegister;
    ProgressBar barProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.et_name);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(String.valueOf(etName.getText()).trim());
            }
        });

        barProgress = findViewById(R.id.bar_progress);
    }

    void register(String name) {
        barProgress.setVisibility(View.VISIBLE);
        Account.getInstance().setName(name);
        User user = new User(Account.getInstance().getName(), Account.getInstance().getPublicKey());
        FirebaseManager.getInstance().usersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finishActivity();
            }
        });
    }

    void finishActivity() {
        setResult(RESULT_OK, null);
        finish();
    }
}
