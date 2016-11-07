package com.indglobal.shedoctor.Activities;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 10/09/2016.
 */
public class FeedbackActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    Spinner spinFeedbackrsn;
    RatingBar ratingBar;
    EditText typefeedback;
    RippleView rplNext;
    int rting = 1;
    String resn = "Reason 1",feedbk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.feedback);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        spinFeedbackrsn = (Spinner)findViewById(R.id.spinFeedbackrsn);
        typefeedback = (EditText) findViewById(R.id.typefeedback);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        rplNext = (RippleView)findViewById(R.id.rplNext);

        rplNext.setOnRippleCompleteListener(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int rtng = Math.round(rating);
                rting = rtng;

                switch (rtng){
                    case 1:
                        Toast.makeText(FeedbackActivity.this,"Poor",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(FeedbackActivity.this,"Like",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(FeedbackActivity.this,"Average",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(FeedbackActivity.this,"Good",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(FeedbackActivity.this,"Excellent",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(FeedbackActivity.this,R.array.reason_array,R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinFeedbackrsn.setAdapter(arrayAdapter);

        spinFeedbackrsn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] ressn = getResources().getStringArray(R.array.reason_array);

                resn = ressn[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

            case R.id.rplNext:
                feedbk = typefeedback.getText().toString();

                if(!Comman.isConnectionAvailable(FeedbackActivity.this)){
                    Toast.makeText(FeedbackActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else if (feedbk.equalsIgnoreCase("")){
                    Toast.makeText(FeedbackActivity.this,"Plesae provide feedback!",Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    new saveFeedbkTask().execute();
                }
                break;
        }
    }

    private class saveFeedbkTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.feedbkUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(FeedbackActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("topic", resn);
                json.put("rating", rting);
                json.put("feedback", feedbk);

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
                            typefeedback.setText("");
                            ratingBar.setRating(1);
                            Toast.makeText(FeedbackActivity.this, message, Toast.LENGTH_SHORT).show();


                        } else {


                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(FeedbackActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(FeedbackActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

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

