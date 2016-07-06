package com.xiawa.read.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.bean.CollectionsItem;

/**
 * 收藏室
 * 
 * @author cpzzzz
 *
 */
public class CollectionActivity extends BaseActivity
{
	// ---start--用BookRank数据模拟
	public static final String BOOK_RANK_URL = "http://www.piaoduwang.com/mobile/app/bookRankForAPP.php";
	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/mobile/images/up_cover_0619/";
	protected static final int UPDATA_LIST = 0;
	// ---end--用BookRank数据模拟
	List<BookRankItem> bookRankItems;
	// 更新总价格
	protected static final int UPDATE_TOTAL_PRICE = 1;
	// 选择所有按钮
	@ViewInject(R.id.cb_select_all)
	private CheckBox cbSelectAll;
	// 合计金额
	@ViewInject(R.id.tv_total_price)
	private TextView tvTotalPrice;
	// 收藏室图书ListView
	@ViewInject(R.id.lv_collections)
	private ListView lvCollections;
	// 收藏室图书列表
	private List<CollectionsItem> mCollectionsItems;
	private float mTotalPrice = 0;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case UPDATA_LIST:
				lvCollections.setAdapter(new CollectionsAdapter());
				lvCollections.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);
						intent.putExtra("BookItem", bookRankItems.get(position));
						startActivity(intent);
					}
				});
				mHandler.sendEmptyMessage(UPDATE_TOTAL_PRICE);
				break;
			case UPDATE_TOTAL_PRICE:
				updateTotalPrice();
				tvTotalPrice.setText("￥" + mTotalPrice);
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		ViewUtils.inject(this);
		setHeaderTitle("收藏室");
		initData();
		initViews();
	}

	private void initViews()
	{
		// TODO Auto-generated method stub
		cbSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				for (int i = 0; i < mCollectionsItems.size(); i++)
					mCollectionsItems.get(i).isCheck = isChecked;
				mHandler.sendEmptyMessage(UPDATA_LIST);
			}
		});
	}

	/**
	 * 计算订单总价格
	 */
	private void updateTotalPrice()
	{
		mTotalPrice = 0;
		for (CollectionsItem i : mCollectionsItems)
		{
			if (i.isCheck)
			{
				float price = Float.parseFloat(i.price);
				mTotalPrice += price;
			}
		}

	}

	/**
	 * 初始化数据，暂用BookRank数据
	 */
	private void initData()
	{
		mCollectionsItems = new ArrayList<CollectionsItem>();
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
				try
				{
					bookRankItems = JSON.parseArray(
							arg0.result, BookRankItem.class);

					for (int i = 0; i < bookRankItems.size(); i++)
					{
						BookRankItem item = bookRankItems.get(i);
						CollectionsItem ci = new CollectionsItem();
						ci.bookname = item.bookname;
						ci.coverpic = item.coverpic;
						ci.price = "1" + i + ".0";
						ci.author = item.author;
//						ci.price = item.price;
						ci.isCheck = Math.random() > 0.5 ? true : false;
						mCollectionsItems.add(ci);
					}
					mHandler.sendEmptyMessage(UPDATA_LIST);
				} catch (Exception e)
				{
				}
			}
		});
	}

	/**
	 * 提交按钮事件
	 * 
	 * @param view
	 */
	public void onSubmitClick(View view)
	{
		Intent intent = new Intent(getApplicationContext(),
				AddressActivity.class);
		ArrayList<BookRankItem> items = new ArrayList<BookRankItem>();
		for (int i = 0; i < mCollectionsItems.size(); i++)
		{
			CollectionsItem item = mCollectionsItems.get(i);
			if (item.isCheck)
			{	
				BookRankItem bookRankItem = new BookRankItem();
				bookRankItem.price=item.price;
				bookRankItem.coverpic=item.coverpic;
				bookRankItem.bookname=item.bookname;
				
				items.add(bookRankItem);
			}
		}

		Bundle bu = new Bundle();
		bu.putSerializable("BOOKS", items);
		intent.putExtras(bu);
		startActivity(intent);
	}

	class CollectionsAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mCollectionsItems.size();
		}

		@Override
		public Object getItem(int position)
		{
			return mCollectionsItems.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent)
		{
			ViewHolder holder;
			CollectionsItem collectionsItem = mCollectionsItems.get(position);
			System.out.println("mCollectionsItems current pos " + position);
			if (convertView == null)
			{
				convertView = View.inflate(CollectionActivity.this,
						R.layout.item_colletion_activity, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_name);
				holder.price = (TextView) convertView
						.findViewById(R.id.tv_book_price);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
				holder.isCheck = (CheckBox) convertView
						.findViewById(R.id.cb_isCheck);
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(collectionsItem.bookname);
//			holder.price.setText("￥" + collectionsItem.price);
			holder.price.setText(collectionsItem.author);
			holder.isCheck.setChecked(collectionsItem.isCheck);
			holder.isCheck.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mCollectionsItems.get(position).isCheck = ((CheckBox) v)
							.isChecked();
					mHandler.sendEmptyMessage(UPDATE_TOTAL_PRICE);
				}
			});

			BitmapUtils bitmapUtils = new BitmapUtils(CollectionActivity.this);
			try
			{
				String url = URLEncoder.encode(collectionsItem.coverpic,
						"utf-8");
				bitmapUtils.display(holder.cover, COVER_PIC_URL + url);
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
		ImageView cover;
		TextView price;
		CheckBox isCheck;
	}

}
