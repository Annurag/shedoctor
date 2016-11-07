package com.indglobal.shedoctor.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Aman on 06-10-2016.
 */
public class DoctorDataResultReciver  extends ResultReceiver {
    private ReceiverDate mReceiver;

    public DoctorDataResultReciver(Handler handler) {
        super(handler);
    }

    public void setReceiver(ReceiverDate receiver) {
        mReceiver = receiver;
    }



    public interface ReceiverDate {
        void onReceiveResultDate(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResultDate(resultCode, resultData);
        }
    }
}
