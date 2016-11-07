package com.indglobal.shedoctor.Activities;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Android on 8/29/16.
 */
public class UploadDocActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener {

    ProgressBar prgLoading;

    RippleView tvUploadSubmit,llGallery,llCamera,ivUploadDocAnimCross,tvuploadMedicalCertificate,tvuploadMedicalDegree,tvuploadGovtId;
    RelativeLayout rlSelectMain,rlMedicalCertificate,rlMedicalDegree,rlGovtId;
    TextView tvMedicalCertificate,tvMedicalDegree,tvGovtId,tvMedicalCertificateVerify,tvMedicalDegreeVerify,tvGovtIdVerify,tvMedicalCertificateReason,tvMedicalDegreeReason,tvGovtIdReason;
    ImageView ivMedicalCertificate,ivMedicalDegree,ivGovtId;

    private static final int REQUEST_ENABLE_STORAGE = 2;
    String [] PERMISSIONS = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    File fileCertf,fileDegree,fileGovt,file;
    String uploadSelect,cerf,degr,gov;
    boolean processing = false;

    FileBody medical_registration_certificate,degree,government_id;
    String pdfImg;

    String medical_registration_certificate_verified,government_id_verified,degree_verified;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.upload_doc_main);

        if(!Comman.verifyStoragePermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_ENABLE_STORAGE);
        }

        tvuploadMedicalDegree = (RippleView)findViewById(R.id.tvuploadMedicalDegree);
        tvuploadGovtId = (RippleView)findViewById(R.id.tvuploadGovtId);
        tvuploadMedicalCertificate = (RippleView)findViewById(R.id.tvuploadMedicalCertificate);

        tvUploadSubmit=(RippleView)findViewById(R.id.tvUploadSubmit);
        llGallery=(RippleView)findViewById(R.id.llGallery);
        llCamera=(RippleView)findViewById(R.id.llCamera);
        ivUploadDocAnimCross = (RippleView)findViewById(R.id.ivUploadDocAnimCross);

        rlSelectMain=(RelativeLayout)findViewById(R.id.rlSelectMain);
        rlMedicalCertificate = (RelativeLayout)findViewById(R.id.rlMedicalCertificate);
        tvMedicalCertificate = (TextView)findViewById(R.id.tvMedicalCertificate);
        ivMedicalCertificate = (ImageView)findViewById(R.id.ivMedicalCertificate);
        rlMedicalDegree = (RelativeLayout)findViewById(R.id.rlMedicalDegree);
        rlGovtId = (RelativeLayout)findViewById(R.id.rlGovtId);
        tvMedicalDegree = (TextView)findViewById(R.id.tvMedicalDegree);
        tvGovtId = (TextView)findViewById(R.id.tvGovtId);
        ivMedicalDegree = (ImageView)findViewById(R.id.ivMedicalDegree);
        ivGovtId = (ImageView)findViewById(R.id.ivGovtId);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvMedicalCertificateVerify = (TextView)findViewById(R.id.tvMedicalCertificateVerify);
        tvMedicalDegreeVerify = (TextView)findViewById(R.id.tvMedicalDegreeVerify);
        tvGovtIdVerify = (TextView)findViewById(R.id.tvGovtIdVerify);
        tvMedicalCertificateReason = (TextView)findViewById(R.id.tvMedicalCertificateReason);
        tvMedicalDegreeReason = (TextView)findViewById(R.id.tvMedicalDegreeReason);
        tvGovtIdReason = (TextView)findViewById(R.id.tvGovtIdReason);


        medical_registration_certificate_verified =  Comman.getPreferences(UploadDocActivity.this, "medical_registration_certificate_verified");
        government_id_verified = Comman.getPreferences(UploadDocActivity.this, "government_id_verified");
        degree_verified = Comman.getPreferences(UploadDocActivity.this, "degree_verified");

        String medical_registration_certificate_reject_reason = Comman.getPreferences(UploadDocActivity.this, "medical_registration_certificate_reject_reason");
        String medical_degree_reject_reason = Comman.getPreferences(UploadDocActivity.this, "medical_degree_reject_reason");
        String government_id_reject_reason = Comman.getPreferences(UploadDocActivity.this, "government_id_reject_reason");


        if (medical_registration_certificate_verified.equalsIgnoreCase("Rejected")){

            tvMedicalCertificateVerify.setText("Rejected");
            tvMedicalCertificateVerify.setTextColor(getResources().getColor(R.color.red));
            tvMedicalCertificateReason.setText(medical_registration_certificate_reject_reason);

        }else if (medical_registration_certificate_verified.equalsIgnoreCase("Under Review")){

            tvMedicalCertificateVerify.setText("Under Review");
            tvMedicalCertificateVerify.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvMedicalCertificateReason.setText("");
            tvuploadMedicalCertificate.setVisibility(View.GONE);
            rlMedicalCertificate.setVisibility(View.GONE);

        }else if (medical_registration_certificate_verified.equalsIgnoreCase("Verified")){

            tvMedicalCertificateVerify.setText("Verified");
            tvMedicalCertificateVerify.setTextColor(getResources().getColor(R.color.green));
            tvMedicalCertificateReason.setText("");
            tvuploadMedicalCertificate.setVisibility(View.GONE);
            rlMedicalCertificate.setVisibility(View.GONE);

        }else {

            tvMedicalCertificateVerify.setText("");
            tvMedicalCertificateReason.setText("");
            tvuploadMedicalCertificate.setVisibility(View.VISIBLE);
            rlMedicalCertificate.setVisibility(View.GONE);

        }


        if (degree_verified.equalsIgnoreCase("Rejected")){

            tvMedicalDegreeVerify.setText("Rejected");
            tvMedicalDegreeVerify.setTextColor(getResources().getColor(R.color.red));
            tvMedicalDegreeReason.setText(medical_degree_reject_reason);

        }else if (degree_verified.equalsIgnoreCase("Under Review")){

            tvMedicalDegreeVerify.setText("Under Review");
            tvMedicalDegreeVerify.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvMedicalDegreeReason.setText("");
            tvuploadMedicalDegree.setVisibility(View.GONE);
            rlMedicalDegree.setVisibility(View.GONE);

        }else if (degree_verified.equalsIgnoreCase("Verified")){

            tvMedicalDegreeVerify.setText("Verified");
            tvMedicalDegreeVerify.setTextColor(getResources().getColor(R.color.green));
            tvMedicalDegreeReason.setText("");
            tvuploadMedicalDegree.setVisibility(View.GONE);
            rlMedicalDegree.setVisibility(View.GONE);

        }else {

            tvMedicalDegreeVerify.setText("");
            tvMedicalDegreeReason.setText("");
            tvuploadMedicalDegree.setVisibility(View.VISIBLE);
            rlMedicalDegree.setVisibility(View.GONE);

        }


        if (government_id_verified.equalsIgnoreCase("Rejected")){

            tvGovtIdVerify.setText("Rejected");
            tvGovtIdVerify.setTextColor(getResources().getColor(R.color.red));
            tvGovtIdReason.setText(government_id_reject_reason);

        }else if (government_id_verified.equalsIgnoreCase("Under Review")){

            tvGovtIdVerify.setText("Under Review");
            tvGovtIdVerify.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvGovtIdReason.setText("");
            tvuploadGovtId.setVisibility(View.GONE);
            rlGovtId.setVisibility(View.GONE);

        }else if (government_id_verified.equalsIgnoreCase("Verified")){

            tvGovtIdVerify.setText("Verified");
            tvGovtIdVerify.setTextColor(getResources().getColor(R.color.green));
            tvGovtIdReason.setText("");
            tvuploadGovtId.setVisibility(View.GONE);
            rlGovtId.setVisibility(View.GONE);

        }else {

            tvGovtIdVerify.setText("");
            tvGovtIdReason.setText("");
            tvuploadGovtId.setVisibility(View.VISIBLE);
            rlGovtId.setVisibility(View.GONE);

        }

        llGallery.setOnRippleCompleteListener(this);
        llCamera.setOnRippleCompleteListener(this);
        ivUploadDocAnimCross.setOnRippleCompleteListener(this);
        tvUploadSubmit.setOnRippleCompleteListener(this);

        tvuploadMedicalDegree.setOnRippleCompleteListener(this);
        tvuploadGovtId.setOnRippleCompleteListener(this);
        tvuploadMedicalCertificate.setOnRippleCompleteListener(this);

        ivMedicalCertificate.setOnClickListener(this);
        ivMedicalDegree.setOnClickListener(this);
        ivGovtId.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.ivMedicalCertificate:
                tvMedicalCertificate.setText("");
                rlMedicalCertificate.setVisibility(View.GONE);
                tvuploadMedicalCertificate.setVisibility(View.VISIBLE);
                break;

            case R.id.ivMedicalDegree:
                tvMedicalDegree.setText("");
                rlMedicalDegree.setVisibility(View.GONE);
                tvuploadMedicalDegree.setVisibility(View.VISIBLE);
                break;

            case R.id.ivGovtId:
                tvGovtId.setText("");
                rlGovtId.setVisibility(View.GONE);
                tvuploadGovtId.setVisibility(View.VISIBLE);
                break;

        }


    }



    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){

            case R.id.ivUploadDocAnimCross:
                clodeAnim();
                break;

            case R.id.tvuploadMedicalCertificate:
                uploadSelect = "1";
                startAnim();
                break;

            case R.id.tvuploadMedicalDegree:
                uploadSelect = "2";
                startAnim();
                break;

            case R.id.tvuploadGovtId:
                uploadSelect = "3";
                startAnim();
                break;


            case R.id.tvUploadSubmit:

                if(!processing) {

                    cerf = tvMedicalCertificate.getText().toString();
                    degr = tvMedicalDegree.getText().toString();
                    gov = tvGovtId.getText().toString();


                    if (!Comman.isConnectionAvailable(UploadDocActivity.this)) {
                        Toast.makeText(UploadDocActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();

                    } else if (cerf.equalsIgnoreCase("")) {
                        if (!medical_registration_certificate_verified.equalsIgnoreCase("Verified")&&!medical_registration_certificate_verified.equalsIgnoreCase("Under Review")) {
                            Toast.makeText(UploadDocActivity.this, "Please provide your Medical Certificate!", Toast.LENGTH_SHORT).show();
                        }else {
                            if (degr.equalsIgnoreCase("")) {
                                if (!degree_verified.equalsIgnoreCase("Verified")&&!degree_verified.equalsIgnoreCase("Under Review")) {
                                    Toast.makeText(UploadDocActivity.this, "Please provide your Medical Degree!", Toast.LENGTH_SHORT).show();
                                }else {
                                    if (gov.equalsIgnoreCase("")) {
                                        if (!government_id_verified.equalsIgnoreCase("Verified")&&!government_id_verified.equalsIgnoreCase("Under Review")) {
                                            Toast.makeText(UploadDocActivity.this, "Please provide your Govt id!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            prgLoading.setVisibility(View.VISIBLE);
                                            processing = true;
                                            new UploadDocTask().execute();
                                        }

                                    } else {
                                        prgLoading.setVisibility(View.VISIBLE);
                                        processing = true;
                                        new UploadDocTask().execute();
                                    }
                                }

                            } else if (gov.equalsIgnoreCase("")) {
                                if (!government_id_verified.equalsIgnoreCase("Verified")&&!government_id_verified.equalsIgnoreCase("Under Review")) {
                                    Toast.makeText(UploadDocActivity.this, "Please provide your Govt id!", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                prgLoading.setVisibility(View.VISIBLE);
                                processing = true;
                                new UploadDocTask().execute();
                            }
                        }

                    } else if (degr.equalsIgnoreCase("")) {
                        if (!degree_verified.equalsIgnoreCase("Verified")&&!degree_verified.equalsIgnoreCase("Under Review")) {
                            Toast.makeText(UploadDocActivity.this, "Please provide your Medical Degree!", Toast.LENGTH_SHORT).show();
                        }else {
                            if (gov.equalsIgnoreCase("")) {
                                if (!government_id_verified.equalsIgnoreCase("Verified")&&!government_id_verified.equalsIgnoreCase("Under Review")) {
                                    Toast.makeText(UploadDocActivity.this, "Please provide your Govt id!", Toast.LENGTH_SHORT).show();
                                }else {
                                    prgLoading.setVisibility(View.VISIBLE);
                                    processing = true;
                                    new UploadDocTask().execute();
                                }

                            } else {
                                prgLoading.setVisibility(View.VISIBLE);
                                processing = true;
                                new UploadDocTask().execute();
                            }
                        }

                    } else if (gov.equalsIgnoreCase("")) {
                        if (!government_id_verified.equalsIgnoreCase("Verified")&&!government_id_verified.equalsIgnoreCase("Under Review")) {
                            Toast.makeText(UploadDocActivity.this, "Please provide your Govt id!", Toast.LENGTH_SHORT).show();
                        }else {
                            prgLoading.setVisibility(View.VISIBLE);
                            processing = true;
                            new UploadDocTask().execute();
                        }

                    } else {
                        prgLoading.setVisibility(View.VISIBLE);
                        processing = true;
                        new UploadDocTask().execute();
                    }
                }

                break;

            case R.id.llGallery:
                selectImage();
                break;

            case R.id.llCamera:
                captureImage();
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
        Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(uri, "*/*");

        try {

            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(UploadDocActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(UploadDocActivity.this, "No Camrera found in this mobile .", Toast.LENGTH_SHORT).show();
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



                if(file.getName().endsWith(".pdf")){
                    String uploadedFileName = file.getName().toString();
                    StringTokenizer tokens = new StringTokenizer(uploadedFileName, ":");


                    switch (uploadSelect){

                        case "1":
                            fileCertf = new File(selectedImage.getPath().toString());
                            tvMedicalCertificate.setText(uploadedFileName);
                            rlMedicalCertificate.setVisibility(View.VISIBLE);
                            tvuploadMedicalCertificate.setVisibility(View.GONE);
                            tvMedicalCertificateVerify.setText("");
                            tvMedicalCertificateReason.setText("");
                            break;

                        case "2":
                            fileDegree = new File(selectedImage.getPath().toString());
                            tvMedicalDegree.setText(uploadedFileName);
                            rlMedicalDegree.setVisibility(View.VISIBLE);
                            tvuploadMedicalDegree.setVisibility(View.GONE);
                            tvMedicalDegreeVerify.setText("");
                            tvMedicalDegreeReason.setText("");
                            break;

                        case "3":
                            fileGovt = new File(selectedImage.getPath().toString());
                            tvGovtId.setText(uploadedFileName);
                            rlGovtId.setVisibility(View.VISIBLE);
                            tvuploadGovtId.setVisibility(View.GONE);
                            tvGovtIdVerify.setText("");
                            tvGovtIdReason.setText("");
                            break;
                    }


                }else if (file.getName().endsWith(".jpg")||file.getName().endsWith(".JPG")||file.getName().endsWith(".png")||file.getName().endsWith(".PNG")||file.getName().endsWith(".JPEG")||file.getName().endsWith(".jpeg")){

                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();

                    switch (uploadSelect){

                        case "1":
                            fileCertf = new File(picturePath);
                            fileCertf = saveBitmapToFile(fileCertf);
                            String Certf = fileCertf.getName().toString();
                            tvMedicalCertificate.setText(Certf);
                            rlMedicalCertificate.setVisibility(View.VISIBLE);
                            tvuploadMedicalCertificate.setVisibility(View.GONE);
                            tvMedicalCertificateVerify.setText("");
                            tvMedicalCertificateReason.setText("");
                            break;

                        case "2":
                            fileDegree = new File(picturePath);
                            fileDegree = saveBitmapToFile(fileDegree);
                            String Degree = fileDegree.getName().toString();
                            tvMedicalDegree.setText(Degree);
                            rlMedicalDegree.setVisibility(View.VISIBLE);
                            tvuploadMedicalDegree.setVisibility(View.GONE);
                            tvMedicalDegreeVerify.setText("");
                            tvMedicalDegreeReason.setText("");
                            break;

                        case "3":
                            fileGovt = new File(picturePath);
                            fileGovt = saveBitmapToFile(fileGovt);
                            String Govt = fileGovt.getName().toString();
                            tvGovtId.setText(Govt);
                            rlGovtId.setVisibility(View.VISIBLE);
                            tvuploadGovtId.setVisibility(View.GONE);
                            tvGovtIdVerify.setText("");
                            tvGovtIdReason.setText("");
                            break;
                    }

                }else {
                    Toast.makeText(UploadDocActivity.this,"Please attached a pdf/image file type !",Toast.LENGTH_SHORT).show();
                }

            }else if (requestCode == 1) {

                String comPath;

                try {
                    comPath = Comman.getPreferences(UploadDocActivity.this, "picturePath");
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }

                finally {

                    comPath = Comman.getPreferences(UploadDocActivity.this, "picturePath");
                    fileCertf = new File(comPath);
                    String Certf = fileCertf.getName().toString();

                }

                switch (uploadSelect){

                    case "1":
                        fileCertf = new File(comPath);
                        fileCertf = saveBitmapToFile(fileCertf);
                        String Certf = fileCertf.getName().toString();
                        tvMedicalCertificate.setText(Certf);
                        rlMedicalCertificate.setVisibility(View.VISIBLE);
                        tvuploadMedicalCertificate.setVisibility(View.GONE);
                        tvMedicalCertificateVerify.setText("");
                        tvMedicalCertificateReason.setText("");
                        break;

                    case "2":
                        fileDegree = new File(comPath);
                        fileDegree = saveBitmapToFile(fileDegree);
                        String Degree = fileDegree.getName().toString();
                        tvMedicalDegree.setText(Degree);
                        rlMedicalDegree.setVisibility(View.VISIBLE);
                        tvuploadMedicalDegree.setVisibility(View.GONE);
                        tvMedicalDegreeVerify.setText("");
                        tvMedicalDegreeReason.setText("");
                        break;

                    case "3":
                        fileGovt = new File(comPath);
                        fileGovt = saveBitmapToFile(fileGovt);
                        String Govt = fileGovt.getName().toString();
                        tvGovtId.setText(Govt);
                        rlGovtId.setVisibility(View.VISIBLE);
                        tvuploadGovtId.setVisibility(View.GONE);
                        tvGovtIdVerify.setText("");
                        tvGovtIdReason.setText("");
                        break;
                }


            }
        }

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

    private class UploadDocTask extends AsyncTask<String, Void, String> {

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

                String token = Comman.getPreferences(UploadDocActivity.this,"TOKEN");
                String productListUrl = getResources().getString(R.string.uploaddocUrl);

                String apiurls = productListUrl+"?token="+token;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(apiurls);


                try {

                    if (!medical_registration_certificate_verified.equalsIgnoreCase("Verified")&&!medical_registration_certificate_verified.equalsIgnoreCase("Under Review")) {
                        medical_registration_certificate = new FileBody(fileCertf);
                    }

                    if (!government_id_verified.equalsIgnoreCase("Verified")&&!government_id_verified.equalsIgnoreCase("Under Review")) {
                        government_id = new FileBody(fileGovt);
                    }

                    if (!degree_verified.equalsIgnoreCase("Verified")&&!degree_verified.equalsIgnoreCase("Under Review")) {
                        degree = new FileBody(fileDegree);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }


                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (!medical_registration_certificate_verified.equalsIgnoreCase("Verified")&&!medical_registration_certificate_verified.equalsIgnoreCase("Under Review")) {
                    reqEntity.addPart("medical_registration_certificate",medical_registration_certificate);
                }

                if (!government_id_verified.equalsIgnoreCase("Verified")&&!government_id_verified.equalsIgnoreCase("Under Review")) {
                    reqEntity.addPart("government_id", government_id);
                }

                if (!degree_verified.equalsIgnoreCase("Verified")&&!degree_verified.equalsIgnoreCase("Under Review")) {
                    reqEntity.addPart("degree", degree);
                }

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

                    if(jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");

//                        String message = jsonObject.getString("message");
//                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            prgLoading.setVisibility(View.GONE);

                            Intent ii = new Intent(UploadDocActivity.this,UploadDocSucess.class);
                            startActivity(ii);
                            finish();

                        } else {

                            String message = jsonObject.getString("message");
                            Toast.makeText(UploadDocActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(UploadDocActivity.this, "Server Down Object null", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(UploadDocActivity.this, "Server Down", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(UploadDocActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            prgLoading.setVisibility(View.GONE);
            processing = false;
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
            Comman.setPreferences(UploadDocActivity.this, "picturePath", totalPathq);

        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
