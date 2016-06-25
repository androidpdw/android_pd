package com.xiawa.read.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.xiawa.read.R;

public class FeedbackActivity extends BaseActivity {

	private EditText et_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setHeaderTitle("留言");
		et_content = (EditText) findViewById(R.id.et_content);
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_content.getText())) {
					Toast.makeText(getApplicationContext(), "反馈内容不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(), "我们已收到您的反馈！", Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
		});
	}
}
