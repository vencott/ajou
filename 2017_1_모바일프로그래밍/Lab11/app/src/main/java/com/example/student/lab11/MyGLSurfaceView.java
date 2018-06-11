package com.example.student.lab11;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGLSurfaceView extends GLSurfaceView{

    private MyGLRenderer myGLRenderer = new MyGLRenderer();

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(myGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void setAmbientOn() {
        myGLRenderer.ambientOn = true;
        System.out.println(myGLRenderer.ambientOn);
        System.out.println(myGLRenderer.diffuseOn);
    }
    public void setAmbientOff() {
        myGLRenderer.ambientOn = false;
        System.out.println(myGLRenderer.ambientOn);
        System.out.println(myGLRenderer.diffuseOn);
    }
    public void setDiffuseOn() {
        myGLRenderer.diffuseOn = true;
        System.out.println(myGLRenderer.ambientOn);
        System.out.println(myGLRenderer.diffuseOn);
    }
    public void setDiffuseOff() {
        myGLRenderer.diffuseOn = false;
        System.out.println(myGLRenderer.ambientOn);
        System.out.println(myGLRenderer.diffuseOn);
    }
}

