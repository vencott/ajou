package com.ajou.android.lab7;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

public class AnimationService extends IntentService{

    private int result = Activity.RESULT_CANCELED;
    private boolean runnable = false;

    public AnimationService() {
        super("AnimationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();

        if(extras != null) {
            result = Activity.RESULT_OK;
            runnable = true;                // set runnable to true

            Messenger messenger = (Messenger) extras.get("MESSENGER");
            float startValue = (float) extras.get("STARTVALUE");
            float endValue = (float) extras.get("ENDVALUE");
            float increment = (float) extras.get("INCREMENT");
            long updateTime = (long) extras.get("UPDATETIME");

            float angle = startValue;

            while(angle <= endValue) {
                Message message = Message.obtain();
                message.arg1 = result;
                message.obj = angle;

                if(runnable) { // true only when runnable == true
                    try {      // when runnable changed to false, while loop would be broken
                        messenger.send(message);
                        angle += increment;
                        Thread.sleep(updateTime);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                    break;
            }
        }
    }

    @Override
    public void onDestroy() { // called by stopService() in onClickStop()
        runnable = false;     // service would be stopped, but thread won't be stopped
        super.onDestroy();    // so, make runnable false to break the while loop
    }
}