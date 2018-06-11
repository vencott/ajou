package com.ajou.android.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;


public class LunarLander extends AppCompatActivity {
    private LunarView.LunarThread mLunarThread;
    private LunarView mLunarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunar_layout);
        mLunarView = (LunarView) findViewById(R.id.lunar);
        mLunarThread = mLunarView.getThread();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mLunarView.getThread().pause(); // pause game when Activity pauses
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLunarThread.saveState(outState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchedX = (int) event.getX();
        int touchedY = (int) event.getY() - 241;
        boolean inside = (touchedX <= mLunarThread.getX() + 100)
                      && (touchedX >= mLunarThread.getX())
                      && (touchedY <= mLunarThread.getY() + 100)
                      && (touchedY >= mLunarThread.getY());

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!inside) {
                if (mLunarThread.isRun())
                    mLunarThread.setRun(false);
                else {
                    mLunarThread.setRun(true);
                    mLunarThread.start();
                }
            }
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE && inside) {
            if(!mLunarThread.isRun()) {
                mLunarThread.setRun(true);
                mLunarThread.start();
            }
            mLunarThread.setX(touchedX - 50);
            mLunarThread.setY(touchedY - 50);
        }

        return true;
    }
}