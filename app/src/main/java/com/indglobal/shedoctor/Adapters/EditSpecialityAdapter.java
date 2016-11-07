package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.shedoctor.Beans.SpecialityItem;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 9/26/16.
 */
public class EditSpecialityAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<SpecialityItem> specialityArrayList;

    public EditSpecialityAdapter(Activity context,ArrayList<SpecialityItem> specialityArrayList) {
        this.activity = context;
        this.specialityArrayList = specialityArrayList;

    }

    @Override
    public int getCount() {
        return specialityArrayList.size();
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
            v = inflater.inflate(R.layout.spinner_item, parent, false);
            holder = new ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        String name = specialityArrayList.get(position).getSpecialty();

        holder.name.setText("- "+name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
