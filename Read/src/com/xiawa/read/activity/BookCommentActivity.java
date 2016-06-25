package com.xiawa.read.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiawa.read.R;
import com.xiawa.read.bean.BookItem;

public class BookCommentActivity extends BaseActivity {

	private ListView listView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_comment);
		setHeaderTitle("观后感");
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new BookAdapter());
	};

	class BookAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public BookItem getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_comment, null);
			}
			TextView tv_comment = (TextView) view.findViewById(R.id.tv_comment);
			return view;
		}

	}
}
