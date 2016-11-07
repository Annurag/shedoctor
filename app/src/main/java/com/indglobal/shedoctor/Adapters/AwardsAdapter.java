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
 * Created by Android on 9/24/16.
 */
public class AwardsAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<String> awrdArrayList;

    public AwardsAdapter(Activity context,ArrayList<String> awrdArrayList) {
        this.activity = context;
        this.awrdArrayList = awrdArrayList;

    }

    @Override
    public int getCount() {
        return awrdArrayList.size();
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
            // LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.education_list_item, parent, false);
            holder = new ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.tvEduListItmText);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        String name = awrdArrayList.get(position);

        holder.name.setText("- "+name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
