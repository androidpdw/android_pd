package com.xiawa.read.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xiawa.read.R;
import com.xiawa.read.activity.PayActivity;
import com.xiawa.read.activity.SubmitOrderActivity;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.bean.OrderBean;

public class OrderFragment extends Fragment {
	public static final String BUNDLE_TITLE = "title";
	private String mTitle = "DefaultValue";

	private List<OrderBean> mDatas;

	public OrderFragment(List<OrderBean> data) {
		mDatas = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			mTitle = arguments.getString(BUNDLE_TITLE);
		}
		if (mDatas.size() == 0) {
			TextView tv = new TextView(getActivity());
			tv.setText("暂无");
			tv.setGravity(Gravity.CENTER);
			return tv;
		}
		ListView listView = new ListView(getContext());
		listView.setFocusable(false);
		listView.setAdapter(new GridViewBookAdpter());
		return listView;
	}

	public static OrderFragment newInstance(List<OrderBean> data) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, "");
		OrderFragment fragment = new OrderFragment(data);
		fragment.setArguments(bundle);
		return fragment;
	}

	class GridViewBookAdpter extends BaseAdapter {
		public static final String COVER_PIC_URL = "http://www.piaoduwang.com/pd_images/pd_BookPick/";

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public OrderBean getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView( int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			final OrderBean book = mDatas.get(position);
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.item_order,
						null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_title);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
				holder.cover.setScaleType(ScaleType.FIT_XY);
				holder.price = (TextView) convertView
						.findViewById(R.id.tv_price);
				holder.tv_ordercode = (TextView) convertView
						.findViewById(R.id.tv_ordercode);
				holder.btn_check = (Button) convertView
						.findViewById(R.id.btn_check);
				holder.btn_pay = (Button) convertView
						.findViewById(R.id.btn_pay);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(book.bookname);
			holder.price.setText("¥ " + book.account);
			holder.tv_ordercode.setText(book.ordercode);
			if (book.status.equals("0") && book.paid.equals("0")) {
				holder.btn_check.setVisibility(View.GONE);
				holder.btn_pay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getContext(),
								SubmitOrderActivity.class);
						ArrayList<BookRankItem> items = new ArrayList<BookRankItem>();
						BookRankItem bookRankItem = new BookRankItem();
						bookRankItem.bookname=book.bookname;
						bookRankItem.price=book.price;
						bookRankItem.coverpic=book.coverpic;
						items.add(bookRankItem);
						Bundle bu = new Bundle();
						bu.putSerializable("BOOKS", items);
						intent.putExtras(bu);
						startActivity(intent);
					}
				});
			} else {
				holder.btn_pay.setVisibility(View.GONE);

			}
			BitmapUtils bitmapUtils = new BitmapUtils(getContext());
			try {
				String url = URLEncoder.encode(book.coverpic, "utf-8");
				bitmapUtils.display(holder.cover, COVER_PIC_URL + url);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	class ViewHolder {
		TextView title;
		ImageView cover;
		TextView price;
		TextView tv_ordercode;
		Button btn_pay;
		Button btn_check;
	}
}
