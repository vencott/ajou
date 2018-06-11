package com.ajou.android.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OpenGLSquareActivity extends AppCompatActivity {

    private MyGLSurfaceView mMyGLSurfaceView;
    float startValue = 0;
    float endValue = 360;
    float increment = 1;
    long updateTime = 10;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_glsquare);

        mMyGLSurfaceView = (MyGLSurfaceView) findViewById(R.id.myGLSurfaceView);

        intent = new Intent(this, AnimationService.class);
    }

    public void onClick(View view) {
        intent.putExtra("MESSENGER", mMyGLSurfaceView.getMessenger());
        intent.putExtra("STARTVALUE", startValue);
        intent.putExtra("ENDVALUE", endValue);
        intent.putExtra("INCREMENT", increment);
        intent.putExtra("UPDATETIME", updateTime);

        startService(intent);
    }

    public void onClickStop(View view) { // calls onDestroy() in service
        stopService(intent);
    }
}