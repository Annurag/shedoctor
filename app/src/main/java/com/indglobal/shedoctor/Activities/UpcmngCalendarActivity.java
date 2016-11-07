package com.indglobal.shedoctor.Activities;

import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.CalendarAdapter;
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
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 10/13/16.
 */
public class UpcmngCalendarActivity extends AppCompatActivity{

    Toolbar mtoolbar;

    RippleView rplClndrapointBack,rplClndrapointNxt;
    TextView tvClndrapointDate,tvNoData;
    GridView grdClndrapoint;
    LinearLayout llMainCalendr;
    ProgressBar prgLoading;

    public Calendar month, itemmonth;
    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.upcoming_calendr_main);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rplClndrapointBack = (RippleView)findViewById(R.id.rplClndrapointBack);
        rplClndrapointNxt = (RippleView)findViewById(R.id.rplClndrapointNxt);
        tvClndrapointDate = (TextView)findViewById(R.id.tvClndrapointDate);
        grdClndrapoint = (GridView)findViewById(R.id.grdClndrapoint);
        llMainCalendr = (LinearLayout)findViewById(R.id.llMainCalendr);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvNoData = (TextView)findViewById(R.id.tvNoData);

        month = Calendar.getInstance();
        itemmonth = (Calendar) month.clone();

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(UpcmngCalendarActivity.this, month);


        tvClndrapointDate.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        rplClndrapointBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        rplClndrapointNxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });


        if(!Comman.isConnectionAvailable(UpcmngCalendarActivity.this)){
            Toast.makeText(UpcmngCalendarActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            tvNoData.setVisibility(View.VISIBLE);
            llMainCalendr.setVisibility(View.GONE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            items.clear();
            new getUpcmngApointTask().execute();
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


    protected void setNextMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) + 1),
                    month.getActualMinimum(Calendar.MONTH), 1);
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) - 1), month.getActualMaximum(Calendar.MONTH), 1);
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
        }
    }


    public void refreshCalendar() {

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater);

        tvClndrapointDate.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    private class getUpcmngApointTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.upcmngapoinUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(UpcmngCalendarActivity.this, "TOKEN");
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
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String date = jsonObject1.getString("date");

                                    items.add(date);

                                }

                                grdClndrapoint.setAdapter(adapter);
                                handler = new Handler();
                                handler.post(calendarUpdater);

                                prgLoading.setVisibility(View.GONE);
                                llMainCalendr.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);


                            }catch (Exception e){
                                e.printStackTrace();
                                tvNoData.setVisibility(View.VISIBLE);
                                llMainCalendr.setVisibility(View.GONE);
                            }


                            prgLoading.setVisibility(View.GONE);


                        } else {

                            tvNoData.setVisibility(View.VISIBLE);
                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(UpcmngCalendarActivity.this, message, Toast.LENGTH_SHORT).show();
                            llMainCalendr.setVisibility(View.GONE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(UpcmngCalendarActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();
                tvNoData.setVisibility(View.VISIBLE);
                llMainCalendr.setVisibility(View.GONE);
            }
        }
    }

    public Runnable calendarUpdater = new Runnable()
    {

        @Override
        public void run()
        {
            itemmonth.add(Calendar.DATE, 1);
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
