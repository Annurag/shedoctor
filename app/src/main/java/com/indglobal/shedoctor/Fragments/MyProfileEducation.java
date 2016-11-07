package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.Adapters.WriteEducationAdapter;
import com.indglobal.shedoctor.Adapters.WriteMedicineAdapter;
import com.indglobal.shedoctor.Beans.AddEducationItem;
import com.indglobal.shedoctor.Beans.AddMedicineItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by SONY on 19/09/2016.
 */
public class MyProfileEducation extends Fragment {

    LayoutInflater inflater;

    public static RecyclerView rvAddEducation;
    RippleView rplAddMoreEducation;

    AddEducationItem addEducationItem;
    WriteEducationAdapter writeEducationAdapter;

    private int size;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile_education, container, false);

        rvAddEducation = (RecyclerView)view.findViewById(R.id.rvAddEducation);
        rplAddMoreEducation = (RippleView)view.findViewById(R.id.rplAddMoreEducation);

        writeEducationAdapter = new WriteEducationAdapter(getActivity());


        if (MyProfileActivity.addEducationItemArrayList.size()==0){
            size = MyProfileActivity.addEducationItemArrayList.size()+1;
            addEducationItem = new AddEducationItem(size,0,0,"","");
            MyProfileActivity.addEducationItemArrayList.add(addEducationItem);

        }else {
            size = MyProfileActivity.addEducationItemArrayList.size();
        }


        writeEducationAdapter = new WriteEducationAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAddEducation.setLayoutManager(mLayoutManager);
        rvAddEducation.setAdapter(writeEducationAdapter);


        rplAddMoreEducation.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                size++;
                addEducationItem = new AddEducationItem(size,0,0,"","");
                MyProfileActivity.addEducationItemArrayList.add(addEducationItem);
                writeEducationAdapter = new WriteEducationAdapter(getActivity());
                writeEducationAdapter.notifyItemInserted(size);
                rvAddEducation.scrollToPosition(size);
            }
        });

        return view;

    }

}