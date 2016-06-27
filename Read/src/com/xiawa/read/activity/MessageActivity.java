package com.xiawa.read.activity;

import android.os.Bundle;

import com.xiawa.read.R;

public class MessageActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		setHeaderTitle("消息中心");
	}
}
