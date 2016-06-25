package com.xiawa.read.activity;

import android.os.Bundle;

import com.xiawa.read.R;

public class BookDetailActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);
		setHeaderTitle("图书详情");
	}
}
