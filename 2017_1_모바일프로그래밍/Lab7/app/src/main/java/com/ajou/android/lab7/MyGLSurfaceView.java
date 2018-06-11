package com.ajou.android.lab7;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.AttributeSet;

import static android.app.Activity.RESULT_OK;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message message) {
            Object angle = message.obj;
            if(message.arg1 == RESULT_OK && angle != null) {
                mRenderer.setAngle((Float) angle);
                requestRender();
            }
        }
    };

    public Messenger getMessenger() {
        Messenger messenger = new Messenger(mHandler);
        return messenger;
    }
}
