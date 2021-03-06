package com.threabba.android.pedometer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.threabba.android.pedometer.App;
import com.threabba.android.pedometer.R;
import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.Record;
import com.threabba.android.pedometer.db.RecordDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jun on 16. 12. 3.
 */

public class RecordFragment extends Fragment {
    @BindView(R.id.frg_record_lv_recordlist) ListView lv_recordlist;
    ListViewRecordAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_record, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new ListViewRecordAdapter(this.getContext());
        lv_recordlist.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaoSession session =((App)getActivity().getApplication()).getDaoSession();
        notifyDataSetChanged(session);
    }

    public static RecordFragment newInstance() {
        RecordFragment frag = new RecordFragment();
        return frag;
    }

    public void notifyDataSetChanged(DaoSession session){

        List<Record> records = session.getRecordDao().queryBuilder().orderDesc(RecordDao.Properties.Id).build().list();
        if(records != null){
            mAdapter.setList(records);
        }
    }

}
