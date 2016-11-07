package com.indglobal.shedoctor.Activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.BankAdapter;
import com.indglobal.shedoctor.Adapters.EditSpecialityAdapter;
import com.indglobal.shedoctor.Beans.SpecialityItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 10/09/2016.
 */
public class BankdetailsActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    EditText etbankdetailAccntNumbr, etbankdetailReAccntNumbr, etbankdetailIFScode;
    Spinner spinBankName;
    RadioButton rbSavings, rbCurrent;
    RadioGroup rgbankdetailRadiogrp;
    RippleView rplAddNow, rplCancel,rplRemove;
    RelativeLayout rlMain,rlBankDetl;
    TextView tvbankdtlBankName,tvbankdtlIfsCode,tvbankdtlAccntNumber,tvbankdtlAccntType;

    String name, number, reaccnumber, ifscode,rbSelected="Savings";
    ArrayList<String> bankArrayList = new ArrayList<>();
    BankAdapter bankAdapter;

    boolean detlAvail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.bank_details);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        spinBankName = (Spinner) findViewById(R.id.spinBankName);
        etbankdetailAccntNumbr = (EditText) findViewById(R.id.etbankdetailAccntNumbr);
        etbankdetailReAccntNumbr = (EditText) findViewById(R.id.etbankdetailReAccntNumbr);
        etbankdetailIFScode = (EditText) findViewById(R.id.etbankdetailIFScode);
        rgbankdetailRadiogrp = (RadioGroup)findViewById(R.id.rgbankdetailRadiogrp);

        tvbankdtlBankName = (TextView)findViewById(R.id.tvbankdtlBankName);
        tvbankdtlIfsCode = (TextView)findViewById(R.id.tvbankdtlIfsCode);
        tvbankdtlAccntNumber = (TextView)findViewById(R.id.tvbankdtlAccntNumber);
        tvbankdtlAccntType = (TextView)findViewById(R.id.tvbankdtlAccntType);

        rlBankDetl = (RelativeLayout)findViewById(R.id.rlBankDetl);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);

        rbSavings = (RadioButton) findViewById(R.id.rbSavings);
        rbCurrent = (RadioButton) findViewById(R.id.rbCurrent);

        rplAddNow = (RippleView) findViewById(R.id.rplAddNow);
        rplCancel = (RippleView) findViewById(R.id.rplCancel);
        rplRemove = (RippleView)findViewById(R.id.rplRemove);

        rplRemove.setOnRippleCompleteListener(this);

        if (!Comman.isConnectionAvailable(BankdetailsActivity.this)){
            Toast.makeText(BankdetailsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            rlMain.setVisibility(View.GONE);
            rlBankDetl.setVisibility(View.GONE);

        }else {
            prgLoading.setVisibility(View.VISIBLE);
            new getBanckDetailTask().execute();
        }


        rgbankdetailRadiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rbSavings:
                        rbSelected="Savings";
                        break;

                    case R.id.rbCurrent:
                        rbSelected="Current";
                        break;
                }
            }
        });

        spinBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = bankArrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rplAddNow.setOnRippleCompleteListener(this);
        rplCancel.setOnRippleCompleteListener(this);
        rbSavings.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

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
            case R.id.rplAddNow:


                number = etbankdetailAccntNumbr.getText().toString();
                reaccnumber = etbankdetailReAccntNumbr.getText().toString();
                ifscode = etbankdetailIFScode.getText().toString();

                if (!Comman.isConnectionAvailable(BankdetailsActivity.this)) {
                    Toast.makeText(BankdetailsActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();

                } else if (name.equalsIgnoreCase("")) {
                    Toast.makeText(BankdetailsActivity.this, "Please provide account name!", Toast.LENGTH_SHORT).show();

                }else if (number.equalsIgnoreCase("")) {
                    Toast.makeText(BankdetailsActivity.this, "Please provide account number!", Toast.LENGTH_SHORT).show();

                }else if (reaccnumber.equalsIgnoreCase("")) {
                    Toast.makeText(BankdetailsActivity.this, "Please provide account number!", Toast.LENGTH_SHORT).show();

                }else if (!reaccnumber.equalsIgnoreCase(number)) {
                    Toast.makeText(BankdetailsActivity.this, "Please check your account number!", Toast.LENGTH_SHORT).show();

                 } else if(ifscode.equalsIgnoreCase("")) {
                    Toast.makeText(BankdetailsActivity.this, "Please provide IFS Code!", Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    new addBankDetailTask().execute();
                }
                break;

            case R.id.rplRemove:

                if (!Comman.isConnectionAvailable(BankdetailsActivity.this)) {
                    Toast.makeText(BankdetailsActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(BankdetailsActivity.this);
                    builder1.setTitle("Delete Bank Details");
                    builder1.setMessage("Are you sure you want to remove bank details?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    prgLoading.setVisibility(View.VISIBLE);
                                    new deleteBankDetailTask().execute();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

                break;

            case R.id.rplCancel:
                if (detlAvail){
                    rlBankDetl.setVisibility(View.VISIBLE);
                    rlMain.setVisibility(View.GONE);
                }else {
                    onBackPressed();
                }

                break;
        }
    }

    private class getBanckDetailTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.bankdtlUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(BankdetailsActivity.this, "TOKEN");
                HttpGet httppost = new HttpGet(productListUrl + "?token=" + token);


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
                                String bank_name = object.getString("bank_name");
                                String account_type = object.getString("account_type");
                                String ifsc = object.getString("ifsc");
                                String account_no = object.getString("account_no");

                                tvbankdtlBankName.setText(bank_name);
                                tvbankdtlAccntType.setText(account_type);
                                tvbankdtlAccntNumber.setText(account_no);
                                tvbankdtlIfsCode.setText(ifsc);
                                rlBankDetl.setVisibility(View.VISIBLE);
                                rlMain.setVisibility(View.GONE);
                                prgLoading.setVisibility(View.GONE);
                                detlAvail = true;

                            }catch (Exception e){
                                e.printStackTrace();
                                rlMain.setVisibility(View.VISIBLE);
                                rlBankDetl.setVisibility(View.GONE);
                                new bankNameTask().execute();
                                detlAvail = false;
                            }




                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            Toast.makeText(BankdetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            rlMain.setVisibility(View.VISIBLE);
                            rlBankDetl.setVisibility(View.GONE);
                            new bankNameTask().execute();
                            detlAvail = false;

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                rlMain.setVisibility(View.VISIBLE);
                new bankNameTask().execute();
                rlBankDetl.setVisibility(View.GONE);
                Toast.makeText(BankdetailsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

                detlAvail = false;
            }


        }
    }

    private class addBankDetailTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.bankdtlUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(BankdetailsActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("bank_name", name);
                json.put("ifsc", ifscode);
                json.put("account_type", rbSelected);
                json.put("account_no", number);
                json.put("confirm_account_no", reaccnumber);

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
                        String message = jsonObject.getString("message");
                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            try{
                                JSONObject object = jsonObject.getJSONObject("data");
                                String bank_name = object.getString("bank_name");
                                String account_type = object.getString("account_type");
                                String ifsc = object.getString("ifsc");
                                String account_no = object.getString("account_no");

                                tvbankdtlBankName.setText(bank_name);
                                tvbankdtlAccntType.setText(account_type);
                                tvbankdtlAccntNumber.setText(account_no);
                                tvbankdtlIfsCode.setText(ifsc);
                                rlBankDetl.setVisibility(View.VISIBLE);
                                rlMain.setVisibility(View.GONE);
                                prgLoading.setVisibility(View.GONE);
                                Toast.makeText(BankdetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                                detlAvail = true;

                            }catch (Exception e){
                                e.printStackTrace();
                                rlMain.setVisibility(View.VISIBLE);
                                rlBankDetl.setVisibility(View.GONE);
                                new bankNameTask().execute();
                                detlAvail = false;
                            }

                        } else {

                            Toast.makeText(BankdetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            rlMain.setVisibility(View.VISIBLE);
                            rlBankDetl.setVisibility(View.GONE);
                            new bankNameTask().execute();
                            detlAvail = false;

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                rlMain.setVisibility(View.VISIBLE);
                new bankNameTask().execute();
                rlBankDetl.setVisibility(View.GONE);
                Toast.makeText(BankdetailsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

                detlAvail = false;
            }


        }
    }

    private class deleteBankDetailTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.bankdtlUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(BankdetailsActivity.this, "TOKEN");
                HttpDelete httppost = new HttpDelete(productListUrl + "?token=" + token);


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

                            detlAvail = false;
                            Toast.makeText(BankdetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            rlBankDetl.setVisibility(View.GONE);
                            rlMain.setVisibility(View.VISIBLE);
                           // prgLoading.setVisibility(View.GONE);
                            new bankNameTask().execute();


                        } else {

                            detlAvail = true;
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(BankdetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            rlMain.setVisibility(View.GONE);
                            rlBankDetl.setVisibility(View.VISIBLE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                rlMain.setVisibility(View.GONE);
                rlBankDetl.setVisibility(View.VISIBLE);
                Toast.makeText(BankdetailsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

                detlAvail = true;
            }

            prgLoading.setVisibility(View.GONE);

        }
    }

    private class bankNameTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.getBanksUrl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(productListUrl);

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

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                String ss = jsonArray.getString(i);

                                bankArrayList.add(ss);
                            }

                            bankAdapter = new BankAdapter(BankdetailsActivity.this,bankArrayList);
                            spinBankName.setAdapter(bankAdapter);
                            name = bankArrayList.get(0);
                            prgLoading.setVisibility(View.GONE);

                        } else {

                            prgLoading.setVisibility(View.GONE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(BankdetailsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
        public void onBackPressed () {
            super.onBackPressed();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            finish();
        }
    }


