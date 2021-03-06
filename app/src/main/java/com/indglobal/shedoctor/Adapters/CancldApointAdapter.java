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
import android.widget.Toast;

import com.indglobal.shedoctor.Activities.CanclApointDetail;
import com.indglobal.shedoctor.Activities.PastApointDetail;
import com.indglobal.shedoctor.Activities.PrescriptionViewActivity;
import com.indglobal.shedoctor.Beans.CancledApointItem;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;

/**
 * Created by Android on 10/13/16.
 */
public class CancldApointAdapter extends RecyclerView.Adapter<CancldApointAdapter.MyViewHolder> {

    private ArrayList<CancledApointItem> cancledApointItemArrayList;
    private int lastPosition = -1;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPastLstItmClock,tvPastLstItmprescription,tvPastLstItmdate,tvPastLstItmname,tvPastLstItmpatientid,tvPastLstItmsuccessful;
        RippleView rplpastapointitem;
        ImageView tvPastLstItmsuccessfulimg,tvPastApntvideocall;

        public MyViewHolder(View view) {
            super(view);

            tvPastLstItmClock = (TextView)view.findViewById(R.id.tvPastLstItmClock);
            tvPastLstItmprescription = (TextView)view.findViewById(R.id.tvPastLstItmprescription);
            tvPastLstItmdate = (TextView)view.findViewById(R.id.tvPastLstItmdate);
            tvPastLstItmname = (TextView)view.findViewById(R.id.tvPastLstItmname);
            tvPastLstItmpatientid = (TextView)view.findViewById(R.id.tvPastLstItmpatientid);
            tvPastLstItmsuccessful = (TextView)view.findViewById(R.id.tvPastLstItmsuccessful);
            rplpastapointitem = (RippleView)view.findViewById(R.id.rplpastapointitem);

            tvPastLstItmsuccessfulimg =(ImageView)view.findViewById(R.id.tvPastLstItmsuccessfulimg);
            tvPastApntvideocall =(ImageView)view.findViewById(R.id.tvPastApntvideocall);

        }
    }

    public CancldApointAdapter(Context context, ArrayList<CancledApointItem> cancledApointItemArrayList) {
        this.cancledApointItemArrayList = cancledApointItemArrayList;
        this.context = context;
    }

    public CancldApointAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_apoint_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CancledApointItem cancldApointItem = cancledApointItemArrayList.get(position);

        holder.tvPastLstItmdate.setText(cancldApointItem.getDate());
        holder.tvPastLstItmClock.setText(cancldApointItem.getTime());
        holder.tvPastLstItmpatientid.setText("Patient ID:"+cancldApointItem.getPid());

        if((cancldApointItem.getPrescription()).equalsIgnoreCase("false")){
            holder.tvPastLstItmprescription.setText("No prescription available");
            holder.tvPastLstItmprescription.setTextColor(context.getResources().getColor(R.color.fullGray));
        }else {
            holder.tvPastLstItmprescription.setText("View Prescriptions");
            holder.tvPastLstItmprescription.setTextColor(context.getResources().getColor(R.color.lightblue));
        }

        if((cancldApointItem.getStatus()).equalsIgnoreCase("Cancelled")){
            holder.tvPastLstItmsuccessful.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvPastLstItmsuccessfulimg.setImageDrawable(context.getResources().getDrawable(R.drawable.cncl));
            holder.tvPastLstItmsuccessful.setText(cancldApointItem.getReason());
        }else if((cancldApointItem.getStatus()).equalsIgnoreCase("Failed")){
            holder.tvPastLstItmsuccessful.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvPastLstItmsuccessfulimg.setImageDrawable(context.getResources().getDrawable(R.drawable.cncl));
            holder.tvPastLstItmsuccessful.setText(cancldApointItem.getReason());
        }else if ((cancldApointItem.getStatus()).equalsIgnoreCase("N/A")){
            holder.tvPastLstItmsuccessful.setTextColor(context.getResources().getColor(R.color.fullGray));
            holder.tvPastLstItmsuccessfulimg.setImageDrawable(context.getResources().getDrawable(R.drawable.pendng));
            holder.tvPastLstItmsuccessful.setText("Pending");
        }else {
            holder.tvPastLstItmsuccessful.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvPastLstItmsuccessfulimg.setImageDrawable(context.getResources().getDrawable(R.drawable.complete));
            holder.tvPastLstItmsuccessful.setText("Success");
        }


        holder.tvPastLstItmname.setText(cancldApointItem.getPname());

        if((cancldApointItem.getType()).equalsIgnoreCase("voice")){

            holder.tvPastApntvideocall.setImageDrawable(context.getResources().getDrawable(R.drawable.call));

        }else {
            holder.tvPastApntvideocall.setImageDrawable(context.getResources().getDrawable(R.drawable.video_call));
        }

        setScaleAnimation(holder.itemView);

        holder.rplpastapointitem.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                int post = holder.getAdapterPosition();

                Intent ii = new Intent(context, CanclApointDetail.class);
                ii.putExtra("post",post+"");
                context.startActivity(ii);
            }
        });

        holder.tvPastLstItmprescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int post = holder.getAdapterPosition();
                if ((cancledApointItemArrayList.get(post).getPrescription()).equalsIgnoreCase("true")){
                    String id1 = cancledApointItemArrayList.get(post).getId();
                    Intent iii = new Intent(context, PrescriptionViewActivity.class);
                    iii.putExtra("apointId", id1);
                    context.startActivity(iii);
                }else {
                    Toast.makeText(context, "Prescription not available!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return cancledApointItemArrayList.size();
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
