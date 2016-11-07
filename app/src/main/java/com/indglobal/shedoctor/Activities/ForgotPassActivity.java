package com.indglobal.shedoctor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
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
 * Created by Android on 10/7/16.
 */
public class ForgotPassActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener {

    ProgressBar prgLoading;
    EditText etMobileno,etOpt,etNewpwd,etConfirm;
    TextView tvResendcode;
    RippleView rplResetPassword,rplChangePassword,rplLoginNow,rplBack;
    RelativeLayout rlSuccessfulpwd,rlFrgtbnr;
    LinearLayout llresetpwd,llforgetpwd;
    String email,pwd,code,confirmpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.forget_password);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        etMobileno = (EditText)findViewById(R.id.etMobileno);
        etOpt = (EditText)findViewById(R.id.etOpt);
        etNewpwd = (EditText)findViewById(R.id.etNewpwd);
        etConfirm = (EditText)findViewById(R.id.etConfirm);
        rplBack = (RippleView) findViewById(R.id.rplBack);
        rplResetPassword = (RippleView) findViewById(R.id.rplResetPassword);
        rplChangePassword = (RippleView)findViewById(R.id.rplChangePassword);
        rplLoginNow = (RippleView)findViewById(R.id.rplLoginNow);
        rlSuccessfulpwd = (RelativeLayout)findViewById(R.id.rlSuccessfulpwd);
        rlFrgtbnr = (RelativeLayout)findViewById(R.id.rlFrgtbnr);
        llresetpwd = (LinearLayout)findViewById(R.id.llresetpwd);
        llforgetpwd = (LinearLayout)findViewById(R.id.llforgetpwd);
        tvResendcode = (TextView)findViewById(R.id.tvResendcode);

        rplChangePassword.setOnRippleCompleteListener(this);
        rplResetPassword.setOnRippleCompleteListener(this);
        rplLoginNow.setOnRippleCompleteListener(this);
        rplBack.setOnRippleCompleteListener(this);
        tvResendcode.setOnClickListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {

            case R.id.rplResetPassword:

                email = etMobileno.getText().toString();

                if(!Comman.isConnectionAvailable(ForgotPassActivity.this)) {
                    Toast.makeText(ForgotPassActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }else if(email.equalsIgnoreCase("")){
                    Toast.makeText(ForgotPassActivity.this, "Please enter Email id/Mobile number!", Toast.LENGTH_SHORT).show();
                }else if (!Comman.emailValidator(email)){
                    if (email.length()<10){
                        Toast.makeText(ForgotPassActivity.this,"Please provide a valid email id/mobile no !",Toast.LENGTH_SHORT).show();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        new frgtPassTask().execute();
                    }
                }else{
                    prgLoading.setVisibility(View.VISIBLE);
                    new frgtPassTask().execute();
                }
                break;

            case R.id.rplChangePassword:

                pwd = etNewpwd.getText().toString();
                code = etOpt.getText().toString();
                confirmpwd = etConfirm.getText().toString();

                if(!Comman.isConnectionAvailable(ForgotPassActivity.this)) {
                    Toast.makeText(ForgotPassActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }else if (code.equalsIgnoreCase("")) {
                    Toast.makeText(ForgotPassActivity.this, "Please provide your reset code!", Toast.LENGTH_SHORT).show();

                }else if (pwd.equalsIgnoreCase("")) {
                    Toast.makeText(ForgotPassActivity.this, "Please provide your new password!", Toast.LENGTH_SHORT).show();

                }else if (!confirmpwd.equalsIgnoreCase(pwd)) {
                    Toast.makeText(ForgotPassActivity.this, " Password didn't match!", Toast.LENGTH_SHORT).show();

                }else {

                    prgLoading.setVisibility(View.VISIBLE);
                    new resetPassTask().execute();
                }
                break;

            case R.id.rplLoginNow:

                    Intent ii = new Intent(ForgotPassActivity.this,AdminActivity.class);
                    startActivity(ii);

                break;

            case R.id.rplBack:
                onBackPressed();
                break;



        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvResendcode:
                email = etMobileno.getText().toString();

                if(!Comman.isConnectionAvailable(ForgotPassActivity.this)) {
                    Toast.makeText(ForgotPassActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }else if(email.equalsIgnoreCase("")){
                    Toast.makeText(ForgotPassActivity.this, "Please enter Email id/Mobile number!", Toast.LENGTH_SHORT).show();
                }else if (!Comman.emailValidator(email)){
                    if (email.length()<10){
                        Toast.makeText(ForgotPassActivity.this,"Please provide a valid email id/mobile no !",Toast.LENGTH_SHORT).show();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        new rsndTask().execute();
                    }
                }else{
                    prgLoading.setVisibility(View.VISIBLE);
                    new rsndTask().execute();
                }

                break;
        }
    }


    private class frgtPassTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.fotgtPassUrl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(productListUrl+"?username="+email);


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
                            Toast.makeText(ForgotPassActivity.this, message, Toast.LENGTH_SHORT).show();
                            prgLoading.setVisibility(View.GONE);
                            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                            llforgetpwd.setVisibility(View.GONE);
                            llforgetpwd.startAnimation(slideDown);

                            rlFrgtbnr.setBackground(getResources().getDrawable(R.drawable.rstbnr));
                            Animation slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                            llresetpwd.setVisibility(View.VISIBLE);
                            llresetpwd.startAnimation(slideup);

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(ForgotPassActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class rsndTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.resendUrl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(productListUrl+"?username="+email);


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
                            Toast.makeText(ForgotPassActivity.this, message, Toast.LENGTH_SHORT).show();
                            prgLoading.setVisibility(View.GONE);

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(ForgotPassActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class resetPassTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.resetPassUrl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(productListUrl);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("username", email));
                nameValuePairs.add(new BasicNameValuePair("password", pwd));
                nameValuePairs.add(new BasicNameValuePair("resetcode", code));
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
                            Toast.makeText(ForgotPassActivity.this, message, Toast.LENGTH_SHORT).show();
                            prgLoading.setVisibility(View.GONE);


                            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_right);
                            rlSuccessfulpwd.setVisibility(View.VISIBLE);
                            rlSuccessfulpwd.startAnimation(slideDown);

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(ForgotPassActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

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

