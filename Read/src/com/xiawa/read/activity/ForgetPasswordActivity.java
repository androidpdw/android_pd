package com.xiawa.read.activity;

import com.xiawa.read.R;
import com.xiawa.read.utils.UIUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
	}

	/**
	 * 顶部返回按钮
	 * 
	 * @param view
	 */
	public void back(View view)
	{
		onBackPressed();
	}

	/**
	 * 重置密码
	 * 
	 * @param view
	 */
	public void resetPwd(View view)
	{
		UIUtils.showToast(this, "重置密码");
	}
}
