package com.indglobal.shedoctor.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.indglobal.shedoctor.Beans.DoctorDoctorTimeSlots;
import com.indglobal.shedoctor.Fragments.ConsultTimeFragment;

/**
 * Created by Anurag on 06-10-2016.
 */
public class DoctorFilterDateService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String TAG = "FilterDateTimeService";

    public DoctorFilterDateService() {
        super(DoctorFilterDateService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG , "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String date = intent.getStringExtra("date");
        String inTimeStr = intent.getStringExtra("inTime");
        String outTimeStr = intent.getStringExtra("outTime");
        int inTime = Integer.parseInt(inTimeStr);
        int outTime = Integer.parseInt(outTimeStr);

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(date)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            try {
                for (int i = 0; i < ConsultTimeFragment.doctorAppointmentTimeModelList.size(); i++) {
                    if (date.equalsIgnoreCase(ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getDate())) {
                        if (inTime < ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getTimeDetails() &&
                                ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getTimeDetails() < outTime) {

                            long timeSlots = ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getTimeStamp();
                            int typeSta = ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getSlotStatus();

                            DoctorDoctorTimeSlots dctrTimeSlots = new DoctorDoctorTimeSlots(timeSlots, ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getTime(), typeSta);
                            ConsultTimeFragment.doctordoctorfilterTimeList.add(dctrTimeSlots);
                        }
                    }
                }
                /* Sending result back to activity */

                bundle.putString("result", "true");
                receiver.send(STATUS_FINISHED, bundle);

            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

}
