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
public class EducationAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<String> eduArrayList;

    public EducationAdapter(Activity context,ArrayList<String> eduArrayList) {
        this.activity = context;
        this.eduArrayList = eduArrayList;

    }

    @Override
    public int getCount() {
        return eduArrayList.size();
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

        String name = eduArrayList.get(position);

        holder.name.setText("- "+name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
