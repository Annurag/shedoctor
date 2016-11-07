package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/1/16.
 */
public class LabGridAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<String> arrayListLabs;

    public LabGridAdapter(Activity context,ArrayList<String> arrayListLabs) {
        this.activity = context;
        this.arrayListLabs = arrayListLabs;

    }

    public LabGridAdapter(Activity context) {
        this.activity = context;

    }

    @Override
    public int getCount() {
        return arrayListLabs.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            v = inflater.inflate(R.layout.lab_test_view_grid_item, parent, false);
            holder = new ViewHolder();

            holder.tvLabItmName = (TextView)v.findViewById(R.id.tvLabItmName);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }



        holder.tvLabItmName.setText(arrayListLabs.get(position));


        return v;
    }

    static class ViewHolder {
        TextView tvLabItmName;
    }

}
