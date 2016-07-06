package com.xiawa.read.activity;

import com.xiawa.read.R;

import android.os.Bundle;

public class ReturnBookActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return_book);
		setHeaderTitle("用户还书订单");
	}
}
