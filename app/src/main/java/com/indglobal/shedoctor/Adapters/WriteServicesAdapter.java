package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/11/16.
 */
public class WriteServicesAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<String> servicesArrayList;

    public WriteServicesAdapter(Activity context,ArrayList<String> servicesArrayList) {
        this.activity = context;
        this.servicesArrayList = servicesArrayList;

    }

    @Override
    public int getCount() {
        return servicesArrayList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.specialization_grid_item, parent, false);
            holder = new ViewHolder();

            holder.tvSpecGrdItmName = (TextView)v.findViewById(R.id.tvSpecGrdItmName);
            holder.ivSpecGrdItmCros = (ImageView)v.findViewById(R.id.ivSpecGrdItmCros);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        String name = servicesArrayList.get(position);

        holder.tvSpecGrdItmName.setText(name);

        holder.ivSpecGrdItmCros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyProfileActivity.servicesArrayList.size() >= 1) {
                    MyProfileActivity.servicesArrayList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        return v;
    }

    static class ViewHolder {
        TextView tvSpecGrdItmName;
        ImageView ivSpecGrdItmCros;
    }

}
