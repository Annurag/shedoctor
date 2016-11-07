package com.indglobal.shedoctor.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 16/09/2016.
 */
public class AddReceptionistsActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    RelativeLayout rlMain;

    EditText etAddRcptnName,etAddRcptnNumber,etAddRcptnEmail,etAddRcptnPass,etAddRcptnConfPass;
    RippleView rplAddRecptn,rplCanclRecptn;

    String update,RecptnId,RecptnName,RecptnEmail,RecptnPhone;
    String name,email,phone,pass,confPass;

    TextView tvTitle,tvAddrecptnadd,tvPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.add_receptionist);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        etAddRcptnName = (EditText)findViewById(R.id.etAddRcptnName);
        etAddRcptnNumber = (EditText)findViewById(R.id.etAddRcptnNumber);
        etAddRcptnEmail = (EditText)findViewById(R.id.etAddRcptnEmail);
        etAddRcptnPass = (EditText)findViewById(R.id.etAddRcptnPass);
        etAddRcptnConfPass = (EditText)findViewById(R.id.etAddRcptnConfPass);

        rplAddRecptn = (RippleView)findViewById(R.id.rplAddRecptn);
        rplCanclRecptn = (RippleView)findViewById(R.id.rplCanclRecptn);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvAddrecptnadd = (TextView)findViewById(R.id.tvAddrecptnadd);
        tvPass = (TextView)findViewById(R.id.tvPass);

        Intent ii = getIntent();
        update = ii.getStringExtra("update");

        if (update.equalsIgnoreCase("0")){
            rlMain.setVisibility(View.VISIBLE);
        }else {
            rlMain.setVisibility(View.VISIBLE);

            tvTitle.setText("Edit Receptionist");
            tvAddrecptnadd.setText("Update");
            tvPass.setText("Change Password");

            RecptnId = Comman.getPreferences(AddReceptionistsActivity.this, "RecptnId");
            RecptnName = Comman.getPreferences(AddReceptionistsActivity.this, "RecptnName");
            RecptnEmail = Comman.getPreferences(AddReceptionistsActivity.this, "RecptnEmail");
            RecptnPhone = Comman.getPreferences(AddReceptionistsActivity.this, "RecptnPhone");

            etAddRcptnName.setText(RecptnName);
            etAddRcptnEmail.setText(RecptnEmail);
            etAddRcptnNumber.setText(RecptnPhone);
        }

        rplAddRecptn.setOnRippleCompleteListener(this);
        rplCanclRecptn.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplAddRecptn:

                name = etAddRcptnName.getText().toString();
                email = etAddRcptnEmail.getText().toString();
                phone = etAddRcptnNumber.getText().toString();
                pass = etAddRcptnPass.getText().toString();
                confPass = etAddRcptnConfPass.getText().toString();


                if (update.equalsIgnoreCase("0")){

                    if(!Comman.isConnectionAvailable(AddReceptionistsActivity.this)){
                        Toast.makeText(AddReceptionistsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();

                    }else if (name.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide receptionist name!",Toast.LENGTH_SHORT).show();

                    }else if (phone.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide receptionist mobile number!",Toast.LENGTH_SHORT).show();

                    }else if (phone.length()<10){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a valid mobile number!",Toast.LENGTH_SHORT).show();

                    }else if (email.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide receptionist email id!",Toast.LENGTH_SHORT).show();

                    }else if (!Comman.emailValidator(email)){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a valid email id!",Toast.LENGTH_SHORT).show();

                    }else if (pass.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a password!",Toast.LENGTH_SHORT).show();

                    }else if (confPass.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a password!",Toast.LENGTH_SHORT).show();

                    }else if (!confPass.equalsIgnoreCase(pass)){
                        Toast.makeText(AddReceptionistsActivity.this,"Password didn't matched!",Toast.LENGTH_SHORT).show();

                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        new addReceptionistTask().execute();
                    }

                }else {

                    if(!Comman.isConnectionAvailable(AddReceptionistsActivity.this)){
                        Toast.makeText(AddReceptionistsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();

                    }else if (name.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide receptionist name!",Toast.LENGTH_SHORT).show();

                    }else if (phone.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide receptionist mobile number!",Toast.LENGTH_SHORT).show();

                    }else if (phone.length()<10){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a valid mobile number!",Toast.LENGTH_SHORT).show();

                    }else if (email.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide receptionist email id!",Toast.LENGTH_SHORT).show();

                    }else if (!Comman.emailValidator(email)){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a valid email id!",Toast.LENGTH_SHORT).show();

                    }else if (pass.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a password!",Toast.LENGTH_SHORT).show();

                    }else if (confPass.equalsIgnoreCase("")){
                        Toast.makeText(AddReceptionistsActivity.this,"Please provide a password!",Toast.LENGTH_SHORT).show();

                    }else if (!confPass.equalsIgnoreCase(pass)){
                        Toast.makeText(AddReceptionistsActivity.this,"Password didn't matched!",Toast.LENGTH_SHORT).show();

                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        new editReceptionistTask().execute();
                    }

                }


                break;

            case R.id.rplCanclRecptn:
                onBackPressed();
                break;
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


    private class editReceptionistTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.receptionistUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(AddReceptionistsActivity.this, "TOKEN");
                HttpPut httppost = new HttpPut(productListUrl+"/"+RecptnId + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("email", email);
                json.put("mobile", phone);
                json.put("password", pass);
                json.put("confirmPassword", confPass);

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
                            Comman.setPreferences(AddReceptionistsActivity.this, "Changed", "1");
                            Toast.makeText(AddReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(AddReceptionistsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    private class addReceptionistTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.receptionistUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(AddReceptionistsActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("email", email);
                json.put("mobile", phone);
                json.put("password", pass);
                json.put("confirmPassword", confPass);

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
                            Comman.setPreferences(AddReceptionistsActivity.this, "Changed", "1");
                            Toast.makeText(AddReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(AddReceptionistsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

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
