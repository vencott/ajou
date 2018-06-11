
package com.ajou.android.lab9;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class LunarView extends SurfaceView implements SurfaceHolder.Callback {
    public Handler mHandler;
    private Context mContext;
    private LunarThread thread;

    public LunarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new LunarThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
            }
        });

        setFocusable(true);
    }

    public LunarThread getThread() {
        return thread;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRun(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRun(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    class LunarThread extends Thread {
        private int mCanvasHeight = 1;
        private int mCanvasWidth = 1;
        private boolean mRun = false;
        private int x=0;
        private int y=0;
        private Bitmap mBackgroundImage;
        private Drawable mLanderImage;
        private SurfaceHolder mSurfaceHolder;

        public LunarThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;

            Resources res = context.getResources();
            mLanderImage = context.getResources().getDrawable(
                    R.drawable.lander_plain);

            mBackgroundImage = BitmapFactory.decodeResource(res,
                    R.drawable.earthrise);
        }
        public void setSurfaceSize(int width, int height) {
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
                mBackgroundImage = mBackgroundImage.createScaledBitmap(
                        mBackgroundImage, width, height, true);
            }
        }

        public void doStart() {
            synchronized (mSurfaceHolder) {
            }
        }

        public void pause() {
            synchronized (mSurfaceHolder) {
            }
        }

        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
            }
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public Bundle saveState(Bundle map) {
            synchronized (mSurfaceHolder) {
            }
            return map;
        }

        public void setRun(boolean run) {
            mRun = run;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean isRun() {
            return mRun;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        private void doDraw(Canvas canvas) {
            canvas.drawBitmap(mBackgroundImage, 0, 0, null);
            mLanderImage.setBounds(x++, y++, x + 100, y + 100);
            if( x > mCanvasWidth ) x = 0;
            if( y > mCanvasHeight ) y = 0;
            mLanderImage.draw(canvas);
        }
    }
}