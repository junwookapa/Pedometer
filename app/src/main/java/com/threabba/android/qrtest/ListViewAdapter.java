package com.threabba.android.qrtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.threabba.android.pedometer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ETRI LSAR Project Team on 2018-03-16.
 */

public class ListViewAdapter extends BaseAdapter{
    private List<ListViewItem1> mListViewItem1List = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    public ListViewAdapter(Context context){
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListViewItem1List.size();
    }

    @Override
    public Object getItem(int i) {
        return mListViewItem1List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void addItem1(ListViewItem1 item1){
        mListViewItem1List.add(item1);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewItem1.Holder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.listview_item1, null);
            holder = new ListViewItem1.Holder(view);
            view.setTag(holder);
        } else {
            holder = (ListViewItem1.Holder) view.getTag();
        }
        holder.update(mListViewItem1List.get(i));
        return view;
    }

}
