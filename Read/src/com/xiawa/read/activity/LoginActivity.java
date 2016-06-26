package com.xiawa.read.activity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiawa.read.R;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.utils.URLString;
import com.xiawa.read.view.CommonProgressDialog;

public class LoginActivity extends Activity {
	// 输入登录名
	@ViewInject(R.id.et_user_name)
	private EditText etUserName;
	// 输入密码
	@ViewInject(R.id.et_password)
	private EditText etPwd;

	private CommonProgressDialog mCustomProgrssDialog;

	/**
	 * 使用ViewUtils给顶部返回添加事件
	 * 
	 * @param v
	 */
	@OnClick(R.id.iv_back_top)
	public void backTopOnClick(View v) {
		finish();
	}

	/**
	 * 注册
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_sign_up)
	public void signUp(View v) {
		startActivity(new Intent(this, SignUpActivity.class));
	}

	/**
	 * 忘记密码
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_forget_pwd)
	public void forgetPwd(View v) {
		startActivity(new Intent(this, ForgetPasswordActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		findViewById(R.id.iv_back_top).setVisibility(View.VISIBLE);
		
	}

	/**
	 * 登录
	 * 
	 * @param view
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws JSONException
	 */
	public void login(View view) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, JSONException {
		if (isUsernameAndPwdValid()) {

			String URL = URLString.URL_DOMAIN + URLString.URL_LOGIN;

			sendPOST(URL);
		}
	}

	/*
	 * 向服务器提交数据
	 */
	public void sendPOST(String url) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("loginname", etUserName.getText().toString().trim());
		params.addBodyParameter("password", etPwd.getText().toString().trim());
		params.addBodyParameter("rememberUser", "1");
		httpUtils.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						Log.i("onstart", "onstart");
						showCustomProgrssDialog();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.i("failure", "网络连接错误！请重试");
						hideCustomProgressDialog();
						Toast.makeText(getApplicationContext(), "网络连接错误！请重试", 0)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						;
						hideCustomProgressDialog();
						try {
							//Log.i("arg0", "arg0: " + arg0.result);
							JSONObject obj = new JSONObject(arg0.result);

							String result = obj.getString("resultCode");
							if (result.equals("0000")) { // 登录成功
								userHomeActivity();
								SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
								Editor editor = sp.edit();
								editor.putBoolean("isisLogin", true);
								editor.commit();
								GlobalConfig.isLogin = true;
								// Toast.makeText(getApplicationContext(),
								// "登录成功", 0).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"用户名或密码错误!", 0).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	final void showCustomProgrssDialog() {
		if (null == mCustomProgrssDialog)
			mCustomProgrssDialog = CommonProgressDialog
					.createProgrssDialog(this);
		if (null != mCustomProgrssDialog) {
			// mCustomProgrssDialog.setMessage(msg);
			mCustomProgrssDialog.show();
			mCustomProgrssDialog.setCancelable(false);
		}
	}

	final void hideCustomProgressDialog() {
		if (null != mCustomProgrssDialog) {
			mCustomProgrssDialog.dismiss();
			mCustomProgrssDialog = null;
		}
	}

	public boolean isUsernameAndPwdValid() { // 判断登录名和密码是否为空
		if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {

			Toast.makeText(this, getString(R.string.loginName_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {

			Toast.makeText(this, getString(R.string.loginPwd_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 跳转到个人主页
	 */
	public void userHomeActivity() {
		// 返回Fragment
		finish();
		// startActivity(new Intent(this, UserInfoActivity.class));
	}

}
