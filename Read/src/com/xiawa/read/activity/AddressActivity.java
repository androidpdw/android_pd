package com.xiawa.read.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiawa.read.R;
import com.xiawa.read.bean.BookItem;

public class AddressActivity extends BaseActivity {
	
	private String[] name = {"闽南师范大学站","江滨公园站","漳州体育学院站","漳州职业技术学院站","漳州卫校"};
	private String[] address = {"漳州市芗城区县前直路附近","江滨路","漳州体育学院站附近","漳州职业技术学院站附近","漳州卫校附近"};
	private String[] distance = {"（距离约300米）","（距离约500米）","（距离约2000米）","（距离约2500米）","（距离约2800米）"};
	
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		setHeaderTitle("选择借书地址");
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new BookAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),PayActivity.class);
				intent.putExtra("url", "http://www.piaoduwang.com/mobile/alipay/index.php");
				startActivity(intent);
			}
		});
	}
	
	class BookAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public BookItem getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_address, null);
			}
			TextView tv_site_name = (TextView) view.findViewById(R.id.tv_site_name);
			TextView tv_site_address = (TextView) view.findViewById(R.id.tv_site_address);
			TextView tv_site_distance = (TextView) view.findViewById(R.id.tv_site_distance);
			tv_site_name.setText(name[position]);
			tv_site_address.setText(address[position]);
			tv_site_distance.setText(distance[position]);
			
			return view;
		}

	}
}
