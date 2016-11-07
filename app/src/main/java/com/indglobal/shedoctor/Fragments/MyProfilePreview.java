package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indglobal.shedoctor.Activities.BaseActivity;
import com.indglobal.shedoctor.Activities.PastApointDetail;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

/**
 * Created by SONY on 20/09/2016.
 */
public class MyProfilePreview extends Fragment implements RippleView.OnRippleCompleteListener{

    LayoutInflater inflater;
    FragmentManager manager;
    RippleView rplcross,rplconfirm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile_preview, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rplcross = (RippleView)getActivity().findViewById(R.id.rplcross);
        rplconfirm = (RippleView)getActivity().findViewById(R.id.rplconfirm);

        rplcross.setOnRippleCompleteListener(this);
        rplconfirm.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id) {
            case R.id.rplcross:
                Intent ii = new Intent(getActivity(), BaseActivity.class);
                startActivity(ii);
                break;

            case R.id.rplconfirm:

                Intent i = new Intent(getActivity(), BaseActivity.class);
                startActivity(i);
                break;


        }
    }
}
