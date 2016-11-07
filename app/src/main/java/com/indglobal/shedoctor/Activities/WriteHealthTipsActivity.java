package com.indglobal.shedoctor.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.indglobal.shedoctor.R;

public class WriteHealthTipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.write_healthtips);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
