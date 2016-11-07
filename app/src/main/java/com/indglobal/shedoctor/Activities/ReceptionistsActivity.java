package com.indglobal.shedoctor.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
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

/**
 * Created by SONY on 16/09/2016.
 */
public class ReceptionistsActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    Toolbar mtoolbar;
    ProgressBar prgLoading;
    LinearLayout llMainNoReceptionist;
    RelativeLayout rlMain;
    RippleView rpladdreceptionist,rplReceptnistEdit,rplReceptnistRemove;

    TextView tvReceptnistName,tvReceptnistEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.receptionist_list);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        llMainNoReceptionist = (LinearLayout)findViewById(R.id.llMainNoReceptionist);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        rpladdreceptionist = (RippleView)findViewById(R.id.rpladdreceptionist);
        rplReceptnistEdit = (RippleView)findViewById(R.id.rplReceptnistEdit);
        rplReceptnistRemove = (RippleView)findViewById(R.id.rplReceptnistRemove);

        tvReceptnistName = (TextView)findViewById(R.id.tvReceptnistName);
        tvReceptnistEmail = (TextView)findViewById(R.id.tvReceptnistEmail);

        if (!Comman.isConnectionAvailable(ReceptionistsActivity.this)){
            Toast.makeText(ReceptionistsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            rlMain.setVisibility(View.GONE);
            llMainNoReceptionist.setVisibility(View.VISIBLE);

        }else {
            prgLoading.setVisibility(View.VISIBLE);
            new getReceptnstTask().execute();
        }


        rpladdreceptionist.setOnRippleCompleteListener(this);
        rplReceptnistEdit.setOnRippleCompleteListener(this);
        rplReceptnistRemove.setOnRippleCompleteListener(this);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {

            case R.id.rpladdreceptionist:
                Intent ii = new Intent(ReceptionistsActivity.this,AddReceptionistsActivity.class);
                ii.putExtra("update", "0");
                startActivity(ii);
                break;

            case R.id.rplReceptnistEdit:
                Intent iii = new Intent(ReceptionistsActivity.this,AddReceptionistsActivity.class);
                iii.putExtra("update", "1");
                startActivity(iii);
                break;

            case R.id.rplReceptnistRemove:
                if(!Comman.isConnectionAvailable(ReceptionistsActivity.this)){
                    Toast.makeText(ReceptionistsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ReceptionistsActivity.this);
                    builder1.setTitle("Remove Receptionist");
                    builder1.setMessage("Are you sure you want to remove receptionist?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    prgLoading.setVisibility(View.VISIBLE);
                                    new removeReceptionTask().execute();

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
        }
    }

    private class getReceptnstTask extends AsyncTask<String, Void, String> {

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

                String token = Comman.getPreferences(ReceptionistsActivity.this, "TOKEN");
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

                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String email = jsonObject1.getString("email");
                                String mobile = jsonObject1.getString("mobile");

                                tvReceptnistName.setText(name);
                                tvReceptnistEmail.setText(email);

                                Comman.setPreferences(ReceptionistsActivity.this, "RecptnId", id);
                                Comman.setPreferences(ReceptionistsActivity.this,"RecptnName",name);
                                Comman.setPreferences(ReceptionistsActivity.this,"RecptnEmail",email);
                                Comman.setPreferences(ReceptionistsActivity.this,"RecptnPhone",mobile);

                                rlMain.setVisibility(View.VISIBLE);
                                llMainNoReceptionist.setVisibility(View.GONE);


                            }catch (Exception e){
                                e.printStackTrace();
                                llMainNoReceptionist.setVisibility(View.VISIBLE);
                                rlMain.setVisibility(View.GONE);
                            }



                            prgLoading.setVisibility(View.GONE);

                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(ReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();
                            llMainNoReceptionist.setVisibility(View.VISIBLE);
                            rlMain.setVisibility(View.GONE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                llMainNoReceptionist.setVisibility(View.VISIBLE);
                rlMain.setVisibility(View.GONE);
                Toast.makeText(ReceptionistsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    private class removeReceptionTask extends AsyncTask<String, Void, String> {

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

                String RecptnId = Comman.getPreferences(ReceptionistsActivity.this, "RecptnId");
                String token = Comman.getPreferences(ReceptionistsActivity.this, "TOKEN");
                HttpDelete httppost = new HttpDelete(productListUrl+"/"+RecptnId + "?token=" + token);

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
                            prgLoading.setVisibility(View.GONE);
                            rlMain.setVisibility(View.GONE);
                            llMainNoReceptionist.setVisibility(View.VISIBLE);
                            Toast.makeText(ReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(ReceptionistsActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(ReceptionistsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String Changed = Comman.getPreferences(ReceptionistsActivity.this, "Changed");
        if (Changed.equalsIgnoreCase("1")){
            rlMain.setVisibility(View.GONE);
            llMainNoReceptionist.setVisibility(View.GONE);

            if (!Comman.isConnectionAvailable(ReceptionistsActivity.this)){
                Toast.makeText(ReceptionistsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                rlMain.setVisibility(View.GONE);
                llMainNoReceptionist.setVisibility(View.VISIBLE);

            }else {
                prgLoading.setVisibility(View.VISIBLE);
                new getReceptnstTask().execute();
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

