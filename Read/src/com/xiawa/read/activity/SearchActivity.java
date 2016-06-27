package com.xiawa.read.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.utils.URLString;

public class SearchActivity extends BaseActivity {

	public static final String BOOK_RANK_URL = "http://www.piaoduwang.com/mobile/app/bookRankForAPP.php";
	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/pd_images/pd_BookPick/";

	private String word;

	@ViewInject(R.id.listView)
	private ListView listView;

	@ViewInject(R.id.tv_loading)
	private TextView tv_loading;

	@ViewInject(R.id.tv_nothing)
	private TextView tv_nothing;

	private List<BookRankItem> mBookList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setHeaderTitle("搜索结果");
		ViewUtils.inject(this);
		word = getIntent().getStringExtra("word");
		loadData();
	}

	private void loadData() {
		String url = URLString.URL_DOMAIN + URLString.URL_SEARCH;
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("word", word);
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				tv_loading.setVisibility(View.GONE);
				tv_nothing.setVisibility(View.VISIBLE);
				System.out.println("Failure" + arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println("success");
				mBookList = JSON.parseArray(arg0.result, BookRankItem.class);
				deleteSameObj();
				tv_loading.setVisibility(View.GONE);
				if (mBookList.size() == 0) {
					tv_nothing.setVisibility(View.VISIBLE);
				} else {
					listView.setAdapter(new BookListAdapter());
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);
							intent.putExtra("BookItem", mBookList.get(position));
							startActivity(intent);
						}
					});
				}
			}

		});
	}

	/** 模糊搜索，删去相同的 */
	private void deleteSameObj() {
		for (int i = 0; i < mBookList.size(); i++) {
			String bookName = mBookList.get(i).bookname;
			for (int j = i + 1; j < mBookList.size();) {
				if (bookName.equals(mBookList.get(j).bookname)) {
					mBookList.remove(j);
				} else {
					j++;
				}
			}
		}
	}

	class BookListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mBookList.size();
		}

		@Override
		public Object getItem(int position) {
			return mBookList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			BookRankItem book = mBookList.get(position);
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_book_rank, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_title);
				holder.author = (TextView) convertView
						.findViewById(R.id.tv_book_author);
				holder.publisher = (TextView) convertView
						.findViewById(R.id.tv_book_publisher);
				holder.desc = (TextView) convertView
						.findViewById(R.id.tv_book_desc);
				holder.index = (TextView) convertView
						.findViewById(R.id.tv_book_index);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
				holder.cover.setScaleType(ScaleType.FIT_XY);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(book.bookname);
			holder.author.setText(book.author);
			holder.publisher.setText(book.publisher);
			holder.desc.setText(book.text);
			BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
			try {
				String url = URLEncoder.encode(book.coverpic, "utf-8");
				System.out.println("picture=" + COVER_PIC_URL + url);
				bitmapUtils.display(holder.cover, COVER_PIC_URL + url);
				holder.index.setText((position + 1) + "");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	class ViewHolder {
		TextView title;
		TextView author;
		TextView publisher;
		TextView desc;
		TextView index;
		ImageView cover;
		
	}
}
