package com.xiawa.read.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.xiawa.read.activity.ForgetPasswordActivity;
import com.xiawa.read.activity.SignUpActivity;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.utils.URLString;
import com.xiawa.read.view.CommonProgressDialog;

/**
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
	
	private Context context;
	private String username;
	private String password;
	
	@ViewInject(R.id.ll_login)
	private LinearLayout ll_login;
	@ViewInject(R.id.ll_userinfo)
	private LinearLayout ll_userinfo;
	
	private CommonProgressDialog mCustomProgrssDialog;
	// @OnClick(R.id.nickName_tv)
	// public void nickNameOnClick(View v)
	// {
	// login();
	// }

	// 输入登录名
	@ViewInject(R.id.et_user_name)
	private EditText etUserName;
	// 输入密码
	@ViewInject(R.id.et_password)
	private EditText etPwd;
	
	/**
	 * 注册
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_sign_up)
	public void signUp(View v) {
		getActivity().startActivity(new Intent(getActivity(), SignUpActivity.class));
	}

	/**
	 * 忘记密码
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_forget_pwd)
	public void forgetPwd(View v) {
		getActivity().startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		ViewUtils.inject(this, view);
		SharedPreferences sp = getActivity().getSharedPreferences("config", 0);
		boolean isLogin = sp.getBoolean("isLogin", false);
		if (isLogin) {
			ll_login.setVisibility(View.GONE);
			ll_userinfo.setVisibility(View.VISIBLE);
		} else {
			ll_login.setVisibility(View.VISIBLE);
			ll_userinfo.setVisibility(View.GONE);
		}
		init();
		return view;
	}

	private void init() {
	}

	@OnClick(R.id.btn_login)
	private void login(View v) {
		username = etUserName.getText().toString().trim();
		password = etPwd.getText().toString().trim();
		String url = URLString.URL_DOMAIN + URLString.URL_LOGIN;
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", username);
		params.addBodyParameter("password", password);
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
						Toast.makeText(context, "网络连接错误！请重试", 0)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						;
						hideCustomProgressDialog();
						try {
							Log.i("hsq", "arg0: " + arg0.result);
							JSONObject obj = new JSONObject(arg0.result);

							String result = obj.getString("resultCode");
							if (result.equals("0000")) { // 登录成功
								SharedPreferences sp = context.getSharedPreferences("config", 0);
								Editor editor = sp.edit();
								editor.putBoolean("isLogin", true);
								editor.putString("username", username);
								editor.putString("password", password);
								editor.commit();
								GlobalConfig.isLogin = true;
								updateUI();
								// Toast.makeText(getApplicationContext(),
								// "登录成功", 0).show();
							} else {
								Toast.makeText(context,
										"用户名或密码错误!", 0).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
				});
	}
	
	/**登录成功后切换并设置UI*/
	private void updateUI() {
		ll_login.setVisibility(View.GONE);
		ll_userinfo.setVisibility(View.VISIBLE);
	}
	
	final void showCustomProgrssDialog() {
		if (null == mCustomProgrssDialog)
			mCustomProgrssDialog = CommonProgressDialog
					.createProgrssDialog(context);
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

			Toast.makeText(context, getString(R.string.loginName_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {

			Toast.makeText(context, getString(R.string.loginPwd_empty),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
