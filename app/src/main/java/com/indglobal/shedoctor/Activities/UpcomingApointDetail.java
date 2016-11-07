package com.indglobal.shedoctor.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.ReportAdapter;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.UpcomingApointFragment;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 16/09/2016.
 */
public class UpcomingApointDetail extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;
    String post,date;
    UpcomingApointItem upcomingApointItem;

    TextView tvUpApntDtlPName,tvUpApntDtlPId,tvUpApntDtlPCallType,tvUpApntDtlPTime,tvUpApntDtlPDate,tvUpApntDtlCallStartIn,tvUpApntDtlPHeight,tvUpApntDtlPWeight,tvUpApntDtlPBlood,tvUpApntDtlPrescriptionCount
            ,tvUpApntDtlPrescrptn,tvUpApntDtlPLabCount,tvUpApntDtlPAllergy,tvUpApntDtlLabNo;
    ImageView ivUpApntDtlPCallIcon,ivUpApntDtlPCallbtn;
    ExpandableHeightGridView grdUpApntDtlLabReports;

    RippleView rplUpApntDtlPCancel;
    Dialog dialog;

    ReportAdapter reportAdapter;
    String reason = "";
    boolean processing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.upcmng_apoint_detail);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvUpApntDtlPName = (TextView)findViewById(R.id.tvUpApntDtlPName);
        tvUpApntDtlPId = (TextView)findViewById(R.id.tvUpApntDtlPId);
        tvUpApntDtlPCallType = (TextView)findViewById(R.id.tvUpApntDtlPCallType);
        tvUpApntDtlPTime = (TextView)findViewById(R.id.tvUpApntDtlPTime);
        tvUpApntDtlPDate = (TextView)findViewById(R.id.tvUpApntDtlPDate);
        tvUpApntDtlCallStartIn = (TextView)findViewById(R.id.tvUpApntDtlCallStartIn);
        tvUpApntDtlPHeight = (TextView)findViewById(R.id.tvUpApntDtlPHeight);
        tvUpApntDtlPWeight = (TextView)findViewById(R.id.tvUpApntDtlPWeight);
        tvUpApntDtlPBlood = (TextView)findViewById(R.id.tvUpApntDtlPBlood);
        tvUpApntDtlPrescriptionCount = (TextView)findViewById(R.id.tvUpApntDtlPrescriptionCount);
        tvUpApntDtlPrescrptn = (TextView)findViewById(R.id.tvUpApntDtlPrescrptn);
        tvUpApntDtlPLabCount = (TextView)findViewById(R.id.tvUpApntDtlPLabCount);
        tvUpApntDtlPAllergy = (TextView)findViewById(R.id.tvUpApntDtlPAllergy);
        tvUpApntDtlLabNo = (TextView)findViewById(R.id.tvUpApntDtlLabNo);
        grdUpApntDtlLabReports = (ExpandableHeightGridView)findViewById(R.id.grdUpApntDtlLabReports);
        rplUpApntDtlPCancel = (RippleView)findViewById(R.id.rplUpApntDtlPCancel);

        ivUpApntDtlPCallIcon = (ImageView)findViewById(R.id.ivUpApntDtlPCallIcon);
        ivUpApntDtlPCallbtn = (ImageView)findViewById(R.id.ivUpApntDtlPCallbtn);

        Intent ii = getIntent();
        post = ii.getStringExtra("post");
        date = ii.getStringExtra("date");

        if (date.equalsIgnoreCase("1")){
            upcomingApointItem = DateApointments.upcomingApointItemArrayList.get(Integer.parseInt(post));
        }else {
            upcomingApointItem = UpcomingApointFragment.upcomingApointItemArrayList.get(Integer.parseInt(post));
        }

        tvUpApntDtlPName.setText(upcomingApointItem.getPname());
        tvUpApntDtlPId.setText("Patient ID : "+upcomingApointItem.getPid());
        tvUpApntDtlPTime.setText(upcomingApointItem.getTime());
        tvUpApntDtlPDate.setText(upcomingApointItem.getDate());
        tvUpApntDtlCallStartIn.setText("Call starts in "+upcomingApointItem.getStrt());
        tvUpApntDtlPHeight.setText(upcomingApointItem.getHeight());
        tvUpApntDtlPWeight.setText(upcomingApointItem.getWeight());
        tvUpApntDtlPBlood.setText(upcomingApointItem.getBloodgroup());
        tvUpApntDtlPAllergy.setText(upcomingApointItem.getAllergies());

        if ((upcomingApointItem.getMedications()).equalsIgnoreCase("")){
            tvUpApntDtlPrescriptionCount.setText("0");
            tvUpApntDtlPrescrptn.setText("No Prescription Available");
        }else {
            tvUpApntDtlPrescrptn.setText(upcomingApointItem.getMedications());
        }

        if ((upcomingApointItem.getLstReport()).equals("0")){
            tvUpApntDtlPLabCount.setText("0");
            tvUpApntDtlLabNo.setVisibility(View.VISIBLE);
        }else {

            int lnth = upcomingApointItem.getLstReport().size();
            tvUpApntDtlPLabCount.setText(lnth+"");
            tvUpApntDtlLabNo.setVisibility(View.GONE);
            reportAdapter = new ReportAdapter(UpcomingApointDetail.this,upcomingApointItem.getLstReport());
            grdUpApntDtlLabReports.setAdapter(reportAdapter);
            grdUpApntDtlLabReports.setExpanded(true);
        }

        if ((upcomingApointItem.getType()).equalsIgnoreCase("voice")){
            ivUpApntDtlPCallbtn.setImageDrawable(getResources().getDrawable(R.drawable.call_unslctd));
            ivUpApntDtlPCallIcon.setImageDrawable(getResources().getDrawable(R.drawable.call_unslctd));
            tvUpApntDtlPCallType.setText("Voice Call");
        }else {
            ivUpApntDtlPCallbtn.setImageDrawable(getResources().getDrawable(R.drawable.video_call));
            ivUpApntDtlPCallIcon.setImageDrawable(getResources().getDrawable(R.drawable.video_call));
            tvUpApntDtlPCallType.setText("Video Call");
        }

        grdUpApntDtlLabReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String path = upcomingApointItem.getLstReport().get(position);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path)));
                }catch (Exception e){
                    Toast.makeText(UpcomingApointDetail.this,"No pdf file opener available" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        rplUpApntDtlPCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog = new Dialog(UpcomingApointDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_reason_dialog);
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView tvCancelRw = (TextView)dialog.findViewById(R.id.tvCancelRw);
                TextView tvPostRw = (TextView)dialog.findViewById(R.id.tvPostRw);
                final EditText etReason = (EditText)dialog.findViewById(R.id.userReview);


                tvCancelRw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tvPostRw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!processing) {

                            if (!Comman.isConnectionAvailable(UpcomingApointDetail.this)) {
                                Toast.makeText(UpcomingApointDetail.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            } else {
                                reason = etReason.getText().toString();
                                prgLoading.setVisibility(View.VISIBLE);
                                processing = true;
                                new cancelApointTask().execute();
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {

        }
    }

    private class cancelApointTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                DefaultHttpClient client = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.canclApointUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(UpcomingApointDetail.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("appointment_id", upcomingApointItem.getId()));
                nameValuePairs.add(new BasicNameValuePair("reason", reason));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse res = httpclient.execute(httppost);
                HttpEntity resEntity = res.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                return response_str;
                //------------------>>
            } catch (ParseException | IOException e1) {
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

                        String message = jsonObject.getString("message");
                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            Toast.makeText(UpcomingApointDetail.this, message, Toast.LENGTH_SHORT).show();

                            if (date.equalsIgnoreCase("1")){
                                DateApointments.upcomingApointItemArrayList.remove(Integer.parseInt(post));
                            }else {
                                UpcomingApointFragment.upcomingApointItemArrayList.remove(Integer.parseInt(post));
                            }

                            Comman.setPreferences(UpcomingApointDetail.this,"Cancelled","1");
                            dialog.dismiss();
                            prgLoading.setVisibility(View.GONE);
                            onBackPressed();

                        } else {

                            Toast.makeText(UpcomingApointDetail.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(UpcomingApointDetail.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            prgLoading.setVisibility(View.GONE);
            processing = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}

