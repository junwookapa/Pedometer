package com.threabba.android.pedometer.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.threabba.android.pedometer.App;
import com.threabba.android.pedometer.R;
import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.Record;
import com.threabba.android.pedometer.db.RecordDao;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jun on 16. 12. 3.
 */

public class PedometerFragment extends Fragment{

    private CallBack mCallback;
    private View view;
    private final String STEP = "STEP";
    private final String ADDRESS = "ADDRESS";
    private final String DIST = "DIST";
    private final String TOGGLE = "TOGGLE";

    @BindView(R.id.frag_main_tv_step) TextView tv_step;
    @BindView(R.id.frag_main_tv_address) TextView tv_address;
    @BindView(R.id.frag_main_tv_dist) TextView tv_dist;
    @BindView(R.id.frag_main_tgbtn_toggle) ToggleButton tgbtn_toggle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_main, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null){
            setAddress(savedInstanceState.getString(ADDRESS));
            setDist(savedInstanceState.getString(DIST));
            setStep(savedInstanceState.getString(STEP));
            setToggle(savedInstanceState.getBoolean(TOGGLE));
        }else{
            initView();
        }
        return view;
    }
    @OnClick(R.id.frag_main_tgbtn_toggle)
    void onClick_tgbtn_toggle(){
        mCallback.onClick_tgbtn_toggle(tgbtn_toggle.isChecked());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CallBack)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MenuButtonClickListener");
        }
    }

    private void initView(){
        setAddress("");
        setDist("0KM");
        setStep("0");
    }

    public interface CallBack{
        void onClick_tgbtn_toggle(boolean isActive);
    }


    public static PedometerFragment newInstance() {
        PedometerFragment frag = new PedometerFragment();
        return frag;
    }
    public void setAddress(String address){
        tv_address.setText(address);
    }
    public void setDist(String dist){
        tv_dist.setText(dist);
    }
    public void setStep(String step){
        tv_step.setText(step);
    }
    public void setToggle(boolean toggle){
        tgbtn_toggle.setChecked(toggle);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            setAddress(savedInstanceState.getString(ADDRESS));
            setDist(savedInstanceState.getString(DIST));
            setStep(savedInstanceState.getString(STEP));
            setToggle(savedInstanceState.getBoolean(TOGGLE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STEP, tv_step.getText().toString());
        outState.putString(ADDRESS, tv_address.getText().toString());
        outState.putString(DIST, tv_dist.getText().toString());
        outState.putBoolean(TOGGLE, tgbtn_toggle.isChecked());
    }
}
