package com.xiawa.read.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.domain.GlobalConfig;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AdminLoginActivity extends BaseActivity
{
	//用户名
	@ViewInject(R.id.et_user_name)
	private EditText etUserName;
	//密码
	@ViewInject(R.id.et_password)
	private EditText etPasswd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_login);
		ViewUtils.inject(this);
		setHeaderTitle("服务站登录");
	}
	
	/**
	 * 登录按钮点击事件
	 * @param view
	 */
	public void onLogin(View view)
	{
		//登录操作
		
		
		
		//登录失败
		
		
		//登录成功
		GlobalConfig.isAdmin=true;
		finish();
		
		
	}
}
