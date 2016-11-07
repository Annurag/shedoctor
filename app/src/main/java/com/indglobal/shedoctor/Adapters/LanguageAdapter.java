package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.Beans.EditLanguageItem;
import com.indglobal.shedoctor.Beans.EditLanguageItem;
import com.indglobal.shedoctor.Beans.LabTestItem;
import com.indglobal.shedoctor.Beans.LanguageItem;
import com.indglobal.shedoctor.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Android on 9/24/16.
 */
public class LanguageAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    public static ArrayList<EditLanguageItem> edtArrayListLang;
    String ids;

    public LanguageAdapter(Activity context,ArrayList<EditLanguageItem> edtArrayListLang) {
        this.activity = context;
        LanguageAdapter.edtArrayListLang = edtArrayListLang;

    }

    @Override
    public int getCount() {
        return edtArrayListLang.size();
    }

    @Override
    public Object getItem(int position) {
        return edtArrayListLang.get(position);
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
            v = inflater.inflate(R.layout.lang_grid_item, parent, false);
            holder = new ViewHolder();

            EditLanguageItem editLanguageItem = new EditLanguageItem();

            holder.cbLang = (CheckBox)v.findViewById(R.id.cbGridItemLang);

            holder.cbLang.setOnCheckedChangeListener(myCheckedList);
            holder.cbLang.setTag(position);
            holder.cbLang.setChecked(editLanguageItem.langSelected);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }


        String selctd = edtArrayListLang.get(position).getSelected();

        if (selctd.equalsIgnoreCase("1")){
            holder.cbLang.setChecked(true);
        }else {
            holder.cbLang.setChecked(false);
        }

        holder.cbLang.setText(edtArrayListLang.get(position).getLanguage());


        return v;
    }

    static class ViewHolder {
        CheckBox cbLang;
    }

    public static ArrayList<EditLanguageItem> getBox(){

        ArrayList<EditLanguageItem> box = new ArrayList<>();
        for (EditLanguageItem p : edtArrayListLang ){
            if(p.langSelected)
                box.add(p);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckedList = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItemDetail((Integer) buttonView.getTag()).langSelected = isChecked;
        }
    };

    private EditLanguageItem getItemDetail(int position){
        return ((EditLanguageItem)getItem(position));
    }


}
