package com.xiawa.read.activity;

import com.xiawa.read.R;

import android.os.Bundle;

/**
 * 服务站管理借书Activity
 * @author CPZ
 *
 */
public class BorrowBookActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_book);
		setHeaderTitle("用户图书借阅订单");
	}
}
