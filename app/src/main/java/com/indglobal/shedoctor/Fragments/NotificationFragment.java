package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indglobal.shedoctor.R;

/**
 * Created by Android on 8/29/16.
 */
public class NotificationFragment extends Fragment {

    LayoutInflater inflater;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }
    public NotificationFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.notification_main, container, false );


        return rootView;
    }



}
