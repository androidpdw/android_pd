package com.xiawa.read.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.fragment.BorrowBookFragment;
import com.xiawa.read.fragment.VpSimpleFragment;
import com.xiawa.read.view.ViewPagerIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

/**
 * 服务站管理借书Activity
 * 
 * @author CPZ
 *
 */
public class BorrowBookActivity extends FragmentActivity
{

	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private List<String> mDatas = Arrays.asList("全部", "未处理", "已借阅");

	@ViewInject(R.id.id_indicator)
	private ViewPagerIndicator mIndicator;
	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_borrow_book);
		ViewUtils.inject(this);
		initUI();
	}

	private void initUI()
	{
		for (String data : mDatas)
		{
			BorrowBookFragment fragment = new BorrowBookFragment(null);
			mTabContents.add(fragment);
		}

		mIndicator.setTabItemTitles(mDatas);

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
		mViewPager.setAdapter(mAdapter);
		// 设置关联的ViewPager
		mIndicator.setViewPager(mViewPager, 0);
	}
}
