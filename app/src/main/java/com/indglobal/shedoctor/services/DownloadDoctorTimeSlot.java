package com.indglobal.shedoctor.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Aman on 06-10-2016.
 */
public class DownloadDoctorTimeSlot extends ResultReceiver {
    private Receiver mReceiver;

    public DownloadDoctorTimeSlot(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}