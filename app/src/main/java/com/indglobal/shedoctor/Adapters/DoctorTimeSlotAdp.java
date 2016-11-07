package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.indglobal.shedoctor.Beans.DoctorDoctorTimeSlots;
import com.indglobal.shedoctor.Fragments.ConsultTimeFragment;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag on 13-10-2016.
 */
public class DoctorTimeSlotAdp extends BaseAdapter {

    private Activity activity;
    private List<DoctorDoctorTimeSlots> doctorSlotTimeModelDoctorArrayList;
    private boolean checkedAll;
    private long timeStamp;

    public DoctorTimeSlotAdp(Activity act, List<DoctorDoctorTimeSlots> doctorSlotTimeModelDoctorArrayList, boolean checked, long timeStamp) {
        this.activity = act;
        this.doctorSlotTimeModelDoctorArrayList = doctorSlotTimeModelDoctorArrayList;
        this.checkedAll = checked;
        this.timeStamp = timeStamp;
    }

    public int getCount() {
        return doctorSlotTimeModelDoctorArrayList== null ? 0 : doctorSlotTimeModelDoctorArrayList.size();
    }

    public Object getItem(int position) {
        return doctorSlotTimeModelDoctorArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dr_time_adapter, null);

            DoctorDoctorTimeSlots doctorDoctorTimeSlots = new DoctorDoctorTimeSlots();
            holder = new ViewHolder();

            holder.discussion = (CheckBox) convertView.findViewById(R.id.tvLangtype);
            holder.tvBooked = (TextView) convertView.findViewById(R.id.tvBooked);

            holder.discussion.setOnCheckedChangeListener(myCheckedList);
            holder.discussion.setTag(position);
            holder.discussion.setChecked(doctorDoctorTimeSlots.brandSelected);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String time = doctorSlotTimeModelDoctorArrayList.get(position).getTime();

        if (timeStamp < doctorSlotTimeModelDoctorArrayList.get(position).getTimeSlots() ) {

            if (doctorSlotTimeModelDoctorArrayList.get(position).getStatus() == 1) {
                holder.discussion.setText(time);
                holder.discussion.setChecked(true);
            } else if (doctorSlotTimeModelDoctorArrayList.get(position).getStatus() == 0) {
                holder.discussion.setText(time);
                holder.discussion.setChecked(false);
            } else {
                holder.discussion.setVisibility(View.GONE);
                holder.tvBooked.setText(time);
                holder.tvBooked.setVisibility(View.VISIBLE);
            }
        } else {
            holder.discussion.setVisibility(View.GONE);
            holder.tvBooked.setText(time);
            holder.tvBooked.setVisibility(View.VISIBLE);
        }

        holder.discussion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    ConsultTimeFragment.doctordoctorfilterTimeList.get(position).setStatus(1);
                    long selectedTimeStamp = doctorSlotTimeModelDoctorArrayList.get(position).getTimeSlots();
                    for (int i=0; i< ConsultTimeFragment.doctorAppointmentTimeModelList.size(); i++){
                        if (ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getTimeStamp() == selectedTimeStamp ) {
                            ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).setSlotStatus(1);
                            break;
                        }
                    }
                } else {
                    ConsultTimeFragment.doctordoctorfilterTimeList.get(position).setStatus(0);
                    long unSelectedTimeStamp = doctorSlotTimeModelDoctorArrayList.get(position).getTimeSlots();
                    for (int i=0; i< ConsultTimeFragment.doctorAppointmentTimeModelList.size(); i++){
                        if (ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).getTimeStamp() == unSelectedTimeStamp ) {
                            ConsultTimeFragment.doctorAppointmentTimeModelList.get(i).setSlotStatus(0);
                            break;
                        }

                    }
                }

            }
        });
        if (checkedAll) {
            holder.discussion.setChecked(true);
        }

        holder.discussion.setSelected(true);
        return convertView;
    }

    private DoctorDoctorTimeSlots getItemDetail(int position){
        return  ((DoctorDoctorTimeSlots)getItem(position));
    }

    public ArrayList<DoctorDoctorTimeSlots> getBox(){

        ArrayList<DoctorDoctorTimeSlots> box = new ArrayList<>();
        for (DoctorDoctorTimeSlots p : doctorSlotTimeModelDoctorArrayList){
            if(p.brandSelected)
                box.add(p);
        }
        return  box;
    }

    CompoundButton.OnCheckedChangeListener myCheckedList = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItemDetail((Integer) buttonView.getTag()).brandSelected = isChecked;
        }
    };

    public void uncheckAllChildrenCascade(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v instanceof CheckBox) {
                ((CheckBox) v).setChecked(false);
            } else if (v instanceof ViewGroup) {
                uncheckAllChildrenCascade((ViewGroup) v);
            }
        }
    }

    class ViewHolder {
        private CheckBox discussion;
        private TextView tvBooked;
    }
}
