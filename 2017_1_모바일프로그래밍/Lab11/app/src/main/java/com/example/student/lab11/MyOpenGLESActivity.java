package com.example.student.lab11;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MyOpenGLESActivity extends Activity
{
    private MyGLSurfaceView mGLSurfaceView;
    private ToggleButton ambientButton;
    private ToggleButton diffuseButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_open_gles);

        mGLSurfaceView = (MyGLSurfaceView)findViewById(R.id.MyGLSurfaceView);
        ambientButton = (ToggleButton) findViewById(R.id.ambientButton);
        diffuseButton = (ToggleButton) findViewById(R.id.diffuseButton);

        ambientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ambientButton.isChecked()) {
                    mGLSurfaceView.setAmbientOn();
                }
                else {
                    mGLSurfaceView.setAmbientOff();
                }
            }
        });

        diffuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diffuseButton.isChecked()) {
                    mGLSurfaceView.setDiffuseOn();
                }
                else {
                    mGLSurfaceView.setDiffuseOff();
                }
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

