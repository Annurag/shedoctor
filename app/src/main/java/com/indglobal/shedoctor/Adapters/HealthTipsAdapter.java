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

import com.indglobal.shedoctor.Activities.HealthDetailActivity;
import com.indglobal.shedoctor.Activities.UpcomingApointDetail;
import com.indglobal.shedoctor.Beans.HealthTipsItem;
import com.indglobal.shedoctor.Beans.UpcomingApointItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Android on 9/28/16.
 */
public class HealthTipsAdapter extends RecyclerView.Adapter<HealthTipsAdapter.MyViewHolder> {

    private ArrayList<HealthTipsItem> healthTipsItemArrayList;
    private int lastPosition = -1;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHealtListItemTitle,tvHealtListItemContent,tvHealtListItemStatus;
        RippleView rplHealtListItemClick;
        ImageView ivHealtListItemPic;

        public MyViewHolder(View view) {
            super(view);

            tvHealtListItemTitle = (TextView)view.findViewById(R.id.tvHealtListItemTitle);
            tvHealtListItemContent = (TextView)view.findViewById(R.id.tvHealtListItemContent);
            tvHealtListItemStatus = (TextView)view.findViewById(R.id.tvHealtListItemStatus);
            rplHealtListItemClick = (RippleView)view.findViewById(R.id.rplHealtListItemClick);
            ivHealtListItemPic = (ImageView)view.findViewById(R.id.ivHealtListItemPic);

        }
    }

    public HealthTipsAdapter(Context context, ArrayList<HealthTipsItem> healthTipsItemArrayList) {
        this.healthTipsItemArrayList = healthTipsItemArrayList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.healthtips_listitems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final HealthTipsItem healthTipsItem = healthTipsItemArrayList.get(position);

        holder.tvHealtListItemTitle.setText(healthTipsItem.getTitle());
        holder.tvHealtListItemContent.setText(healthTipsItem.getContent());

        if ((healthTipsItem.getActive()).equalsIgnoreCase("0")){
            holder.tvHealtListItemStatus.setText("Status:- Pending Approval");
        }else {
            holder.tvHealtListItemStatus.setText("Status:- Approved");
        }

        Picasso.with(context).load(healthTipsItem.getImage()).resize(100, 100).error(R.drawable.health_tips)
                .placeholder(R.drawable.health_tips).into(holder.ivHealtListItemPic);

        setScaleAnimation(holder.itemView);

        holder.rplHealtListItemClick.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                int post = holder.getAdapterPosition();

                Intent ii = new Intent(context, HealthDetailActivity.class);
                ii.putExtra("post",post+"");
                context.startActivity(ii);
            }
        });



    }

    @Override
    public int getItemCount() {
        return healthTipsItemArrayList.size();
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