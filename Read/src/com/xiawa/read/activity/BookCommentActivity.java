package com.xiawa.read.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.bean.BookReviewBean;

public class BookCommentActivity extends BaseActivity {

	private ListView listView;
	private BookRankItem bookItem;
	private ArrayList<BookReviewBean> list = new ArrayList<BookReviewBean>();
	private BookAdapter adapter;
	
	private TextView tv_no_comment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_comment);
		setHeaderTitle("观后感");
		bookItem = (BookRankItem) getIntent().getSerializableExtra("bookItem");
		adapter = new BookAdapter();
		getReview();
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		tv_no_comment = (TextView) findViewById(R.id.tv_no_comment);
	};

	private void getReview() {
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("isbn",bookItem.isbn);
		utils.send(HttpMethod.POST, "http://www.piaoduwang.com/mobile/app/bookReview.php",params,
				new RequestCallBack<String>()
				{

					@Override
					public void onFailure(HttpException arg0, String arg1)
					{
						Log.e("hsq", arg1);
						tv_no_comment.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0)
					{
						try {
							list = (ArrayList<BookReviewBean>) JSON.parseArray(arg0.result, BookReviewBean.class);
							if (list.size() == 0) {
								tv_no_comment.setVisibility(View.VISIBLE);
							} else {
								adapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
						}
					}
				});
	}

	class BookAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public BookReviewBean getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_comment, null);
			}
			BookReviewBean bean = list.get(position);
			TextView tv_comment = (TextView) view.findViewById(R.id.tv_comment);
			TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_comment.setText(bean.tips);
			tv_username.setText(bean.loginname);
			tv_time.setText(bean.tdate);
			return view;
		}

	}
}
