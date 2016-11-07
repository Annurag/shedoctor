package com.indglobal.shedoctor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.indglobal.shedoctor.Adapters.ReportAdapter;
import com.indglobal.shedoctor.Beans.PaymentItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

/**
 * Created by SONY on 30/09/2016.
 */
public class PaymentDetails  extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;
    String post;
    PaymentItem paymentItem;

    TextView tvPaymentDetailptnId,tvPaymentDetailptnDate,tvPaymentDetailtransactionId,tvPaymentDetailRemarks,tvPaymentDetailAmount;
    ImageView ivPaymentDetailreceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.payment_details);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvPaymentDetailptnId = (TextView)findViewById(R.id.tvPaymentDetailptnId);
        tvPaymentDetailptnDate = (TextView)findViewById(R.id.tvPaymentDetailptnDate);
        tvPaymentDetailtransactionId = (TextView)findViewById(R.id.tvPaymentDetailtransactionId);
        tvPaymentDetailRemarks = (TextView)findViewById(R.id.tvPaymentDetailRemarks);
        tvPaymentDetailAmount = (TextView)findViewById(R.id.tvPaymentDetailAmount);

        ivPaymentDetailreceived = (ImageView)findViewById(R.id.ivPaymentDetailreceived);




        Intent ii = getIntent();
        post = ii.getStringExtra("post");

        paymentItem = PaymentActivity.paymentItemArrayList.get(Integer.parseInt(post));

        tvPaymentDetailtransactionId.setText("Transaction ID:\n"+paymentItem.getTransaction_id());
        tvPaymentDetailptnDate.setText(paymentItem.getDate());
        tvPaymentDetailRemarks.setText(paymentItem.getRemarks());
        tvPaymentDetailptnId.setText(paymentItem.getTransaction_id());
        tvPaymentDetailAmount.setText(paymentItem.getAmount());


        if ((paymentItem.getStatus()).equalsIgnoreCase("Done")) {
            ivPaymentDetailreceived.setImageDrawable(getResources().getDrawable(R.drawable.received));
        } else if((paymentItem.getStatus()).equalsIgnoreCase("Pending")){
            ivPaymentDetailreceived.setImageDrawable(getResources().getDrawable(R.drawable.pending));
        }else{
            ivPaymentDetailreceived.setImageDrawable(getResources().getDrawable(R.drawable.faild));
        }



//        String message = paymentItem.getOther_details();
//        message = message.replace("<br />", System.getProperty("line.separator"));
//        tvLedgerDetailothers.setText(message);
//
//        if ((paymentItem.getTransaction_id()).equalsIgnoreCase("null")){
//            tvLedgerDetailtrnstnId.setText("Not Available");
//        }else {
//            tvLedgerDetailtrnstnId.setText(paymentItem.getTransaction_id());
//        }
//
//
//        if ((paymentItem.getConsultation_type()).equalsIgnoreCase("voice")) {
//
//            ivLedgerDetailvideo.setImageDrawable(getResources().getDrawable(R.drawable.call));
//        } else {
//            ivLedgerDetailvideo.setImageDrawable(getResources().getDrawable(R.drawable.video_call));
//        }
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

