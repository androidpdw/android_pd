package com.xiawa.read.fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.activity.BorrowBookActivity;
import com.xiawa.read.activity.ReturnBookActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
/**
 * 服务站登录
 * @author CPZ
 *
 */
public class AdminFragment extends Fragment implements OnClickListener
{
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_admin, container,false);
		ViewUtils.inject(this,view);
		initUI(view);
		return view;
	}


	private void initUI(View view)
	{
		view.findViewById(R.id.ll_fun).setOnClickListener(this);
		view.findViewById(R.id.ll_borrow_book).setOnClickListener(this);
		view.findViewById(R.id.ll_return_book).setOnClickListener(this);
		view.findViewById(R.id.ll_donate).setOnClickListener(this);
	}


	@Override
	public void onClick(View v)
	{
		
		switch (v.getId())
		{
		case R.id.ll_fun://功能
			Toast.makeText(getContext(), "功能", Toast.LENGTH_SHORT).show();
			break;
		case R.id.ll_borrow_book://图书借阅
			startActivity(new Intent(getContext(),BorrowBookActivity.class));
			break;
		case R.id.ll_return_book://图书归还
			startActivity(new Intent(getContext(),ReturnBookActivity.class));
			break;
		case R.id.ll_donate://捐赠
			Toast.makeText(getContext(), "捐赠", Toast.LENGTH_SHORT).show();
			break;

		}
	}
}
