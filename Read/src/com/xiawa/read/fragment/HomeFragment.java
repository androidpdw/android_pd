package com.xiawa.read.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xiawa.read.R;
import com.xiawa.read.activity.BookRankActivity;
import com.xiawa.read.activity.FeedbackActivity;
import com.xiawa.read.view.ImageCycleView;

public class HomeFragment extends Fragment implements OnClickListener {

	private ImageCycleView mImageCycleView;
	private String[] mHomeItemImage = {
			"http://www.piaoduwang.com/mobile/images/img_main_1.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_2.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_3.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_4.jpg",
			"http://www.piaoduwang.com/mobile/images/img_main_5.jpg" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		mImageCycleView = (ImageCycleView) view.findViewById(R.id.icv_top);
		mImageCycleView.setIndicationStyle(
				ImageCycleView.IndicationStyle.IMAGE, R.drawable.dot_blur,
				R.drawable.dot_focus, 0);
		initImageCycleView();
		view.findViewById(R.id.ll_category).setOnClickListener(this);
		view.findViewById(R.id.ll_donate).setOnClickListener(this);
		view.findViewById(R.id.ll_drift).setOnClickListener(this);
		view.findViewById(R.id.ll_my).setOnClickListener(this);
		view.findViewById(R.id.ll_book_rank).setOnClickListener(this);
		view.findViewById(R.id.ll_collect).setOnClickListener(this);
		view.findViewById(R.id.ll_feedback).setOnClickListener(this);
		view.findViewById(R.id.ll_points).setOnClickListener(this);
		return view;
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

			break;
		case R.id.ll_donate:// 捐赠

			break;
		case R.id.ll_drift:// 漂流

			break;
		case R.id.ll_my:// 我的

			break;
		case R.id.ll_book_rank:// 漂读榜
			startActivity(new Intent(getContext(), BookRankActivity.class));
			break;
		case R.id.ll_feedback:// 留言板
			startActivity(new Intent(getContext(), FeedbackActivity.class));
			break;
		case R.id.ll_collect:// 收藏室

			break;
		case R.id.ll_points:// 积分

			break;

		}

	}

	private void initImageCycleView() {
		List<ImageCycleView.ImageInfo> list = new ArrayList<ImageCycleView.ImageInfo>();
		// res图片资源
		// list.add(new
		// ImageCycleView.ImageInfo(R.mipmap.a,"","http://www.baidu.com/"));
		// list.add(new ImageCycleView.ImageInfo(R.mipmap.b,"","2"));
		// list.add(new ImageCycleView.ImageInfo(R.mipmap.c,"","3"));
		// list.add(new ImageCycleView.ImageInfo(R.mipmap.d,"","4"));
		// list.add(new ImageCycleView.ImageInfo(R.mipmap.e,"","5"));

		// 使用网络加载图片
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_1.jpg", "",
				"https://www.baidu.com/"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_2.jpg", "",
				"http://www.163.com/"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_3.jpg", "",
				"http://www.sina.com.cn/"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_4.jpg", "",
				"http://www.sina.com.cn/"));
		list.add(new ImageCycleView.ImageInfo(
				"http://www.piaoduwang.com/mobile/images/img_main_5.jpg", "",
				"http://www.sina.com.cn/"));
		mImageCycleView
				.setOnPageClickListener(new ImageCycleView.OnPageClickListener() {
					@Override
					public void onClick(View imageView,
							ImageCycleView.ImageInfo imageInfo) {
						Toast.makeText(getActivity(),
								"你点击了" + imageInfo.value.toString(),
								Toast.LENGTH_SHORT).show();
					}
				});

		mImageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack() {
			@Override
			public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {

				// //本地图片
				// ImageView imageView=new ImageView(getActivity());
				// imageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
				// return imageView;

				// //使用SD卡图片
				// SmartImageView smartImageView=new
				// SmartImageView(MainActivity.this);
				// smartImageView.setImageURI(Uri.fromFile((File)imageInfo.image));
				// return smartImageView;

				// //使用SmartImageView，既可以使用网络图片也可以使用本地资源
				// SmartImageView smartImageView=new
				// SmartImageView(getActivity());
				// smartImageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
				// return smartImageView;

				// 使用BitmapUtils,只能使用网络图片
				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
				ImageView imageView = new ImageView(getActivity());
				bitmapUtils.display(imageView, imageInfo.image.toString());
				return imageView;
			}
		});
	}

}
