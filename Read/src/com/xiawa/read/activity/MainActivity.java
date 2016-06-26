package com.xiawa.read.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.lidroid.xutils.ViewUtils;
import com.xiawa.read.R;
import com.xiawa.read.fragment.FindFragment;
import com.xiawa.read.fragment.HomeFragment;
import com.xiawa.read.fragment.MyFragment;

public class MainActivity extends FragmentActivity implements
		ViewPager.OnPageChangeListener, View.OnClickListener {
	// @ViewInject(R.id.iv_user)
	// private ImageView ivUser;
	private ViewPager vpContainer;
	private FragmentPagerAdapter mAdapter;
	private String[] mTitles = new String[] { "1", "2", "3", "4" };
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private ImageButton ibHome;
	private ImageButton ibBook;
	private ImageButton ibMy;

	private MyFragment myFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		initData();
		initUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		myFragment.updateUI();
	}

	private void initData() {
		mTabs.add(new HomeFragment());
		mTabs.add(new FindFragment());
		myFragment = new MyFragment(this);
		mTabs.add(myFragment);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}
		};
		initTabIndicator();

	}

	private void initTabIndicator() {

	}

	private void initUI() {
		vpContainer = (ViewPager) findViewById(R.id.vp_container);
		vpContainer.addOnPageChangeListener(this);
		vpContainer.setAdapter(mAdapter);
		vpContainer.setOffscreenPageLimit(3);
		// ivUser.setOnClickListener(this);
		ibHome = (ImageButton) findViewById(R.id.tab_image_1);
		ibHome.setSelected(true);
		ibBook = (ImageButton) findViewById(R.id.tab_image_2);
		ibMy = (ImageButton) findViewById(R.id.tab_image_3);
		ibHome.setOnClickListener(this);
		ibBook.setOnClickListener(this);
		ibMy.setOnClickListener(this);
	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.tab_image_1:
			changeTab(1);
			break;
		case R.id.tab_image_2:
			changeTab(2);
			break;
		case R.id.tab_image_3:
			changeTab(3);
			// startActivity(new Intent(this,LoginActivity.class));
			break;
		// case R.id.iv_user:
		// startActivity(new Intent(this, LoginActivity.class));
		// break;
		}
	}

	public void changeTab(int tab) {
		vpContainer.setCurrentItem(tab - 1, false);
		switch (tab) {
		case 1:
			ibHome.setSelected(true);
			ibBook.setSelected(false);
			ibMy.setSelected(false);
			break;
		case 2:
			ibHome.setSelected(false);
			ibBook.setSelected(true);
			ibMy.setSelected(false);
			break;
		case 3:
			ibHome.setSelected(false);
			ibBook.setSelected(false);
			ibMy.setSelected(true);
			break;

		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
