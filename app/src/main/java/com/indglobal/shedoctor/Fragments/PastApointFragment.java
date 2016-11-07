package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Activities.PastApointDetail;
import com.indglobal.shedoctor.Adapters.PastApointAdapter;
import com.indglobal.shedoctor.Adapters.UpcomingApointAdapter;
import com.indglobal.shedoctor.Beans.PastApointItem;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.Comman;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 8/29/16.
 */
public class PastApointFragment extends Fragment implements FragmentManager.OnBackStackChangedListener{

    LayoutInflater inflater;
    FragmentManager manager;

    RecyclerView rvPastApnt;
    ProgressBar prgLoading;
    TextView tvNoPast;

    PastApointItem PastApointItem;
    public static ArrayList<PastApointItem> pastApointItemArrayList;
    PastApointAdapter pastApointAdapter;
    ArrayList<String> arraylstReport;

    String success_status,reason,details,status;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_appointment_fragment, container, false);

        rvPastApnt = (RecyclerView) view.findViewById(R.id.rvPastApnt);
        prgLoading = (ProgressBar) view.findViewById(R.id.prgLoading);
        tvNoPast = (TextView)view.findViewById(R.id.tvNoPast);

        pastApointItemArrayList = new ArrayList<>();
        pastApointAdapter = new PastApointAdapter(getActivity());

        if (!Comman.isConnectionAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            tvNoPast.setVisibility(View.VISIBLE);
        } else {
            prgLoading.setVisibility(View.VISIBLE);
            pastApointItemArrayList.clear();
            new getpastApointTask().execute();
        }


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        manager = getFragmentManager();

        manager.addOnBackStackChangedListener(this);
    }


    @Override
    public void onBackStackChanged() {

    }

    private class getpastApointTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.pastapointUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(getActivity(), "TOKEN");
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

                                    String id = jsonObject1.getString("id");
                                    String shedct_id = jsonObject1.getString("shedct_id");
                                    String date = jsonObject1.getString("date");
                                    String time = jsonObject1.getString("time");
                                    String datetime = jsonObject1.getString("datetime");
                                    String start_in = jsonObject1.getString("start_in");
                                    String type = jsonObject1.getString("type");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("patient");

                                    String name = jsonObject2.getString("name");
                                    String patntId = jsonObject2.getString("shedct_id");
                                    String height = jsonObject2.getString("height");
                                    String blood_group = jsonObject2.getString("blood_group");
                                    String weight = jsonObject2.getString("weight");
                                    String purpose = jsonObject2.getString("purpose");
                                    String medications = jsonObject2.getString("medications");
                                    String allergies = jsonObject2.getString("allergies");

                                    arraylstReport = new ArrayList<>();
                                    arraylstReport.clear();
                                    JSONArray jsonArray1 = jsonObject2.getJSONArray("reports");
                                    if (jsonArray1.length()!=0){
                                        for (int k=0;k<jsonArray1.length();k++){
                                            JSONObject jsonObject3 = jsonArray1.getJSONObject(k);

                                            String path = jsonObject3.getString("path");
                                            arraylstReport.add(path);
                                        }
                                    }else {
                                        arraylstReport.add("0");
                                    }



                                    try {
                                        JSONObject jsonObjectStatus = jsonObject1.getJSONObject("status");

                                        success_status = jsonObjectStatus.getString("success");
                                        reason = jsonObjectStatus.getString("reason");
                                        details = jsonObjectStatus.getString("details");
                                        status = jsonObjectStatus.getString("status");

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                        success_status = "N/A";
                                        reason = "N/A";
                                        details = "N/A";
                                        status = "N/A";
                                    }

                                    String prescription = jsonObject1.getString("prescription");

                                    PastApointItem  = new PastApointItem(id,name,patntId,start_in,date,time,type,height,blood_group,
                                            weight,purpose,medications,allergies,prescription,success_status,reason,details,status,arraylstReport);
                                    pastApointItemArrayList.add(PastApointItem);

                                }

                                pastApointAdapter = new PastApointAdapter(getActivity(),pastApointItemArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                rvPastApnt.setLayoutManager(mLayoutManager);
                                rvPastApnt.setAdapter(pastApointAdapter);

                                if(jsonArray.length()==0){
                                    tvNoPast.setVisibility(View.VISIBLE);
                                }else {
                                    tvNoPast.setVisibility(View.GONE);
                                }

                                Comman.setPreferences(getActivity(),"StatusChanged","0");

                            }catch (Exception e){
                                e.printStackTrace();
                                tvNoPast.setVisibility(View.VISIBLE);
                            }



                            prgLoading.setVisibility(View.GONE);


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            tvNoPast.setVisibility(View.VISIBLE);
                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

                tvNoPast.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void onResume() {

        String StatusChanged = Comman.getPreferences(getActivity(),"StatusChanged");
        if (StatusChanged.equalsIgnoreCase("1")){

            if (!Comman.isConnectionAvailable(getActivity())) {
                Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                tvNoPast.setVisibility(View.VISIBLE);
            } else {
                prgLoading.setVisibility(View.VISIBLE);
                pastApointItemArrayList.clear();
                new getpastApointTask().execute();
            }

        }

        super.onResume();
    }
}

