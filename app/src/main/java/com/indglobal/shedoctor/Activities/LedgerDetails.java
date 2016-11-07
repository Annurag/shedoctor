package com.indglobal.shedoctor.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.ReportAdapter;
import com.indglobal.shedoctor.Beans.LedgerItem;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.UpcomingApointFragment;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SONY on 30/09/2016.
 */
public class LedgerDetails extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;
    String post;
    LedgerItem ledgerItem;

    TextView tvLedgerDetailtrnstnId,tvLedgerDetailDate,tvLedgerDetailpatntId,tvLedgerDetailAmmount,tvLedgerDetailCharges,tvLedgerDetaildctrfee,tvLedgerDetailfromPatient,tvLedgerDetailothers;
    ImageView ivLedgerDetailvideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.ledger_details);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvLedgerDetailtrnstnId = (TextView)findViewById(R.id.tvLedgerDetailtrnstnId);
        tvLedgerDetailDate = (TextView)findViewById(R.id.tvLedgerDetailDate);
        tvLedgerDetailpatntId = (TextView)findViewById(R.id.tvLedgerDetailpatntId);
        tvLedgerDetailAmmount = (TextView)findViewById(R.id.tvLedgerDetailAmmount);
        tvLedgerDetailCharges = (TextView)findViewById(R.id.tvLedgerDetailCharges);
        tvLedgerDetaildctrfee = (TextView)findViewById(R.id.tvLedgerDetaildctrfee);
        tvLedgerDetailfromPatient = (TextView)findViewById(R.id.tvLedgerDetailfromPatient);
        tvLedgerDetailothers = (TextView)findViewById(R.id.tvLedgerDetailothers);

        ivLedgerDetailvideo = (ImageView)findViewById(R.id.ivLedgerDetailvideo);


        Intent ii = getIntent();
        post = ii.getStringExtra("post");

        ledgerItem = LedgerActivity.ledgerItemArrayList.get(Integer.parseInt(post));

        tvLedgerDetailtrnstnId.setText(ledgerItem.getTransaction_id());
        tvLedgerDetailpatntId.setText(ledgerItem.getPatient_id());
        tvLedgerDetailAmmount.setText(ledgerItem.getPatient_rs());
        tvLedgerDetailCharges.setText(ledgerItem.getShedoctr_rs());
        tvLedgerDetailDate.setText(ledgerItem.getDate());
        tvLedgerDetaildctrfee.setText(ledgerItem.getDoctor_rs());
        tvLedgerDetailfromPatient.setText(ledgerItem.getPatient_rs());


        String message = ledgerItem.getOther_details();
        message = message.replace("<br />", System.getProperty("line.separator"));
        tvLedgerDetailothers.setText(message);

        if ((ledgerItem.getTransaction_id()).equalsIgnoreCase("null")){
            tvLedgerDetailtrnstnId.setText("Not Available");
        }else {
            tvLedgerDetailtrnstnId.setText(ledgerItem.getTransaction_id());
        }


        if ((ledgerItem.getConsultation_type()).equalsIgnoreCase("voice")) {

            ivLedgerDetailvideo.setImageDrawable(getResources().getDrawable(R.drawable.call));
        } else {
            ivLedgerDetailvideo.setImageDrawable(getResources().getDrawable(R.drawable.video_call));
        }
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

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}

