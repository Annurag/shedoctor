package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.indglobal.shedoctor.Activities.BaseActivity;
import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.Activities.MyProfileEditOneActivity;
import com.indglobal.shedoctor.Activities.PastApointDetail;
import com.indglobal.shedoctor.Activities.PastWriteOneActivity;
import com.indglobal.shedoctor.Adapters.WriteAwardAdapter;
import com.indglobal.shedoctor.Adapters.WriteEducationAdapter;
import com.indglobal.shedoctor.Beans.AddAwardItem;
import com.indglobal.shedoctor.Beans.AddEducationItem;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 19/09/2016.
 */
public class MyProfileAward extends Fragment implements RippleView.OnRippleCompleteListener{

    LayoutInflater inflater;

    ProgressBar prgLoading;
    public static RecyclerView rvAddAward;
    RippleView rplAddMoreAward,rplAddAwardSave,rplAddAwardCncl;

    AddAwardItem addAwardItem;
    WriteAwardAdapter writeAwardAdapter;
    JSONArray awrdArray;
    JSONObject awrdJsonObj;

    private int size;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile_awards, container, false);

        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        rvAddAward = (RecyclerView)view.findViewById(R.id.rvAddAward);

        rplAddMoreAward = (RippleView)view.findViewById(R.id.rplAddMoreAward);
        rplAddAwardSave = (RippleView)view.findViewById(R.id.rplAddAwardSave);
        rplAddAwardCncl = (RippleView)view.findViewById(R.id.rplAddAwardCncl);

        writeAwardAdapter = new WriteAwardAdapter(getActivity());


        if (MyProfileActivity.addAwardItemArrayList.size()==0){
            size = MyProfileActivity.addAwardItemArrayList.size()+1;
            addAwardItem = new AddAwardItem(size,0,"","",0);
            MyProfileActivity.addAwardItemArrayList.add(addAwardItem);

        }else {
            size = MyProfileActivity.addAwardItemArrayList.size();
        }


        writeAwardAdapter = new WriteAwardAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAddAward.setLayoutManager(mLayoutManager);
        rvAddAward.setAdapter(writeAwardAdapter);


        rplAddMoreAward.setOnRippleCompleteListener(this);
        rplAddAwardSave.setOnRippleCompleteListener(this);
        rplAddAwardCncl.setOnRippleCompleteListener(this);

        return view;

    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){

            case R.id.rplAddMoreAward:
                size++;
                addAwardItem = new AddAwardItem(size,0,"","",0);
                MyProfileActivity.addAwardItemArrayList.add(addAwardItem);
                writeAwardAdapter = new WriteAwardAdapter(getActivity());
                writeAwardAdapter.notifyItemInserted(size);
                rvAddAward.scrollToPosition(size);
                break;

            case R.id.rplAddAwardCncl:
                onBackPressed();
                break;

            case R.id.rplAddAwardSave:

                if(!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);

                    awrdArray = new JSONArray();

                    for (int p = 0; p < MyProfileAward.rvAddAward.getAdapter().getItemCount(); p++) {

                        View awrdview = MyProfileAward.rvAddAward.getChildAt(p);

                        if (awrdview.findViewById(R.id.etawardName) != null) {

                            Spinner spinAwrdyear = (Spinner)awrdview.findViewById(R.id.spinAwrdyear);
                            EditText etawardName = (EditText)awrdview.findViewById(R.id.etawardName);
                            EditText etawardDtl = (EditText)awrdview.findViewById(R.id.etawardDtl);

                            String awardname = etawardName.getText().toString();
                            String awardDtl = etawardDtl.getText().toString();

                            if (MyProfileActivity.addAwardItemArrayList.size()>1){

                                if (awardname.equalsIgnoreCase("")){
                                    Toast.makeText(getActivity(),"Please provide Award name!",Toast.LENGTH_SHORT).show();
                                }else if(awardDtl.equalsIgnoreCase("")){
                                    Toast.makeText(getActivity(),"Please provide award details!",Toast.LENGTH_SHORT).show();
                                }else {

                                    awrdJsonObj = new JSONObject();

                                    try{

                                        if (MyProfileActivity.addAwardItemArrayList.get(p).getRlid()==0){
                                            awrdJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            awrdJsonObj.put("name",awardname);
                                            awrdJsonObj.put("year",Integer.parseInt((String) spinAwrdyear.getSelectedItem()));
                                            awrdJsonObj.put("details",awardDtl);

                                        }else {
                                            awrdJsonObj.put("id",MyProfileActivity.addAwardItemArrayList.get(p).getRlid());
                                            awrdJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            awrdJsonObj.put("name",awardname);
                                            awrdJsonObj.put("year",Integer.parseInt((String) spinAwrdyear.getSelectedItem()));
                                            awrdJsonObj.put("details",awardDtl);
                                        }



                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                    awrdArray.put(awrdJsonObj);
                                }

                            }else {

                                if (!awardname.equalsIgnoreCase("")){

                                    awrdJsonObj = new JSONObject();

                                    try{
                                        if (MyProfileActivity.addAwardItemArrayList.get(p).getRlid()==0){
                                            awrdJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            awrdJsonObj.put("name",awardname);
                                            awrdJsonObj.put("year",Integer.parseInt((String) spinAwrdyear.getSelectedItem()));
                                            awrdJsonObj.put("details",awardDtl);

                                        }else {
                                            awrdJsonObj.put("id",MyProfileActivity.addAwardItemArrayList.get(p).getRlid());
                                            awrdJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            awrdJsonObj.put("name",awardname);
                                            awrdJsonObj.put("year",Integer.parseInt((String) spinAwrdyear.getSelectedItem()));
                                            awrdJsonObj.put("details",awardDtl);
                                        }

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                    awrdArray.put(awrdJsonObj);
                                }
                            }
                        }
                    }


                    new updateProfileDtlTask().execute();
                }

                break;
        }
    }

    private class updateProfileDtlTask extends AsyncTask<String, Void, String> {

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

                String token = Comman.getPreferences(getActivity(), "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("prefix", MyProfileEditOneActivity.prefix);
                json.put("name", MyProfileEditOneActivity.name);
                json.put("gender", MyProfileEditOneActivity.gender);
                json.put("specialty",Integer.parseInt(MyProfileEditOneActivity.specialty));

                JSONArray jsonArray  = new JSONArray();
                json.put("subspecialty",jsonArray);

                json.put("experience",MyProfileEditOneActivity.experience);
                json.put("languages",MyProfileEditOneActivity.checkLangArray);
                json.put("mother_tongue",Integer.parseInt(MyProfileEditOneActivity.mother_tongue));
                json.put("address_line_1",MyProfileEditOneActivity.address_line_1);
                json.put("address_line_2",MyProfileEditOneActivity.address_line_2);
                json.put("state",MyProfileEditOneActivity.state);
                json.put("city",MyProfileEditOneActivity.city);
                json.put("pincode",MyProfileEditOneActivity.pincode);
                json.put("medicine_type",Integer.parseInt(MyProfileEditOneActivity.medicine_type));
                json.put("self_details",MyProfileEditOneActivity.self_details);
                json.put("education",MyProfileEditOneActivity.educationArray);
                json.put("specialization",MyProfileEditOneActivity.speclznArray);
                json.put("services",MyProfileEditOneActivity.servcArray);
                json.put("awards",awrdArray);
                json.put("email",MyProfileEditOneActivity.email);
                json.put("mobile",MyProfileEditOneActivity.mobile);
                json.put("registration_no",MyProfileEditOneActivity.registration_no);

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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            Intent ii = new Intent(getActivity(), MyProfileActivity.class);
                            MyProfileEditOneActivity.activity.finish();
                            MyProfileActivity.activity.finish();
                            startActivity(ii);

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}