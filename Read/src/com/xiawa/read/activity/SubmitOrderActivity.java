package com.xiawa.read.activity;

import android.os.Bundle;

import com.xiawa.read.R;

public class SubmitOrderActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		setHeaderTitle("提交订单");
	}
}