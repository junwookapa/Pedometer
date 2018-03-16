package com.threabba.android.qrtest;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.threabba.android.pedometer.R;

/**
 * Created by ETRI LSAR Project Team on 2018-03-16.
 */

public class ListViewItem1 {
    private String tv1;
    private String btn1;

    public String getTv1() {
        return tv1;
    }

    public void setTv1(String tv1) {
        this.tv1 = tv1;
    }

    public String getBtn1() {
        return btn1;
    }

    public void setBtn1(String btn1) {
        this.btn1 = btn1;
    }

    public static class Holder {
        private TextView tv1;
        private Button btn1;
        public Holder(View view) {
            tv1 = (TextView)view.findViewById(R.id.tv1);
            btn1 = (Button)view.findViewById(R.id.btn1);
        }
        public void update(ListViewItem1 item){
            tv1.setText(item.getTv1());
            btn1.setText(item.getBtn1());
        }
    }
}
