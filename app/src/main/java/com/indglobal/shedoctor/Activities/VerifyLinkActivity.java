package com.indglobal.shedoctor.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.indglobal.shedoctor.R;

/**
 * Created by Android on 9/22/16.
 */
public class VerifyLinkActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.register_verify);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}