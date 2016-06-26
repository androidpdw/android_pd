package com.xiawa.read.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.xiawa.read.R;

public class AuthenActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authen);
		ViewUtils.inject(this);
		initView();
	}

	private void initView()
	{
	}
}
