package com.indglobal.shedoctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Activities.DateApointments;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    private Calendar month;
    public GregorianCalendar pmonth;

    ViewHolder holder;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> dayString;
    ArrayList<String> apoints;
    private View previousView;

    public CalendarAdapter(Context c, Calendar monthCalendar) {
        dayString = new ArrayList<String>();
        apoints = new ArrayList<>();
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView dayView;
        RippleView rplclndrdatetitem;
        TextView tvappt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendr_grid_item, parent, false);
            holder = new ViewHolder();

            holder.dayView = (TextView) v.findViewById(R.id.tvClndrGrdItmDate);
            holder.tvappt = (TextView) v.findViewById(R.id.tvClndrGrdItmApnts);
            holder.rplclndrdatetitem = (RippleView)v.findViewById(R.id.rplclndrdatetitem);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        String[] separatedTime = dayString.get(position).split("-");

        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            holder.dayView.setTextColor(Color.WHITE);
            holder.dayView.setBackgroundColor(Color.WHITE);
            holder.dayView.setClickable(false);
            holder.dayView.setFocusable(false);

        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            holder.dayView.setTextColor(Color.WHITE);
            holder.dayView.setBackgroundColor(Color.WHITE);
            holder.dayView.setClickable(false);
            holder.dayView.setFocusable(false);

        } else {
            holder.dayView.setTextColor(Color.BLACK);
            holder.dayView.setBackgroundColor(Color.WHITE);
        }

        if (dayString.get(position).equals(curentDateString)) {
            holder.dayView.setBackgroundResource(R.drawable.oval_with_stroke);

        } else {
            holder.dayView.setBackgroundColor(Color.WHITE);
        }

        holder.dayView.setText(gridvalue);

        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }

        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        if (date.length() > 0 && items != null && items.contains(date)) {
            if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
                holder.tvappt.setVisibility(View.GONE);
                apoints.set(position,"0");
            } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
                holder.tvappt.setVisibility(View.GONE);
                apoints.set(position, "0");
            } else {
                holder.tvappt.setVisibility(View.VISIBLE);

                String tempDate= dayString.get(position);
                holder.tvappt.setText(String.valueOf(Collections.frequency(items, tempDate)));

               // apoints.add(String.valueOf(Collections.frequency(items, tempDate)));
                apoints.set(position, String.valueOf(Collections.frequency(items, tempDate)));
            }
        } else {
            holder.tvappt.setVisibility(View.INVISIBLE);
            apoints.set(position,"0");
        }


        holder.rplclndrdatetitem.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                String date1 = dayString.get(position);
                String ss = apoints.get(position);

                if (!ss.equalsIgnoreCase("0")) {
                    Intent ii = new Intent(mContext, DateApointments.class);
                    ii.putExtra("date", date1);
                    mContext.startActivity(ii);
                } else {
                    Toast.makeText(mContext, "No Appontments available on this date!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }


    public void refreshDays() {

        dayString.clear();
        pmonth = (GregorianCalendar) month.clone();

        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);

        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);

        mnthlength = maxWeeknumber * 7;

        calMaxP = maxP - (firstDay - 1);

        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);
            apoints.add(itemvalue);
        }
    }
}
