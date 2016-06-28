package com.xiawa.read.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiawa.read.R;
import com.xiawa.read.bean.OrderBean;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.fragment.OrderFragment;
import com.xiawa.read.view.ViewPagerIndicator;

public class OrderActivity extends FragmentActivity {
	
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	
	
	
	private List<String> mDatas = Arrays.asList("全部", "待付款", "审核中");
	
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;
	
	private List<OrderBean> mAllOrderList;
	private List<OrderBean> mPayOrderList;
	private List<OrderBean> mCheckOrderList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify);
		getOrder();
		initView();
		mIndicator.setTabItemTitles(mDatas);
	}
	
	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
	}
	
	private void initDatas()
	{

		
		for (int i = 0 ; i<mDatas.size();i++)
		{
			List<OrderBean> data;
			if (i==0) {
				data = mAllOrderList;
			} else if (i == 1) {
				data = mPayOrderList;
			} else {
				data = mCheckOrderList;
			}
			OrderFragment fragment = OrderFragment.newInstance(data);
			mTabContents.add(fragment);
		}
		

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return mTabContents.get(position);
			}
		};
	}
	
	private void getOrder() {
		mAllOrderList = new ArrayList<OrderBean>();
		mPayOrderList = new ArrayList<OrderBean>();
		mCheckOrderList = new ArrayList<OrderBean>();
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("loginname", GlobalConfig.username);
		utils.send(HttpMethod.POST, "http://www.piaoduwang.com/mobile/app/shopping_cartList.php",params, new RequestCallBack<String>()
		{

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				System.out.println(arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				try {
					System.out.println(arg0.result);
					mAllOrderList = JSON.parseArray(arg0.result,
							OrderBean.class);
					
					for (int i = 0; i < mAllOrderList.size(); i++)
					{
						OrderBean bean = mAllOrderList.get(i);
						if (bean.status.equals("0")) {
							if (bean.paid.equals("0")) {
								mPayOrderList.add(bean);
							} else {
								mCheckOrderList.add(bean);
							}
						}
					}
					initDatas();
					int index = getIntent().getIntExtra("index", 0);
					//设置Tab上的标题
					
					mViewPager.setAdapter(mAdapter);
					//设置关联的ViewPager
					mIndicator.setViewPager(mViewPager,0);
					mViewPager.setCurrentItem(index);
				} catch (Exception e) {
				}
			}
		});
	}
}
