package com.threabba.android.pedometer.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threabba.android.pedometer.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jun on 16. 12. 3.
 */

public class StepMiniView extends RelativeLayout{

    @BindView(R.id.distance)
    TextView tv_dist;
    @BindView(R.id.steps)
    TextView tv_step;

    public StepMiniView(Context context) {
        super(context);
        initView(context);

    }
    private void initView(Context context) {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.view_mini, this, false);
        addView(v);
        ButterKnife.bind(this);
    }

    public void setDist(String dist){
        tv_dist.setText(dist);
    }
    public void setStep(String step){
        tv_step.setText(step);
    }

}
