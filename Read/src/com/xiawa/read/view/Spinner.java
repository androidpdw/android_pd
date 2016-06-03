package com.xiawa.read.view;

import java.lang.reflect.Constructor;

import com.xiawa.read.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Spinner extends TextView implements OnClickListener
{
	private String[] mItems = { "普通会员证", "教师会员", "学生会员", "家长会员" };
	private ListView mListView;

	public Spinner(Context context)
	{
		super(context);
		init(context, null);
	}

	public Spinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context, attrs);
	}

	public Spinner(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs)
	{
		setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		setPadding(0, 0, 0, 0);
		setClickable(true);
		setOnClickListener(this);

		mListView = new ListView(context);
		// Set the spinner's id into the listview to make it pretend to be the
		// right parent in
		// onItemClick
		mListView.setId(getId());
		mListView.setDivider(null);
		mListView.setItemsCanFocus(true);
		// hide vertical and horizontal scrollbars
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setHorizontalScrollBarEnabled(false);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

	}

}
