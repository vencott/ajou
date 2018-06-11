package com.ajou.android.lab10;

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

    public void setColorToRed() {
        myGLRenderer.setCubeColorDataToRed();
    }

    public void setColorToGreen() {
        myGLRenderer.setCubeColorDataToGreen();
    }

    public void setColorToBlue() {
        myGLRenderer.setCubeColorDataToBlue();
    }
}
