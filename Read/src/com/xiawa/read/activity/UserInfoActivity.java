package com.xiawa.read.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;

public class UserInfoActivity extends Activity implements OnClickListener
{

	@ViewInject(R.id.msgView)
	private RelativeLayout msgView;

	@ViewInject(R.id.feedbackView)
	private RelativeLayout feedbackView;

	@ViewInject(R.id.settingView)
	private RelativeLayout settingView;

	@ViewInject(R.id.rl_about)
	private RelativeLayout rlAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		ViewUtils.inject(this);
		initView();
	}

	private void initView()
	{
		msgView.setOnClickListener(this);
		feedbackView.setOnClickListener(this);
		settingView.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.msgView:
			Toast.makeText(this, "消息中心", Toast.LENGTH_SHORT).show();
			break;
		case R.id.feedbackView:
			Toast.makeText(this, "意见反馈", Toast.LENGTH_SHORT).show();
			break;
		case R.id.settingView:
			Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_about:
			startActivity(new Intent(this,AboutUsActivity.class));
			break;
		default:
			break;
		}
	}
}
