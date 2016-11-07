package com.indglobal.shedoctor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

/**
 * Created by Android on 10/11/16.
 */
public class WriteAwardAdapter extends RecyclerView.Adapter<WriteAwardAdapter.MyViewHolder> {

    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RippleView rplAwrdRemove;
        Spinner spinAwrdyear;
        EditText etawardName,etawardDtl;

        public MyViewHolder(View view) {
            super(view);

            spinAwrdyear = (Spinner)view.findViewById(R.id.spinAwrdyear);
            rplAwrdRemove = (RippleView)view.findViewById(R.id.rplAwrdRemove);
            etawardName = (EditText)view.findViewById(R.id.etawardName);
            etawardDtl = (EditText)view.findViewById(R.id.etawardDtl);

        }
    }


    public WriteAwardAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_writelist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position == 0) {
            holder.rplAwrdRemove.setVisibility(View.GONE);
        }else {
            holder.rplAwrdRemove.setVisibility(View.VISIBLE);
        }

        holder.etawardName.setText(MyProfileActivity.addAwardItemArrayList.get(position).getAwardname());
        holder.etawardDtl.setText(MyProfileActivity.addAwardItemArrayList.get(position).getAwarddtl());

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(context, R.array.years_array, R.layout.spinner_bigitem);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinAwrdyear.setAdapter(arrayAdapter);

        int yr = MyProfileActivity.addAwardItemArrayList.get(position).getYear();
        String yrs = yr+"";
        int ii = 0;
        String[] androidStrings = context.getResources().getStringArray(R.array.years_array);
        for (int i =0; i<androidStrings.length; i++){
            String newYrs = androidStrings[i];
            if (newYrs.equalsIgnoreCase(yrs)){
                ii = i;
                break;
            }
        }


        holder.spinAwrdyear.setSelection(ii);

        setScaleAnimation(holder.itemView);

        holder.rplAwrdRemove.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (MyProfileActivity.addAwardItemArrayList.size() >= 2) {
                    MyProfileActivity.addAwardItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, MyProfileActivity.addAwardItemArrayList.size());
                }
            }
        });


        final int i  = position;

        holder.spinAwrdyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = parent.getItemAtPosition(position).toString();
                MyProfileActivity.addAwardItemArrayList.get(i).setYear(Integer.parseInt(year));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return MyProfileActivity.addAwardItemArrayList == null ? 0 : MyProfileActivity.addAwardItemArrayList.size();
    }


    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }
}