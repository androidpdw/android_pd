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

	private List<String> mDatas = Arrays.asList("全部", "待付款", "可还书","可退款");

	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;

	public static List<OrderBean> mAllOrderList;
	public static List<OrderBean> mPayOrderList;
	public static List<OrderBean> mReturnBookList;
	public static List<OrderBean> mRefundList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify);
		getOrder();
		initView();
		mIndicator.setTabItemTitles(mDatas);
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
	}

	private void initDatas() {
		mTabContents.clear();
		for (int i = 0; i < mDatas.size(); i++) {
			List<OrderBean> data;
			int type = 0;
			if (i == 0) {
				data = mAllOrderList;
				type = 0;
			} else if (i == 1) {
				data = mPayOrderList;
				type = 1;
			} else if (i == 2){
				data = mReturnBookList;
				type = 2;
			} else {
				data = mRefundList;
				type = 3;
			}
			OrderFragment fragment = OrderFragment.newInstance(data,type);
			mTabContents.add(fragment);
			
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabContents.get(position);
			}
		};
	}
	
	public void cancelOrder(OrderBean ob,int type){
		((OrderFragment)mTabContents.get(0)).remove(ob);
		((OrderFragment)mTabContents.get(1)).remove(ob);
	}
	
	public void refreshUI() {
		initDatas();
		mAdapter.notifyDataSetChanged();
	}

	private void getOrder() {
		mAllOrderList = new ArrayList<OrderBean>();
		mPayOrderList = new ArrayList<OrderBean>();
		mReturnBookList = new ArrayList<OrderBean>();
		mRefundList = new ArrayList<OrderBean>();
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("loginname", GlobalConfig.username);
		utils.send(HttpMethod.POST,
				"http://www.piaoduwang.com/mobile/app/shopping_cartList.php",
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							System.out.println(arg0.result);
							mAllOrderList = JSON.parseArray(arg0.result,
									OrderBean.class);
							for (int i = 0; i < mAllOrderList.size(); i++) {
								OrderBean bean = mAllOrderList.get(i);
								if (bean.status.equals("0")) {
									if (bean.paid.equals("0")) {
										mPayOrderList.add(bean);
									}
								} else if(bean.status.equals("1")) {
									mReturnBookList.add(bean);
								} else if (bean.status.equals("2")) {
									mRefundList.add(bean);
								}
							}
							initDatas();
							int index = getIntent().getIntExtra("index", 0);
							// 设置Tab上的标题

							mViewPager.setAdapter(mAdapter);
							// 设置关联的ViewPager
							mIndicator.setViewPager(mViewPager, 0);
							mViewPager.setCurrentItem(index);
						} catch (Exception e) {
						}
					}
				});
	}

	public void returnBook(OrderBean ob, int pos) {
		for (int i = 0; i < mAllOrderList.size(); i++) {
			if (mAllOrderList.get(i).ordercode.equals(ob.ordercode)) {
				mAllOrderList.get(i).status = "3";
			}
		}
		((OrderFragment)mTabContents.get(0)).returnBook(ob);
		((OrderFragment)mTabContents.get(2)).remove(ob);
	}

	public void refund(OrderBean ob, int pos) {
		mAllOrderList.get(pos).status = "3";
		((OrderFragment)mTabContents.get(0)).refund(ob);
		((OrderFragment)mTabContents.get(3)).remove(ob);
	}
}
