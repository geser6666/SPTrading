package com.trading.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ProgressThread extends Thread {
	
    Handler mHandler;
    public final static int STATE_DONE = 0;
    final static int STATE_RUNNING = 1;
    int mState;
    int mTotal;
   
    public ProgressThread(Handler h) {
        mHandler = h;
    }
   
    public void run() {
        mState = STATE_RUNNING;   
        mTotal = 0;
        while (mState == STATE_RUNNING) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e("ERROR", "Thread Interrupted");
            }
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("total", mTotal);
            msg.setData(b);
            mHandler.sendMessage(msg);
            mTotal++;
        }
    }
    
    public void setState(int state) {
        mState = state;
    }
}
