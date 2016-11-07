package com.indglobal.shedoctor.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.indglobal.shedoctor.Beans.DoctorSlotTimeModelDoctor;
import com.indglobal.shedoctor.Fragments.ConsultTimeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anurag on 06-10-2016.
 */
public class DownloadDataService  extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public DownloadDataService() {
        super(DownloadDataService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("'", "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
         String url = intent.getStringExtra("url");


        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {

                try {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date1 = df.format(c.getTime());
                    Date date = null;
                    date = (Date) formatter.parse(date1);
                    long dd = date.getTime();

                    long output = date.getTime() / 1000L;
                    String str = Long.toString(output);
                    long timestamp = Long.parseLong(str) * 1000;

                    for (int i = 0; i < 2879; i++) {
                        long newTimeStamp = (timestamp / 1000L);
                        timestamp = (timestamp + TimeUnit.MINUTES.toMillis(15));

                        String dateConvert = new SimpleDateFormat("dd/MM/yyyy").format(new Date(newTimeStamp * 1000));

                        String time = new SimpleDateFormat("hh:mm a").format(newTimeStamp * 1000);
                        String timeSlot = new SimpleDateFormat("kkmm").format(newTimeStamp * 1000);

                        int timeSlotMain = Integer.parseInt(timeSlot);
                        DoctorSlotTimeModelDoctor doctorAppointmentTimeModel = new DoctorSlotTimeModelDoctor(dateConvert, time, newTimeStamp, timeSlotMain, 0);
                        ConsultTimeFragment.doctorAppointmentTimeModelList.add(doctorAppointmentTimeModel);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String results = downloadData(url);

                /* Sending result back to activity */

                bundle.putString("result", results);
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

    private String downloadData(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream ;
        HttpURLConnection urlConnection ;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();


        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            String results = parseResult(response);
            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private  String parseResult(String result) {


        try {
            JSONObject jsonObject = new JSONObject(result);

            boolean success = jsonObject.getBoolean("success");
            if(success) {
                JSONObject dataObject = jsonObject.getJSONObject("data");

                JSONArray timeslotsArray = dataObject.getJSONArray("active");
                long slothLength = timeslotsArray.length();
                if (slothLength != 0) {
                    for (int i = 0; i < slothLength; i++) {
                        long timeStamp = timeslotsArray.getLong(i);

                        for (int j = 0; j < ConsultTimeFragment.doctorAppointmentTimeModelList.size(); j++) {
                            if (ConsultTimeFragment.doctorAppointmentTimeModelList.get(j).getTimeStamp() == timeStamp) {
                                ConsultTimeFragment.doctorAppointmentTimeModelList.get(j).setSlotStatus(1);
                                break;
                            }
                        }
                    }
                }
                JSONArray disabledSlotsArray = dataObject.getJSONArray("disabled");
                if (disabledSlotsArray.length()!=0) {
                    for (int i = 0; i < disabledSlotsArray.length(); i++) {
                        long timeStamp = disabledSlotsArray.getLong(i);

                        for (int k = 0; k < ConsultTimeFragment.doctorAppointmentTimeModelList.size(); k++){
                            if (ConsultTimeFragment.doctorAppointmentTimeModelList.get(k).getTimeStamp() == timeStamp) {
                                ConsultTimeFragment.doctorAppointmentTimeModelList.get(k).setSlotStatus(2);
                                break;
                            }
                        }
                    }
                }


               // Collections.sort(ConsultTimeFragment.doctorAppointmentTimeModelList, DoctorSlotTimeModelDoctor.sortBySlot);
                return "true";
            }
            return "false";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
