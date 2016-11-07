package com.indglobal.shedoctor.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.LabGridAdapter;
import com.indglobal.shedoctor.Adapters.MedicnListAdapter;
import com.indglobal.shedoctor.Adapters.UpcomingApointAdapter;
import com.indglobal.shedoctor.Beans.MedicinItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 10/1/16.
 */
public class PrescriptionViewActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    TextView tvPrscptnDtlDate,tvPrscptnDtlDocName,tvPrscptnDtlDocSpec,tvPrscptnDtlPtntName,
            tvPrscptnDtlPtntId,tvPrscptnDtlDiagnosRprt,tvPrscptnDtlLabNotAvail,tvPrscptnDtlNextVisit,
            tvPrscptnDtlMdcnNotAvail,tvPrscptnDtlDocBtmName,tvPrscptnDtlDocBtmSpec,tvPrscptnDtlDocBtmRegNo,tvNoData;
    ImageView ivPrscptnDtlDocSign;
    ExpandableHeightGridView grdPrscptnDtlLab;
    RecyclerView rvPrscptnDtlMdcn;
    RelativeLayout rlMainNew;

    String apointId;

    ArrayList<String> arrayListLabs = new ArrayList<>();
    LabGridAdapter labGridAdapter;
    MedicinItem medicinItem;
    ArrayList<MedicinItem> medicinItemArrayList;
    MedicnListAdapter medicnListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.prescription_view);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rlMainNew = (RelativeLayout)findViewById(R.id.rlMainNew);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvPrscptnDtlDate = (TextView)findViewById(R.id.tvPrscptnDtlDate);
        tvPrscptnDtlDocName = (TextView)findViewById(R.id.tvPrscptnDtlDocName);
        tvPrscptnDtlDocSpec = (TextView)findViewById(R.id.tvPrscptnDtlDocSpec);
        tvPrscptnDtlPtntName = (TextView)findViewById(R.id.tvPrscptnDtlPtntName);
        tvPrscptnDtlPtntId = (TextView)findViewById(R.id.tvPrscptnDtlPtntId);
        tvPrscptnDtlDiagnosRprt = (TextView)findViewById(R.id.tvPrscptnDtlDiagnosRprt);
        tvPrscptnDtlLabNotAvail = (TextView)findViewById(R.id.tvPrscptnDtlLabNotAvail);
        tvPrscptnDtlMdcnNotAvail = (TextView)findViewById(R.id.tvPrscptnDtlMdcnNotAvail);
        tvPrscptnDtlDocBtmName = (TextView)findViewById(R.id.tvPrscptnDtlDocBtmName);
        tvPrscptnDtlDocBtmSpec = (TextView)findViewById(R.id.tvPrscptnDtlDocBtmSpec);
        tvPrscptnDtlDocBtmRegNo = (TextView)findViewById(R.id.tvPrscptnDtlDocBtmRegNo);
        tvPrscptnDtlNextVisit = (TextView)findViewById(R.id.tvPrscptnDtlNextVisit);
        tvNoData = (TextView)findViewById(R.id.tvNoData);

        ivPrscptnDtlDocSign = (ImageView)findViewById(R.id.ivPrscptnDtlDocSign);
        grdPrscptnDtlLab = (ExpandableHeightGridView)findViewById(R.id.grdPrscptnDtlLab);
        rvPrscptnDtlMdcn = (RecyclerView)findViewById(R.id.rvPrscptnDtlMdcn);

        Intent ii = getIntent();
        apointId = ii.getStringExtra("apointId");

        medicinItemArrayList = new ArrayList<>();

        if(!Comman.isConnectionAvailable(PrescriptionViewActivity.this)){
            Toast.makeText(PrescriptionViewActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            rlMainNew.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            new getPrescriptionDtlTask().execute();
        }


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


    private class getPrescriptionDtlTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.getPrscrptnUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(PrescriptionViewActivity.this, "TOKEN");
                HttpGet httppost = new HttpGet(productListUrl + "?token=" + token+"&appointment_id="+apointId);


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

                        if (success) {

                            try{

                                JSONObject object = jsonObject.getJSONObject("data");

                                String report = object.getString("report");
                                String next_visit = object.getString("next_visit");
                                String date = object.getString("date");

                                tvPrscptnDtlDiagnosRprt.setText(report);
                                tvPrscptnDtlNextVisit.setText("Next Visit:- "+next_visit);

                                String newdate = date.substring(0,10);
                                tvPrscptnDtlDate.setText(newdate);


                                try {
                                    JSONArray jsonLab = object.getJSONArray("lab_tests");
                                    for (int i = 0;i<jsonLab.length();i++){

                                        String lab = jsonLab.getString(i);
                                        arrayListLabs.add(lab);
                                    }

                                    labGridAdapter = new LabGridAdapter(PrescriptionViewActivity.this,arrayListLabs);
                                    grdPrscptnDtlLab.setAdapter(labGridAdapter);
                                    grdPrscptnDtlLab.setExpanded(true);

                                    if (jsonLab.length()==0){
                                        tvPrscptnDtlLabNotAvail.setVisibility(View.VISIBLE);
                                    }else {
                                        tvPrscptnDtlLabNotAvail.setVisibility(View.GONE);
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                    tvPrscptnDtlLabNotAvail.setVisibility(View.VISIBLE);
                                }


                                try {
                                    JSONArray jsonArrayMedcn = object.getJSONArray("medicines");
                                    for (int j = 0; j<jsonArrayMedcn.length(); j++){

                                        JSONObject jsonObjectMdcn = jsonArrayMedcn.getJSONObject(j);

                                        String name = jsonObjectMdcn.getString("name");
                                        String type = jsonObjectMdcn.getString("type");
                                        String morning = jsonObjectMdcn.getString("morning");
                                        String afternoon = jsonObjectMdcn.getString("afternoon");
                                        String evening = jsonObjectMdcn.getString("evening");
                                        String night = jsonObjectMdcn.getString("night");
                                        String note = jsonObjectMdcn.getString("note");

                                        medicinItem  = new MedicinItem(name,type,morning,afternoon,evening,night,note);
                                        medicinItemArrayList.add(medicinItem);

                                    }

                                    if (jsonArrayMedcn.length()==0){
                                        tvPrscptnDtlMdcnNotAvail.setVisibility(View.VISIBLE);
                                    }else {
                                        tvPrscptnDtlMdcnNotAvail.setVisibility(View.GONE);
                                    }

                                    medicnListAdapter = new MedicnListAdapter(PrescriptionViewActivity.this,medicinItemArrayList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PrescriptionViewActivity.this);
                                    rvPrscptnDtlMdcn.setLayoutManager(mLayoutManager);
                                    rvPrscptnDtlMdcn.setAdapter(medicnListAdapter);



                                }catch (Exception e){
                                    e.printStackTrace();
                                    tvPrscptnDtlMdcnNotAvail.setVisibility(View.VISIBLE);
                                }


                                JSONObject jsonObjDoc = object.getJSONObject("doctor");
                                String docname = jsonObjDoc.getString("name");
                                String specialty = jsonObjDoc.getString("specialty");
                                String registration_no = jsonObjDoc.getString("registration_no");
                                String signature = jsonObjDoc.getString("signature");

                                if (!signature.equalsIgnoreCase("")){
                                    Bitmap myBitmapAgain = decodeBase64(signature);
                                    ivPrscptnDtlDocSign.setImageBitmap(myBitmapAgain);
                                }

                                tvPrscptnDtlDocName.setText(docname);
                                tvPrscptnDtlDocBtmName.setText(docname);
                                tvPrscptnDtlDocSpec.setText("Speciality : "+specialty);
                                tvPrscptnDtlDocBtmRegNo.setText(registration_no);

                                JSONObject jsonObjPtnt = object.getJSONObject("patient");
                                String patntName = jsonObjPtnt.getString("name");
                                String shedct_id = jsonObjPtnt.getString("shedct_id");

                                tvPrscptnDtlPtntName.setText(patntName);
                                tvPrscptnDtlPtntId.setText("Patient ID : "+shedct_id);

                                rlMainNew.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);


                            }catch (Exception e){
                                e.printStackTrace();
                                rlMainNew.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            Toast.makeText(PrescriptionViewActivity.this, message, Toast.LENGTH_SHORT).show();
                            rlMainNew.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                            prgLoading.setVisibility(View.GONE);

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                rlMainNew.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(PrescriptionViewActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}