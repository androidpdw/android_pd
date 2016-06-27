package com.xiawa.read.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;

public class AuthenActivity extends BaseActivity
{
	@ViewInject(R.id.lv_items)
	private ListView lvItems;
	private String[] mTitle = { "学生", "教师", "家长", "普通会员" };
	private String[] mItems;
	private int mTypeIds[] = { R.array.authen_type_student,
			R.array.authen_type_teacher, R.array.authen_type_parents,
			R.array.authen_type_general };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authen);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);
		initView(type);
		findViewById(R.id.rl_root).setBackgroundColor(getResources().getColor(R.color.red_all));
		TextView tv = (TextView) findViewById(R.id.tv_title);
		tv.setTextColor(getResources().getColor(R.color.white));
	}
	/***
	 * 提交按钮点击事件
	 * @param view
	 */
	public void onSubmit(View view)
	{
		//TODO
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), "认证成功！", 0).show();
		finish();
		
	}

	private void initView(int type)
	{
		if (type < 1 || type > 4)
			return;
		setHeaderTitle("认证为" + mTitle[type - 1]);
		mItems = getResources().getStringArray(R.array.authen_type_student);
		lvItems.setAdapter(new AuthTypeAdapter());
	}

	class AuthTypeAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return mItems.length;
		}

		@Override
		public Object getItem(int position)
		{
			return mItems[position];
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
			String item = mItems[position];
			if (convertView == null)
			{
				convertView = View.inflate(AuthenActivity.this,
						R.layout.item_authen_type, null);
				holder = new ViewHolder();
				holder.key = (TextView) convertView.findViewById(R.id.tv_key);
				holder.value = (EditText) convertView
						.findViewById(R.id.et_value);
				holder.value.setBackgroundColor(getResources().getColor(R.color.white));
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.key.setText(item);
			holder.value.setHint("请输入"+item);;

			return convertView;
		}
	}

	class ViewHolder
	{
		TextView key;
		EditText value;

	}
}
