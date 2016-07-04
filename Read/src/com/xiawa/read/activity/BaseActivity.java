package com.xiawa.read.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiawa.read.R;
import com.xiawa.read.domain.GlobalConfig;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalConfig.add(this);
	}

	public void onBack(View view) {
		onBackPressed();
	}
	
	public void setHeaderTitle(String title) {
		TextView tv = (TextView) findViewById(R.id.tv_title);
		tv.setText(title);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalConfig.remove(this);
	}
}
