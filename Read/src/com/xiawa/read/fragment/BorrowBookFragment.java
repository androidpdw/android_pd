package com.xiawa.read.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiawa.read.R;
import com.xiawa.read.activity.BaseActivity;
import com.xiawa.read.activity.BookRankActivity;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.bean.OrderBean;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BorrowBookFragment extends Fragment
{
	private ListView lvContent;
	private List<OrderBean> mOrders = new ArrayList<OrderBean>();

	public BorrowBookFragment(List<OrderBean> mOrders)
	{
		super();
		this.mOrders = mOrders;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// ，因为每一个View只能有一个父view即parentView
		View view = inflater.inflate(R.layout.fragment_borrow_book, null);
		lvContent = (ListView) view.findViewById(R.id.lv_content);

		return view;
	}

	class BorrowBookAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return mOrders.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return mOrders.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			OrderBean order = mOrders.get(position);
			if (convertView == null)
			{
				convertView = View.inflate(getContext(),
						R.layout.item_book_rank, null);
				holder = new ViewHolder();
//				holder.title = (TextView) convertView
//						.findViewById(R.id.tv_book_title);
//				holder.author = (TextView) convertView
//						.findViewById(R.id.tv_book_author);
//				holder.publisher = (TextView) convertView
//						.findViewById(R.id.tv_book_publisher);
//				holder.desc = (TextView) convertView
//						.findViewById(R.id.tv_book_desc);
//				holder.index = (TextView) convertView
//						.findViewById(R.id.tv_book_index);
//				holder.cover = (ImageView) convertView
//						.findViewById(R.id.iv_book_cover);
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}
//			holder.title.setText(book.bookname);
//			holder.author.setText(book.author);
//			holder.publisher.setText(book.publisher);
//			holder.desc.setText(book.text);
//			BitmapUtils bitmapUtils = new BitmapUtils(BookRankActivity.this);
//			try
//			{
//				String url = URLEncoder.encode(book.coverpic, "utf-8");
//				bitmapUtils.display(holder.cover, COVER_PIC_URL + url);
//				holder.index.setText((position+1)+"");
//			} catch (UnsupportedEncodingException e)
//			{
//				e.printStackTrace();
//			}
//			
			return convertView;
		}
	}

	class ViewHolder
	{
		TextView adim;
		TextView userName;
		TextView orderId;
		TextView bookName;
		TextView bookNum;
		TextView bookPrice;
		TextView isPaied;
		TextView isChecked;
		//是否退款
		TextView isDrawbacked;
		

	}
}
