package com.indglobal.shedoctor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.AwardsAdapter;
import com.indglobal.shedoctor.Adapters.EditSpecialityAdapter;
import com.indglobal.shedoctor.Adapters.EducationAdapter;
import com.indglobal.shedoctor.Adapters.MedicinTypeAdapter;
import com.indglobal.shedoctor.Adapters.ServicesAdapter;
import com.indglobal.shedoctor.Adapters.SpeciltyAdapter;
import com.indglobal.shedoctor.Beans.AddAwardItem;
import com.indglobal.shedoctor.Beans.AddEducationItem;
import com.indglobal.shedoctor.Beans.LanguageItem;
import com.indglobal.shedoctor.Beans.MedicinTypeItem;
import com.indglobal.shedoctor.Beans.SpecialityItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Commans.UtilityList;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

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

/**
 * Created by Android on 9/24/16.
 */
public class MyProfileActivity extends AppCompatActivity{

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    ImageView ivMyPrflImg;
    TextView tvMyPrflEdit,tvMyPrflName,tvMyPrflDegree,tvMyPrflClg,tvMyPrflExp,tvMyPrflLang,tvMyPrflCity,tvMyPrflAbt,
            tvMyPrflMothrTong,tvMyPrflMedicineType,tvMyPrflSpecType,
    tvEduNotAvlbl,tvSpecNotAvlbl,tvServcNotAvlbl,tvAwardsNotAvlbl;

    ListView lstMyPrflEductn,lstMyPrflAward;
    ArrayList<String> eduArrayList = new ArrayList<>();
    EducationAdapter educationAdapter;
    AddEducationItem addEducationItem;
    public static ArrayList<AddEducationItem> addEducationItemArrayList;
    ArrayList<String> awrdArrayList = new ArrayList<>();
    AwardsAdapter awardAdapter;
    AddAwardItem addAwardItem;
    public static ArrayList<AddAwardItem> addAwardItemArrayList;

    ExpandableHeightGridView grdMyPrflSpeclt,grdMyPrflServce;
    ArrayList<String> specArrayList = new ArrayList<>();
    public static ArrayList<String> specializationArrayList = new ArrayList<>();
    SpeciltyAdapter specltyAdapter;
    ArrayList<String> srvcArrayList = new ArrayList<>();
    public static ArrayList<String> servicesArrayList = new ArrayList<>();
    ServicesAdapter srvcAdapter;

    public static ArrayList<LanguageItem> langArrayList = new ArrayList<>();
    public static ArrayList<String> myLangList = new ArrayList<>();
    LanguageItem languageItem;
    public static JSONArray jsonArray;

    MedicinTypeItem medicinTypeItem;
    public static ArrayList<MedicinTypeItem> medicinTypeItemArrayList;
    SpecialityItem specialityItem;
    public static ArrayList<SpecialityItem> specialityArrayList;

    public static String DrId,email,mobile,prefix,names,registration_no,gender,self_details,experience,address_line_1,address_line_2,
            city, state,pincode,mother_tongue,medicine_type,specialty;

    LinearLayout llMain;

    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.my_profile);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        activity = MyProfileActivity.this;

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        ivMyPrflImg = (ImageView)findViewById(R.id.ivMyPrflImg);
        tvMyPrflEdit = (TextView)findViewById(R.id.tvMyPrflEdit);
        tvMyPrflName = (TextView)findViewById(R.id.tvMyPrflName);
        tvMyPrflDegree = (TextView)findViewById(R.id.tvMyPrflDegree);
        tvMyPrflClg = (TextView)findViewById(R.id.tvMyPrflClg);
        tvMyPrflExp = (TextView)findViewById(R.id.tvMyPrflExp);
        tvMyPrflLang = (TextView)findViewById(R.id.tvMyPrflLang);
        tvMyPrflCity = (TextView)findViewById(R.id.tvMyPrflCity);
        tvMyPrflAbt = (TextView)findViewById(R.id.tvMyPrflAbt);
        tvEduNotAvlbl = (TextView)findViewById(R.id.tvEduNotAvlbl);
        tvSpecNotAvlbl = (TextView)findViewById(R.id.tvSpecNotAvlbl);
        tvServcNotAvlbl = (TextView)findViewById(R.id.tvServcNotAvlbl);
        tvAwardsNotAvlbl = (TextView)findViewById(R.id.tvAwardsNotAvlbl);
        tvMyPrflMothrTong = (TextView)findViewById(R.id.tvMyPrflMothrTong);
        tvMyPrflMedicineType = (TextView)findViewById(R.id.tvMyPrflMedicineType);
        tvMyPrflSpecType = (TextView)findViewById(R.id.tvMyPrflSpecType);
        llMain = (LinearLayout)findViewById(R.id.llMain);

        lstMyPrflEductn = (ListView)findViewById(R.id.lstMyPrflEductn);
        grdMyPrflSpeclt = (ExpandableHeightGridView)findViewById(R.id.grdMyPrflSpeclt);
        grdMyPrflServce = (ExpandableHeightGridView)findViewById(R.id.grdMyPrflServce);
        lstMyPrflAward = (ListView)findViewById(R.id.lstMyPrflAward);

        medicinTypeItemArrayList = new ArrayList<>();
        specialityArrayList = new ArrayList<>();
        addEducationItemArrayList = new ArrayList<>();
        addAwardItemArrayList = new ArrayList<>();

        if (!Comman.isConnectionAvailable(MyProfileActivity.this)){
            Toast.makeText(MyProfileActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            llMain.setVisibility(View.GONE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            langArrayList.clear();
            specArrayList.clear();
            specializationArrayList.clear();
            myLangList.clear();
            eduArrayList.clear();
            addEducationItemArrayList.clear();
            addAwardItemArrayList.clear();
            awrdArrayList.clear();
            srvcArrayList.clear();
            myLangList.clear();
            medicinTypeItemArrayList.clear();
            specialityArrayList.clear();
            new langTask().execute();
        }

        tvMyPrflEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(MyProfileActivity.this,MyProfileEditOneActivity.class);
                startActivity(ii);
            }
        });
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

    private class langTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.langUrl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(productListUrl);

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

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String id = jsonObject1.getString("id");
                                String language = jsonObject1.getString("language");

                                Comman.setPreferences(MyProfileActivity.this, "language" + id, language);

                                languageItem = new LanguageItem(id,language);
                                langArrayList.add(languageItem);
                            }

                            new medicnTypeTask().execute();


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(MyProfileActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }


    private class medicnTypeTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.medcntypeUrl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(productListUrl);

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

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String id = jsonObject1.getString("id");
                                String medicine_type = jsonObject1.getString("medicine_type");

                                medicinTypeItem = new MedicinTypeItem(id,medicine_type);
                                medicinTypeItemArrayList.add(medicinTypeItem);
                            }

                            new getSpecialtyTask().execute();


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(MyProfileActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }



        }
    }

    private class getSpecialtyTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.specltyurl);
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(productListUrl);

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

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String id = jsonObject1.getString("id");
                                String specialty = jsonObject1.getString("specialty");
                                String slug = jsonObject1.getString("slug");
                                String parent = jsonObject1.getString("parent");

                                specialityItem = new SpecialityItem(id,specialty,slug,parent);
                                specialityArrayList.add(specialityItem);
                            }

//                            editSpecialityAdapter = new EditSpecialityAdapter(MyProfileEditOneActivity.this,specialityArrayList);
//                            spinEditPrflSpeclt.setAdapter(editSpecialityAdapter);

                            new DrProfileUrl().execute();

                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(MyProfileActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


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

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                DefaultHttpClient client = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.prflUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(MyProfileActivity.this, "TOKEN");

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

                            DrId = jsonObject1.getString("id");
                            email = jsonObject1.getString("email");
                            mobile = jsonObject1.getString("mobile");
                            prefix = jsonObject1.getString("prefix");
                            names = jsonObject1.getString("name");
                            registration_no = jsonObject1.getString("registration_no");
                            gender = jsonObject1.getString("gender");
                            self_details = jsonObject1.getString("self_details");
                            experience = jsonObject1.getString("experience");
                            address_line_1 = jsonObject1.getString("address_line_1");
                            address_line_2 = jsonObject1.getString("address_line_2");
                            city = jsonObject1.getString("city");
                            state = jsonObject1.getString("state");
                            pincode = jsonObject1.getString("pincode");
                            mother_tongue = jsonObject1.getString("mother_tongue");
                            medicine_type = jsonObject1.getString("medicine_type");
                            specialty = jsonObject1.getString("specialty");

                            if (!specialty.equalsIgnoreCase("")&&!specialty.equalsIgnoreCase("null")){

                                for (int i =0;i<specialityArrayList.size();i++){

                                    SpecialityItem specialityItem = specialityArrayList.get(i);

                                    String id = specialityItem.getId();

                                    if (id.equalsIgnoreCase(specialty)){

                                        tvMyPrflSpecType.setText(specialityItem.getSpecialty());
                                    }

                                }
                            }else {
                                tvMyPrflSpecType.setText("Not Available");
                            }

                            if (!medicine_type.equalsIgnoreCase("")&&!medicine_type.equalsIgnoreCase("null")){

                                for (int i =0;i<medicinTypeItemArrayList.size();i++){

                                    MedicinTypeItem medicinTypeItem = medicinTypeItemArrayList.get(i);

                                    String id = medicinTypeItem.getId();

                                    if (id.equalsIgnoreCase(medicine_type)){

                                        tvMyPrflMedicineType.setText(medicinTypeItem.getMedicin());
                                    }

                                }
                            }else {
                                tvMyPrflMedicineType.setText("Not Available");
                            }

                            String DoctrImg = Comman.getPreferences(MyProfileActivity.this, "PRFLIMG");

                            if (!DoctrImg.equalsIgnoreCase("")&&!DoctrImg.equalsIgnoreCase("null")){

                                Picasso.with(MyProfileActivity.this).load(DoctrImg).resize(100, 100).error(R.drawable.doc)
                                        .placeholder(R.drawable.doc).into(ivMyPrflImg);

                            }

                            tvMyPrflName.setText(prefix+" "+names);
                            tvMyPrflExp.setText(experience+" yrs Experiences");

                            if (city.equalsIgnoreCase("")){
                                tvMyPrflCity.setText("Not Available");
                            }else {
                                tvMyPrflCity.setText(address_line_1+", "+address_line_2+", "+city+"-"+pincode+","+state);
                            }


                            if (self_details.equalsIgnoreCase("null")){
                                tvMyPrflAbt.setText("Self Details:- Not Available");
                            }else {
                                tvMyPrflAbt.setText(self_details);
                            }



                            String degree = "";
                            String college_name = "";
                            String year = "";
                            JSONArray jsonArrayEdu = jsonObject1.getJSONArray("education");
                            for (int i = 0;i<jsonArrayEdu.length();i++){
                                JSONObject jsonObjectEdu = jsonArrayEdu.getJSONObject(i);

                                int id = jsonObjectEdu.getInt("id");
                                String dgr = jsonObjectEdu.getString("degree");
                                college_name = jsonObjectEdu.getString("college_name");
                                year = jsonObjectEdu.getString("year");

                                if(i!=0) {
                                    degree = degree + "," + dgr;
                                }else {

                                    degree = dgr;
                                }

                                eduArrayList.add(dgr+" "+"from "+college_name+","+year);

                                addEducationItem = new AddEducationItem(i,id,Integer.parseInt(year),college_name,dgr);
                                addEducationItemArrayList.add(addEducationItem);

                            }

                            educationAdapter = new EducationAdapter(MyProfileActivity.this,eduArrayList);
                            lstMyPrflEductn.setAdapter(educationAdapter);
                            UtilityList.setListViewHeightBasedOnChildren(lstMyPrflEductn);

                            if (jsonArrayEdu.length()==0){
                                tvMyPrflDegree.setText("Degree:- Not Available");
                                tvMyPrflClg.setText("Colege Name:- Not Available");
                                tvEduNotAvlbl.setVisibility(View.VISIBLE);
                            }else {
                                tvMyPrflDegree.setText(degree);
                                tvMyPrflClg.setText(college_name + " " + year);
                                tvEduNotAvlbl.setVisibility(View.GONE);
                            }



                            JSONArray jsonArraySpec = jsonObject1.getJSONArray("specialization");
                            for (int k=0;k<jsonArraySpec.length();k++){

                                String spec = jsonArraySpec.getString(k);

                                if (!spec.equalsIgnoreCase("NA")){
                                    specArrayList.add(spec);
                                    specializationArrayList.add(spec);
                                }

                            }

                            specltyAdapter = new SpeciltyAdapter(MyProfileActivity.this,specArrayList);
                            grdMyPrflSpeclt.setAdapter(specltyAdapter);
                            grdMyPrflSpeclt.setExpanded(true);

                            if (specArrayList.size()==0){
                                tvSpecNotAvlbl.setVisibility(View.VISIBLE);
                            }else {
                                tvSpecNotAvlbl.setVisibility(View.GONE);
                            }

                            JSONArray jsonArraySrvc = jsonObject1.getJSONArray("services");
                            for (int p = 0;p<jsonArraySrvc.length();p++){

                                String srvc = jsonArraySrvc.getString(p);
                                if(!srvc.equalsIgnoreCase("NA")){
                                    srvcArrayList.add(srvc);
                                    servicesArrayList.add(srvc);
                                }
                            }

                            srvcAdapter = new ServicesAdapter(MyProfileActivity.this,srvcArrayList);
                            grdMyPrflServce.setAdapter(srvcAdapter);
                            grdMyPrflServce.setExpanded(true);

                            if (srvcArrayList.size()==0){
                                tvServcNotAvlbl.setVisibility(View.VISIBLE);
                            }else {
                                tvServcNotAvlbl.setVisibility(View.GONE);
                            }

                            JSONArray jsonArrayAward = jsonObject1.getJSONArray("awards");
                            for (int q = 0;q<jsonArrayAward.length();q++){

                                JSONObject objectAwrd = jsonArrayAward.getJSONObject(q);

                                int id = objectAwrd.getInt("id");
                                String nme = objectAwrd.getString("name");
                                String yrs = objectAwrd.getString("year");
                                String dtls = objectAwrd.getString("details");

                                awrdArrayList.add(nme+","+yrs);

                                addAwardItem = new AddAwardItem(q,id,nme,dtls,Integer.parseInt(yrs));
                                addAwardItemArrayList.add(addAwardItem);
                            }

                            awardAdapter = new AwardsAdapter(MyProfileActivity.this,awrdArrayList);
                            lstMyPrflAward.setAdapter(awardAdapter);
                            UtilityList.setListViewHeightBasedOnChildren(lstMyPrflAward);

                            if (awrdArrayList.size()==0){
                                tvAwardsNotAvlbl.setVisibility(View.VISIBLE);
                            }else {
                                tvAwardsNotAvlbl.setVisibility(View.GONE);
                            }

                            String languages = "";

                            jsonArray = jsonObject1.getJSONArray("languages");

                            for (int j = 0; j<jsonArray.length();j++){

                                int id = jsonArray.getInt(j);

                                myLangList.add(id+"");

                                if(j!=0) {
                                    languages = languages + "," + Comman.getPreferences(MyProfileActivity.this, "language" + id);
                                }else {

                                    languages = Comman.getPreferences(MyProfileActivity.this, "language" + id);
                                }


                            }

                            if (jsonArray.length()==0){
                                tvMyPrflLang.setText("Not Available");
                            }else {
                                tvMyPrflLang.setText(languages);
                            }

                            if (!mother_tongue.equalsIgnoreCase("")&&!mother_tongue.equalsIgnoreCase("null")){

                                tvMyPrflMothrTong.setText(Comman.getPreferences(MyProfileActivity.this, "language" + mother_tongue));

                            }else {
                                tvMyPrflMothrTong.setText("Not Available");
                            }


                            prgLoading.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            llMain.setVisibility(View.GONE);

                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MyProfileActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

                llMain.setVisibility(View.GONE);
            }

            prgLoading.setVisibility(View.GONE);

        }
    }

    @Override
    public void onResume() {
        String sf = Comman.getPreferences(MyProfileActivity.this, "PicChang");
        if (sf.equalsIgnoreCase("1")){
            String DoctrImg = Comman.getPreferences(MyProfileActivity.this, "PRFLIMG");

            if (!DoctrImg.equalsIgnoreCase("")&&!DoctrImg.equalsIgnoreCase("null")){

                Picasso.with(MyProfileActivity.this).load(DoctrImg).resize(100, 100).error(R.drawable.doc)
                        .placeholder(R.drawable.doc).into(ivMyPrflImg);

            }
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
