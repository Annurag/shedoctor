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

import com.indglobal.shedoctor.Activities.DateApointments;
import com.indglobal.shedoctor.Activities.UpcomingApointDetail;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/6/16.
 */
public class DatewiseApointAdapter extends RecyclerView.Adapter<DatewiseApointAdapter.MyViewHolder> {

    private ArrayList<UpcomingApointItem> upcomingApointItemArrayList;
    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUpcmgLstItmTime,tvUpcmgLstItmDate,tvUpcmgLstItmName,tvUpcmgLstItmId,tvUpcmgLstItmStart;
        RippleView rplupcmngapointitem;
        ImageView tvUpcmgApntvideocall;

        public MyViewHolder(View view) {
            super(view);

            tvUpcmgLstItmTime = (TextView)view.findViewById(R.id.tvUpcmgLstItmTime);
            tvUpcmgLstItmDate = (TextView)view.findViewById(R.id.tvUpcmgLstItmDate);
            tvUpcmgLstItmName = (TextView)view.findViewById(R.id.tvUpcmgLstItmName);
            tvUpcmgLstItmId = (TextView)view.findViewById(R.id.tvUpcmgLstItmId);
            tvUpcmgLstItmStart = (TextView)view.findViewById(R.id.tvUpcmgLstItmStart);
            rplupcmngapointitem = (RippleView)view.findViewById(R.id.rplupcmngapointitem);
            tvUpcmgApntvideocall = (ImageView)view.findViewById(R.id.tvUpcmgApntvideocall);

        }
    }

    public DatewiseApointAdapter(Context context, ArrayList<UpcomingApointItem> upcomingApointItemArrayList) {
        this.upcomingApointItemArrayList = upcomingApointItemArrayList;
        this.context = context;
    }

    public DatewiseApointAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcmng_apoin_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final UpcomingApointItem upcomingApointItem = upcomingApointItemArrayList.get(position);

        holder.tvUpcmgLstItmTime.setText(upcomingApointItem.getTime());
        holder.tvUpcmgLstItmDate.setText(upcomingApointItem.getDate());
        holder.tvUpcmgLstItmName.setText(upcomingApointItem.getPname());
        holder.tvUpcmgLstItmId.setText("Patient ID:"+upcomingApointItem.getPid());
        holder.tvUpcmgLstItmStart.setText("Call starts in "+upcomingApointItem.getStrt());

        setScaleAnimation(holder.itemView);

        holder.rplupcmngapointitem.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                int post = holder.getAdapterPosition();

                Intent ii = new Intent(context, UpcomingApointDetail.class);
                ii.putExtra("post",post+"");
                ii.putExtra("date","1");
                context.startActivity(ii);
            }
        });


        if((upcomingApointItem.getType()).equalsIgnoreCase("voice")){

            holder.tvUpcmgApntvideocall.setImageDrawable(context.getResources().getDrawable(R.drawable.call));
        }else {
            holder.tvUpcmgApntvideocall.setImageDrawable(context.getResources().getDrawable(R.drawable.video_call));
        }
    }

    @Override
    public int getItemCount() {
        return upcomingApointItemArrayList.size();
    }

    private void setAnimation(View viewToAnimate) {
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