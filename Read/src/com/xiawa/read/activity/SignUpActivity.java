package com.xiawa.read.activity;

<<<<<<< HEAD
import java.io.IOException;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.utils.AssetsUtils;
import com.xiawa.read.view.MySpringSwitchButton;
import com.xiawa.read.view.picker.AddressPicker;
import com.xiawa.read.view.picker.AddressPicker.Country;
import com.xiawa.read.view.picker.DatePicker;

public class SignUpActivity extends Activity implements OnClickListener
{
	@ViewInject(R.id.mssb_sex)
	private MySpringSwitchButton mssbSex;
	@ViewInject(R.id.tv_birth_date)
	private TextView tvBirthDate;
	@ViewInject(R.id.tv_address)
	private TextView tvAddress;
	@ViewInject(R.id.ll_country)
	private LinearLayout llCountry;
	@ViewInject(R.id.tv_country)
	private TextView tvCountry;
	@ViewInject(R.id.view_divide)
	private View viewDivide;

	private ListView lvCountries;
	private List<AddressPicker.Country> mCountries;
	private CountryAdapter mAdapter;
	private AlertDialog mDialog;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		ViewUtils.inject(this);
		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI()
	{
		/**
		 * 性别选择
		 */
		mssbSex.animate();
		mssbSex.setOnToggleListener(new MySpringSwitchButton.OnToggleListener()
		{
			@Override
			public void onToggle(boolean left)
			{

			}
		});
		llCountry.setVisibility(View.GONE);
		viewDivide.setVisibility(View.GONE);
		tvBirthDate.setOnClickListener(this);
		tvAddress.setOnClickListener(this);
		tvCountry.setOnClickListener(this);
		mAdapter = new CountryAdapter();
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		switch (id)
		{
		case R.id.tv_birth_date:
			showDatePicker();
			break;
		case R.id.tv_address:
			showAddressPicker();
			break;
		case R.id.tv_country:
			showCountryPicker();
			break;
		}

	}

	/**
	 * 地址选择器(街道/乡镇)
	 */
	private void showCountryPicker()
	{
		Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.dialog_list_view, null);
		builder.setView(view);
		lvCountries = (ListView) view.findViewById(R.id.lv_countries);
		lvCountries.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				tvCountry.setText(mCountries.get(position).getAreaName());
				mDialog.dismiss();
			}
		});
		lvCountries.setAdapter(mAdapter);
		mDialog=builder.create();
		mDialog.show();
	}

	/**
	 * 地址选择器(省市县)
	 */
	private void showAddressPicker()
	{
		// TODO Auto-generated method stub
		final ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
		String json = AssetsUtils.readText(this, "city.json");
		data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
		AddressPicker picker = new AddressPicker(this, data);
		picker.setSelectedItem("福建", "漳州", "芗城");
		picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener()
		{

			@Override
			public void onAddressPicked(String province, String city,
					String county, int selectedProvinceIndex,
					int selectedCityIndex)
			{
				// TODO Auto-generated method stub

				String areaId = data.get(selectedProvinceIndex).getCities()
						.get(selectedCityIndex).getAreaId();
				System.out.println("selectedCityIndex=" + selectedCityIndex
						+ "," + "selectedProvinceIndex="
						+ selectedProvinceIndex + "," + areaId);
				getTown(areaId);
				mAdapter.notifyDataSetChanged();
				tvAddress.setText(province + city + county);
				llCountry.setVisibility(View.VISIBLE);
				viewDivide.setVisibility(View.VISIBLE);
				if (mCountries != null)
					if (mCountries.size() != 0)
						tvCountry.setText(mCountries.get(1).getAreaName());

			}
		});
		picker.setAnimationStyle(R.style.Animation_Popup);
		picker.show();
	}

	/**
	 * 日期选择器
	 */
	private void showDatePicker()
	{
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePicker picker = new DatePicker(this);
		picker.setRange(1900, year);
		picker.setSelectedItem(year, month + 1, day);
		picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener()
		{
			@Override
			public void onDatePicked(String year, String month, String day)
			{
				tvBirthDate.setText(year + "年" + month + "月" + day + "日");
				llCountry.setVisibility(View.VISIBLE);
			}
		});
		picker.setAnimationStyle(R.style.Animation_Popup);
		picker.show();
	}

	/**
	 * 从国家统计局网站http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2014 获取乡镇/街道
	 * 
	 * @param areaId
	 */
	private void getTown(String areaId)
	{
		// http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2014/35/3505.html
		final String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2014/"
				+ areaId.substring(0, 2)
				+ "/"
				+ areaId.substring(0, 4)
				+ ".html";
		new Thread()
		{
			public void run()
			{
				Document connect = connect(url);
				Elements newsHeadlines = connect.select("tr.countytr");//
				// 获取表格的一行数据
				mCountries = new ArrayList<AddressPicker.Country>();
				for (Element element : newsHeadlines)
				{
					Elements select = element.select("a");
					String name = element.select("td").last().text();
					String id = element.select("td").first().text();
					Country country = new AddressPicker.Country(name, id);
					mCountries.add(country);
				}

				// for (AddressPicker.Country country : mCountries)
				// {
				// System.err.println("test==" + country.toString());
				// }
			};
		}.start();
	}

	/**
	 * 连接统计局网站
	 * 
	 * @param url
	 * @return
	 */
	private static Document connect(String url)
	{
		if (url == null || url.isEmpty())
		{
			throw new IllegalArgumentException("The input url('" + url
					+ "') is invalid!");
		}
		try
		{
			return Jsoup.connect(url).timeout(60 * 1000).get();
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	class CountryAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return mCountries.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return mCountries.get(position).getAreaName();
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			TextView textView = new TextView(SignUpActivity.this);
			textView.setText(mCountries.get(position).getAreaName());
			textView.setTextSize(20.0f);
			textView.setPadding(20, 5, 20, 0);
			return textView;
		}
	}
=======
import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.view.MySpringSwitchButton;


public class SignUpActivity extends Activity
{
    @ViewInject(R.id.mssb_sex)
    private MySpringSwitchButton mssbSex;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ViewUtils.inject(this);
        mssbSex.animate();
        mssbSex.setOnToggleListener(new MySpringSwitchButton.OnToggleListener()
        {
            @Override
            public void onToggle(boolean left)
            {

            }
        });
    }
>>>>>>> 943c1dd2c71b78bf397d718af6da1cf5a5707a11
}
