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
public class WriteEducationAdapter extends RecyclerView.Adapter<WriteEducationAdapter.MyViewHolder> {

    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RippleView rplEduRemove;
        Spinner spinEduyear;
        EditText eteducatnName,eteducatnDegree;

        public MyViewHolder(View view) {
            super(view);

            spinEduyear = (Spinner)view.findViewById(R.id.spinEduyear);
            rplEduRemove = (RippleView)view.findViewById(R.id.rplEduRemove);
            eteducatnName = (EditText)view.findViewById(R.id.eteducatnName);
            eteducatnDegree = (EditText)view.findViewById(R.id.eteducatnDegree);

        }
    }


    public WriteEducationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.educationwrite_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position == 0) {
            holder.rplEduRemove.setVisibility(View.GONE);
        }else {
            holder.rplEduRemove.setVisibility(View.VISIBLE);
        }

        holder.eteducatnName.setText(MyProfileActivity.addEducationItemArrayList.get(position).getCollgename());
        holder.eteducatnDegree.setText(MyProfileActivity.addEducationItemArrayList.get(position).getCollegedegree());

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(context, R.array.years_array, R.layout.spinner_bigitem);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_new);
        holder.spinEduyear.setAdapter(arrayAdapter);

        int yr = MyProfileActivity.addEducationItemArrayList.get(position).getYear();
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


        holder.spinEduyear.setSelection(ii);

        setScaleAnimation(holder.itemView);

        holder.rplEduRemove.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (MyProfileActivity.addEducationItemArrayList.size() >= 2) {
                    MyProfileActivity.addEducationItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, MyProfileActivity.addEducationItemArrayList.size());
                }
            }
        });


        final int i  = position;

        holder.spinEduyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = parent.getItemAtPosition(position).toString();
                MyProfileActivity.addEducationItemArrayList.get(i).setYear(Integer.parseInt(year));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return MyProfileActivity.addEducationItemArrayList == null ? 0 : MyProfileActivity.addEducationItemArrayList.size();
    }


    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }
}