package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Spinner;
import android.widget.TextView;

import com.indglobal.shedoctor.Activities.UpcomingApointDetail;
import com.indglobal.shedoctor.Beans.AddMedicineItem;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.Past_Write_Three;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/7/16.
 */
public class WriteMedicineAdapter extends RecyclerView.Adapter<WriteMedicineAdapter.MyViewHolder> {

    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RippleView rplRemove;
        Spinner spinMedicineType,spinMorning,spinAftrnon,spinEvng,spinNght;
        EditText etAddNote,etmedicineName;

        public MyViewHolder(View view) {
            super(view);

            spinMorning = (Spinner)view.findViewById(R.id.spinMorning);
            spinAftrnon = (Spinner)view.findViewById(R.id.spinAftrnon);
            spinEvng = (Spinner)view.findViewById(R.id.spinEvng);
            spinNght = (Spinner)view.findViewById(R.id.spinNght);
            spinMedicineType = (Spinner)view.findViewById(R.id.spinMedicineType);

            rplRemove = (RippleView)view.findViewById(R.id.rplRemove);

            etAddNote = (EditText)view.findViewById(R.id.etAddNote);
            etmedicineName = (EditText)view.findViewById(R.id.etmedicineName);

        }
    }


    public WriteMedicineAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicinewrite_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position == 0) {
            holder.rplRemove.setVisibility(View.GONE);
        }else {
            holder.rplRemove.setVisibility(View.VISIBLE);
        }

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(context,R.array.medicin_type,R.layout.spinner_bigitem);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinMedicineType.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter1 = ArrayAdapter.createFromResource(context,R.array.medicin_time,R.layout.spinner_bigitem);
        arrayAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinMorning.setAdapter(arrayAdapter1);

        ArrayAdapter arrayAdapter12 = ArrayAdapter.createFromResource(context,R.array.medicin_time,R.layout.spinner_bigitem);
        arrayAdapter12.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinAftrnon.setAdapter(arrayAdapter12);

        ArrayAdapter arrayAdapter13 = ArrayAdapter.createFromResource(context,R.array.medicin_time,R.layout.spinner_bigitem);
        arrayAdapter13.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinEvng.setAdapter(arrayAdapter13);

        ArrayAdapter arrayAdapter14 = ArrayAdapter.createFromResource(context,R.array.medicin_time,R.layout.spinner_bigitem);
        arrayAdapter14.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinNght.setAdapter(arrayAdapter14);


        setScaleAnimation(holder.itemView);

        holder.rplRemove.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (Past_Write_Three.addMedicineItemArrayList.size() >= 2) {
                    Past_Write_Three.addMedicineItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, Past_Write_Three.addMedicineItemArrayList.size());
                }
            }
        });


        final int i  = position;

        holder.spinMedicineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Past_Write_Three.addMedicineItemArrayList.get(i).setMedicinetype(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinMorning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Past_Write_Three.addMedicineItemArrayList.get(i).setMorningtab(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinAftrnon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Past_Write_Three.addMedicineItemArrayList.get(i).setAfternoontab(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinEvng.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Past_Write_Three.addMedicineItemArrayList.get(i).setEveningtab(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinNght.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Past_Write_Three.addMedicineItemArrayList.get(i).setNighttab(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    @Override
    public int getItemCount() {
        return Past_Write_Three.addMedicineItemArrayList == null ? 0 : Past_Write_Three.addMedicineItemArrayList.size();
    }


    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }
}