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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.UpcomingApointAdapter;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 9/13/16.
 */
public class ConsultFeeFragment extends Fragment {

    LayoutInflater inflater;
    ProgressBar prgLoading;
    RelativeLayout rlMainFrg;

    EditText etConsltFeeFragVideoPrice,etConsltFeeFragAudioPrice;
    CheckBox cbConsltFeeFragVideoAvailbl,cbConsltFeeFragAudioAvailbl;
    RippleView rplConsltFeeFragUpdate;

    String videoFee,audioFee;
    boolean videoAvail = false,audioCall = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultation_fee_fragment, container, false);

        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        rlMainFrg = (RelativeLayout)view.findViewById(R.id.rlMainFrg);
        etConsltFeeFragVideoPrice = (EditText)view.findViewById(R.id.etConsltFeeFragVideoPrice);
        etConsltFeeFragAudioPrice = (EditText)view.findViewById(R.id.etConsltFeeFragAudioPrice);
        cbConsltFeeFragVideoAvailbl = (CheckBox)view.findViewById(R.id.cbConsltFeeFragVideoAvailbl);
        cbConsltFeeFragAudioAvailbl = (CheckBox)view.findViewById(R.id.cbConsltFeeFragAudioAvailbl);
        rplConsltFeeFragUpdate = (RippleView)view.findViewById(R.id.rplConsltFeeFragUpdate);


        if (!Comman.isConnectionAvailable(getActivity())){
            Toast.makeText(getActivity(),"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            rlMainFrg.setVisibility(View.GONE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            new getFeeTask().execute();
        }

        cbConsltFeeFragVideoAvailbl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    videoAvail = true;
                }else {
                    videoAvail = false;
                }
            }
        });

        cbConsltFeeFragAudioAvailbl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    audioCall = true;
                }else {
                    audioCall = false;
                }
            }
        });

        rplConsltFeeFragUpdate.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                videoFee = etConsltFeeFragVideoPrice.getText().toString();
                audioFee = etConsltFeeFragAudioPrice.getText().toString();

                if (!Comman.isConnectionAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "Please check your internet conection!", Toast.LENGTH_SHORT).show();

                } else if (videoFee.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please provide video call fee!", Toast.LENGTH_SHORT).show();

                } else if (audioFee.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please provide voice call fee!", Toast.LENGTH_SHORT).show();

                } else {
                    prgLoading.setVisibility(View.VISIBLE);
                    new saveFeeTask().execute();

                }

            }
        });

        return view;

    }

    private class saveFeeTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.consltFee);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(getActivity(), "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("video_call_price", videoFee);
                json.put("video_call_available", videoAvail);
                json.put("voice_call_price", audioFee);
                json.put("voice_call_available", audioCall);

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

    private class getFeeTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.consltFee);
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
                                JSONObject object = jsonObject.getJSONObject("data");

                                String video_call_price = object.getString("video_call_price");
                                String video_call_available = object.getString("video_call_available");
                                String voice_call_price = object.getString("voice_call_price");
                                String voice_call_available = object.getString("voice_call_available");

                                etConsltFeeFragVideoPrice.setText(video_call_price);
                                etConsltFeeFragAudioPrice.setText(voice_call_price);

                                if (video_call_available.equalsIgnoreCase("true")){
                                    cbConsltFeeFragVideoAvailbl.setChecked(true);
                                }else {
                                    cbConsltFeeFragVideoAvailbl.setChecked(false);
                                }

                                if(voice_call_available.equalsIgnoreCase("true")){
                                    cbConsltFeeFragAudioAvailbl.setChecked(true);
                                }else {
                                    cbConsltFeeFragAudioAvailbl.setChecked(false);
                                }



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

            rlMainFrg.setVisibility(View.VISIBLE);

        }
    }

}