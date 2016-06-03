package com.xiawa.read.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiawa.read.R;
import com.xiawa.read.activity.LoginActivity;
import com.xiawa.read.domain.GlobalConfig;

/**
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment
{
	@OnClick(R.id.nickName_tv)
	public void nickNameOnClick(View v)
	{
		login();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		ViewUtils.inject(this, view);
		init();
		return view;
	}

	private void init()
	{
	}

	private void login()
	{
		if (!GlobalConfig.isLogin)
		{// 未登录
			startActivity(new Intent(getContext(),LoginActivity.class));
		}
	}
}
