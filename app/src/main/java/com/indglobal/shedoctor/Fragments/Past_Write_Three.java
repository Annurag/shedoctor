package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indglobal.shedoctor.Adapters.UpcomingApointAdapter;
import com.indglobal.shedoctor.Adapters.WriteMedicineAdapter;
import com.indglobal.shedoctor.Beans.AddMedicineItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 9/12/16.
 */
public class Past_Write_Three extends Fragment {

    LayoutInflater inflater;

    public static RecyclerView rvAddMedicins;
    RippleView rplAddMoreMedicins;

    AddMedicineItem addMedicineItem;
    public static ArrayList<AddMedicineItem> addMedicineItemArrayList;
    WriteMedicineAdapter writeMedicineAdapter;

    private int size;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_write_three, container, false);

        rvAddMedicins = (RecyclerView)view.findViewById(R.id.rvAddMedicins);
        rplAddMoreMedicins = (RippleView)view.findViewById(R.id.rplAddMoreMedicins);

        addMedicineItemArrayList = new ArrayList<>();
        writeMedicineAdapter = new WriteMedicineAdapter(getActivity());

        size = 1;
        addMedicineItem = new AddMedicineItem(size,0,0,0,0,0,"","");
        addMedicineItemArrayList.add(addMedicineItem);

        writeMedicineAdapter = new WriteMedicineAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAddMedicins.setLayoutManager(mLayoutManager);
        rvAddMedicins.setAdapter(writeMedicineAdapter);


        rplAddMoreMedicins.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                size++;
                addMedicineItem = new AddMedicineItem(size,0,0,0,0,0,"","");
                addMedicineItemArrayList.add(addMedicineItem);
                writeMedicineAdapter = new WriteMedicineAdapter(getActivity());
                writeMedicineAdapter.notifyItemInserted(size);
                rvAddMedicins.scrollToPosition(size);
            }
        });

        return view;

    }

}