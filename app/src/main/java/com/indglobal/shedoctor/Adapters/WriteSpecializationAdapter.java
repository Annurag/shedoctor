package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.Fragments.Past_Write_Three;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/11/16.
 */
public class WriteSpecializationAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<String> specializationArrayList;

    public WriteSpecializationAdapter(Activity context,ArrayList<String> specializationArrayList) {
        this.activity = context;
        this.specializationArrayList = specializationArrayList;

    }

    @Override
    public int getCount() {
        return specializationArrayList.size();
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

        String name = specializationArrayList.get(position);

        holder.tvSpecGrdItmName.setText(name);

        holder.ivSpecGrdItmCros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyProfileActivity.specializationArrayList.size() >= 1) {
                    MyProfileActivity.specializationArrayList.remove(position);
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
