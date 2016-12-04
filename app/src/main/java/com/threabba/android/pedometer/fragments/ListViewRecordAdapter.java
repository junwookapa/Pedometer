package com.threabba.android.pedometer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.threabba.android.pedometer.R;
import com.threabba.android.pedometer.db.Record;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jun on 16. 12. 3.
 */

public class ListViewRecordAdapter extends BaseAdapter {

    private List<Record> mList;
    private LayoutInflater mLayoutInflater;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final DecimalFormat mDecimalFormat = new DecimalFormat("0.##");

    public ListViewRecordAdapter(Context context){
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setList(List<Record> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mList != null){
            return mList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(mList != null){
            return mList.get(position);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = this.mLayoutInflater.inflate(R.layout.lv_row_record, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Record record = (Record)getItem(position);
        if(record != null){
            holder.tv_date.setText(mDateFormat.format(record.getDate()));
            holder.tv_distance.setText(mDecimalFormat.format(record.getDistance())+"KM");
            holder.tv_step_count.setText(record.getStep_count()+"");
        }

        return convertView;
    }

    protected class ViewHolder {
        @BindView(R.id.lv_row_record_tv_date)
        TextView tv_date;
        @BindView(R.id.lv_row_record_tv_distance)
        TextView tv_distance;
        @BindView(R.id.lv_row_record_tv_step_count)
        TextView tv_step_count;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
