package com.xiawa.read.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiawa.read.R;
import com.xiawa.read.activity.BookArrangeActivity;
import com.xiawa.read.activity.BookDetailActivity;
import com.xiawa.read.activity.BookRankActivity;
import com.xiawa.read.activity.ClassifyActivity;
import com.xiawa.read.activity.CollectionActivity;
import com.xiawa.read.activity.FeedbackActivity;
import com.xiawa.read.activity.MainActivity;
import com.xiawa.read.activity.SearchActivity;
import com.xiawa.read.activity.SubmitOrderActivity;
import com.xiawa.read.activity.WebActivity;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.view.ImageCycleView;
import com.xiawa.read.view.MyGridView;

public class HomeFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	public static final String BOOK_RANK_URL = "http://www.piaoduwang.com/mobile/app/bookRankForAPP.php";
	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/mobile/images/up_cover_0619/";
	protected static final int UPDATA_GRID_VIEW = 0;
	private ImageCycleView mImageCycleView;
	private String[] mHomeItemImage = {
			"http://www.piaoduwang.com/mobile/images/img_main_1.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_2.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_3.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_4.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_5.jpg" };

	private List<BookRankItem> mBookList;
	private MyGridView gvBooks;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case UPDATA_GRID_VIEW:
				gvBooks.setAdapter(new GridViewBookAdpter());
				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		ViewUtils.inject(this, view);
		mImageCycleView = (ImageCycleView) view.findViewById(R.id.icv_top);
		mImageCycleView.setIndicationStyle(
				ImageCycleView.IndicationStyle.IMAGE, R.drawable.dot_blur,
				R.drawable.dot_focus, 0);
		initImageCycleView();

		gvBooks = (MyGridView) view.findViewById(R.id.gv_books);
		gvBooks.setOnItemClickListener(this);
		initData();
		view.findViewById(R.id.ll_zuowen).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(),
								BookArrangeActivity.class));
					}
				});
		view.findViewById(R.id.ll_xiaoxue).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								ClassifyActivity.class);
						intent.putExtra("index", 0);
						startActivity(intent);
					}
				});
		view.findViewById(R.id.ll_zhonggao).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								ClassifyActivity.class);
						intent.putExtra("index", 1);
						startActivity(intent);
					}
				});
		setImgButtonListener(view);

		return view;
	}

	/**
	 * GridView onItemClick
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getContext(), BookDetailActivity.class);
		intent.putExtra("BookItem", mBookList.get(position));
		startActivity(intent);
	}

	private void initData() {
		mBookList = new ArrayList<BookRankItem>();
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, BOOK_RANK_URL,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							List<BookRankItem> bookRankItems = JSON.parseArray(
									arg0.result, BookRankItem.class);

							for (int i = 0; i < bookRankItems.size(); i++) {
								mBookList.add(bookRankItems.get(i));
							}
							mHandler.sendEmptyMessage(UPDATA_GRID_VIEW);
						} catch (Exception e) {
						}
					}
				});

	}

	/**
	 * 设置主页图片按钮监听器
	 * 
	 * @param view
	 */
	private void setImgButtonListener(View view) {
		view.findViewById(R.id.ll_category).setOnClickListener(this);
		view.findViewById(R.id.ll_donate).setOnClickListener(this);
		view.findViewById(R.id.ll_drift).setOnClickListener(this);
		view.findViewById(R.id.ll_my).setOnClickListener(this);
		view.findViewById(R.id.ll_book_rank).setOnClickListener(this);
		view.findViewById(R.id.ll_collect).setOnClickListener(this);
		view.findViewById(R.id.ll_feedback).setOnClickListener(this);
		view.findViewById(R.id.ll_points).setOnClickListener(this);
	}

	/**
	 * 监听主页图片按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		// 主页分类图片按钮点击事件
		case R.id.ll_category:// 分类
			startActivity(new Intent(getContext(), ClassifyActivity.class));
			break;
		case R.id.ll_donate:// 捐赠
			// 测试
			startActivity(new Intent(getContext(), SubmitOrderActivity.class));
			break;
		case R.id.ll_drift:// 漂流

			break;
		case R.id.ll_my:// 我的
			((MainActivity) getActivity()).changeTab(3);
			break;
		case R.id.ll_book_rank:// 漂读榜
			startActivity(new Intent(getContext(), BookRankActivity.class));
			break;
		case R.id.ll_feedback:// 留言板
			startActivity(new Intent(getContext(), FeedbackActivity.class));
			break;
		case R.id.ll_collect:// 收藏室
			startActivity(new Intent(getContext(), CollectionActivity.class));
			break;
		case R.id.ll_points:// 积分
			// startActivity(new Intent(getContext(),
			// BookDetailActivity.class));
			break;

		}

	}

	private void initImageCycleView() {
		List<ImageCycleView.ImageInfo> list = new ArrayList<ImageCycleView.ImageInfo>();

		// 使用网络加载图片
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_1.jpg", "",
				"http://www.piaoduwang.com/mobile/images/img_main_1.jpg"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_2.jpg", "",
				"http://www.piaoduwang.com/mobile/images/img_main_2.jpg"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_3.jpg", "",
				"http://www.piaoduwang.com/mobile/images/img_main_3.jpg"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_4.jpg", "",
				"http://www.piaoduwang.com/mobile/images/img_main_4.jpg"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_5.jpg", "",
				"http://www.piaoduwang.com/mobile/images/img_main_5.jpg"));
		mImageCycleView
				.setOnPageClickListener(new ImageCycleView.OnPageClickListener() {
					@Override
					public void onClick(View imageView,
							ImageCycleView.ImageInfo imageInfo) {
						Intent intent = new Intent(getContext(),
								WebActivity.class);
						intent.putExtra("url", imageInfo.value.toString());
						startActivity(intent);
					}
				});

		mImageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack() {
			@Override
			public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
				ImageView imageView = new ImageView(getActivity());
				bitmapUtils.display(imageView, imageInfo.image.toString());
				return imageView;
			}
		});
	}

	class GridViewBookAdpter extends BaseAdapter {
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
				convertView = View.inflate(getContext(),
						R.layout.item_gridview_book, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_name);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(book.bookname);
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

	}

	@ViewInject(R.id.et_word)
	private EditText et_word;

	@OnClick(R.id.iv_search)
	public void search(View view) {
		if (TextUtils.isEmpty(et_word.getText().toString())) {
			Toast.makeText(getContext(), "请输入关键字", 0).show();
			return;
		}
		Intent intent = new Intent(getContext(), SearchActivity.class);
		intent.putExtra("word", et_word.getText().toString());
		startActivity(intent);
	}

}
