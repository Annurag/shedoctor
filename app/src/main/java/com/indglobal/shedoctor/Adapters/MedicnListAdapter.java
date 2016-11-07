package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.shedoctor.Beans.MedicinItem;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/1/16.
 */
public class MedicnListAdapter extends RecyclerView.Adapter<MedicnListAdapter.MyViewHolder> {

    private ArrayList<MedicinItem> medicinItemArrayList;
    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMedcnListItemName,tvMedcnListItemType,tvMedcnListItemMrng,tvMedcnListItemAftr,
                tvMedcnListItemEvng,tvMedcnListItemNght,tvMedcnListItemNots;


        public MyViewHolder(View view) {
            super(view);

            tvMedcnListItemName = (TextView)view.findViewById(R.id.tvMedcnListItemName);
            tvMedcnListItemType = (TextView)view.findViewById(R.id.tvMedcnListItemType);
            tvMedcnListItemMrng = (TextView)view.findViewById(R.id.tvMedcnListItemMrng);
            tvMedcnListItemAftr = (TextView)view.findViewById(R.id.tvMedcnListItemAftr);
            tvMedcnListItemEvng = (TextView)view.findViewById(R.id.tvMedcnListItemEvng);
            tvMedcnListItemNght = (TextView)view.findViewById(R.id.tvMedcnListItemNght);
            tvMedcnListItemNots = (TextView)view.findViewById(R.id.tvMedcnListItemNots);

        }
    }

    public MedicnListAdapter(Context context, ArrayList<MedicinItem> medicinItemArrayList) {
        this.medicinItemArrayList = medicinItemArrayList;
        this.context = context;
    }

    public MedicnListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicn_view_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MedicinItem medicinItem = medicinItemArrayList.get(position);

        holder.tvMedcnListItemName.setText(medicinItem.getName());
        holder.tvMedcnListItemType.setText(medicinItem.getType());
        holder.tvMedcnListItemMrng.setText(medicinItem.getMorning());
        holder.tvMedcnListItemAftr.setText(medicinItem.getAfternoon());
        holder.tvMedcnListItemEvng.setText(medicinItem.getEvening());
        holder.tvMedcnListItemNght.setText(medicinItem.getNight());

        if ((medicinItem.getNote()).equalsIgnoreCase("null")){
            holder.tvMedcnListItemNots.setText("Not Available");
        }else {
            holder.tvMedcnListItemNots.setText(medicinItem.getNote());
        }


    }

    @Override
    public int getItemCount() {
        return medicinItemArrayList.size();
    }

}