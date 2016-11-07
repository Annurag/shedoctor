package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.shedoctor.Beans.EditLanguageItem;
import com.indglobal.shedoctor.Beans.SpecialityItem;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/8/16.
 */
public class SpinLanguageAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    ArrayList<EditLanguageItem> edtArrayListLang;

    public SpinLanguageAdapter(Activity context,ArrayList<EditLanguageItem> edtArrayListLang) {
        this.activity = context;
        this.edtArrayListLang = edtArrayListLang;

    }

    @Override
    public int getCount() {
        return edtArrayListLang.size();
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

        String name = edtArrayListLang.get(position).getLanguage();

        holder.name.setText("- "+name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
