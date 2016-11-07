package com.indglobal.shedoctor.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.DoctorTimeSlotAdp;
import com.indglobal.shedoctor.Beans.DoctorDoctorTimeSlots;
import com.indglobal.shedoctor.Beans.DoctorSlotTimeModelDoctor;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;
import com.indglobal.shedoctor.services.DoctorDataResultReciver;
import com.indglobal.shedoctor.services.DoctorFilterDateService;
import com.indglobal.shedoctor.services.DownloadDataService;
import com.indglobal.shedoctor.services.DownloadDoctorTimeSlot;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 9/13/16.
 */
public class ConsultTimeFragment extends Fragment implements View.OnClickListener, DownloadDoctorTimeSlot.Receiver, DoctorDataResultReciver.ReceiverDate, RippleView.OnRippleCompleteListener {

    LayoutInflater inflater;
    public static List<DoctorSlotTimeModelDoctor> doctorAppointmentTimeModelList;
    public static List<DoctorDoctorTimeSlots> doctordoctorfilterTimeList;
    int slot =0;
    private TextView tvMorning, tvAfterNoon, tvEvening, tvDate, tvNight, cbSelectWholeDay, cbSelectNone, cbRepeatDays;
    private ExpandableHeightGridView gvCnsltTimeView;
    private ProgressBar prgLoading;
    private ImageView consulcal;
    private DoctorDataResultReciver mDateReciver;
    private DoctorTimeSlotAdp timeSlotAdp;
    JSONArray timeStampArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultation_time_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvMorning = (TextView) getActivity().findViewById(R.id.tvMorning);
        tvAfterNoon = (TextView) getActivity().findViewById(R.id.tvAfterNoon);
        tvEvening = (TextView) getActivity().findViewById(R.id.tvEvening);
        tvDate = (TextView) getActivity().findViewById(R.id.tvDate);
        tvNight = (TextView) getActivity().findViewById(R.id.tvNight);
        consulcal = (ImageView) getActivity().findViewById(R.id.consulcal);
        cbSelectNone = (TextView)  getActivity().findViewById(R.id.cbSelectNone);
        cbRepeatDays = (TextView)  getActivity().findViewById(R.id.cbRepeatDays);
        cbSelectWholeDay = (TextView)  getActivity().findViewById(R.id.cbSelectWholeDay);
        prgLoading = (ProgressBar) getActivity().findViewById(R.id.prgLoading);
        gvCnsltTimeView = (ExpandableHeightGridView) getActivity().findViewById(R.id.gvCnsltTimeView);
        RippleView rplConsltFeeFragUpdate = (RippleView) getActivity().findViewById(R.id.rplConsltFeeFragUpdate);


        /*long unixTimestampCurrent = System.currentTimeMillis();
        Log.d("current Til",unixTimestampCurrent+"" );
        long unixTimestamp = System.currentTimeMillis();

        long nowPlus5Minutes = (unixTimestamp + TimeUnit.MINUTES.toMillis(15))/1000L;
        Log.d("timeStamp",nowPlus5Minutes+"" );

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(2016, Calendar.OCTOBER, 14);
        long secondsSinceEpoch = calendar.getTimeInMillis() / 1000L;
        Log.d("timeStamp",secondsSinceEpoch+"" );

        String input = "2062-10-19 02:30:00" ;
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf( input ) ;

        Log.d("timeStampCurr",ts.toString()+"" );*/


//   String str_date=12+"-"+2+"-"+2016;
       // String str_date=2016+"-"+2+"-"+12;

        //  DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");



       /* Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date1 = df.format(c.getTime());
        Date date = null;
        try {
            date = (Date)formatter.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dd = date.getTime();
        Log.d("ddd",dd+"");

        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;

        for(int i=0; i<2879; i++){
            timestamp = (timestamp + TimeUnit.MINUTES.toMillis(15));
            long newTimeStamp = (timestamp/1000L );
            Log.d("newTimeStamp",newTimeStamp+"");
        }

        Log.d("ttt",timestamp+"");*/

        if (Comman.isConnectionAvailable(getActivity())) {
            DownloadDoctorTimeSlot mReceiver = new DownloadDoctorTimeSlot(new Handler());
            mReceiver.setReceiver(this);
            doctorAppointmentTimeModelList = new ArrayList<>();
            doctordoctorfilterTimeList = new ArrayList<>();
            String token = Comman.getPreferences(getActivity(), "TOKEN");
            String handlerUrl = "https://beta.shedoctr.com/v1/doctor/consultationTime"+"?token="+token;
            Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadDataService.class);

         //    Send optional extras to Download IntentService
            intent.putExtra("url", handlerUrl);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);
            getActivity().startService(intent);
        }

        tvMorning.setOnClickListener(this);
        tvAfterNoon.setOnClickListener(this);
        tvEvening.setOnClickListener(this);
        tvNight.setOnClickListener(this);
        consulcal.setOnClickListener(this);
        cbRepeatDays.setOnClickListener(this);
        cbSelectWholeDay.setOnClickListener(this);
        cbSelectNone.setOnClickListener(this);
        rplConsltFeeFragUpdate.setOnRippleCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.tvMorning:
                tvMorning.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvAfterNoon.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvEvening.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvNight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));

                showTimeSlot(tvDate.getText().toString(), 0550, 1148);
                break;

            case R.id.tvAfterNoon:

                tvMorning.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvAfterNoon.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvEvening.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvNight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));

                showTimeSlot(tvDate.getText().toString(), 1159, 1548);
                break;
            case R.id.tvEvening:

                tvMorning.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvAfterNoon.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvEvening.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvNight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));

                showTimeSlot(tvDate.getText().toString(), 1559, 1948);

                break;

            case R.id.tvNight:
                tvMorning.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvAfterNoon.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvEvening.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvNight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                showTimeSlot(tvDate.getText().toString(), 1959, 2348);
                break;

            case R.id.consulcal:
                openCalander();
                break;

            case R.id.cbSelectWholeDay:

                String date = tvDate.getText().toString();
                for (int i=0; i<doctorAppointmentTimeModelList.size(); i++) {
                    if (doctorAppointmentTimeModelList.get(i).getDate().equals(date) ){
                        if(doctorAppointmentTimeModelList.get(i).getSlotStatus()==0) {
                            doctorAppointmentTimeModelList.get(i).setSlotStatus(1);
                        }
                    }
                }
                for (int j=0; j<doctordoctorfilterTimeList.size(); j++){
                    if (doctordoctorfilterTimeList.get(j).getStatus() ==0) {
                        doctordoctorfilterTimeList.get(j).setStatus(1);
                    }
                }
                timeSlotAdp.notifyDataSetChanged();
                break;

            case R.id.cbSelectNone:
                String nonedate = tvDate.getText().toString();
                for (int i=0; i<doctorAppointmentTimeModelList.size(); i++) {
                    if (doctorAppointmentTimeModelList.get(i).getDate().equals(nonedate) ){
                        if(doctorAppointmentTimeModelList.get(i).getSlotStatus()==1) {
                            doctorAppointmentTimeModelList.get(i).setSlotStatus(0);
                        }
                    }
                }
                for (int j=0; j<doctordoctorfilterTimeList.size(); j++){
                    if (doctordoctorfilterTimeList.get(j).getStatus() ==1) {
                        doctordoctorfilterTimeList.get(j).setStatus(0);
                    }
                }
                timeSlotAdp.notifyDataSetChanged();
                break;

            case R.id.cbRepeatDays:
                for (int j=0; j<doctordoctorfilterTimeList.size(); j++){
                    if (doctordoctorfilterTimeList.get(j).getStatus() ==1) {
                        String selectTime = doctordoctorfilterTimeList.get(j).getTime();

                        for (int i=0; i<doctorAppointmentTimeModelList.size(); i++) {
                            if (doctorAppointmentTimeModelList.get(i).getTime().equals(selectTime)){
                                if (doctorAppointmentTimeModelList.get(i).getSlotStatus()==0) {
                                    doctorAppointmentTimeModelList.get(i).setSlotStatus(1);
                                }

                            }

                        }
                    } else {
                        if (doctordoctorfilterTimeList.get(j).getStatus() ==0){
                            String selectTime = doctordoctorfilterTimeList.get(j).getTime();

                            for (int i=0; i<doctorAppointmentTimeModelList.size(); i++) {
                                if (doctorAppointmentTimeModelList.get(i).getTime().equals(selectTime)){
                                    if (doctorAppointmentTimeModelList.get(i).getSlotStatus()==1) {
                                        doctorAppointmentTimeModelList.get(i).setSlotStatus(0);
                                    }

                                }

                            }
                        }
                }
            }

                /*for (DoctorDoctorTimeSlots f : timeSlotAdp.getBox()) {
                    if (f.brandSelected) {
                        String localTime = f.getTime();
                        for (int i=0; i<doctorAppointmentTimeModelList.size(); i++) {
                            if (doctorAppointmentTimeModelList.get(i).getTime().equals(localTime)){
                                if (doctorAppointmentTimeModelList.get(i).getSlotStatus()==0) {
                                    doctorAppointmentTimeModelList.get(i).setSlotStatus(1);
                                }

                            }

                        }

                    }

                }*/
                break;
        }
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DoctorFilterDateService.STATUS_RUNNING:
                prgLoading.setVisibility(View.VISIBLE);
                break;

            case DoctorFilterDateService.STATUS_FINISHED:
                prgLoading.setVisibility(View.GONE);
                /* Hide progress & extract result from bundle */
                String results = resultData.getString("result");

                if (results.equalsIgnoreCase("true")) {
                    try {
                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String date = df.format(c.getTime());
                        tvDate.setText(date);
                        mDateReciver = new DoctorDataResultReciver(new Handler());
                        mDateReciver.setReceiver(this);

                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DoctorFilterDateService.class);
                        /* Send optional extras to Download IntentService */
                        intent.putExtra("date", date);
                        intent.putExtra("receiver", mDateReciver);
                        intent.putExtra("requestId", 101);
                        intent.putExtra("inTime", "0600");
                        intent.putExtra("outTime", "1148");
                        getActivity().startService(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case DoctorFilterDateService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                prgLoading.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onReceiveResultDate(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DoctorFilterDateService.STATUS_RUNNING:
                prgLoading.setVisibility(View.VISIBLE);
                break;

            case DoctorFilterDateService.STATUS_FINISHED:
                prgLoading.setVisibility(View.GONE);
                /* Hide progress & extract result from bundle */
                String results = resultData.getString("result");
                if (results.equalsIgnoreCase("true")) {
                    try {
                        if (doctorAppointmentTimeModelList.size()!=0) {
                            long unixTimestamp = System.currentTimeMillis()/1000L;
                            gvCnsltTimeView.setAdapter(null);
                            timeSlotAdp = new DoctorTimeSlotAdp(getActivity(), doctordoctorfilterTimeList, false, unixTimestamp);
                            gvCnsltTimeView.setAdapter(timeSlotAdp);
                            gvCnsltTimeView.setExpanded(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case DoctorFilterDateService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void openCalander() {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),  new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, monthOfYear);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tvDate.setText(sdf.format(mcurrentDate.getTime()));
                String selectDate = sdf.format(mcurrentDate.getTime());
                tvDate.setText(selectDate);
                tvMorning.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvAfterNoon.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvEvening.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                tvNight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.liggray));
                showTimeSlot(selectDate,0550, 1148);
            }
        }, mYear, mMonth, mDay);
        mcurrentDate.add(Calendar.DATE, 30);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.getDatePicker().setMaxDate(mcurrentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimeSlot(String selectDate, int inTime, int outTime){
        try {

            if (doctordoctorfilterTimeList.size() != 0){
                doctordoctorfilterTimeList.clear();
                slot = 0;

                gvCnsltTimeView.setAdapter(null);
            }
            //      tvTimeNotAvailable.setVisibility(View.GONE);
            mDateReciver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null,getActivity(), DoctorFilterDateService.class);

            /* Send optional extras to Download IntentService */
            intent.putExtra("date", selectDate);
            intent.putExtra("receiver", mDateReciver);
            intent.putExtra("requestId", 101);
            intent.putExtra("inTime", String.valueOf(inTime));
            intent.putExtra("outTime", String.valueOf(outTime));
            intent.putExtra("reschedule", "no");
            getActivity().startService(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {
            case R.id.rplConsltFeeFragUpdate:
                timeStampArray = new JSONArray();

                for (int i=0; i< doctorAppointmentTimeModelList.size(); i++) {
                    if (doctorAppointmentTimeModelList.get(i).getSlotStatus() ==1){
                        timeStampArray.put( doctorAppointmentTimeModelList.get(i).getTimeStamp());
                    }
                }



                if (timeStampArray != null && timeStampArray.length()!=0){
                    new UpdateApptTime(timeStampArray).execute();
                }

                break;
        }
    }

    private class UpdateApptTime extends AsyncTask<String, Void, String> {

        JSONArray jsonArray;
        public UpdateApptTime(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                HttpClient httpclient = new DefaultHttpClient();
                JSONObject json = new JSONObject();
                try {
                    String token = Comman.getPreferences(getActivity(), "TOKEN");
                    String updateBooking = getResources().getString(R.string.consultationTime)+"?token="+token;
                    HttpPost post = new HttpPost(updateBooking);
                    json.put("timeslots", timeStampArray);

                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    HttpResponse response = httpclient.execute(post);
                    HttpEntity resEntity = response.getEntity();
                    final String response_str = EntityUtils.toString(resEntity);
                    return response_str;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
                if(result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");
                        Toast.makeText(getActivity(), message+"", Toast.LENGTH_SHORT).show();
                        if(success) {


                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateList() {
        try {
            for (int i=0; i<timeStampArray.length(); i++) {
                long selectTime = (long) timeStampArray.get(i);
                for (int j = 0; j < doctorAppointmentTimeModelList.size(); j++){
                    if(doctorAppointmentTimeModelList.get(j).getTimeStamp() == selectTime){
                        doctorAppointmentTimeModelList.get(j).setSlotStatus(1);
                        break;
                    }
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}