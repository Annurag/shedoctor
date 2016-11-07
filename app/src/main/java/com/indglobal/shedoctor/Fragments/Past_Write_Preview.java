package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Activities.BaseActivity;
import com.indglobal.shedoctor.Activities.PastApointDetail;
import com.indglobal.shedoctor.Activities.PastWriteOneActivity;
import com.indglobal.shedoctor.Adapters.LabGridAdapter;
import com.indglobal.shedoctor.Adapters.MedicnListAdapter;
import com.indglobal.shedoctor.Beans.MedicinItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 9/12/16.
 */
public class Past_Write_Preview extends Fragment implements RippleView.OnRippleCompleteListener {

    LayoutInflater inflater;

    ProgressBar prgLoading;

    TextView tvPrscptnDtlDate,tvPrscptnDtlDocName,tvPrscptnDtlDocSpec,tvPrscptnDtlPtntName,
            tvPrscptnDtlPtntId,tvPrscptnDtlDiagnosRprt,tvPrscptnDtlLabNotAvail,tvPrscptnDtlNextVisit,
            tvPrscptnDtlMdcnNotAvail,tvPrscptnDtlDocBtmName,tvPrscptnDtlDocBtmSpec,tvPrscptnDtlDocBtmRegNo,tvNoData,tvPrscptnDtlLabOthers;
    ImageView ivPrscptnDtlDocSign;
    ExpandableHeightGridView grdPrscptnDtlLab;
    RecyclerView rvPrscptnDtlMdcn;
    RelativeLayout rlMainNew;
    RippleView rpltvEdit,rplivCross,rplPastWrtOne;

    LabGridAdapter labGridAdapter;
    MedicinItem medicinItem;
    ArrayList<MedicinItem> medicinItemArrayList;
    MedicnListAdapter medicnListAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_write_preview, container, false);

        rlMainNew = (RelativeLayout)view.findViewById(R.id.rlMainNew);
        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        tvPrscptnDtlDate = (TextView)view.findViewById(R.id.tvPrscptnDtlDate);
        tvPrscptnDtlDocName = (TextView)view.findViewById(R.id.tvPrscptnDtlDocName);
        tvPrscptnDtlDocSpec = (TextView)view.findViewById(R.id.tvPrscptnDtlDocSpec);
        tvPrscptnDtlPtntName = (TextView)view.findViewById(R.id.tvPrscptnDtlPtntName);
        tvPrscptnDtlPtntId = (TextView)view.findViewById(R.id.tvPrscptnDtlPtntId);
        tvPrscptnDtlDiagnosRprt = (TextView)view.findViewById(R.id.tvPrscptnDtlDiagnosRprt);
        tvPrscptnDtlLabNotAvail = (TextView)view.findViewById(R.id.tvPrscptnDtlLabNotAvail);
        tvPrscptnDtlMdcnNotAvail = (TextView)view.findViewById(R.id.tvPrscptnDtlMdcnNotAvail);
        tvPrscptnDtlDocBtmName = (TextView)view.findViewById(R.id.tvPrscptnDtlDocBtmName);
        tvPrscptnDtlDocBtmSpec = (TextView)view.findViewById(R.id.tvPrscptnDtlDocBtmSpec);
        tvPrscptnDtlDocBtmRegNo = (TextView)view.findViewById(R.id.tvPrscptnDtlDocBtmRegNo);
        tvPrscptnDtlNextVisit = (TextView)view.findViewById(R.id.tvPrscptnDtlNextVisit);
        tvPrscptnDtlLabOthers = (TextView)view.findViewById(R.id.tvPrscptnDtlLabOthers);
        tvNoData = (TextView)view.findViewById(R.id.tvNoData);
        rpltvEdit = (RippleView)view.findViewById(R.id.rpltvEdit);
        rplivCross = (RippleView)view.findViewById(R.id.rplivCross);
        rplPastWrtOne = (RippleView)view.findViewById(R.id.rplPastWrtOne);

        ivPrscptnDtlDocSign = (ImageView)view.findViewById(R.id.ivPrscptnDtlDocSign);
        grdPrscptnDtlLab = (ExpandableHeightGridView)view.findViewById(R.id.grdPrscptnDtlLab);
        rvPrscptnDtlMdcn = (RecyclerView)view.findViewById(R.id.rvPrscptnDtlMdcn);

        labGridAdapter = new LabGridAdapter(getActivity());
        medicnListAdapter = new MedicnListAdapter(getActivity());
        medicinItemArrayList = new ArrayList<>();

        if(!Comman.isConnectionAvailable(getActivity())){
            Toast.makeText(getActivity(),"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            rlMainNew.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            new getPrescriptionDtlTask().execute();
        }

        rplPastWrtOne.setOnRippleCompleteListener(this);
        rplivCross.setOnRippleCompleteListener(this);
        rpltvEdit.setOnRippleCompleteListener(this);

        return view;

    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplivCross:
                onBackPressed();
                break;

            case R.id.rpltvEdit:
                onBackPressed();
                break;

            case R.id.rplPastWrtOne:
                if(!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    new savePrescriptionDtlTask().execute();
                }
                break;
        }
    }


    private class savePrescriptionDtlTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.getPrscrptnUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(getActivity(), "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                JSONObject json = new JSONObject();
                json.put("appointment_id", PastApointDetail.appointment_id);
                json.put("report", PastWriteOneActivity.DiagnosisReport);
                json.put("next_visit", PastWriteOneActivity.NextVisit);

                JSONArray jsonArray  = new JSONArray();
                for (int i=0;i<PastWriteOneActivity.checkLabsNameList.size();i++){
                    jsonArray.put(i,PastWriteOneActivity.checkLabsNameList.get(i));
                }

                json.put("lab_tests",jsonArray);
                json.put("other_lab_tests",PastWriteOneActivity.OthersLab);
                json.put("medicines",PastWriteOneActivity.medicinjsonArray);


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
                            Intent ii = new Intent(getActivity(), BaseActivity.class);
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

    private class getPrescriptionDtlTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.getDocPatntDtlUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(getActivity(), "TOKEN");
                HttpGet httppost = new HttpGet(productListUrl + "?token=" + token+"&appointment_id="+ PastApointDetail.appointment_id);


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

                                JSONObject object = jsonObject.getJSONObject("data");

                                tvPrscptnDtlDiagnosRprt.setText(PastWriteOneActivity.DiagnosisReport);
                                tvPrscptnDtlNextVisit.setText("Next Visit:- "+PastWriteOneActivity.NextVisit);

                                tvPrscptnDtlDate.setText(PastApointDetail.date);


                                try {

                                    labGridAdapter = new LabGridAdapter(getActivity(),PastWriteOneActivity.checkLabsNameList);
                                    grdPrscptnDtlLab.setAdapter(labGridAdapter);
                                    grdPrscptnDtlLab.setExpanded(true);

                                    if (PastWriteOneActivity.checkLabsNameList.size()==0){
                                        tvPrscptnDtlLabNotAvail.setVisibility(View.VISIBLE);
                                    }else {
                                        tvPrscptnDtlLabNotAvail.setVisibility(View.GONE);
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                    tvPrscptnDtlLabNotAvail.setVisibility(View.VISIBLE);
                                }

                                if (!PastWriteOneActivity.OthersLab.equalsIgnoreCase("")){
                                    tvPrscptnDtlLabOthers.setText(PastWriteOneActivity.OthersLab);
                                    tvPrscptnDtlLabOthers.setTextColor(getResources().getColor(R.color.lightBlack));
                                }


                                try {

                                    for (int j = 0; j<PastWriteOneActivity.medicinjsonArray.length(); j++){

                                        JSONObject jsonObjectMdcn = PastWriteOneActivity.medicinjsonArray.getJSONObject(j);

                                        String name = jsonObjectMdcn.getString("name");
                                        String type = jsonObjectMdcn.getString("type");
                                        String morning = jsonObjectMdcn.getString("morning");
                                        String afternoon = jsonObjectMdcn.getString("afternoon");
                                        String evening = jsonObjectMdcn.getString("evening");
                                        String night = jsonObjectMdcn.getString("night");
                                        String note = jsonObjectMdcn.getString("note");

                                        medicinItem  = new MedicinItem(name,type,morning,afternoon,evening,night,note);
                                        medicinItemArrayList.add(medicinItem);

                                    }

                                    if (PastWriteOneActivity.medicinjsonArray.length()==0){
                                        tvPrscptnDtlMdcnNotAvail.setVisibility(View.VISIBLE);
                                    }else {
                                        tvPrscptnDtlMdcnNotAvail.setVisibility(View.GONE);
                                    }

                                    medicnListAdapter = new MedicnListAdapter(getActivity(),medicinItemArrayList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    rvPrscptnDtlMdcn.setLayoutManager(mLayoutManager);
                                    rvPrscptnDtlMdcn.setAdapter(medicnListAdapter);



                                }catch (Exception e){
                                    e.printStackTrace();
                                    tvPrscptnDtlMdcnNotAvail.setVisibility(View.VISIBLE);
                                }


                                JSONObject jsonObjDoc = object.getJSONObject("doctor");
                                String docname = jsonObjDoc.getString("name");
                                String specialty = jsonObjDoc.getString("specialty");
                                String registration_no = jsonObjDoc.getString("registration_no");
                                String signature = jsonObjDoc.getString("signature");

                                if (!signature.equalsIgnoreCase("")){
                                    Bitmap myBitmapAgain = decodeBase64(signature);
                                    ivPrscptnDtlDocSign.setImageBitmap(myBitmapAgain);
                                }

                                tvPrscptnDtlDocName.setText(docname);
                                tvPrscptnDtlDocBtmName.setText(docname);
                                tvPrscptnDtlDocSpec.setText("Speciality : "+specialty);
                                tvPrscptnDtlDocBtmRegNo.setText(registration_no);

                                JSONObject jsonObjPtnt = object.getJSONObject("patient");
                                String patntName = jsonObjPtnt.getString("name");
                                String shedct_id = jsonObjPtnt.getString("shedct_id");

                                tvPrscptnDtlPtntName.setText(patntName);
                                tvPrscptnDtlPtntId.setText("Patient ID : "+shedct_id);

                                rlMainNew.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);


                            }catch (Exception e){
                                e.printStackTrace();
                                rlMainNew.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }

                            prgLoading.setVisibility(View.GONE);

                        } else {

                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            rlMainNew.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                            prgLoading.setVisibility(View.GONE);

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                rlMainNew.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }



}