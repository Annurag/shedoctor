package com.indglobal.shedoctor.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

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
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class PendingStatusActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvPendngStatsText,tvsuccessful;
    LinearLayout llsuccessful, llunsuccessful,llnoshow;
    RadioButton rdsuccessful, rdunsuccessful,rdnoshow;
    RadioGroup rgPendingStatus;
    Spinner spinPendingUnscs,spinPendingNotnw;
    EditText etPendingReason;
    RippleView rplPrescription;
    ArrayList<String> ArrayList = new ArrayList<>();
    ProgressBar prgLoading;

    String issue,appointment_id,status="1",reason="Success",details="Success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_status);

        rgPendingStatus = (RadioGroup)findViewById(R.id.rgPendingStatus);
        tvPendngStatsText = (TextView)findViewById(R.id.tvPendngStatsText);
        tvsuccessful = (TextView) findViewById(R.id.tvsuccessful);
        rdsuccessful = (RadioButton) findViewById(R.id.rdsuccessful);
        rdunsuccessful = (RadioButton) findViewById(R.id.rdunsuccessful);
        rdnoshow = (RadioButton)findViewById(R.id.rdnoshow);
        llsuccessful = (LinearLayout) findViewById(R.id.llsuccessful);
        llunsuccessful = (LinearLayout) findViewById(R.id.llunsuccessful);
        llnoshow = (LinearLayout)findViewById(R.id.llnoshow);
        etPendingReason = (EditText)findViewById(R.id.etPendingReason);
        spinPendingUnscs = (Spinner)findViewById(R.id.spinPendingUnscs);
        spinPendingNotnw = (Spinner)findViewById(R.id.spinPendingNotnw);
        rplPrescription = (RippleView)findViewById(R.id.rplPrescription);
        prgLoading =(ProgressBar)findViewById(R.id.prgLoading);

        Intent ii = getIntent();
        appointment_id = ii.getStringExtra("appointment_id");

        rgPendingStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId){
                    case R.id.rdsuccessful:
                        llsuccessful.setVisibility(View.VISIBLE);
                        llunsuccessful.setVisibility(View.GONE);
                        llnoshow.setVisibility(View.GONE);
                        tvsuccessful.setText("Thank you!");
                        tvPendngStatsText.setText("Write Prescription");
                        status = "1";
                        reason="Success";
                        details="Success";
                        break;

                    case R.id.rdunsuccessful:
                        llsuccessful.setVisibility(View.GONE);
                        llunsuccessful.setVisibility(View.VISIBLE);
                        llnoshow.setVisibility(View.GONE);
                        tvsuccessful.setText("We are sorry");
                        tvPendngStatsText.setText("Submit Issue");
                        status = "0";
                        break;

                    case R.id.rdnoshow:
                        llnoshow.setVisibility(View.VISIBLE);
                        llsuccessful.setVisibility(View.GONE);
                        llunsuccessful.setVisibility(View.GONE);
                        tvsuccessful.setText("We are sorry");
                        tvPendngStatsText.setText("Submit");
                        status = "0";
                        reason="No Show";

                }
            }
        });


        ArrayAdapter adapterState = ArrayAdapter.createFromResource(PendingStatusActivity.this, R.array.unsuccess_reason, R.layout.spinner_item);
        adapterState.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinPendingUnscs.setAdapter(adapterState);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(PendingStatusActivity.this, R.array.not_show, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinPendingNotnw.setAdapter(adapter);

        rplPrescription.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                int id = rgPendingStatus.getCheckedRadioButtonId();

                switch (id){

                    case R.id.rdsuccessful:
                        if(!Comman.isConnectionAvailable(PendingStatusActivity.this)){
                            Toast.makeText(PendingStatusActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                        }else {
                            prgLoading.setVisibility(View.VISIBLE);
                            new saveIssueTask().execute();
                        }

                        break;
                    case R.id.rdunsuccessful:
                        issue = etPendingReason.getText().toString();
                        details = issue;
                        if(!Comman.isConnectionAvailable(PendingStatusActivity.this)){
                            Toast.makeText(PendingStatusActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                        }else if(spinPendingUnscs.getSelectedItemPosition()==0) {
                            Toast.makeText(PendingStatusActivity.this,"Please select a reason",Toast.LENGTH_SHORT).show();
                        }else  if(issue.equalsIgnoreCase("")){
                            Toast.makeText(PendingStatusActivity.this,"Please write exact reason",Toast.LENGTH_SHORT).show();
                        }else {
                            reason = spinPendingUnscs.getSelectedItem().toString();
                            prgLoading.setVisibility(View.VISIBLE);
                            new saveIssueTask().execute();
                        }

                        break;
                    case R.id.rdnoshow:
                        if(!Comman.isConnectionAvailable(PendingStatusActivity.this)){
                            Toast.makeText(PendingStatusActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }else if(spinPendingNotnw.getSelectedItemPosition()==0) {
                            Toast.makeText(PendingStatusActivity.this,"Please select a reason",Toast.LENGTH_SHORT).show();
                        }else{
                            details = spinPendingNotnw.getSelectedItem().toString()+" did not attend call";
                            prgLoading.setVisibility(View.VISIBLE);
                            new saveIssueTask().execute();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {


        }
    }


    private class saveIssueTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                DefaultHttpClient client = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.pendingUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(PendingStatusActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("appointment_id", appointment_id);
                json.put("status", status);
                json.put("reason", reason);
                json.put("details", details);

                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                HttpResponse res = httpclient.execute(httppost);
                HttpEntity resEntity = res.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                return response_str;
                //------------------>>
            } catch (ParseException | IOException | JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            prgLoading.setVisibility(View.GONE);

                            if (status.equalsIgnoreCase("0")){
                                Toast.makeText(PendingStatusActivity.this, "Issue successful Submited! ", Toast.LENGTH_SHORT).show();
                                PastApointDetail.activity.finish();
                                Comman.setPreferences(PendingStatusActivity.this,"StatusChanged","1");
                                onBackPressed();

                            }else {
                                Intent i = new Intent(PendingStatusActivity.this,PastWriteOneActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            Toast.makeText(PendingStatusActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(PendingStatusActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

}