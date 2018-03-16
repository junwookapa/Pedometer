package com.threabba.android.qrtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.threabba.android.pedometer.R;

import java.util.List;

/**
 * Created by ETRI LSAR Project Team on 2018-03-16.
 */

public class QRActivity extends AppCompatActivity{
    IntentIntegrator mIntentIntegrator;
    ListViewAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mIntentIntegrator = new IntentIntegrator(this);
        bindStartButton();
        bindListView();
    }

    private void bindStartButton() {
        findViewById(R.id.btn_start_scan)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntentIntegrator.initiateScan();
                    }
                });
    }

    private void bindListView() {
        mAdapter = new ListViewAdapter(this);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
    }


    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                ListViewItem1 item = new ListViewItem1();
                item.setTv1(result.getContents());
                item.setBtn1((mAdapter.getCount()+1)+"");
                mAdapter.addItem1(item);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
