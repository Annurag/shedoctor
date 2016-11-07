package com.indglobal.shedoctor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Beans.HealthTipsItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

public class HealthDetailActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener{

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    TextView tvHealthDtlTolbrTitle,tvHealthDtlTitle,tvHealthDtlPost,tvHealthDtlContent;
    ImageView ivHealthDtlImg;
    RippleView rplHealthDtlEdit;

    HealthTipsItem healthTipsItem;
    String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.health_details);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvHealthDtlTolbrTitle = (TextView)findViewById(R.id.tvHealthDtlTolbrTitle);
        tvHealthDtlTitle = (TextView)findViewById(R.id.tvHealthDtlTitle);
        tvHealthDtlPost = (TextView)findViewById(R.id.tvHealthDtlPost);
        tvHealthDtlContent = (TextView)findViewById(R.id.tvHealthDtlContent);
        ivHealthDtlImg = (ImageView)findViewById(R.id.ivHealthDtlImg);
        rplHealthDtlEdit = (RippleView)findViewById(R.id.rplHealthDtlEdit);

        Intent ii = getIntent();
        post = ii.getStringExtra("post");
        healthTipsItem = HealthtipsActivity.healthTipsItemArrayList.get(Integer.parseInt(post));

        tvHealthDtlTolbrTitle.setText(healthTipsItem.getTitle());
        tvHealthDtlTitle.setText(healthTipsItem.getTitle());
        tvHealthDtlPost.setText(healthTipsItem.getPosted_at());
        tvHealthDtlContent.setText(healthTipsItem.getContent());

        Picasso.with(HealthDetailActivity.this).load(healthTipsItem.getImage()).error(R.drawable.health_tips)
                .placeholder(R.drawable.health_tips).into(ivHealthDtlImg);

        rplHealthDtlEdit.setOnRippleCompleteListener(this);

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

            case R.id.rplHealthDtlEdit:
                Intent ii = new Intent(HealthDetailActivity.this,AddHealthTipsActivity.class);
                ii.putExtra("updated","1");
                ii.putExtra("post",post);
                startActivity(ii);

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

