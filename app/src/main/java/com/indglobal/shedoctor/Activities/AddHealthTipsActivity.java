package com.indglobal.shedoctor.Activities;

import android.content.Context;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Beans.HealthTipsItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class AddHealthTipsActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener {

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    TextView tvAddHealthTitle,tvAddHealthPublish;
    EditText etAddHealthTitle,etAddHealthContent;
    ImageView ivAddHealthImgPreview;
    RelativeLayout rlAddHealthImgUpload,rlSelectMain;
    RippleView rplAddHealthPublish,rplAddHealthCancel,llGallery,llCamera,ivUploadDocAnimCross;

    String updated,post;
    HealthTipsItem healthTipsItem;

    File file,fileCertf;
    FileBody ffImg;
    public static final int MEDIA_TYPE_IMAGE = 1;

    String title,contnt,img = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.add_health_tips);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvAddHealthTitle = (TextView)findViewById(R.id.tvAddHealthTitle);
        etAddHealthTitle = (EditText)findViewById(R.id.etAddHealthTitle);
        etAddHealthContent = (EditText)findViewById(R.id.etAddHealthContent);
        ivAddHealthImgPreview = (ImageView)findViewById(R.id.ivAddHealthImgPreview);
        ivUploadDocAnimCross = (RippleView)findViewById(R.id.ivUploadDocAnimCross);
        rplAddHealthPublish = (RippleView)findViewById(R.id.rplAddHealthPublish);
        rplAddHealthCancel = (RippleView)findViewById(R.id.rplAddHealthCancel);
        llGallery = (RippleView)findViewById(R.id.llGallery);
        llCamera = (RippleView)findViewById(R.id.llCamera);
        rlAddHealthImgUpload = (RelativeLayout)findViewById(R.id.rlAddHealthImgUpload);
        rlSelectMain = (RelativeLayout)findViewById(R.id.rlSelectMain);

        Intent ii = getIntent();
        updated = ii.getStringExtra("updated");
        post = ii.getStringExtra("post");

        if (updated.equalsIgnoreCase("1")){
            healthTipsItem = HealthtipsActivity.healthTipsItemArrayList.get(Integer.parseInt(post));

            tvAddHealthTitle.setText("Edit Health Tips");
            etAddHealthTitle.setText(healthTipsItem.getTitle());
            etAddHealthContent.setText(healthTipsItem.getContent());

            img = healthTipsItem.getImage();
            Picasso.with(AddHealthTipsActivity.this).load(healthTipsItem.getImage()).error(R.drawable.health_tips)
                    .placeholder(R.drawable.health_tips).into(ivAddHealthImgPreview);

        }else {
            ivAddHealthImgPreview.setVisibility(View.GONE);
        }


        rlAddHealthImgUpload.setOnClickListener(this);
        llCamera.setOnRippleCompleteListener(this);
        llGallery.setOnRippleCompleteListener(this);
        ivUploadDocAnimCross.setOnRippleCompleteListener(this);
        rplAddHealthPublish.setOnRippleCompleteListener(this);
        rplAddHealthCancel.setOnRippleCompleteListener(this);
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
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.rlAddHealthImgUpload:
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(etAddHealthTitle.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startAnim();
                break;

        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {

            case R.id.llGallery:
                selectImage();
                break;

            case R.id.llCamera:
                captureImage();
                break;

            case R.id.ivUploadDocAnimCross:
                clodeAnim();
                break;

            case R.id.rplAddHealthCancel:
                onBackPressed();
                break;

            case R.id.rplAddHealthPublish:

                title = etAddHealthTitle.getText().toString();
                contnt = etAddHealthContent.getText().toString();

                if(!Comman.isConnectionAvailable(AddHealthTipsActivity.this)){
                    Toast.makeText(AddHealthTipsActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();

                }else if(title.equalsIgnoreCase("")){
                    Toast.makeText(AddHealthTipsActivity.this,"Please provide a title!",Toast.LENGTH_SHORT).show();

                }else if (contnt.equalsIgnoreCase("")){
                    Toast.makeText(AddHealthTipsActivity.this,"Please provide details!",Toast.LENGTH_SHORT).show();

                }else if(img.equalsIgnoreCase("")&&img.equalsIgnoreCase("null")){
                    Toast.makeText(AddHealthTipsActivity.this,"Please provide a image!",Toast.LENGTH_SHORT).show();

                }else {
                    prgLoading.setVisibility(View.VISIBLE);

                    if (updated.equalsIgnoreCase("1")){
                        new updateHealthTipTask().execute();
                    }else {
                        new addHealthTipTask().execute();
                    }
                }

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

            Toast.makeText(AddHealthTipsActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }

    }

    public void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        try {

            startActivityForResult(intent, 1);

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(AddHealthTipsActivity.this, "No Camrera found in this mobile .", Toast.LENGTH_SHORT).show();
        }

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

                    img = picturePath;
                    Bitmap myBitmap = decodeFile(fileCertf);
                    ivAddHealthImgPreview.setImageBitmap(myBitmap);
                    ivAddHealthImgPreview.setVisibility(View.VISIBLE);
                    clodeAnim();

                }else {
                    Toast.makeText(AddHealthTipsActivity.this,"Please attached a image file type !",Toast.LENGTH_SHORT).show();
                }

            }else if (requestCode == 1) {

                try {
                    String comPath = Comman.getPreferences(AddHealthTipsActivity.this, "picturePath");
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }

                finally {

                    String comPath = Comman.getPreferences(AddHealthTipsActivity.this, "picturePath");
                    fileCertf = new File(comPath);
                    fileCertf = saveBitmapToFile(fileCertf);
                    String Certf = fileCertf.getName().toString();

                    img = comPath;
                    Bitmap myBitmap = decodeFile(fileCertf);
                    ivAddHealthImgPreview.setImageBitmap(myBitmap);
                    ivAddHealthImgPreview.setVisibility(View.VISIBLE);

                    clodeAnim();
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
            Comman.setPreferences(AddHealthTipsActivity.this, "picturePath", totalPathq);

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

    private Bitmap decodeFile(File f){
        Bitmap b = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        try {
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            final int IMAGE_MAX_SIZE=170;
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return b;
    }

    private class updateHealthTipTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.updthealthtipsUrl);
                String id = healthTipsItem.getId();
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(AddHealthTipsActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl+"/"+Integer.parseInt(id)+ "?token=" + token);

                try {

                    ffImg = new FileBody(fileCertf);

                }catch (Exception e){
                    e.printStackTrace();
                }


                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("title",new StringBody(title));
                reqEntity.addPart("content",new StringBody(contnt));
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
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");
                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddHealthTipsActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent ii = new Intent(AddHealthTipsActivity.this,HealthtipsActivity.class);
                            startActivity(ii);
                            finish();

                        } else {
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddHealthTipsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(AddHealthTipsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class addHealthTipTask extends AsyncTask<String, Void, String> {

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

                String productListUrl = getResources().getString(R.string.healthtipsUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(AddHealthTipsActivity.this, "TOKEN");
                HttpPost httppost = new HttpPost(productListUrl + "?token=" + token);

                try {

                    ffImg = new FileBody(fileCertf);

                }catch (Exception e){
                    e.printStackTrace();
                }


                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("title",new StringBody(title));
                reqEntity.addPart("content",new StringBody(contnt));
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
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            String message = jsonObject.getString("response");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddHealthTipsActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                            Comman.setPreferences(AddHealthTipsActivity.this,"updtd","1");
                            onBackPressed();
                        } else {
                            String message = jsonObject.getString("message");
                            message = message.replace("<br />", System.getProperty("line.separator"));
                            prgLoading.setVisibility(View.GONE);
                            Toast.makeText(AddHealthTipsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                prgLoading.setVisibility(View.GONE);
                Toast.makeText(AddHealthTipsActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

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