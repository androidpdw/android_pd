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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.xiawa.read.activity.AboutUsActivity;
import com.xiawa.read.activity.FeedbackActivity;
import com.xiawa.read.activity.ForgetPasswordActivity;
import com.xiawa.read.activity.MessageActivity;
import com.xiawa.read.activity.OrderActivity;
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

	private boolean finished = false;

	public MyFragment() {

	}

	public MyFragment(Context context) {
		this.context = context;
	}

	@ViewInject(R.id.ll_login)
	private LinearLayout ll_login;
	@ViewInject(R.id.ll_userinfo)
	private LinearLayout ll_userinfo;
	@ViewInject(R.id.nickName_tv)
	private TextView tv_nickName;
	@ViewInject(R.id.tv_score)
	private TextView tv_score;

	private CommonProgressDialog mCustomProgrssDialog;

	// 输入登录名
	@ViewInject(R.id.et_user_name)
	private EditText etUserName;
	// 输入密码
	@ViewInject(R.id.et_password)
	private EditText etPwd;

	@ViewInject(R.id.msgView)
	private RelativeLayout msgView;

	@ViewInject(R.id.feedbackView)
	private RelativeLayout feedbackView;

	@ViewInject(R.id.settingView)
	private RelativeLayout settingView;
	
	@ViewInject(R.id.all_order_rl)
	private LinearLayout all_order_rl;
	
	@ViewInject(R.id.wait_pay_order_rl)
	private RelativeLayout wait_pay_order_rl;
	
	@ViewInject(R.id.no_comment_rl)
	private RelativeLayout check_rl;

	@ViewInject(R.id.rl_refund)
	private RelativeLayout rl_refund;

	@ViewInject(R.id.rl_about)
	private RelativeLayout rlAbout;
	
	private boolean isFinished = false;

	/**
	 * 注册
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_sign_up)
	public void signUp(View v) {
		getActivity().startActivity(
				new Intent(getActivity(), SignUpActivity.class));
	}

	/**
	 * 忘记密码
	 * 
	 * @param v
	 */
	@OnClick(R.id.tv_forget_pwd)
	public void forgetPwd(View v) {
		getActivity().startActivity(
				new Intent(getActivity(), ForgetPasswordActivity.class));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		ViewUtils.inject(this, view);
		SharedPreferences sp = getActivity().getSharedPreferences("config", 0);
		boolean isLogin = sp.getBoolean("isLogin", false);
		if (isLogin) {
			ll_login.setVisibility(View.GONE);
			ll_userinfo.setVisibility(View.VISIBLE);
			updateUserInfo();
		} else {
			ll_login.setVisibility(View.VISIBLE);
			ll_userinfo.setVisibility(View.GONE);
		}

		init(view);
		isFinished = true;
		return view;
	}

	private void init(View view) {

	}

	private void updateUserInfo() {
		tv_nickName.setText(GlobalConfig.username);
		msgView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context, MessageActivity.class));
			}
		});
		feedbackView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context,
						FeedbackActivity.class));
			}
		});
		settingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sp = context.getApplicationContext()
						.getSharedPreferences("config", 0);
				sp.edit().putBoolean("isLogin", false).commit();
				GlobalConfig.isLogin = false;
				updateUI();
			}
		});
		all_order_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(),OrderActivity.class);
				intent.putExtra("index", 0);
				startActivity(intent);
			}
		});
		
		wait_pay_order_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(),OrderActivity.class);
				intent.putExtra("index", 1);
				startActivity(intent);
			}
		});
		check_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(),OrderActivity.class);
				intent.putExtra("index", 2);
				startActivity(intent);
			}
		});
		rl_refund.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(),OrderActivity.class);
				intent.putExtra("index", 3);
				startActivity(intent);
			}
		});
		
		
		rlAbout.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(context,AboutUsActivity.class));
			}
		});
	}

	public void updateUI() {
		if (context == null || !isFinished) {
			return;
		}
		SharedPreferences sp = context.getApplicationContext()
				.getSharedPreferences("config", 0);
		boolean isLogin = sp.getBoolean("isLogin", false);
		if (isLogin) {
			ll_login.setVisibility(View.GONE);
			ll_userinfo.setVisibility(View.VISIBLE);
			updateUserInfo();
		} else {
			ll_login.setVisibility(View.VISIBLE);
			ll_userinfo.setVisibility(View.GONE);
		}
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

	@OnClick(R.id.btn_login)
	public void login(View v) {
		String url = URLString.URL_DOMAIN + URLString.URL_LOGIN;
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", etUserName.getText().toString()
				.trim());
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
						Toast.makeText(context, "网络连接错误！请重试", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						;
						hideCustomProgressDialog();
						try {
							Log.i("arg0", "arg0: " + arg0.result);
							JSONObject obj = new JSONObject(arg0.result);

							String result = new String(obj
									.getString("resultCode"));
							if (result.equals("0000")) {
								SharedPreferences sp = context
										.getApplicationContext()
										.getSharedPreferences("config", 0);
								Editor editor = sp.edit();
								editor.putBoolean("isLogin", true);
								editor.putString("username", etUserName
										.getText().toString().trim());
								editor.putString("password", etPwd.getText()
										.toString().trim());
								editor.commit();
								updateUI();
								GlobalConfig.isLogin = true;
								GlobalConfig.username = etUserName.getText()
										.toString().trim();
								tv_nickName.setText(GlobalConfig.username);
							} else {

								Toast.makeText(context, "用户名或密码错误!", 0).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
