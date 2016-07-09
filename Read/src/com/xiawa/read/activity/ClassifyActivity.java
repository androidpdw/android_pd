package com.xiawa.read.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xiawa.read.R;
import com.xiawa.read.fragment.VpSimpleFragment;
import com.xiawa.read.view.ViewPagerIndicator;

public class ClassifyActivity extends FragmentActivity {
	
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	
	private List<String> mDatas = Arrays.asList("小学低年级", "小学中年级", "小学高年级", "初中");
	
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify);
		initView();
		initDatas();
		//设置Tab上的标题
		mIndicator.setTabItemTitles(mDatas);
		mViewPager.setAdapter(mAdapter);
		//设置关联的ViewPager
		mIndicator.setViewPager(mViewPager,0);
		int index = getIntent().getIntExtra("index", 0);
		mViewPager.setCurrentItem(index);
	}
	
	

	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
	}
	
	private void initDatas()
	{

		for (String data : mDatas)
		{
			VpSimpleFragment fragment = VpSimpleFragment.newInstance(data);
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
}
