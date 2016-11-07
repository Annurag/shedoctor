package com.indglobal.shedoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.shedoctor.Activities.LedgerDetails;
import com.indglobal.shedoctor.Activities.UpcomingApointDetail;
import com.indglobal.shedoctor.Beans.LedgerItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by SONY on 30/09/2016.
 */
public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.MyViewHolder> {

    private ArrayList<LedgerItem> leaderItmArrayList;
    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvLedgertrnstnId,tvLedgerDate,tvLedgerpatntId,tvLedgerAmmount;
        RippleView rplLedgeritem;
        ImageView ivLedgervideo;

        public MyViewHolder(View view) {
            super(view);

            tvLedgertrnstnId = (TextView)view.findViewById(R.id.tvLedgertrnstnId);
            tvLedgerDate = (TextView)view.findViewById(R.id.tvLedgerDate);
            tvLedgerpatntId = (TextView)view.findViewById(R.id.tvLedgerpatntId);
            tvLedgerAmmount = (TextView)view.findViewById(R.id.tvLedgerAmmount);

            rplLedgeritem = (RippleView)view.findViewById(R.id.rplLedgeritem);
            ivLedgervideo = (ImageView)view.findViewById(R.id.ivLedgervideo);

        }
    }
    public LedgerAdapter(Context context, ArrayList<LedgerItem> leaderItmArrayList) {
        this.leaderItmArrayList = leaderItmArrayList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final LedgerItem ledgerItem = leaderItmArrayList.get(position);

        if ((ledgerItem.getTransaction_id()).equalsIgnoreCase("null")){
            holder.tvLedgertrnstnId.setText("Not Available");
        }else {
            holder.tvLedgertrnstnId.setText(ledgerItem.getTransaction_id());
        }


        holder.tvLedgerDate.setText(ledgerItem.getDate());
        holder.tvLedgerpatntId.setText(ledgerItem.getPatient_id());
        holder.tvLedgerAmmount.setText(ledgerItem.getPatient_rs());

        if ((ledgerItem.getConsultation_type()).equalsIgnoreCase("voice")) {

            holder.ivLedgervideo.setImageDrawable(context.getResources().getDrawable(R.drawable.call));
        } else {
            holder.ivLedgervideo.setImageDrawable(context.getResources().getDrawable(R.drawable.video_call));
        }

        setScaleAnimation(holder.itemView);

        holder.rplLedgeritem.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                int post = holder.getAdapterPosition();

                Intent ii = new Intent(context, LedgerDetails.class);
                ii.putExtra("post",post+"");
                context.startActivity(ii);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leaderItmArrayList.size();
    }

    private void setAnimation(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setDuration(700);
        viewToAnimate.startAnimation(animation);

    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }
}