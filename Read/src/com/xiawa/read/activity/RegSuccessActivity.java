package com.xiawa.read.activity;

import com.xiawa.read.R;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class RegSuccessActivity extends BaseActivity
{

	private ListView lvAuthType;
	private String[] mAuthType = { "认证为学生", "认证为教师", "认证为家长", "认证为普通会员", "以后再说" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		setHeaderTitle("注册成功");
	}

	/**
	 * 判断认证类型
	 * 
	 * @param view
	 */
	public void onItemClick(View view)
	{
		int id = view.getId();
		Intent intent = new Intent();
		intent.setClass(this, AuthenActivity.class);
		switch (id)
		{
		case R.id.tv_auth_student:// 学生
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		case R.id.tv_auth_teacher:// 老师
			intent.putExtra("type", 2);
			startActivity(intent);
			break;
		case R.id.tv_auth_parents:// 家长
			intent.putExtra("type", 3);
			startActivity(intent);
			break;
		case R.id.tv_auth_general:// 普通
			intent.putExtra("type", 4);
			startActivity(intent);
			break;
		case R.id.tv_auth_later:// 以后再说
			finish();
			startActivity(new Intent(this, UserInfoActivity.class));
			break;

		}
	}

}
