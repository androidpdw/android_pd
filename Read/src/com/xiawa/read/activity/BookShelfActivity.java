package com.xiawa.read.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookItem;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.utils.URLString;
import com.xiawa.read.view.ViewPagerIndicator;

public class BookShelfActivity extends BaseActivity {
	
	private List<View> mTabContents = new ArrayList<View>();
	
	private List<String> mDatas = Arrays.asList("已阅读","已发表");
	
	private List<BookItem> list;
	
	@ViewInject(R.id.id_vp)
	private ViewPager mViewPager;
	@ViewInject(R.id.id_indicator)
	private ViewPagerIndicator mIndicator;
	
	private int viewCount = 2;
	
	private ViewAdapter mAdapter;
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_shelf);
		ViewUtils.inject(this);
		setHeaderTitle("我的读后感");
		
		//设置Tab上的标题
		mIndicator.setTabItemTitles(mDatas);
		mAdapter = new ViewAdapter();
		mViewPager.setAdapter(mAdapter);
		//设置关联的ViewPager
		mIndicator.setViewPager(mViewPager,0);
		int index = getIntent().getIntExtra("index", 0);
		mViewPager.setCurrentItem(index);
		tv = new TextView(getApplicationContext());
		tv.setText("暂无");
		tv.setTextColor(getResources().getColor(R.color.black));
		tv.setGravity(Gravity.CENTER);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initDatas();
	}

	private void initDatas() {
		HttpUtils utils = new HttpUtils();
		list = new ArrayList<BookItem>();
		list.clear();
		RequestParams params = new RequestParams();
		params.addBodyParameter("loginname",GlobalConfig.username);
		utils.send(HttpMethod.POST, URLString.URL_DOMAIN + URLString.URL_GET_BOOKSHELF, params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.e("hsq", arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				list = JSON.parseArray(arg0.result, BookItem.class);
				ListView lv = new ListView(getApplicationContext());
				lv.setFocusable(false);
				lv.setAdapter(new ListAdapter());
				mTabContents.add(lv);
				tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				mTabContents.add(tv);
				mAdapter.notifyDataSetChanged();
				Log.e("hsq", "onSuccess");
			}
		});
	}
	
	class ListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public BookItem getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_book_shelf, null);
			}
			BookItem item = list.get(position);
			ImageView iv_book_cover = (ImageView) view.findViewById(R.id.iv_book_cover);
			TextView tv_book_title = (TextView) view.findViewById(R.id.tv_book_title);
			TextView tv_book_author = (TextView) view.findViewById(R.id.tv_book_author);
			BitmapUtils utils = new BitmapUtils(getApplicationContext());
			utils.display(iv_book_cover, URLString.COVER_PIC_URL+ item.coverpic);
			tv_book_title.setText(item.bookname);
			tv_book_author.setText(item.author);
			view.findViewById(R.id.btn_add_review).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),AddReviewActivity.class);
					intent.putExtra("BookItem", list.get(position));
					startActivity(intent);
				}
			});
			return view;
		}
		
	}
	
	class ViewAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mTabContents.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = mTabContents.get(position);
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
