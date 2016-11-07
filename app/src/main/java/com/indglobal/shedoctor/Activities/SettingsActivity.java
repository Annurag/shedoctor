package com.indglobal.shedoctor.Activities;

import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.AwardsAdapter;
import com.indglobal.shedoctor.Adapters.EducationAdapter;
import com.indglobal.shedoctor.Adapters.ServicesAdapter;
import com.indglobal.shedoctor.Adapters.SpeciltyAdapter;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Commans.UtilityList;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener {

    EditText etSettingsEmail,etSettingsMobile, etSettingsNewPswd,etSettingsConfirmPswd;
    RippleView rplChangePassword,rplSettingsCancel;
    Toolbar mtoolbar;
    ProgressBar prgLoading;
    RelativeLayout rlMain;

    String email,number,newpswd,confirmpswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.settings);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        etSettingsEmail = (EditText) findViewById(R.id.etSettingsEmail);
        etSettingsMobile = (EditText) findViewById(R.id.etSettingsMobile);
        etSettingsNewPswd = (EditText) findViewById(R.id.etSettingsNewPswd);
        etSettingsConfirmPswd = (EditText)findViewById(R.id.etSettingsConfirmPswd);

        rplChangePassword = (RippleView)findViewById(R.id.rplChangePassword);
        rplSettingsCancel = (RippleView)findViewById(R.id.rplSettingsCancel);

        rplChangePassword.setOnRippleCompleteListener(this);
        rplSettingsCancel.setOnRippleCompleteListener(this);

        if (!Comman.isConnectionAvailable(SettingsActivity.this)){
            Toast.makeText(SettingsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            rlMain.setVisibility(View.GONE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            new DrProfileUrl().execute();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {
            case R.id.rplChangePassword:

                email = etSettingsEmail.getText().toString();
                number = etSettingsMobile.getText().toString();
                newpswd = etSettingsNewPswd.getText().toString();
                confirmpswd = etSettingsConfirmPswd.getText().toString();

                if (!Comman.isConnectionAvailable(SettingsActivity.this)) {
                    Toast.makeText(SettingsActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();

                } else if (email.equalsIgnoreCase("")) {
                    Toast.makeText(SettingsActivity.this, "Please provide your email!", Toast.LENGTH_SHORT).show();

                }else if (!Comman.emailValidator(email)){
                    Toast.makeText(SettingsActivity.this,"Please provide a valid email id!",Toast.LENGTH_SHORT).show();

                }else if (number.equalsIgnoreCase("")) {
                    Toast.makeText(SettingsActivity.this, "Please provide your Mobile Number!", Toast.LENGTH_SHORT).show();

                }else if (number.length()<10) {
                    Toast.makeText(SettingsActivity.this, "Please provide your Mobile Number!", Toast.LENGTH_SHORT).show();

                }else if (newpswd.equalsIgnoreCase("")) {
                    Toast.makeText(SettingsActivity.this, "Please provide your new password!", Toast.LENGTH_SHORT).show();

                }else if (!confirmpswd.equalsIgnoreCase(newpswd)) {
                    Toast.makeText(SettingsActivity.this, "Please check your password!", Toast.LENGTH_SHORT).show();

                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    new addSettingTask().execute();
                }
                break;

            case R.id.rplSettingsCancel:
                onBackPressed();
                break;

        }
    }

    private class DrProfileUrl extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.prflUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(SettingsActivity.this, "TOKEN");

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

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                           String emaill = jsonObject1.getString("email");
                           String mobile = jsonObject1.getString("mobile");

                            etSettingsEmail.setText(emaill);
                            etSettingsMobile.setText(mobile);

                            prgLoading.setVisibility(View.GONE);
                            rlMain.setVisibility(View.VISIBLE);


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

                            rlMain.setVisibility(View.GONE);
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SettingsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            prgLoading.setVisibility(View.GONE);


        }
    }

    private class addSettingTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.chamgLoginStngUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(SettingsActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("mobile_no", number);
                json.put("password", newpswd);
                json.put("confirm_password", confirmpswd);

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

                            prgLoading.setVisibility(View.GONE);
                            etSettingsEmail.setText("");
                            etSettingsMobile.setText("");
                            etSettingsNewPswd.setText("");
                            etSettingsConfirmPswd.setText("");

                            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

                            Intent ii = new Intent(SettingsActivity.this,AdminActivity.class);
                            Comman.setPreferences(SettingsActivity.this, "userLogin", "0");
                            startActivity(ii);


                        } else {


                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(SettingsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

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