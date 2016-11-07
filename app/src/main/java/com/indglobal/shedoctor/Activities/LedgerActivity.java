package com.indglobal.shedoctor.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.LedgerAdapter;
import com.indglobal.shedoctor.Adapters.UpcomingApointAdapter;
import com.indglobal.shedoctor.Beans.LedgerItem;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
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
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


public class LedgerActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    Toolbar mtoolbar;
    RecyclerView rvledgeritem;
    ProgressBar prgLoading;
    LinearLayout llMain;
    TextView tvNoData,tvLedgrFromDate,tvLedgrToDate;
    RippleView rvLedgrFromClndr,rvLedgrToClndr;

    LedgerItem ledgerItem;
    public static ArrayList<LedgerItem> ledgerItemArrayList;
    LedgerAdapter ledgerAdapter;

    ArrayList<LedgerItem> ledgerFilterList;

    Spinner spinLedgerType;
    String fromDate="",toDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.ledger);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spinLedgerType = (Spinner)findViewById(R.id.spinLedgerType);
        rvledgeritem = (RecyclerView)findViewById(R.id.rvledgeritem);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        llMain = (LinearLayout)findViewById(R.id.llMain);
        tvNoData = (TextView)findViewById(R.id.tvNoData);
        tvLedgrFromDate = (TextView)findViewById(R.id.tvLedgrFromDate);
        tvLedgrToDate = (TextView)findViewById(R.id.tvLedgrToDate);
        rvLedgrFromClndr = (RippleView)findViewById(R.id.rvLedgrFromClndr);
        rvLedgrToClndr = (RippleView)findViewById(R.id.rvLedgrToClndr);

        rvLedgrFromClndr.setOnRippleCompleteListener(this);
        rvLedgrToClndr.setOnRippleCompleteListener(this);

        ledgerItemArrayList = new ArrayList<>();
        ledgerFilterList = new ArrayList<>();

        if(!Comman.isConnectionAvailable(LedgerActivity.this)){
            Toast.makeText(LedgerActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            llMain.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            ledgerItemArrayList.clear();
            new getLedgeritemTask().execute();
        }


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(LedgerActivity.this,R.array.type_array,R.layout.spinner_bigitem);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        spinLedgerType.setAdapter(arrayAdapter);

        spinLedgerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:

                        if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){
                            prgLoading.setVisibility(View.VISIBLE);
                            rvledgeritem.setAdapter(null);
                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager);
                            rvledgeritem.setAdapter(ledgerAdapter);
                            prgLoading.setVisibility(View.GONE);

                        }else if (!fromDate.equalsIgnoreCase("")){

                            if (!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String fromDate1 = fromDate.replace("-", "");
                                    long fd = Long.parseLong(fromDate1);

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (fd<dt){
                                        if (td>dt){
                                            ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);


                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String fromDate1 = fromDate.replace("-", "");
                                    long fd = Long.parseLong(fromDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (fd<dt){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }

                        }else if (!toDate.equalsIgnoreCase("")){

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                String toDate1 = toDate.replace("-", "");
                                long td = Long.parseLong(toDate1);

                                String date1 = date.replace("-", "");
                                long dt = Long.parseLong(date1);

                                if (td>dt){
                                    ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                    ledgerFilterList.add(ledgerItem);
                                }

                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        }else {

                            prgLoading.setVisibility(View.VISIBLE);
                            rvledgeritem.setAdapter(null);
                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager);
                            rvledgeritem.setAdapter(ledgerAdapter);
                            prgLoading.setVisibility(View.GONE);
                        }


                        break;

                    case 1:

                        if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                if (consultation_type.equalsIgnoreCase("voice")){
                                    ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                    ledgerFilterList.add(ledgerItem);
                                }
                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        }else if (!fromDate.equalsIgnoreCase("")) {

                            if (!toDate.equalsIgnoreCase("")) {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String fromDate1 = fromDate.replace("-", "");
                                    long fd = Long.parseLong(fromDate1);

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("voice")) {
                                        if (fd < dt) {
                                            if (td > dt) {
                                                ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String fromDate1 = fromDate.replace("-", "");
                                    long fd = Long.parseLong(fromDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("voice")) {
                                        if (fd < dt) {
                                            ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);
                            }

                        }else if(!toDate.equalsIgnoreCase("")){

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                String toDate1 = toDate.replace("-", "");
                                long td = Long.parseLong(toDate1);

                                String date1 = date.replace("-", "");
                                long dt = Long.parseLong(date1);

                                if (consultation_type.equalsIgnoreCase("voice")) {

                                    if (td > dt) {
                                        ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }

                                }

                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        } else {

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                if (consultation_type.equalsIgnoreCase("voice")){
                                    ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                    ledgerFilterList.add(ledgerItem);
                                }
                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);
                        }


                        break;

                    case 2:
                        if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                if (consultation_type.equalsIgnoreCase("video")){
                                    ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                    ledgerFilterList.add(ledgerItem);
                                }
                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        }else if (!fromDate.equalsIgnoreCase("")) {

                            if (!toDate.equalsIgnoreCase("")) {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String fromDate1 = fromDate.replace("-", "");
                                    long fd = Long.parseLong(fromDate1);

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("video")) {
                                        if (fd < dt) {
                                            if (td > dt) {
                                                ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String fromDate1 = fromDate.replace("-", "");
                                    long fd = Long.parseLong(fromDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("video")) {
                                        if (fd < dt) {
                                            ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);
                            }

                        }else if(!toDate.equalsIgnoreCase("")){

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                String toDate1 = toDate.replace("-", "");
                                long td = Long.parseLong(toDate1);

                                String date1 = date.replace("-", "");
                                long dt = Long.parseLong(date1);

                                if (consultation_type.equalsIgnoreCase("video")) {

                                    if (td > dt) {
                                        ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }

                                }

                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        } else {

                            prgLoading.setVisibility(View.VISIBLE);
                            ledgerFilterList.clear();
                            rvledgeritem.setAdapter(null);
                            for(int i=0;i<ledgerItemArrayList.size();i++){
                                LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                String appointment_id = ledgerItem.getAppointment_id();
                                String date = ledgerItem.getDate();
                                String patient_id = ledgerItem.getPatient_id();
                                String transaction_id = ledgerItem.getTransaction_id();
                                String consultation_type = ledgerItem.getConsultation_type();
                                String patient_rs = ledgerItem.getPatient_rs();
                                String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                String doctor_rs = ledgerItem.getDoctor_rs();
                                String other_details = ledgerItem.getOther_details();

                                if (consultation_type.equalsIgnoreCase("video")){
                                    ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                    ledgerFilterList.add(ledgerItem);
                                }
                            }

                            ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                            rvledgeritem.setLayoutManager(mLayoutManager1);
                            rvledgeritem.setAdapter(ledgerAdapter);

                            if (ledgerFilterList.size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                            }

                            prgLoading.setVisibility(View.GONE);
                        }


                        break;
                }
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

            case R.id.rvLedgrFromClndr:
                openCalander("1");
                break;

            case R.id.rvLedgrToClndr:
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
                LedgerActivity.this,  new DatePickerDialog.OnDateSetListener() {

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
                    tvLedgrFromDate.setText(showDate);
                    fromDate = selectDate;

                    int pos = spinLedgerType.getSelectedItemPosition();

                    switch (pos){
                        case 0:

                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){
                                prgLoading.setVisibility(View.VISIBLE);
                                rvledgeritem.setAdapter(null);
                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager);
                                rvledgeritem.setAdapter(ledgerAdapter);
                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")){

                                if (!toDate.equalsIgnoreCase("")){

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            if (td>dt){
                                                ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);


                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }

                            }else if (!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (td>dt){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                rvledgeritem.setAdapter(null);
                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager);
                                rvledgeritem.setAdapter(ledgerAdapter);
                                prgLoading.setVisibility(View.GONE);
                            }


                            break;

                        case 1:

                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("voice")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")) {

                                if (!toDate.equalsIgnoreCase("")) {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("voice")) {
                                            if (fd < dt) {
                                                if (td > dt) {
                                                    ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                    ledgerFilterList.add(ledgerItem);
                                                }
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("voice")) {
                                            if (fd < dt) {
                                                ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);
                                }

                            }else if(!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("voice")) {

                                        if (td > dt) {
                                            ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }

                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            } else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("voice")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);
                            }


                            break;

                        case 2:
                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("video")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")) {

                                if (!toDate.equalsIgnoreCase("")) {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("video")) {
                                            if (fd < dt) {
                                                if (td > dt) {
                                                    ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                    ledgerFilterList.add(ledgerItem);
                                                }
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("video")) {
                                            if (fd < dt) {
                                                ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);
                                }

                            }else if(!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("video")) {

                                        if (td > dt) {
                                            ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }

                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            } else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("video")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);
                            }


                            break;
                    }

                }else {
                    tvLedgrToDate.setText(showDate);
                    toDate = selectDate;

                    int post = spinLedgerType.getSelectedItemPosition();

                    switch (post){
                        case 0:

                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){
                                prgLoading.setVisibility(View.VISIBLE);
                                rvledgeritem.setAdapter(null);
                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager);
                                rvledgeritem.setAdapter(ledgerAdapter);
                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")){

                                if (!toDate.equalsIgnoreCase("")){

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            if (td>dt){
                                                ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);


                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (fd<dt){
                                            ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }

                            }else if (!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (td>dt){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else {

                                prgLoading.setVisibility(View.VISIBLE);
                                rvledgeritem.setAdapter(null);
                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager);
                                rvledgeritem.setAdapter(ledgerAdapter);
                                prgLoading.setVisibility(View.GONE);
                            }


                            break;

                        case 1:

                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("voice")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")) {

                                if (!toDate.equalsIgnoreCase("")) {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("voice")) {
                                            if (fd < dt) {
                                                if (td > dt) {
                                                    ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                    ledgerFilterList.add(ledgerItem);
                                                }
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("voice")) {
                                            if (fd < dt) {
                                                ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);
                                }

                            }else if(!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("voice")) {

                                        if (td > dt) {
                                            ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }

                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            } else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("voice")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);
                            }


                            break;

                        case 2:
                            if (fromDate.equalsIgnoreCase("")&&toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("video")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            }else if (!fromDate.equalsIgnoreCase("")) {

                                if (!toDate.equalsIgnoreCase("")) {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String toDate1 = toDate.replace("-", "");
                                        long td = Long.parseLong(toDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("video")) {
                                            if (fd < dt) {
                                                if (td > dt) {
                                                    ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                    ledgerFilterList.add(ledgerItem);
                                                }
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);

                                }else {

                                    prgLoading.setVisibility(View.VISIBLE);
                                    ledgerFilterList.clear();
                                    rvledgeritem.setAdapter(null);
                                    for(int i=0;i<ledgerItemArrayList.size();i++){
                                        LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                        String appointment_id = ledgerItem.getAppointment_id();
                                        String date = ledgerItem.getDate();
                                        String patient_id = ledgerItem.getPatient_id();
                                        String transaction_id = ledgerItem.getTransaction_id();
                                        String consultation_type = ledgerItem.getConsultation_type();
                                        String patient_rs = ledgerItem.getPatient_rs();
                                        String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                        String doctor_rs = ledgerItem.getDoctor_rs();
                                        String other_details = ledgerItem.getOther_details();

                                        String fromDate1 = fromDate.replace("-", "");
                                        long fd = Long.parseLong(fromDate1);

                                        String date1 = date.replace("-", "");
                                        long dt = Long.parseLong(date1);

                                        if (consultation_type.equalsIgnoreCase("video")) {
                                            if (fd < dt) {
                                                ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                                ledgerFilterList.add(ledgerItem);
                                            }
                                        }

                                    }

                                    ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                    rvledgeritem.setLayoutManager(mLayoutManager1);
                                    rvledgeritem.setAdapter(ledgerAdapter);

                                    if (ledgerFilterList.size()==0){
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }else {
                                        tvNoData.setVisibility(View.GONE);
                                    }

                                    prgLoading.setVisibility(View.GONE);
                                }

                            }else if(!toDate.equalsIgnoreCase("")){

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    String toDate1 = toDate.replace("-", "");
                                    long td = Long.parseLong(toDate1);

                                    String date1 = date.replace("-", "");
                                    long dt = Long.parseLong(date1);

                                    if (consultation_type.equalsIgnoreCase("video")) {

                                        if (td > dt) {
                                            ledgerItem = new LedgerItem(appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details);
                                            ledgerFilterList.add(ledgerItem);
                                        }

                                    }

                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);

                            } else {

                                prgLoading.setVisibility(View.VISIBLE);
                                ledgerFilterList.clear();
                                rvledgeritem.setAdapter(null);
                                for(int i=0;i<ledgerItemArrayList.size();i++){
                                    LedgerItem ledgerItem = ledgerItemArrayList.get(i);

                                    String appointment_id = ledgerItem.getAppointment_id();
                                    String date = ledgerItem.getDate();
                                    String patient_id = ledgerItem.getPatient_id();
                                    String transaction_id = ledgerItem.getTransaction_id();
                                    String consultation_type = ledgerItem.getConsultation_type();
                                    String patient_rs = ledgerItem.getPatient_rs();
                                    String shedoctr_rs = ledgerItem.getShedoctr_rs();
                                    String doctor_rs = ledgerItem.getDoctor_rs();
                                    String other_details = ledgerItem.getOther_details();

                                    if (consultation_type.equalsIgnoreCase("video")){
                                        ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                        ledgerFilterList.add(ledgerItem);
                                    }
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerFilterList);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager1);
                                rvledgeritem.setAdapter(ledgerAdapter);

                                if (ledgerFilterList.size()==0){
                                    tvNoData.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoData.setVisibility(View.GONE);
                                }

                                prgLoading.setVisibility(View.GONE);
                            }


                            break;
                    }
                }

            }
        }, mYear, mMonth, mDay);


        datePickerDialog.show();
    }

    private class getLedgeritemTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.ledgerUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(LedgerActivity.this, "TOKEN");
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

                                    String appointment_id = jsonObject1.getString("appointment_id");
                                    String date = jsonObject1.getString("date");
                                    String patient_id = jsonObject1.getString("patient_id");
                                    String transaction_id = jsonObject1.getString("transaction_id");
                                    String consultation_type = jsonObject1.getString("consultation_type");
                                    String patient_rs = jsonObject1.getString("patient_rs");
                                    String shedoctr_rs = jsonObject1.getString("shedoctr_rs");
                                    String doctor_rs = jsonObject1.getString("doctor_rs");
                                    String other_details = jsonObject1.getString("other_details");

                                    ledgerItem  = new LedgerItem(appointment_id,date,patient_id,transaction_id,consultation_type,patient_rs,shedoctr_rs,doctor_rs,other_details);
                                    ledgerItemArrayList.add(ledgerItem);
                                }

                                ledgerAdapter = new LedgerAdapter(LedgerActivity.this,ledgerItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LedgerActivity.this);
                                rvledgeritem.setLayoutManager(mLayoutManager);
                                rvledgeritem.setAdapter(ledgerAdapter);

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
                            Toast.makeText(LedgerActivity.this, message, Toast.LENGTH_SHORT).show();
                            llMain.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(LedgerActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

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