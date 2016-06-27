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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiawa.read.R;
import com.xiawa.read.activity.BookDetailActivity;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.utils.URLString;

public class VpSimpleFragment extends Fragment
{
	public static final String BUNDLE_TITLE = "title";
	private String mTitle = "DefaultValue";
	
	private List<BookRankItem> mBookList1;
	private List<BookRankItem> mBookList2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Bundle arguments = getArguments();
		if (arguments != null)
		{
			mTitle = arguments.getString(BUNDLE_TITLE);
		}
		GridView gridView = new GridView(getContext());
		gridView.setNumColumns(2);
		if (mTitle.equals("小学低年级")) {
			getData(gridView,1);
		} else if(mTitle.equals("小学中年级")||mTitle.equals("小学高年级")){
			getData(gridView,2);
		} else {
			TextView tv = new TextView(getActivity());
			tv.setText("暂无");
			tv.setGravity(Gravity.CENTER);
			return tv;
		}
		return gridView;
	}

	private void getData(final GridView gv,final int i) {
		String url = URLString.URL_DOMAIN + URLString.URL_CLASSIFYBOOK;
		mBookList1 = new ArrayList<BookRankItem>();
		mBookList2 = new ArrayList<BookRankItem>();
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("type", i+"");
		utils.send(HttpMethod.POST, url,params, new RequestCallBack<String>()
		{

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				try {
					System.out.println(arg0.result);
					if (i==1) {
						mBookList1 = JSON.parseArray(arg0.result,
								BookRankItem.class);
					} else {
					mBookList2 = JSON.parseArray(arg0.result,
							BookRankItem.class);
					}
					gv.setAdapter(new GridViewBookAdpter(i));
					gv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(getContext(),BookDetailActivity.class);
							intent.putExtra("BookItem", (BookRankItem)gv.getAdapter().getItem(position));
							startActivity(intent);
						}
					});
				} catch (Exception e) {
				}
			}
		});
	}

	public static VpSimpleFragment newInstance(String title)
	{
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		VpSimpleFragment fragment = new VpSimpleFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	class GridViewBookAdpter extends BaseAdapter
	{
		public static final String COVER_PIC_URL = "http://www.piaoduwang.com/pd_images/pd_BookPick/";
		public  List<BookRankItem> bookList;
		public GridViewBookAdpter(int i) {
			if (i==1) {
				bookList = mBookList1;
			} else {
				bookList = mBookList2;
			}
		}

		@Override
		public int getCount()
		{
			return bookList.size();
		}

		@Override
		public BookRankItem getItem(int position)
		{
			return bookList.get(position);
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
			BookRankItem book = bookList.get(position);
			if (convertView == null)
			{
				convertView = View.inflate(getContext(),
						R.layout.item_gridview_book, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_name);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
				holder.cover.setScaleType(ScaleType.FIT_XY);
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(book.bookname);
			BitmapUtils bitmapUtils = new BitmapUtils(getContext());
			try
			{
				String url = URLEncoder.encode(book.coverpic, "utf-8");
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

	}
}
