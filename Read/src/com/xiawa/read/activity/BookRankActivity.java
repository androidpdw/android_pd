package com.xiawa.read.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookRankItem;

public class BookRankActivity extends BaseActivity
{
	public static final String BOOK_RANK_URL = "http://www.piaoduwang.com/mobile/app/bookRankForAPP.php";
	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/mobile/images/up_cover_0619/";
	protected static final int UPDATA_LIST = 0;
	private List<BookRankItem> mBookList;
	private ListView lvBookList;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			int what = msg.what;
			switch (what)
			{
			case UPDATA_LIST:
				lvBookList.setAdapter(new BookListAdapter());
				lvBookList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);
						intent.putExtra("BookItem", mBookList.get(position));
						startActivity(intent);
					}
				});
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_rank);
		setHeaderTitle("飘读榜");
		lvBookList = (ListView) findViewById(R.id.lv_book_list);
		initData();
	}

	private void initData()
	{
		mBookList = new ArrayList<BookRankItem>();
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, BOOK_RANK_URL, new RequestCallBack<String>()
		{

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				try {
					List<BookRankItem> bookRankItems = JSON.parseArray(arg0.result,
							BookRankItem.class);
					
					for (int i = 0; i < bookRankItems.size(); i++)
					{
						mBookList.add(bookRankItems.get(i));
					}
					mHandler.sendEmptyMessage(UPDATA_LIST);
				} catch (Exception e) {
				}
			}
		});
	}

	class BookListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return mBookList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return mBookList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			BookRankItem book = mBookList.get(position);
			if (convertView == null)
			{
				convertView = View.inflate(BookRankActivity.this,
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
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(book.bookname);
			holder.author.setText(book.author);
			holder.publisher.setText(book.publisher);
			holder.desc.setText(book.text);
			BitmapUtils bitmapUtils = new BitmapUtils(BookRankActivity.this);
			try
			{
				String url = URLEncoder.encode(book.coverpic, "utf-8");
				System.out.println("picture="+COVER_PIC_URL+url);
				bitmapUtils.display(holder.cover, COVER_PIC_URL + url);
				holder.index.setText((position+1)+"");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			
			return convertView;
		}
	}

	class ViewHolder
	{
		TextView title;
		TextView author;
		TextView publisher;
		TextView desc;
		TextView index;
		ImageView cover;

	}

}
