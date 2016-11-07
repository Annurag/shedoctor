package com.indglobal.shedoctor.Activities;

import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.HealthTipsAdapter;
import com.indglobal.shedoctor.Beans.HealthTipsItem;
import com.indglobal.shedoctor.Commans.Comman;
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

public class HealthtipsActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener {

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    RelativeLayout rlNoRecentHealthtips,rlMain;
    RippleView rplwritenew,rpladdHealthtips;

    HealthTipsItem healthTipsItem;
    public static ArrayList<HealthTipsItem> healthTipsItemArrayList;
    HealthTipsAdapter healthTipsAdapter;

    RecyclerView rvHealthList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.health_tips);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rlNoRecentHealthtips = (RelativeLayout)findViewById(R.id.rlNoRecentHealthtips);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        rplwritenew = (RippleView)findViewById(R.id.rplwritenew);
        rpladdHealthtips = (RippleView)findViewById(R.id.rpladdHealthtips);
        rvHealthList = (RecyclerView)findViewById(R.id.rvHealthList);

        healthTipsItemArrayList = new ArrayList<>();

        if (!Comman.isConnectionAvailable(HealthtipsActivity.this)){
            Toast.makeText(HealthtipsActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            rlMain.setVisibility(View.GONE);
            rlNoRecentHealthtips.setVisibility(View.VISIBLE);
            rplwritenew.setVisibility(View.GONE);

        }else {
            healthTipsItemArrayList.clear();
            prgLoading.setVisibility(View.VISIBLE);
            new getHealthsTask().execute();
        }


        rplwritenew.setOnRippleCompleteListener(this);
        rpladdHealthtips.setOnRippleCompleteListener(this);

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

            case R.id.rpladdHealthtips:
                Intent ii = new Intent(HealthtipsActivity.this,AddHealthTipsActivity.class);
                ii.putExtra("updated", "0");
                ii.putExtra("post", "0");
                startActivity(ii);
                break;

            case R.id.rplwritenew:
                Intent iii = new Intent(HealthtipsActivity.this,AddHealthTipsActivity.class);
                iii.putExtra("updated", "0");
                iii.putExtra("post", "0");
                startActivity(iii);
                break;

        }
    }


    private class getHealthsTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.healthtipsUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(HealthtipsActivity.this, "TOKEN");
                HttpGet httppost = new HttpGet(productListUrl + "?token=" + token+"&limit="+100+"&start="+0);


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

                                    if (jsonArray.length()==0){
                                        rlMain.setVisibility(View.GONE);
                                        rlNoRecentHealthtips.setVisibility(View.VISIBLE);
                                        rplwritenew.setVisibility(View.GONE);

                                    }else {

                                        for (int i=0;i<jsonArray.length();i++){
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                            String id = jsonObject1.getString("id");
                                            String title = jsonObject1.getString("title");
                                            title = title.replace("<br />", " ");
                                            String content = jsonObject1.getString("content");
                                            content = content.replace("<br />", " ");
                                            String image = jsonObject1.getString("image");
                                            String active = jsonObject1.getString("active");
                                            String slug = jsonObject1.getString("slug");
                                            String posted_at = jsonObject1.getString("posted_at");

                                            healthTipsItem = new HealthTipsItem(id,title,content,image,active,slug,posted_at);
                                            healthTipsItemArrayList.add(healthTipsItem);
                                        }


                                        healthTipsAdapter = new HealthTipsAdapter(HealthtipsActivity.this,healthTipsItemArrayList);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HealthtipsActivity.this);
                                        rvHealthList.setLayoutManager(mLayoutManager);
                                        rvHealthList.setAdapter(healthTipsAdapter);

                                        rlMain.setVisibility(View.VISIBLE);
                                        rlNoRecentHealthtips.setVisibility(View.GONE);
                                        rplwritenew.setVisibility(View.VISIBLE);
                                    }


                                }catch (Exception e){
                                    e.printStackTrace();
                                    rlMain.setVisibility(View.GONE);
                                    rlNoRecentHealthtips.setVisibility(View.VISIBLE);
                                    rplwritenew.setVisibility(View.GONE);
                                }


                            prgLoading.setVisibility(View.GONE);

                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(HealthtipsActivity.this, message, Toast.LENGTH_SHORT).show();
                            rlNoRecentHealthtips.setVisibility(View.VISIBLE);
                            rlMain.setVisibility(View.GONE);
                            rplwritenew.setVisibility(View.GONE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                rlNoRecentHealthtips.setVisibility(View.VISIBLE);
                rlMain.setVisibility(View.GONE);
                Toast.makeText(HealthtipsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String updtd = Comman.getPreferences(HealthtipsActivity.this, "updtd");
        if (updtd.equalsIgnoreCase("1")){
            prgLoading.setVisibility(View.VISIBLE);
            rlMain.setVisibility(View.GONE);
            rlNoRecentHealthtips.setVisibility(View.GONE);
            healthTipsItemArrayList.clear();
            new getHealthsTask().execute();
            Comman.setPreferences(HealthtipsActivity.this, "updtd", "0");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}