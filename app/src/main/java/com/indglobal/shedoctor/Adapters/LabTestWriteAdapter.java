package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.indglobal.shedoctor.Beans.LabTestItem;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/7/16.
 */
public class LabTestWriteAdapter  extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    public static ArrayList<LabTestItem> arrayListLabs;

    public LabTestWriteAdapter(Activity context) {
        this.activity = context;
    }

    public LabTestWriteAdapter(Activity context,ArrayList<LabTestItem> arrayListLabs) {
        this.activity = context;
        LabTestWriteAdapter.arrayListLabs = arrayListLabs;
    }

    @Override
    public int getCount() {
        return arrayListLabs.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListLabs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.lab_write_grid_item, parent, false);
            holder = new ViewHolder();

            LabTestItem labTestItem = new LabTestItem();

            holder.cbLabTestname = (CheckBox)v.findViewById(R.id.cbLabTestname);

            holder.cbLabTestname.setOnCheckedChangeListener(myCheckedList);
            holder.cbLabTestname.setTag(position);
            holder.cbLabTestname.setChecked(labTestItem.labSelected);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }


        holder.cbLabTestname.setText(arrayListLabs.get(position).getLabName());


        return v;
    }

    static class ViewHolder {
        CheckBox cbLabTestname;
    }

    public static ArrayList<LabTestItem> getBox(){

        ArrayList<LabTestItem> box = new ArrayList<>();
        for (LabTestItem p : arrayListLabs ){
            if(p.labSelected)
                box.add(p);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckedList = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItemDetail((Integer) buttonView.getTag()).labSelected = isChecked;
        }
    };

    private LabTestItem getItemDetail(int position){
        return ((LabTestItem)getItem(position));
    }


}
