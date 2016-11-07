package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.shedoctor.Beans.MedicinTypeItem;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 9/26/16.
 */
public class MedicinTypeAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<MedicinTypeItem> medicinTypeItemArrayList;

    public MedicinTypeAdapter(Activity context,ArrayList<MedicinTypeItem> medicinTypeItemArrayList) {
        this.activity = context;
        this.medicinTypeItemArrayList = medicinTypeItemArrayList;

    }

    @Override
    public int getCount() {
        return medicinTypeItemArrayList.size();
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

        String name = medicinTypeItemArrayList.get(position).getMedicin();

        holder.name.setText("- "+name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
