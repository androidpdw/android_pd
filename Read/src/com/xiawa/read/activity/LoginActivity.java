package com.xiawa.read.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiawa.read.R;

public class LoginActivity extends Activity
{
	// 输入登录名
	@ViewInject(R.id.et_user_name)
	private EditText etUserName;
	// 输入密码
	@ViewInject(R.id.et_password)
	private EditText etPwd;
	/**
	 * 使用ViewUtils给顶部返回添加事件
	 *
	 * @param v
	 */
	@OnClick(R.id.iv_back_top)
	public void backTopOnClick(View v)
	{
		finish();
	}

	/**
	 * 注册
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_sign_up)
	public void signUp(View v)
	{
		startActivity(new Intent(this, SignUpActivity.class));
	}
	/**
	 * 忘记密码
	 * @param v
	 */
	@OnClick(R.id.tv_forget_pwd)
	public void forgetPwd(View v)
	{
		startActivity(new Intent(this, ForgetPasswordActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}

	/**
	 * 登录
	 *
	 * @param view
	 */
	public void login(View view)
	{

	}

}
