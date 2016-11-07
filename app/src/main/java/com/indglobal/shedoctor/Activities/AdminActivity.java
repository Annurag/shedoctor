package com.indglobal.shedoctor.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 8/29/16.
 */
public class AdminActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener {

    ProgressBar prgLoading;

    TextView tvBackSignIn, tvBackSignUp, tvSignIn, tvSignUp, tvSignupBackSignIn,tvForgotPass;
    LinearLayout llSignIn, llSignUp;

    RippleView tvLoginNext, tvSignUpNext;

    EditText etSignUpUserName, etSignUpMobile, etSignUpEmail, etSignUpRegistrno, etSignUpPass, etLoginUsername, etLoginPass;
    String upName, upMobile, upEmail, upRegistrno, upPass, inuser, inPass;

    boolean processing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.administrator_main);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        tvBackSignIn = (TextView) findViewById(R.id.tvBackSignIn);
        tvBackSignUp = (TextView) findViewById(R.id.tvBackSignUp);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);
        llSignUp = (LinearLayout) findViewById(R.id.llSignUp);

        tvLoginNext = (RippleView) findViewById(R.id.tvLoginNext);
        tvSignUpNext = (RippleView) findViewById(R.id.tvSignUpNext);
        tvSignupBackSignIn = (TextView) findViewById(R.id.tvSignupBackSignIn);
        tvForgotPass = (TextView)findViewById(R.id.tvForgotPass);

        etSignUpUserName = (EditText) findViewById(R.id.etSignUpUserName);
        etSignUpMobile = (EditText) findViewById(R.id.etSignUpMobile);
        etSignUpEmail = (EditText) findViewById(R.id.etSignUpEmail);
        etSignUpRegistrno = (EditText) findViewById(R.id.etSignUpRegistrno);
        etSignUpPass = (EditText) findViewById(R.id.etSignUpPass);
        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPass = (EditText) findViewById(R.id.etLoginPass);

        tvSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        llSignIn.setOnClickListener(this);
        llSignUp.setOnClickListener(this);
        tvBackSignIn.setOnClickListener(this);
        tvBackSignUp.setOnClickListener(this);
        tvForgotPass.setOnClickListener(this);

        tvLoginNext.setOnRippleCompleteListener(this);
        tvSignUpNext.setOnRippleCompleteListener(this);
        tvSignupBackSignIn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.tvSignIn:

                if (!processing) {

                    llSignIn.setVisibility(View.VISIBLE);
                    llSignUp.setVisibility(View.GONE);

                    tvBackSignIn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tvBackSignUp.setBackgroundColor(getResources().getColor(R.color.white));

                    tvSignUp.setTextColor(getResources().getColor(R.color.darkGray));
                    tvSignIn.setTextColor(getResources().getColor(R.color.lightBlack));

                }

                break;

            case R.id.tvSignUp:

                if (!processing) {

                    llSignIn.setVisibility(View.GONE);
                    llSignUp.setVisibility(View.VISIBLE);

                    tvBackSignIn.setBackgroundColor(getResources().getColor(R.color.white));
                    tvBackSignUp.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    tvSignUp.setTextColor(getResources().getColor(R.color.lightBlack));
                    tvSignIn.setTextColor(getResources().getColor(R.color.darkGray));

                }
                break;

            case R.id.tvSignupBackSignIn:

                if (!processing) {

                    llSignIn.setVisibility(View.VISIBLE);
                    llSignUp.setVisibility(View.GONE);

                    tvBackSignIn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tvBackSignUp.setBackgroundColor(getResources().getColor(R.color.white));

                    tvSignUp.setTextColor(getResources().getColor(R.color.darkGray));
                    tvSignIn.setTextColor(getResources().getColor(R.color.lightBlack));

                }
                break;

            case R.id.tvForgotPass:
                Intent ii = new Intent(AdminActivity.this,ForgotPassActivity.class);
                startActivity(ii);
                break;
        }


    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {

            case R.id.tvLoginNext:

                inuser = etLoginUsername.getText().toString();
                inPass = etLoginPass.getText().toString();

                if (!Comman.isConnectionAvailable(AdminActivity.this)) {
                    Toast.makeText(AdminActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();

                } else if (inuser.equalsIgnoreCase("")) {
                    Toast.makeText(AdminActivity.this, "Please provide your email/mobile !", Toast.LENGTH_SHORT).show();

                } else if (!Comman.emailValidator(inuser)) {

                    if (inuser.length() < 10) {
                        Toast.makeText(AdminActivity.this, "Please provide a valid mobile no.!", Toast.LENGTH_SHORT).show();
                    } else if (inPass.equalsIgnoreCase("")) {
                        Toast.makeText(AdminActivity.this, "Please provide your password!", Toast.LENGTH_SHORT).show();

                    } else {
                        new SignInTask().execute();
                        processing = true;
                        prgLoading.setVisibility(View.VISIBLE);
                    }

                } else if (inPass.equalsIgnoreCase("")) {
                    Toast.makeText(AdminActivity.this, "Please provide your password!", Toast.LENGTH_SHORT).show();

                } else {

                    prgLoading.setVisibility(View.VISIBLE);
                    new SignInTask().execute();
                    processing = true;
                }


                break;

            case R.id.tvSignUpNext:

                if (!processing) {

                    upName = etSignUpUserName.getText().toString();
                    upMobile = etSignUpMobile.getText().toString();
                    upEmail = etSignUpEmail.getText().toString();
                    upRegistrno = etSignUpRegistrno.getText().toString();
                    upPass = etSignUpPass.getText().toString();

                    if (!Comman.isConnectionAvailable(AdminActivity.this)) {
                        Toast.makeText(AdminActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();

                    } else if (upName.equalsIgnoreCase("")) {
                        Toast.makeText(AdminActivity.this, "Please provide your name!", Toast.LENGTH_SHORT).show();

                    } else if (upMobile.equalsIgnoreCase("")) {
                        Toast.makeText(AdminActivity.this, "Please provide your mobile no.", Toast.LENGTH_SHORT).show();

                    } else if (upMobile.length() < 10) {
                        Toast.makeText(AdminActivity.this, "Please provide a valid mobile no.!", Toast.LENGTH_SHORT).show();

                    } else if (upEmail.equalsIgnoreCase("")) {
                        Toast.makeText(AdminActivity.this, "Please provide your email id !", Toast.LENGTH_SHORT).show();

                    } else if (!Comman.emailValidator(upEmail)) {
                        Toast.makeText(AdminActivity.this, "Please provide a valid email id !", Toast.LENGTH_SHORT).show();

                    } else if (upRegistrno.equalsIgnoreCase("")) {
                        Toast.makeText(AdminActivity.this, "Please provide your medical registration no.!", Toast.LENGTH_SHORT).show();

                    } else if (upPass.equalsIgnoreCase("")) {
                        Toast.makeText(AdminActivity.this, "Please provide a password!", Toast.LENGTH_SHORT).show();
                    } else {

                        prgLoading.setVisibility(View.VISIBLE);
                        new SignUpTask().execute();
                        processing = true;
                    }
                }

//                Intent iiSignup = new Intent(AdminActivity.this,UploadDocActivity.class);
//                startActivity(iiSignup);
                break;
        }

    }

    private class SignInTask extends AsyncTask<String, Void, String> {

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

// Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.signinUrl);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(productListUrl);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username", inuser));
                nameValuePairs.add(new BasicNameValuePair("password", inPass));
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

//                        String message = jsonObject.getString("message");
//                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            etLoginUsername.setText("");
                            etLoginPass.setText("");

                            JSONObject jsobj = jsonObject.getJSONObject("data");

                            String token = jsobj.getString("token");

                            Comman.setPreferences(AdminActivity.this, "TOKEN", token);

                            new ShortProfileTask().execute();

                        } else {

                            String message = jsonObject.getString("message");
                            Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
                            prgLoading.setVisibility(View.GONE);
                            processing = false;

                        }
                    } else {

                        prgLoading.setVisibility(View.GONE);
                        processing = false;
                        Toast.makeText(AdminActivity.this, "Server Down Object null", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    prgLoading.setVisibility(View.GONE);
                    processing = false;
                    Toast.makeText(AdminActivity.this, "Server Down", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AdminActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

                prgLoading.setVisibility(View.GONE);
                processing = false;
            }


        }
    }

    private class SignUpTask extends AsyncTask<String, Void, String> {

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

// Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.signupUrl);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(productListUrl);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("user_type", "doctor"));
                nameValuePairs.add(new BasicNameValuePair("name", upName));
                nameValuePairs.add(new BasicNameValuePair("email", upEmail));
                nameValuePairs.add(new BasicNameValuePair("mobileno", upMobile));
                nameValuePairs.add(new BasicNameValuePair("pass", upPass));
                nameValuePairs.add(new BasicNameValuePair("medical_registration_no", upRegistrno));
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

                            etSignUpUserName.setText("");
                            etSignUpEmail.setText("");
                            etSignUpMobile.setText("");
                            etSignUpRegistrno.setText("");
                            etSignUpPass.setText("");

                            Comman.setPreferences(AdminActivity.this, "NAME", upName);
                            Comman.setPreferences(AdminActivity.this, "EMAIL", upEmail);
                            Comman.setPreferences(AdminActivity.this, "PHONE", upMobile);
                            Comman.setPreferences(AdminActivity.this, "REGNO", upRegistrno);

                            prgLoading.setVisibility(View.GONE);

                            Intent ii = new Intent(AdminActivity.this, VerifyLinkActivity.class);
                            startActivity(ii);

                        } else {

                            Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(AdminActivity.this, "Server Down Object null", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(AdminActivity.this, "Server Down", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AdminActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            prgLoading.setVisibility(View.GONE);
            processing = false;
        }
    }

    private class ShortProfileTask extends AsyncTask<String, Void, String> {

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

// Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.shortPrflUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(AdminActivity.this, "TOKEN");
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

                        String message = jsonObject.getString("message");
                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            String prefix = jsonObject1.getString("prefix");
                            String name = jsonObject1.getString("name");
                            String shedct_id = jsonObject1.getString("shedct_id");
                            String profile_image = jsonObject1.getString("profile_image");
                            String registration_no = jsonObject1.getString("registration_no");
                            String online = jsonObject1.getString("online");
                            Boolean is_verified = jsonObject1.getBoolean("is_verified");

                            Comman.setPreferences(AdminActivity.this, "PREF", prefix);
                            Comman.setPreferences(AdminActivity.this, "NAME", name);
                            Comman.setPreferences(AdminActivity.this, "PRFLIMG", profile_image);
                            Comman.setPreferences(AdminActivity.this, "ONLINE", online);
                            Comman.setPreferences(AdminActivity.this, "REGNO", registration_no);
                            Comman.setPreferences(AdminActivity.this, "SDOCID", shedct_id);



                            try {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject("documents");

                                String medical_registration_certificate_verified = jsonObject2.getString("medical_registration_certificate_verified");
                                String government_id_verified = jsonObject2.getString("government_id_verified");
                                String degree_verified = jsonObject2.getString("degree_verified");
                                String medical_registration_certificate_reject_reason = jsonObject2.getString("medical_registration_certificate_reject_reason");
                                String medical_degree_reject_reason = jsonObject2.getString("medical_degree_reject_reason");
                                String government_id_reject_reason = jsonObject2.getString("government_id_reject_reason");

                                Comman.setPreferences(AdminActivity.this, "medical_registration_certificate_verified", medical_registration_certificate_verified);
                                Comman.setPreferences(AdminActivity.this, "government_id_verified", government_id_verified);
                                Comman.setPreferences(AdminActivity.this, "degree_verified", degree_verified);

                                if (medical_registration_certificate_reject_reason.equalsIgnoreCase("null")) {
                                    Comman.setPreferences(AdminActivity.this, "medical_registration_certificate_reject_reason", "");

                                } else {
                                    Comman.setPreferences(AdminActivity.this, "medical_registration_certificate_reject_reason", medical_registration_certificate_reject_reason);

                                }

                                if (medical_degree_reject_reason.equalsIgnoreCase("null")) {
                                    Comman.setPreferences(AdminActivity.this, "medical_degree_reject_reason", "");

                                } else {
                                    Comman.setPreferences(AdminActivity.this, "medical_degree_reject_reason", medical_degree_reject_reason);

                                }

                                if (government_id_reject_reason.equalsIgnoreCase("null")) {
                                    Comman.setPreferences(AdminActivity.this, "government_id_reject_reason", "");

                                } else {
                                    Comman.setPreferences(AdminActivity.this, "government_id_reject_reason", government_id_reject_reason);

                                }

                                if (medical_registration_certificate_verified.equalsIgnoreCase("Verified") && degree_verified.equalsIgnoreCase("Verified") && government_id_verified.equalsIgnoreCase("Verified")) {

                                    Intent ii = new Intent(AdminActivity.this, BaseActivity.class);
                                    Comman.setPreferences(AdminActivity.this, "userLogin", "1");
                                    startActivity(ii);

                                } else if (medical_registration_certificate_verified.equalsIgnoreCase("Under Review") || degree_verified.equalsIgnoreCase("Under Review") || government_id_verified.equalsIgnoreCase("Under Review")) {

                                    Intent intent = new Intent(AdminActivity.this, UploadDocActivity.class);
                                    startActivity(intent);

                                } else {

                                    Intent intent = new Intent(AdminActivity.this, UploadDocActivity.class);
                                    startActivity(intent);

                                }

                            }catch (Exception e){
                                e.printStackTrace();
                                prgLoading.setVisibility(View.GONE);
                                Intent intent = new Intent(AdminActivity.this, UploadDocActivity.class);
                                startActivity(intent);
                            }




                            prgLoading.setVisibility(View.GONE);

                        } else {

                            Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(AdminActivity.this, "Server Down Object null", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(AdminActivity.this, "Server Down", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AdminActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            prgLoading.setVisibility(View.GONE);
            processing = false;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminActivity.this);
        builder1.setTitle("Exit");
        builder1.setMessage("Are you sure you want to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent setIntent = new Intent(Intent.ACTION_MAIN);
                        setIntent.addCategory(Intent.CATEGORY_HOME);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);
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


}