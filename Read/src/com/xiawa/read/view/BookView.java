package com.xiawa.read.view;

import com.xiawa.read.R;
import com.xiawa.read.utils.InitGetImei;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookView extends RelativeLayout
{

	private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
	private Drawable mCover;
	private String mPrice, mDesc;

	private RelativeLayout mLayout;
	private ImageView ivCover;
	private TextView tvDesc;
	private TextView tvPrice;

	public BookView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		final TypedArray typedArray = getContext().obtainStyledAttributes(
				attrs, R.styleable.BookView);
		mCover=typedArray.getDrawable(R.styleable.BookView_cover);
		mDesc=typedArray.getString(R.styleable.BookView_description);
		mPrice=typedArray.getString(R.styleable.BookView_price);
		init();
	}

	public BookView(Context context)
	{
		super(context);
		init();
	}

	public BookView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init()
	{
		mLayout = (RelativeLayout) View.inflate(getContext(), R.layout.view_book,
				this);
		ivCover = (ImageView) mLayout.findViewById(R.id.iv_cover);
		tvDesc = (TextView) mLayout.findViewById(R.id.tv_desc);
		tvPrice = (TextView) mLayout.findViewById(R.id.tv_price);
		ivCover.setBackgroundDrawable(mCover);
		tvDesc.setText(mDesc);
		tvPrice.setText(mPrice);
	}

}
