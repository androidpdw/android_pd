package com.xiawa.read.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiawa.read.R;
import com.xiawa.read.activity.BookDetailActivity;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.view.MyGridView;

/**
 * Activities that contain this fragment must implement the
 * {@link FindFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment
{
	
	public static final String BOOK_RANK_URL = "http://www.piaoduwang.com/mobile/app/bookRankForAPP.php";
	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/mobile/images/up_cover_0619/";

	private List<BookRankItem> mBookList;
	
	private MyGridView gvBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
    	gvBooks = new MyGridView(getContext());
    	gvBooks.setNumColumns(2);
    	initData();
    	
    	gvBooks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getContext(), BookDetailActivity.class);
				intent.putExtra("BookItem", mBookList.get(position));
				startActivity(intent);
			}
		});
    	return gvBooks;
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

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				try
				{
					mBookList = JSON.parseArray(
							arg0.result, BookRankItem.class);
					gvBooks.setAdapter(new GridViewBookAdpter());
				} catch (Exception e)
				{
				}
			}
		});
	}
    
    class GridViewBookAdpter extends BaseAdapter
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
				convertView = View.inflate(getContext(),
						R.layout.item_gridview_book, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_name);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
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
