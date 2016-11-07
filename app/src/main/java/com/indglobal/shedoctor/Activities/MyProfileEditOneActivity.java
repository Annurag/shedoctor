package com.indglobal.shedoctor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.EditSpecialityAdapter;
import com.indglobal.shedoctor.Adapters.LabTestWriteAdapter;
import com.indglobal.shedoctor.Adapters.LanguageAdapter;
import com.indglobal.shedoctor.Adapters.MedicinTypeAdapter;
import com.indglobal.shedoctor.Adapters.SpinLanguageAdapter;
import com.indglobal.shedoctor.Beans.EditLanguageItem;
import com.indglobal.shedoctor.Beans.LabTestItem;
import com.indglobal.shedoctor.Beans.LanguageItem;
import com.indglobal.shedoctor.Beans.MedicinTypeItem;
import com.indglobal.shedoctor.Beans.SpecialityItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.MyProfileAward;
import com.indglobal.shedoctor.Fragments.MyProfileEducation;
import com.indglobal.shedoctor.Fragments.MyProfilePreview;
import com.indglobal.shedoctor.Fragments.MyProfileSpecailization;
import com.indglobal.shedoctor.Fragments.Past_Write_Preview;
import com.indglobal.shedoctor.Fragments.Past_Write_Three;
import com.indglobal.shedoctor.Fragments.Past_Write_Two;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 16/09/2016.
 */
public class MyProfileEditOneActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener, FragmentManager.OnBackStackChangedListener {

    Toolbar mtoolbar;
    FragmentManager manager;
    ProgressBar prgLoading;

    RippleView rplmyprofileNext;
    TextView tvTlbrbasicinfo,tvTlbreducation,tvTlbrsplservices,tvTlbrawdscert;
    int count;

    LinearLayout llprfEdtUplImg;
    ImageView ivMyPrflEdtImg;
    RelativeLayout rlSelectMain;
    RippleView llGallery,llCamera,ivUploadDocAnimCross;
    File file,fileCertf;
    FileBody ffImg;
    public static final int MEDIA_TYPE_IMAGE = 1;

    EditText etEditPrflName,etEditPrflExp,etEditPrflCity,etEditPrflPin,etEditPrflAbout,etEditPrflAddrsOne,etEditPrflAddrsTwo;
    TextView tvEditPrflEmail,tvEditPrflPhone,tvEditPrflRegno;
    Spinner spinEditPrflPrefx,spinEditPrflGendr,spinEditPrflSpeclt,spinEditPrflState,spinEditPrflMedcnTyp,spinEditPrflMothrTong;
    ExpandableHeightGridView grdEditPrflLang;

    LanguageAdapter languageAdapter;
    SpinLanguageAdapter spinLanguageAdapter;

    EditSpecialityAdapter editSpecialityAdapter;

    MedicinTypeAdapter medicinTypeAdapter;

    RelativeLayout rlMain;

    ArrayList<EditLanguageItem> edtArrayListLang;
    EditLanguageItem editLanguageItem;

    public static String prefix,name,gender,specialty,experience,mother_tongue,state,city,pincode,address_line_1,address_line_2,
            medicine_type, self_details,email,mobile,registration_no;
    public static JSONArray checkLangArray,educationArray,speclznArray,servcArray;
    JSONObject eduJsonObj;

    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.myprofile_one);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        manager = getSupportFragmentManager();
        activity = MyProfileEditOneActivity.this;

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplmyprofileNext = (RippleView) findViewById(R.id.rplmyprofileNext);
        tvTlbrbasicinfo = (TextView) findViewById(R.id.tvTlbrbasicinfo);
        tvTlbreducation = (TextView) findViewById(R.id.tvTlbreducation);
        tvTlbrsplservices = (TextView) findViewById(R.id.tvTlbrsplservices);
        tvTlbrawdscert = (TextView) findViewById(R.id.tvTlbrawdscert);

        llprfEdtUplImg = (LinearLayout)findViewById(R.id.llprfEdtUplImg);
        ivMyPrflEdtImg = (ImageView)findViewById(R.id.ivMyPrflEdtImg);
        rlSelectMain = (RelativeLayout)findViewById(R.id.rlSelectMain);
        llGallery = (RippleView) findViewById(R.id.llGallery);
        llCamera = (RippleView) findViewById(R.id.llCamera);
        ivUploadDocAnimCross = (RippleView) findViewById(R.id.ivUploadDocAnimCross);

        etEditPrflName = (EditText)findViewById(R.id.etEditPrflName);
        etEditPrflAddrsOne = (EditText)findViewById(R.id.etEditPrflAddrsOne);
        etEditPrflAddrsTwo = (EditText)findViewById(R.id.etEditPrflAddrsTwo);
        tvEditPrflEmail = (TextView)findViewById(R.id.tvEditPrflEmail);
        tvEditPrflPhone = (TextView)findViewById(R.id.tvEditPrflPhone);
        spinEditPrflPrefx = (Spinner)findViewById(R.id.spinEditPrflPrefx);
        etEditPrflExp = (EditText)findViewById(R.id.etEditPrflExp);
        etEditPrflCity = (EditText)findViewById(R.id.etEditPrflCity);
        etEditPrflPin = (EditText)findViewById(R.id.etEditPrflPin);
        tvEditPrflRegno = (TextView)findViewById(R.id.tvEditPrflRegno);
        spinEditPrflGendr = (Spinner)findViewById(R.id.spinEditPrflGendr);
        spinEditPrflSpeclt = (Spinner)findViewById(R.id.spinEditPrflSpeclt);
        spinEditPrflState = (Spinner)findViewById(R.id.spinEditPrflState);
        spinEditPrflMedcnTyp = (Spinner)findViewById(R.id.spinEditPrflMedcnTyp);
        spinEditPrflMothrTong = (Spinner)findViewById(R.id.spinEditPrflMothrTong);
        etEditPrflAbout = (EditText)findViewById(R.id.etEditPrflAbout);
        grdEditPrflLang = (ExpandableHeightGridView)findViewById(R.id.grdEditPrflLang);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);

        String DoctrImg = Comman.getPreferences(MyProfileEditOneActivity.this, "PRFLIMG");

        if (!DoctrImg.equalsIgnoreCase("")&&!DoctrImg.equalsIgnoreCase("null")){

            Picasso.with(MyProfileEditOneActivity.this).load(DoctrImg).resize(100, 100).error(R.drawable.doc)
                    .placeholder(R.drawable.doc).into(ivMyPrflEdtImg);

        }

        etEditPrflName.setText(MyProfileActivity.names);
        etEditPrflAddrsOne.setText(MyProfileActivity.address_line_1);
        etEditPrflAddrsTwo.setText(MyProfileActivity.address_line_2);
        tvEditPrflEmail.setText(MyProfileActivity.email);
        tvEditPrflPhone.setText(MyProfileActivity.mobile);
        etEditPrflExp.setText(MyProfileActivity.experience);
        etEditPrflCity.setText(MyProfileActivity.city);
        etEditPrflPin.setText(MyProfileActivity.pincode);
        tvEditPrflRegno.setText(MyProfileActivity.registration_no);
        etEditPrflAbout.setText(MyProfileActivity.self_details);

        edtArrayListLang = new ArrayList<>();

        for (int i=0;i<MyProfileActivity.langArrayList.size();i++){

            LanguageItem languageItem = MyProfileActivity.langArrayList.get(i);

            String id = languageItem.getId();
            String lang = languageItem.getLanguage();

            int position = -1;
            position = MyProfileActivity.myLangList.indexOf(id);
            if (position == -1) {
                editLanguageItem = new EditLanguageItem(id,lang,"0");
                edtArrayListLang.add(editLanguageItem);
            } else {
                editLanguageItem = new EditLanguageItem(id,lang,"1");
                edtArrayListLang.add(editLanguageItem);
            }
        }

        languageAdapter = new LanguageAdapter(MyProfileEditOneActivity.this,edtArrayListLang);
        grdEditPrflLang.setAdapter(languageAdapter);
        grdEditPrflLang.setExpanded(true);

        spinLanguageAdapter = new SpinLanguageAdapter(MyProfileEditOneActivity.this,edtArrayListLang);
        spinEditPrflMothrTong.setAdapter(spinLanguageAdapter);
        if (!MyProfileActivity.mother_tongue.equalsIgnoreCase("")&&!MyProfileActivity.mother_tongue.equalsIgnoreCase("null")){

            for (int i =0;i<edtArrayListLang.size();i++){

                EditLanguageItem editLanguageItem = edtArrayListLang.get(i);

                String id = editLanguageItem.getId();

                if (id.equalsIgnoreCase(MyProfileActivity.mother_tongue)){
                    spinEditPrflMothrTong.setSelection(i);
                }

            }
        }

        rplmyprofileNext.setOnRippleCompleteListener(this);
        llGallery.setOnRippleCompleteListener(this);
        llCamera.setOnRippleCompleteListener(this);
        ivUploadDocAnimCross.setOnRippleCompleteListener(this);

        llprfEdtUplImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });

        manager.addOnBackStackChangedListener(this);


        editSpecialityAdapter = new EditSpecialityAdapter(MyProfileEditOneActivity.this,MyProfileActivity.specialityArrayList);
        spinEditPrflSpeclt.setAdapter(editSpecialityAdapter);
        if (!MyProfileActivity.specialty.equalsIgnoreCase("")&&!MyProfileActivity.specialty.equalsIgnoreCase("null")){

            for (int i =0;i<MyProfileActivity.specialityArrayList.size();i++){

                SpecialityItem specialityItem = MyProfileActivity.specialityArrayList.get(i);

                String id = specialityItem.getId();

                if (id.equalsIgnoreCase(MyProfileActivity.specialty)){

                    spinEditPrflSpeclt.setSelection(i);
                }

            }
        }

        medicinTypeAdapter = new MedicinTypeAdapter(MyProfileEditOneActivity.this,MyProfileActivity.medicinTypeItemArrayList);
        spinEditPrflMedcnTyp.setAdapter(medicinTypeAdapter);

        if (!MyProfileActivity.medicine_type.equalsIgnoreCase("")&&!MyProfileActivity.medicine_type.equalsIgnoreCase("null")){

            for (int i =0;i<MyProfileActivity.medicinTypeItemArrayList.size();i++){

                MedicinTypeItem medicinTypeItem = MyProfileActivity.medicinTypeItemArrayList.get(i);

                String id = medicinTypeItem.getId();

                if (id.equalsIgnoreCase(MyProfileActivity.medicine_type)){

                    spinEditPrflMedcnTyp.setSelection(i);
                }

            }
        }


        ArrayAdapter adapterState = ArrayAdapter.createFromResource(MyProfileEditOneActivity.this, R.array.state, R.layout.spinner_item);
        adapterState.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinEditPrflState.setAdapter(adapterState);

         String[] ss = getResources().getStringArray(R.array.state);

        for (int i =0;i<ss.length;i++){

            if ((ss[i]).equalsIgnoreCase(MyProfileActivity.state)){
                spinEditPrflState.setSelection(i);
            }
        }

        ArrayAdapter adaptergender = ArrayAdapter.createFromResource(MyProfileEditOneActivity.this, R.array.gender_array, R.layout.spinner_item);
        adapterState.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinEditPrflGendr.setAdapter(adaptergender);

        String[] sss = getResources().getStringArray(R.array.gender_array);

        for (int i =0;i<sss.length;i++){

            if ((sss[i]).equalsIgnoreCase(MyProfileActivity.gender)){
                spinEditPrflGendr.setSelection(i);
            }
        }

        ArrayAdapter adapterprfx = ArrayAdapter.createFromResource(MyProfileEditOneActivity.this, R.array.prefx_array, R.layout.spinner_item);
        adapterState.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinEditPrflPrefx.setAdapter(adapterprfx);

        String[] ssss = getResources().getStringArray(R.array.prefx_array);

        for (int i =0;i<ssss.length;i++){

            if ((ssss[i]).equalsIgnoreCase(MyProfileActivity.prefix)){
                spinEditPrflPrefx.setSelection(i);
            }
        }
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


    @Override
    public void onBackStackChanged() {
        count = manager.getBackStackEntryCount();

        if (count == 0) {
            tvTlbrbasicinfo.setBackgroundResource(R.drawable.arrow_shape);
            tvTlbreducation.setBackgroundColor(getResources().getColor(R.color.lightGray));
            tvTlbrsplservices.setBackgroundColor(getResources().getColor(R.color.lightGray));
            tvTlbrawdscert.setBackgroundColor(getResources().getColor(R.color.lightGray));

        }

        for (int i = count - 1; i >= 0; i--) {

            FragmentManager.BackStackEntry backStackEntry = manager.getBackStackEntryAt(i);
            String name1 = backStackEntry.getName();

            if (name1.equalsIgnoreCase("Two")) {
                tvTlbrbasicinfo.setBackgroundColor(getResources().getColor(R.color.gray));
                tvTlbreducation.setBackgroundResource(R.drawable.arrow_shape);
                tvTlbrsplservices.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTlbrawdscert.setBackgroundColor(getResources().getColor(R.color.lightGray));

                return;

            } else if (name1.equalsIgnoreCase("Three")) {
                tvTlbrbasicinfo.setBackgroundColor(getResources().getColor(R.color.gray));
                tvTlbreducation.setBackgroundColor(getResources().getColor(R.color.gray));
                tvTlbrsplservices.setBackgroundResource(R.drawable.arrow_shape);
                tvTlbrawdscert.setBackgroundColor(getResources().getColor(R.color.lightGray));

                return;

            } else if (name1.equalsIgnoreCase("Four")) {
                tvTlbrbasicinfo.setBackgroundColor(getResources().getColor(R.color.gray));
                tvTlbreducation.setBackgroundColor(getResources().getColor(R.color.gray));
                tvTlbrsplservices.setBackgroundColor(getResources().getColor(R.color.gray));
                tvTlbrawdscert.setBackgroundColor(getResources().getColor(R.color.gray));

                return;

            }else {
                tvTlbrbasicinfo.setBackgroundResource(R.drawable.arrow_shape);
                tvTlbreducation.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTlbrsplservices.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTlbrawdscert.setBackgroundColor(getResources().getColor(R.color.lightGray));

                return;
            }
        }
    }
    @Override
    public void onComplete(RippleView rippleView) {

        int id = rippleView.getId();

        switch (id) {

            case R.id.rplmyprofileNext:
                if (count == 0) {

                    prefix = spinEditPrflPrefx.getSelectedItem().toString();
                    name = etEditPrflName.getText().toString();
                    email = tvEditPrflEmail.getText().toString();
                    mobile = tvEditPrflPhone.getText().toString();
                    gender = spinEditPrflGendr.getSelectedItem().toString();
                    registration_no = tvEditPrflRegno.getText().toString();
                    experience = etEditPrflExp.getText().toString();
                    state  = spinEditPrflState.getSelectedItem().toString();
                    city = etEditPrflCity.getText().toString();
                    pincode = etEditPrflPin.getText().toString();
                    address_line_1 = etEditPrflAddrsOne.getText().toString();
                    address_line_2 = etEditPrflAddrsTwo.getText().toString();

                    int iii = spinEditPrflSpeclt.getSelectedItemPosition();
                    SpecialityItem specialityItem1 = MyProfileActivity.specialityArrayList.get(iii);
                    specialty = specialityItem1.getId();

                    int ii = spinEditPrflMedcnTyp.getSelectedItemPosition();
                    MedicinTypeItem medicinTypeItem1 = MyProfileActivity.medicinTypeItemArrayList.get(ii);
                    medicine_type = medicinTypeItem1.getId();

                    int pos = spinEditPrflMothrTong.getSelectedItemPosition();
                    LanguageItem languageItem = MyProfileActivity.langArrayList.get(pos);
                    mother_tongue = languageItem.getId();

                    self_details = etEditPrflAbout.getText().toString();

                    checkLangArray = new JSONArray();
                    for (EditLanguageItem f : LanguageAdapter.getBox()) {
                        if (f.langSelected) {
                            //checkLangList.add(f.getId());
                            checkLangArray.put(Integer.parseInt(f.getId()));
                        }
                    }

                    if (!Comman.isConnectionAvailable(MyProfileEditOneActivity.this)){
                        Toast.makeText(MyProfileEditOneActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                    }else if (name.equalsIgnoreCase("")){
                        Toast.makeText(MyProfileEditOneActivity.this,"Please provide your name!",Toast.LENGTH_SHORT).show();
                    }else if(experience.equalsIgnoreCase("")){
                        Toast.makeText(MyProfileEditOneActivity.this,"Please provide your experience!",Toast.LENGTH_SHORT).show();
                    }else {
                        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right);
                        transaction.add(R.id.fragment_myprofile_one, new MyProfileEducation(), "Two");
                        transaction.addToBackStack("Two");
                        transaction.commit();
                    }

                } else if (count == 1) {

                    educationArray = new JSONArray();

                    for (int i = 0; i < MyProfileEducation.rvAddEducation.getAdapter().getItemCount(); i++) {

                        View educationView = MyProfileEducation.rvAddEducation.getChildAt(i);

                        if (educationView.findViewById(R.id.eteducatnName) != null) {

                            Spinner spinEduyear = (Spinner)educationView.findViewById(R.id.spinEduyear);
                            EditText eteducatnName = (EditText)educationView.findViewById(R.id.eteducatnName);
                            EditText eteducatnDegree = (EditText)educationView.findViewById(R.id.eteducatnDegree);

                            String educatnName = eteducatnName.getText().toString();
                            String educatnDegree = eteducatnDegree.getText().toString();

                            if (MyProfileActivity.addEducationItemArrayList.size()>1){

                                if (educatnName.equalsIgnoreCase("")){
                                    Toast.makeText(MyProfileEditOneActivity.this,"Please provide College name!",Toast.LENGTH_SHORT).show();
                                }else if(educatnDegree.equalsIgnoreCase("")){
                                    Toast.makeText(MyProfileEditOneActivity.this,"Please provide degree!",Toast.LENGTH_SHORT).show();
                                }else {

                                    eduJsonObj = new JSONObject();

                                    try{

                                        if (MyProfileActivity.addEducationItemArrayList.get(i).getRlid()==0){
                                            eduJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            eduJsonObj.put("degree",educatnDegree);
                                            eduJsonObj.put("year",Integer.parseInt((String)spinEduyear.getSelectedItem()));
                                            eduJsonObj.put("college_name",educatnName);

                                        }else {
                                            eduJsonObj.put("id",MyProfileActivity.addEducationItemArrayList.get(i).getRlid());
                                            eduJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            eduJsonObj.put("degree",educatnDegree);
                                            eduJsonObj.put("year",Integer.parseInt((String)spinEduyear.getSelectedItem()));
                                            eduJsonObj.put("college_name",educatnName);

                                        }

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                    educationArray.put(eduJsonObj);
                                }

                            }else {

                                if (!educatnName.equalsIgnoreCase("")){

                                    eduJsonObj = new JSONObject();

                                    try{
                                        if (MyProfileActivity.addEducationItemArrayList.get(i).getRlid()==0){
                                            eduJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            eduJsonObj.put("degree",educatnDegree);
                                            eduJsonObj.put("year",Integer.parseInt((String)spinEduyear.getSelectedItem()));
                                            eduJsonObj.put("college_name",educatnName);

                                        }else {
                                            eduJsonObj.put("id",MyProfileActivity.addEducationItemArrayList.get(i).getRlid());
                                            eduJsonObj.put("doctor_id",Integer.parseInt(MyProfileActivity.DrId));
                                            eduJsonObj.put("degree",educatnDegree);
                                            eduJsonObj.put("year",Integer.parseInt((String)spinEduyear.getSelectedItem()));
                                            eduJsonObj.put("college_name",educatnName);

                                        }

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                    educationArray.put(eduJsonObj);
                                }
                            }
                        }
                    }

                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.trans_in_left, R.anim.trans_in_left, R.anim.trans_right_out, R.anim.trans_right_out);
                    transaction.add(R.id.fragment_myprofile_one, new MyProfileSpecailization(), "Three");
                    transaction.addToBackStack("Three");
                    transaction.commit();

                } else if (count == 2) {

                    speclznArray = new JSONArray();
                    servcArray = new JSONArray();

                    for (int j=0;j<MyProfileSpecailization.grdEditPrflSpeclizations.getAdapter().getCount();j++){

                        View speclznview = MyProfileSpecailization.grdEditPrflSpeclizations.getChildAt(j);

                        if (speclznview.findViewById(R.id.tvSpecGrdItmName)!=null){

                            TextView tvSpecGrdItmName = (TextView)speclznview.findViewById(R.id.tvSpecGrdItmName);

                            String speclzn = tvSpecGrdItmName.getText().toString();

                            if (MyProfileActivity.specializationArrayList.size()!=0){

                               speclznArray.put(speclzn);
                            }
                        }
                    }

                    for (int k=0;k<MyProfileSpecailization.grdEditPrflServcs.getAdapter().getCount();k++){

                        View srvcview = MyProfileSpecailization.grdEditPrflServcs.getChildAt(k);

                        if (srvcview.findViewById(R.id.tvSpecGrdItmName)!=null){

                            TextView tvSpecGrdItmName = (TextView)srvcview.findViewById(R.id.tvSpecGrdItmName);

                            String srvc = tvSpecGrdItmName.getText().toString();

                            if (MyProfileActivity.servicesArrayList.size()!=0){

                                servcArray.put(srvc);
                            }
                        }
                    }

                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.trans_in_left, R.anim.trans_in_left, R.anim.trans_right_out, R.anim.trans_right_out);
                    transaction.add(R.id.fragment_myprofile_award, new MyProfileAward(), "Four");
                    transaction.addToBackStack("Four");
                    transaction.commit();

                } else if (count == 3) {

//                    awrdArray = new JSONArray();
//
//                    for (int p = 0; p < MyProfileAward.rvAddAward.getAdapter().getItemCount(); p++) {
//
//                        View awrdview = MyProfileAward.rvAddAward.getChildAt(p);
//
//                        if (awrdview.findViewById(R.id.etawardName) != null) {
//
//                            Spinner spinAwrdyear = (Spinner)awrdview.findViewById(R.id.spinAwrdyear);
//                            EditText etawardName = (EditText)awrdview.findViewById(R.id.etawardName);
//                            EditText etawardDtl = (EditText)awrdview.findViewById(R.id.etawardDtl);
//
//                            String awardname = etawardName.getText().toString();
//                            String awardDtl = etawardDtl.getText().toString();
//
//                            if (MyProfileActivity.addAwardItemArrayList.size()>1){
//
//                                if (awardname.equalsIgnoreCase("")){
//                                    Toast.makeText(MyProfileEditOneActivity.this,"Please provide Award name!",Toast.LENGTH_SHORT).show();
//                                }else if(awardDtl.equalsIgnoreCase("")){
//                                    Toast.makeText(MyProfileEditOneActivity.this,"Please provide award details!",Toast.LENGTH_SHORT).show();
//                                }else {
//
//                                    awrdJsonObj = new JSONObject();
//
//                                    try{
//                                        awrdJsonObj.put("id",p);
//                                        awrdJsonObj.put("name",awardname);
//                                        awrdJsonObj.put("year",spinAwrdyear.getSelectedItem());
//                                        awrdJsonObj.put("details",awardDtl);
//
//                                    }catch (JSONException e){
//                                        e.printStackTrace();
//                                    }
//
//                                    awrdArray.put(awrdJsonObj);
//                                }
//
//                            }else {
//
//                                if (!awardname.equalsIgnoreCase("")){
//
//                                    awrdJsonObj = new JSONObject();
//
//                                    try{
//                                        awrdJsonObj.put("id",p);
//                                        awrdJsonObj.put("name",awardname);
//                                        awrdJsonObj.put("year",spinAwrdyear.getSelectedItem());
//                                        awrdJsonObj.put("details",awardDtl);
//
//                                    }catch (JSONException e){
//                                        e.printStackTrace();
//                                    }
//
//                                    awrdArray.put(awrdJsonObj);
//                                }
//                            }
//                        }
//                    }

                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.trans_in_left, R.anim.trans_in_left, R.anim.trans_right_out, R.anim.trans_right_out);
                    transaction.add(R.id.fragment_myprofile_preview, new MyProfilePreview(),"Four");
                    transaction.addToBackStack("Four");
                    transaction.commit();
                }
                break;

            case R.id.llGallery:
                selectImage();
                break;

            case R.id.llCamera:
                captureImage();
                break;

            case R.id.ivUploadDocAnimCross:
                clodeAnim();
                break;
        }

    }

    public void startAnim(){
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_layout);
        rlSelectMain.startAnimation(slideUp);
        rlSelectMain.setVisibility(View.VISIBLE);
    }

    public void clodeAnim(){
        Animation slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_layout);
        rlSelectMain.startAnimation(slidedown);
        rlSelectMain.setVisibility(View.GONE);
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        try {

            startActivityForResult(Intent.createChooser(intent, "Select Picture to Upload"), 2);

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(MyProfileEditOneActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
        clodeAnim();
    }

    public void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        try {

            startActivityForResult(intent, 1);

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(MyProfileEditOneActivity.this, "No Camrera found in this mobile .", Toast.LENGTH_SHORT).show();
        }
        clodeAnim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {

                Uri selectedImage = data.getData();

                file = new File(selectedImage.getPath().toString());
                try {
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    file = new File(picturePath);
                    c.close();
                }catch (Exception e){
                    e.printStackTrace();
                    file = new File(selectedImage.getPath().toString());
                }

                if (file.getName().endsWith(".jpg")||file.getName().endsWith(".JPG")||file.getName().endsWith(".png")||file.getName().endsWith(".PNG")||file.getName().endsWith(".JPEG")||file.getName().endsWith(".jpeg")){

                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();

                    fileCertf = new File(picturePath);
                    fileCertf = saveBitmapToFile(fileCertf);

                    if(!Comman.isConnectionAvailable(MyProfileEditOneActivity.this)){
                        Toast.makeText(MyProfileEditOneActivity.this,"PLease check your internet connection!",Toast.LENGTH_SHORT).show();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        new UploadImgTask().execute();
                    }

                }else {
                    Toast.makeText(MyProfileEditOneActivity.this,"Please attached a image file type !",Toast.LENGTH_SHORT).show();
                }

            }else if (requestCode == 1) {

                try {
                    String comPath = Comman.getPreferences(MyProfileEditOneActivity.this, "picturePath");
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }

                finally {

                    String comPath = Comman.getPreferences(MyProfileEditOneActivity.this, "picturePath");
                    fileCertf = new File(comPath);
                    fileCertf = saveBitmapToFile(fileCertf);
                    String Certf = fileCertf.getName().toString();

                    if(!Comman.isConnectionAvailable(MyProfileEditOneActivity.this)){
                        Toast.makeText(MyProfileEditOneActivity.this,"PLease check your internet connection!",Toast.LENGTH_SHORT).show();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        new UploadImgTask().execute();
                    }
                }



            }
        }

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private  File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SheDoctFoldr");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

           String totalPathq = mediaFile.getAbsolutePath();
            Comman.setPreferences(MyProfileEditOneActivity.this, "picturePath", totalPathq);

        } else {
            return null;
        }

        return mediaFile;
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    private class UploadImgTask extends AsyncTask<String, Void, String> {

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

                String token = Comman.getPreferences(MyProfileEditOneActivity.this, "TOKEN");
                String productListUrl = getResources().getString(R.string.uploadimgUrl);

                String apiurls = productListUrl+"?token="+token;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(apiurls);

                try {

                    ffImg = new FileBody(fileCertf);

                }catch (Exception e){
                    e.printStackTrace();
                }


                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("file", ffImg);

                httppost.setEntity(reqEntity);

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
                if(result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");
                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            String data = jsonObject.getString("data");
                            Comman.setPreferences(MyProfileEditOneActivity.this, "PRFLIMG", data);
                            Comman.setPreferences(MyProfileEditOneActivity.this, "PicChang", "1");


                            String DoctrImg = Comman.getPreferences(MyProfileEditOneActivity.this, "PRFLIMG");

                            if (!DoctrImg.equalsIgnoreCase("")&&!DoctrImg.equalsIgnoreCase("null")){

                                Picasso.with(MyProfileEditOneActivity.this).load(DoctrImg).resize(100, 100).error(R.drawable.doc)
                                        .placeholder(R.drawable.doc).into(ivMyPrflEdtImg);

                            }

                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(MyProfileEditOneActivity.this,message,Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(MyProfileEditOneActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MyProfileEditOneActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            prgLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
