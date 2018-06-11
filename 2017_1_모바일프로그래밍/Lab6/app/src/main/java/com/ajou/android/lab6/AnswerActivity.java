package com.ajou.android.lab6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AnswerActivity extends AppCompatActivity {

    private ImageButton mAnswerButton;

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.answer_in, R.anim.question_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        mAnswerButton = (ImageButton) findViewById(R.id.answerButton);
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(AnswerActivity.this, QuestionActivity.class);
                startActivity(newIntent);
            }
        });


    }
}
