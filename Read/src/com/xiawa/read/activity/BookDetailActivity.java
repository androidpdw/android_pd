package com.xiawa.read.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiawa.read.R;

public class BookDetailActivity extends BaseActivity {

	private String ISBN;
	private Button btn_content;
	private Button btn_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);
		setHeaderTitle("图书详情");
		findViewById(R.id.rl_book_text).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getApplicationContext(),
								BookIntroductActivity.class));
					}
				});
			findViewById(R.id.rl_book_comment).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getApplicationContext(),
								BookCommentActivity.class));
					}
				});
	}
}
