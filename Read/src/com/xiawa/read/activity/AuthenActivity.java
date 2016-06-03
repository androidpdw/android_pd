package com.xiawa.read.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;

import android.app.Activity;
import android.os.Bundle;

public class AuthenActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authen);
		ViewUtils.inject(this);
		initView();
	}

	private void initView()
	{
	}
}
