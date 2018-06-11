package com.ajou.android.lab10;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyOpenGLESActivity extends Activity
{
    private MyGLSurfaceView mGLSurfaceView;
    private Button redButton;
    private Button greenButton;
    private Button blueButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_open_gles);

        mGLSurfaceView = (MyGLSurfaceView)findViewById(R.id.MyGLSurfaceView);

        redButton = (Button) findViewById(R.id.redButton);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGLSurfaceView.setColorToRed();
                System.out.println("red");
            }
        });

        greenButton = (Button) findViewById(R.id.greenButton);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGLSurfaceView.setColorToGreen();
                System.out.println("green");
            }
        });

        blueButton = (Button) findViewById(R.id.blueButton);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGLSurfaceView.setColorToBlue();
                System.out.println("blue");
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
       // mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //mGLSurfaceView.onPause();
    }
}

