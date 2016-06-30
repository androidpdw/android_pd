package com.xiawa.read.activity;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.xiawa.read.R;
import com.xiawa.read.bean.BookDetailItem;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.utils.URLString;
import com.xiawa.read.view.ImageCycleView;

public class BookDetailActivity extends BaseActivity implements OnClickListener
{

	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/mobile/images/book_intro/";

	private BookRankItem bookItem;
	private BookDetailItem bookDetailItem;

	private ImageCycleView mImageCycleView;

	@ViewInject(R.id.tv_price)
	private TextView tv_price;
	@ViewInject(R.id.tv_book_name)
	private TextView tv_book_name;
	@ViewInject(R.id.tv_book_author)
	private TextView tv_book_author;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);
		ViewUtils.inject(this);
		setHeaderTitle("图书详情");
		bookItem = (BookRankItem) getIntent().getSerializableExtra("BookItem");
		getBookDetail(bookItem.isbn);
		findViewById(R.id.rl_book_text).setOnClickListener(this);
		findViewById(R.id.rl_book_comment).setOnClickListener(this);
		findViewById(R.id.btn_collect).setOnClickListener(this);
		findViewById(R.id.btn_brrow).setOnClickListener(this);
	}

	List<ImageCycleView.ImageInfo> list;

	private void initImageCycleView() throws IOException
	{
		mImageCycleView = (ImageCycleView) findViewById(R.id.icv_top);
		mImageCycleView.setIndicationStyle(
				ImageCycleView.IndicationStyle.IMAGE, R.drawable.dot_blur,
				R.drawable.dot_focus, 0);

		list = new ArrayList<ImageCycleView.ImageInfo>();

		// 使用网络加载图片
		if (!TextUtils.isEmpty(bookDetailItem.coverpic))
		{
			list.add(new ImageCycleView.ImageInfo(COVER_PIC_URL
					+ URLEncoder.encode(bookDetailItem.coverpic, "UTF-8"), "",
					"https://www.baidu.com/"));
		}

		if (!TextUtils.isEmpty(bookDetailItem.catpic))
		{
			list.add(new ImageCycleView.ImageInfo(COVER_PIC_URL
					+ URLEncoder.encode(bookDetailItem.catpic, "UTF-8"), "",
					"https://www.baidu.com/"));
		}

		if (!TextUtils.isEmpty(bookDetailItem.cappic))
		{
			list.add(new ImageCycleView.ImageInfo(COVER_PIC_URL
					+ URLEncoder.encode(bookDetailItem.cappic, "UTF-8"), "",
					"https://www.baidu.com/"));
		}
		if (!TextUtils.isEmpty(bookDetailItem.backpic))
		{
			list.add(new ImageCycleView.ImageInfo(COVER_PIC_URL
					+ URLEncoder.encode(bookDetailItem.backpic, "UTF-8"), "",
					"https://www.baidu.com/"));
		}
		mImageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack()
		{
			@Override
			public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo)
			{
				BitmapUtils bitmapUtils = new BitmapUtils(
						BookDetailActivity.this);
				ImageView imageView = new ImageView(BookDetailActivity.this);
				System.out.println(imageInfo.image.toString());
				bitmapUtils.display(imageView, imageInfo.image.toString());
				return imageView;
			}
		});
	}

	private void getBookDetail(String isbn)
	{
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, URLString.URL_DOMAIN
				+ URLString.URL_GET_BOOKDETAIL + "?isbn=" + isbn,
				new RequestCallBack<String>()
				{

					@Override
					public void onFailure(HttpException arg0, String arg1)
					{

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0)
					{
						// System.out.println(arg0.result);
						bookDetailItem = JSON.parseObject(arg0.result,
								BookDetailItem.class);
						tv_price.setText("¥" + bookDetailItem.price);
						tv_book_name.setText(bookDetailItem.bookname);
						tv_book_author.setText(bookDetailItem.author);
						try
						{
							initImageCycleView();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.rl_book_text:
			Intent intent = new Intent(getApplicationContext(),
					BookIntroductActivity.class);
			intent.putExtra("bookItem", bookItem);
			startActivity(intent);
			break;
		case R.id.rl_book_comment:
			startActivity(new Intent(getApplicationContext(),
					BookCommentActivity.class));
			break;
		case R.id.btn_collect:
			/*******************************************************/
			break;
		case R.id.btn_brrow:
			if (!GlobalConfig.isLogin)
			{
				Toast.makeText(getApplicationContext(), "请先登录",
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getApplicationContext(),
						LoginActivity.class));
			} else
			{
				Intent intent2 = new Intent(getApplicationContext(),
						AddressActivity.class);
				ArrayList<BookRankItem> items = new ArrayList<BookRankItem>();
				bookItem.price=bookDetailItem.price;
				items.add(bookItem);
				Bundle bu = new Bundle();
				bu.putSerializable("BOOKS", items);
				intent2.putExtras(bu);
				startActivity(intent2);
			}
			break;
		default:
			break;
		}
	}
}
