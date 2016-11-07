package com.indglobal.shedoctor.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.PaymentAdapter;
import com.indglobal.shedoctor.Beans.LedgerItem;
import com.indglobal.shedoctor.Beans.PaymentItem;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class PaymentActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    RecyclerView rvPayment;
    LinearLayout llMain;
    TextView tvNoData,tvPaymentFromDate,tvPaymentToDate;
    RippleView rvPaymentFromClndr,rvPaymentToClndr;

    PaymentItem paymentItem;
    public static ArrayList<PaymentItem> paymentItemArrayList;
    PaymentAdapter paymentAdapter;
    String fromDate="",toDate="";
    ArrayList<PaymentItem> paymentFilterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.payment);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rvPayment = (RecyclerView)findViewById(R.id.rvPayment);
        llMain = (LinearLayout)findViewById(R.id.llMain);
        tvNoData = (TextView)findViewById(R.id.tvNoData);
        tvPaymentFromDate = (TextView)findViewById(R.id.tvPaymentFromDate);
        tvPaymentToDate = (TextView)findViewById(R.id.tvPaymentToDate);
        rvPaymentFromClndr = (RippleView)findViewById(R.id.rvPaymentFromClndr);
        rvPaymentToClndr = (RippleView)findViewById(R.id.rvPaymentToClndr);

        rvPaymentFromClndr.setOnRippleCompleteListener(this);
        rvPaymentToClndr.setOnRippleCompleteListener(this);

        paymentItemArrayList = new ArrayList<>();
        paymentFilterList = new ArrayList<>();

        if(!Comman.isConnectionAvailable(PaymentActivity.this)){
            Toast.makeText(PaymentActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            llMain.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            paymentItemArrayList.clear();
            new gePaymentitemTask().execute();
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
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {

            case R.id.rvPaymentFromClndr:
                openCalander("1");
                break;

            case R.id.rvPaymentToClndr:
                openCalander("2");
                break;
        }
    }

    private void openCalander(final String fromto) {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PaymentActivity.this,  new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, monthOfYear);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                String showDateFormt = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String selectDate = sdf.format(mcurrentDate.getTime());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(showDateFormt,Locale.US);
                String showDate = simpleDateFormat.format(mcurrentDate.getTime());

                if (fromto.equalsIgnoreCase("1")){
                    tvPaymentFromDate.setText(showDate);
                    fromDate = selectDate;

                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){
                                prgLoading.setVisibility(View.VISIBLE);
                                rvPayment.setAdapter(null);
                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager);
                                rvPayment.setAdapter(paymentAdapter);
                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")){

                                if (!toDate.equalsIgnoreCase("")){

                                    prgLoading.setVisibility(View.VISIBLE);
                                    paymentFilterList.clear();
                                    rvPayment.setAdapter(null);
                                    for(int i=0;i<paymentItemArrayList.size();i++){
                                        PaymentItem paymentItem = paymentItemArrayList.get(i);

                                        String date = paymentItem.getDate();
                                        String amount = paymentItem.getAmount();
                                        String transaction_id = paymentItem.getTransaction_id();
                                        String status = paymentItem.getStatus();
                                        String remarks = paymentItem.getRemarks();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            if (td>dt){
                                                paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                                paymentFilterList.add(paymentItem);
                                            }
                                        }

                                    }

                                    paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(PaymentActivity.this);
                                    rvPayment.setLayoutManager(mLayoutManager1);
                                    rvPayment.setAdapter(paymentAdapter);

                                    if (paymentFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);


                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    paymentFilterList.clear();
                                    rvPayment.setAdapter(null);
                                    for(int i=0;i<paymentItemArrayList.size();i++){
                                        PaymentItem paymentItem = paymentItemArrayList.get(i);

                                        String date = paymentItem.getDate();
                                        String amount = paymentItem.getAmount();
                                        String transaction_id = paymentItem.getTransaction_id();
                                        String status = paymentItem.getStatus();
                                        String remarks = paymentItem.getRemarks();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                            paymentFilterList.add(paymentItem);
                                        }

                                    }

                                    paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(PaymentActivity.this);
                                    rvPayment.setLayoutManager(mLayoutManager1);
                                    rvPayment.setAdapter(paymentAdapter);

                                    if (paymentFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }

                            }else if (!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                paymentFilterList.clear();
                                rvPayment.setAdapter(null);
                                for(int i=0;i<paymentItemArrayList.size();i++){
                                    PaymentItem paymentItem = paymentItemArrayList.get(i);

                                    String date = paymentItem.getDate();
                                    String amount = paymentItem.getAmount();
                                    String transaction_id = paymentItem.getTransaction_id();
                                    String status = paymentItem.getStatus();
                                    String remarks = paymentItem.getRemarks();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (td>dt){
                                        paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                        paymentFilterList.add(paymentItem);
                                    }

                                }

                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager1);
                                rvPayment.setAdapter(paymentAdapter);

                                if (paymentFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                rvPayment.setAdapter(null);
                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager);
                                rvPayment.setAdapter(paymentAdapter);
                                prgLoading.setVisibility(View.GONE);
                            }


                }else {

                    tvPaymentToDate.setText(showDate);
                    toDate = selectDate;


                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){
                                prgLoading.setVisibility(View.VISIBLE);
                                rvPayment.setAdapter(null);
                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager);
                                rvPayment.setAdapter(paymentAdapter);
                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")){

                                if (!toDate.equalsIgnoreCase("")){

                                    prgLoading.setVisibility(View.VISIBLE);
                                    paymentFilterList.clear();
                                    rvPayment.setAdapter(null);
                                    for(int i=0;i<paymentItemArrayList.size();i++){
                                        PaymentItem paymentItem = paymentItemArrayList.get(i);

                                        String date = paymentItem.getDate();
                                        String amount = paymentItem.getAmount();
                                        String transaction_id = paymentItem.getTransaction_id();
                                        String status = paymentItem.getStatus();
                                        String remarks = paymentItem.getRemarks();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            if (td>dt){
                                                paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                                paymentFilterList.add(paymentItem);
                                            }
                                        }

                                    }

                                    paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(PaymentActivity.this);
                                    rvPayment.setLayoutManager(mLayoutManager1);
                                    rvPayment.setAdapter(paymentAdapter);

                                    if (paymentFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);


                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    paymentFilterList.clear();
                                    rvPayment.setAdapter(null);
                                    for(int i=0;i<paymentItemArrayList.size();i++){
                                        PaymentItem paymentItem = paymentItemArrayList.get(i);

                                        String date = paymentItem.getDate();
                                        String amount = paymentItem.getAmount();
                                        String transaction_id = paymentItem.getTransaction_id();
                                        String status = paymentItem.getStatus();
                                        String remarks = paymentItem.getRemarks();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                            paymentFilterList.add(paymentItem);
                                        }

                                    }

                                    paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(PaymentActivity.this);
                                    rvPayment.setLayoutManager(mLayoutManager1);
                                    rvPayment.setAdapter(paymentAdapter);

                                    if (paymentFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }

                            }else if (!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                paymentFilterList.clear();
                                rvPayment.setAdapter(null);
                                for(int i=0;i<paymentItemArrayList.size();i++){
                                    PaymentItem paymentItem = paymentItemArrayList.get(i);

                                    String date = paymentItem.getDate();
                                    String amount = paymentItem.getAmount();
                                    String transaction_id = paymentItem.getTransaction_id();
                                    String status = paymentItem.getStatus();
                                    String remarks = paymentItem.getRemarks();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (td>dt){
                                        paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                        paymentFilterList.add(paymentItem);
                                    }

                                }

                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager1);
                                rvPayment.setAdapter(paymentAdapter);

                                if (paymentFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                rvPayment.setAdapter(null);
                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager);
                                rvPayment.setAdapter(paymentAdapter);
                                prgLoading.setVisibility(View.GONE);
                            }



                }

            }
        }, mYear, mMonth, mDay);


        datePickerDialog.show();
    }

    private class gePaymentitemTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.paymentUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(PaymentActivity.this, "TOKEN");
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String date = jsonObject1.getString("date");
                                    String amount = jsonObject1.getString("amount");
                                    String transaction_id = jsonObject1.getString("transaction_id");
                                    String status = jsonObject1.getString("status");
                                    String remarks = jsonObject1.getString("remarks");

                                    paymentItem = new PaymentItem(date,amount,transaction_id,status,remarks);
                                    paymentItemArrayList.add(paymentItem);
                                }

                                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                                rvPayment.setLayoutManager(mLayoutManager);
                                rvPayment.setAdapter(paymentAdapter);

                                if(jsonArray.length()==0){
                                    llMain.setVisibility(View.GONE);
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    llMain.setVisibility(View.VISIBLE);
                                    tvNoData.setVisibility(View.GONE);
                                }


                            }catch (Exception e){
                                e.printStackTrace();
                                llMain.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }

                            prgLoading.setVisibility(View.GONE);


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                            llMain.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(PaymentActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();
                llMain.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
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
