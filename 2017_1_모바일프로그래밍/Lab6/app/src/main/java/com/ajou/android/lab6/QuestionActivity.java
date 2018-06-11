package com.ajou.android.lab6;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class QuestionActivity extends AppCompatActivity {

    private ImageButton mQuestionButton;

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.question_in, R.anim.answer_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mQuestionButton = (ImageButton) findViewById(R.id.questionButton);
        mQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(QuestionActivity.this, AnswerActivity.class);
                startActivity(newIntent);
            }
        });
    }
}
