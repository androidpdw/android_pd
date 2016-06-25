package com.xiawa.read.activity;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xiawa.read.R;

public class BaseActivity extends Activity {

	public void onBack(View view) {
		onBackPressed();
	}
	
	public void setHeaderTitle(String title) {
		TextView tv = (TextView) findViewById(R.id.tv_title);
		tv.setText(title);
	}
}
