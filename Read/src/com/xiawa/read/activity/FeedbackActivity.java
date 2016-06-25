package com.xiawa.read.activity;

import android.os.Bundle;

import com.xiawa.read.R;

public class FeedbackActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setHeaderTitle("留言");
	}
}
