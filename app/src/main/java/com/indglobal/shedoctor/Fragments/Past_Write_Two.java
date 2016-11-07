package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.LabTestWriteAdapter;
import com.indglobal.shedoctor.Adapters.UpcomingApointAdapter;
import com.indglobal.shedoctor.Beans.LabTestItem;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
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

/**
 * Created by Android on 8/30/16.
 */
public class Past_Write_Two extends Fragment {

    LayoutInflater inflater;

    ExpandableHeightGridView gvLabTest;
    public static EditText etOthers;
    ProgressBar prgLoading;

    LabTestItem labTestItem;
    public static ArrayList<LabTestItem> arrayList;
    LabTestWriteAdapter labTestWriteAdapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_write_two, container, false);

        gvLabTest = (ExpandableHeightGridView)view.findViewById(R.id.gvLabTest);
        etOthers = (EditText)view.findViewById(R.id.etOthers);
        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);

        arrayList = new ArrayList<>();
        labTestWriteAdapter = new LabTestWriteAdapter(getActivity());

        if(!Comman.isConnectionAvailable(getActivity())){
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            arrayList.clear();
            new getLabTestTask().execute();
        }

        return view;

    }


    private class getLabTestTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.writlabTstUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(getActivity(), "TOKEN");
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

                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    String lab = jsonArray.getString(i);

                                    labTestItem = new LabTestItem(lab);
                                    arrayList.add(labTestItem);
                                }

                                labTestWriteAdapter = new LabTestWriteAdapter(getActivity(),arrayList);
                                gvLabTest.setAdapter(labTestWriteAdapter);
                                gvLabTest.setExpanded(true);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            prgLoading.setVisibility(View.GONE);


                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
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

}