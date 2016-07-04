package com.xiawa.read.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.bean.SiteBean;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.utils.URLString;

public class AddressActivity extends BaseActivity {

	@ViewInject(R.id.listView)
	private ListView listView;
	private Bundle extras;
	BookRankItem item;

	private ArrayList<SiteBean> siteList;

	@ViewInject(R.id.tv_loading)
	private TextView tv_loading;
	@ViewInject(R.id.tv_no_site)
	private TextView tv_no_site;

	private boolean isSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		Intent intent = getIntent();
		extras = intent.getExtras();
		if (extras != null) {
			item = ((ArrayList<BookRankItem>) extras.getSerializable("BOOKS"))
					.get(0);
			isSite = extras.getBoolean("isSite", false);
		}
		if (isSite) {
			setHeaderTitle("附近站点");
		} else {
			setHeaderTitle("选择借书地址");
		}
		ViewUtils.inject(this);
		getLocation();
		if (!isSite) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					/*
					 * Intent intent = new
					 * Intent(getApplicationContext(),PayActivity.class);
					 * intent.putExtra("url",
					 * "http://www.piaoduwang.com/mobile/alipay/index.php");
					 */
					Intent intent2 = new Intent(getApplicationContext(),
							SubmitOrderActivity.class);
					extras.putSerializable("SITE", siteList.get(position));
					intent2.putExtras(extras);
					startActivity(intent2);
				}
			});
		}
	}

	private void getLocation() {
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("loginname", GlobalConfig.username);
		params.addBodyParameter("isbn", item.isbn);
		utils.send(HttpMethod.POST, URLString.URL_DOMAIN
				+ URLString.URL_GET_BOOKLOCATION, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.e("hsq", arg1);
						tv_no_site.setVisibility(View.VISIBLE);
						tv_loading.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							tv_loading.setVisibility(View.GONE);
							siteList = (ArrayList<SiteBean>) JSON.parseArray(
									arg0.result, SiteBean.class);
							listView.setAdapter(new BookAdapter());
							if (siteList.size() == 0) {
								tv_no_site.setVisibility(View.VISIBLE);
							}
						} catch (Exception e) {
						}
					}
				});
	}

	class BookAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return siteList.size();
		}

		@Override
		public SiteBean getItem(int position) {
			return siteList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.item_address, null);
			}
			SiteBean site = siteList.get(position);
			TextView tv_site_name = (TextView) view
					.findViewById(R.id.tv_site_name);
			TextView tv_site_address = (TextView) view
					.findViewById(R.id.tv_site_address);
			TextView tv_book_id = (TextView) view.findViewById(R.id.tv_book_id);
			TextView tv_book_name = (TextView) view
					.findViewById(R.id.tv_book_name);
			tv_site_name.setText(site.nickname);
			tv_site_address.setText(site.address);
			tv_book_id.setText(site.bookcode);
			tv_book_name.setText(site.bookname);
			return view;
		}

	}
}
