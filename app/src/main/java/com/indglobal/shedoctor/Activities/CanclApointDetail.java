package com.indglobal.shedoctor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.ReportAdapter;
import com.indglobal.shedoctor.Beans.CancledApointItem;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.CalendarApointFragment;
import com.indglobal.shedoctor.Fragments.PastApointFragment;
import com.indglobal.shedoctor.R;

/**
 * Created by Android on 10/13/16.
 */
public class CanclApointDetail extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;
    String post;

    LayoutInflater inflater;
    RippleView tvPastDtlWritePriscptn;
    TextView tvPastDtlTitle,tvPastApntDtlsuccessful;
    int count;
    ImageView ivPastApntDtl,ivPastApntDtlPCallIcon;

    TextView tvPastApntDtlPName,tvPastApntDtlPId,tvPastApntDtlPCallType,tvPastApntDtlPTime,tvPastApntDtlPDate,tvPastApntDtlPHeight,tvPastApntDtlPWeight,tvPastApntDtlPBlood,tvPastApntDtlPrescriptionCount
            ,tvPastApntDtlPrescrptn,tvPastApntDtlPLabCount,tvPastApntDtlPAllergy,tvPastApntDtlLabNo;
    ExpandableHeightGridView grdPastApntDtlLabReports;

    CancledApointItem cancledApointItem;
    ReportAdapter reportAdapter;

    RelativeLayout rlWritePrscptn;
    RippleView rplPastApntDtlPrescrptn;
    public static String appointment_id,date;

    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.past_apoint_detail);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        activity = CanclApointDetail.this;

        tvPastDtlWritePriscptn = (RippleView)findViewById(R.id.tvPastDtlWritePriscptn);
        tvPastDtlTitle = (TextView)findViewById(R.id.tvPastDtlTitle);
        tvPastApntDtlsuccessful = (TextView)findViewById(R.id.tvPastApntDtlsuccessful);
        ivPastApntDtl =(ImageView)findViewById(R.id.ivPastApntDtl);

        rlWritePrscptn = (RelativeLayout)findViewById(R.id.rlWritePrscptn);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvPastApntDtlPName = (TextView)findViewById(R.id.tvPastApntDtlPName);
        tvPastApntDtlPId = (TextView)findViewById(R.id.tvPastApntDtlPId);
        tvPastApntDtlPCallType = (TextView)findViewById(R.id.tvPastApntDtlPCallType);
        tvPastApntDtlPTime = (TextView)findViewById(R.id.tvPastApntDtlPTime);
        tvPastApntDtlPDate = (TextView)findViewById(R.id.tvPastApntDtlPDate);
        tvPastApntDtlPHeight = (TextView)findViewById(R.id.tvPastApntDtlPHeight);
        tvPastApntDtlPWeight = (TextView)findViewById(R.id.tvPastApntDtlPWeight);
        tvPastApntDtlPBlood = (TextView)findViewById(R.id.tvPastApntDtlPBlood);
        tvPastApntDtlPrescriptionCount = (TextView)findViewById(R.id.tvPastApntDtlPrescriptionCount);
        tvPastApntDtlPrescrptn = (TextView)findViewById(R.id.tvPastApntDtlPrescrptn);
        tvPastApntDtlPLabCount = (TextView)findViewById(R.id.tvPastApntDtlPLabCount);
        tvPastApntDtlPAllergy = (TextView)findViewById(R.id.tvPastApntDtlPAllergy);
        tvPastApntDtlLabNo = (TextView)findViewById(R.id.tvPastApntDtlLabNo);
        grdPastApntDtlLabReports = (ExpandableHeightGridView)findViewById(R.id.grdPastApntDtlLabReports);
        rplPastApntDtlPrescrptn = (RippleView)findViewById(R.id.rplPastApntDtlPrescrptn);

        ivPastApntDtlPCallIcon = (ImageView)findViewById(R.id.ivPastApntDtlPCallIcon);

        tvPastDtlWritePriscptn.setOnRippleCompleteListener(this);

        Intent ii = getIntent();
        post = ii.getStringExtra("post");

        cancledApointItem = CalendarApointFragment.cancledApointItemArrayList.get(Integer.parseInt(post));

        tvPastApntDtlPName.setText(cancledApointItem.getPname());
        tvPastApntDtlPId.setText("Patient ID : "+cancledApointItem.getPid());
        tvPastApntDtlPTime.setText(cancledApointItem.getTime());
        date = cancledApointItem.getDate();
        tvPastApntDtlPDate.setText(cancledApointItem.getDate());
        tvPastApntDtlPHeight.setText(cancledApointItem.getHeight());
        tvPastApntDtlPWeight.setText(cancledApointItem.getWeight());
        tvPastApntDtlPBlood.setText(cancledApointItem.getBloodgroup());
        appointment_id = cancledApointItem.getId();

        if((cancledApointItem.getAllergies()).equalsIgnoreCase("")){
            tvPastApntDtlPAllergy.setText("Not Available");
            tvPastApntDtlPAllergy.setTextColor(getResources().getColor(R.color.fullGray));

        }else {
            tvPastApntDtlPAllergy.setText(cancledApointItem.getAllergies());
        }



        if ((cancledApointItem.getPrescription()).equalsIgnoreCase("false")){
            tvPastApntDtlPrescriptionCount.setText("0");
            tvPastApntDtlPrescrptn.setText("No Prescription Available");
        }else {
            tvPastApntDtlPrescrptn.setText(cancledApointItem.getMedications());
            tvPastApntDtlPrescrptn.setText("View Prescription");
            tvPastApntDtlPrescrptn.setTextColor(getResources().getColor(R.color.lightblue));
            tvPastApntDtlPrescriptionCount.setText("1");
        }

        if ((cancledApointItem.getLstReport()).equals("0")){
            tvPastApntDtlPLabCount.setText("0");
            tvPastApntDtlLabNo.setVisibility(View.VISIBLE);
        }else {

            int lnth = cancledApointItem.getLstReport().size();
            tvPastApntDtlPLabCount.setText(lnth+"");
            tvPastApntDtlLabNo.setVisibility(View.GONE);
            reportAdapter = new ReportAdapter(CanclApointDetail.this,cancledApointItem.getLstReport());
            grdPastApntDtlLabReports.setAdapter(reportAdapter);
            grdPastApntDtlLabReports.setExpanded(true);
        }

        if ((cancledApointItem.getType()).equalsIgnoreCase("voice")){
            ivPastApntDtlPCallIcon .setImageDrawable(getResources().getDrawable(R.drawable.call));
            tvPastApntDtlPCallType.setText("Voice Call");

        }else {
            ivPastApntDtlPCallIcon.setImageDrawable(getResources().getDrawable(R.drawable.video_call));
            tvPastApntDtlPCallType.setText("Video Call");
        }

        if((cancledApointItem.getStatus()).equalsIgnoreCase("Cancelled")){
            tvPastApntDtlsuccessful.setText(cancledApointItem.getReason());
            tvPastApntDtlsuccessful.setTextColor(getResources().getColor(R.color.red));
            ivPastApntDtl.setImageDrawable(getResources().getDrawable(R.drawable.cncl));
            rlWritePrscptn.setVisibility(View.GONE);
        }else if((cancledApointItem.getStatus()).equalsIgnoreCase("Failed")){
            tvPastApntDtlsuccessful.setText(cancledApointItem.getReason());
            tvPastApntDtlsuccessful.setTextColor(getResources().getColor(R.color.red));
            ivPastApntDtl.setImageDrawable(getResources().getDrawable(R.drawable.cncl));
            rlWritePrscptn.setVisibility(View.GONE);
        }else if ((cancledApointItem.getStatus()).equalsIgnoreCase("N/A")){
            tvPastApntDtlsuccessful.setText("Pending");
            tvPastApntDtlsuccessful.setTextColor(getResources().getColor(R.color.fullGray));
            ivPastApntDtl.setImageDrawable(getResources().getDrawable(R.drawable.pendng));
            rlWritePrscptn.setVisibility(View.VISIBLE);
        }else {
            tvPastApntDtlsuccessful.setTextColor(getResources().getColor(R.color.green));
            ivPastApntDtl.setImageDrawable(getResources().getDrawable(R.drawable.complete));
            tvPastApntDtlsuccessful.setText("Success");
            rlWritePrscptn.setVisibility(View.GONE);
        }



        grdPastApntDtlLabReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String path = cancledApointItem.getLstReport().get(position);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path)));
                } catch (Exception e) {
                    Toast.makeText(CanclApointDetail.this, "File is currupted!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        rplPastApntDtlPrescrptn.setOnRippleCompleteListener(this);

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

            case R.id.tvPastDtlWritePriscptn:
                Intent ii = new Intent(CanclApointDetail.this, PendingStatusActivity.class);
                ii.putExtra("appointment_id",appointment_id);
                startActivity(ii);
                break;

            case R.id.rplPastApntDtlPrescrptn:

                if ((cancledApointItem.getPrescription()).equalsIgnoreCase("true")){
                    String id1 = cancledApointItem.getId();
                    Intent iii = new Intent(CanclApointDetail.this, PrescriptionViewActivity.class);
                    iii.putExtra("apointId", id1);
                    startActivity(iii);
                }else {
                    Toast.makeText(CanclApointDetail.this,"Prescription not available!",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}

