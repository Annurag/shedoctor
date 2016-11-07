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
import com.indglobal.shedoctor.Activities.PaymentDetails;
import com.indglobal.shedoctor.Beans.LedgerItem;
import com.indglobal.shedoctor.Beans.PaymentItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by SONY on 30/09/2016.
 */
public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    private ArrayList<PaymentItem> paymentItemArrayList;
    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPaymenttransactionId,tvPaymentDate,tvPaymentAmount,tvPaymentrecieved;
        RippleView rplPaymentitem;
        ImageView tvPaymentComplete;

        public MyViewHolder(View view) {
            super(view);

            tvPaymenttransactionId = (TextView)view.findViewById(R.id.tvPaymenttransactionId);
            tvPaymentDate = (TextView)view.findViewById(R.id.tvPaymentDate);
            tvPaymentAmount = (TextView)view.findViewById(R.id.tvPaymentAmount);
            tvPaymentrecieved = (TextView)view.findViewById(R.id.tvPaymentrecieved);

            rplPaymentitem = (RippleView)view.findViewById(R.id.rplPaymentitem);
            tvPaymentComplete = (ImageView)view.findViewById(R.id.tvPaymentComplete);

        }
    }
    public PaymentAdapter(Context context, ArrayList<PaymentItem> paymentItemArrayList) {
        this.paymentItemArrayList = paymentItemArrayList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_listitems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final PaymentItem paymentItem = paymentItemArrayList.get(position);

        if ((paymentItem.getTransaction_id()).equalsIgnoreCase("null")){
            holder.tvPaymenttransactionId.setText("Not Available");
        }else {
            holder.tvPaymenttransactionId.setText(paymentItem.getTransaction_id());
        }


        holder.tvPaymenttransactionId.setText(paymentItem.getTransaction_id());
        holder.tvPaymentDate.setText(paymentItem.getDate());
        holder.tvPaymentAmount.setText(paymentItem.getAmount());
        holder.tvPaymentrecieved.setText(paymentItem.getStatus());

        //Pending,Failed,Done

        if ((paymentItem.getStatus()).equalsIgnoreCase("Done")) {
            holder.tvPaymentComplete.setImageDrawable(context.getResources().getDrawable(R.drawable.complete));
            holder.tvPaymentrecieved.setText("Received");
            holder.tvPaymentrecieved.setTextColor(context.getResources().getColor(R.color.green));
        } else if((paymentItem.getStatus()).equalsIgnoreCase("Pending")){
            holder.tvPaymentComplete.setImageDrawable(context.getResources().getDrawable(R.drawable.pendng));
            holder.tvPaymentrecieved.setText("Pending");
            holder.tvPaymentrecieved.setTextColor(context.getResources().getColor(R.color.fullGray));
        }else{
            holder.tvPaymentComplete.setImageDrawable(context.getResources().getDrawable(R.drawable.cncl));
            holder.tvPaymentrecieved.setText("Failed");
            holder.tvPaymentrecieved.setTextColor(context.getResources().getColor(R.color.red));
        }

        setScaleAnimation(holder.itemView);

        holder.rplPaymentitem.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                int post = holder.getAdapterPosition();

                Intent ii = new Intent(context, PaymentDetails.class);
                ii.putExtra("post",post+"");
                context.startActivity(ii);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paymentItemArrayList.size();
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
