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
 * Created by SONY on 29/09/2016.
 */
public class BankAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<String> bankArrayList;

    public BankAdapter(Activity context,ArrayList<String> bankArrayList) {
        this.activity = context;
        this.bankArrayList = bankArrayList;

    }

    @Override
    public int getCount() {
        return bankArrayList.size();
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

        String name = bankArrayList.get(position);

        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
