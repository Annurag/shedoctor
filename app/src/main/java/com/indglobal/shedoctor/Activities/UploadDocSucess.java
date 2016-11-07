package com.indglobal.shedoctor.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

/**
 * Created by Android on 9/23/16.
 */
public class UploadDocSucess extends AppCompatActivity {

    RippleView tvUploadScsComeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.upld_doc_sucs);

        tvUploadScsComeBack = (RippleView)findViewById(R.id.tvUploadScsComeBack);

        tvUploadScsComeBack.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

}