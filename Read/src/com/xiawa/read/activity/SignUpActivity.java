package com.xiawa.read.activity;

import java.io.IOException;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.utils.AssetsUtils;
import com.xiawa.read.utils.UIUtils;
import com.xiawa.read.view.MaterialEditText;
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
	@ViewInject(R.id.tv_education)
	private TextView tvEducation;
	@ViewInject(R.id.met_login_name)
	private MaterialEditText metLoginName;
	@ViewInject(R.id.met_nick_name)
	private MaterialEditText metNickName;
	@ViewInject(R.id.met_password)
	private MaterialEditText metPassword;
	@ViewInject(R.id.met_password_confirm)
	private MaterialEditText metPasswordConfirm;
	@ViewInject(R.id.met_pwd_question)
	private MaterialEditText metPwdQuestion;
	@ViewInject(R.id.met_pwd_answer)
	private MaterialEditText metPwdAnswer;

	private ListView lvCountries;
	private ListView lvEducation;
	private List<AddressPicker.Country> mCountries;
	private CountryAdapter mCountryAdapter;
	private AlertDialog mCountryDialog;
	private AlertDialog mEducationDialog;
	// 博士，硕士，本科，大专，中专，技工学校，高中，初中，小学，
	private String[] mEducation = { "文盲与半文盲", "小学", "初中", "高中", "技工学校", "中专",
			"大专", "本科", "硕士", "博士" };
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			tvCountry.setText("点击以选择乡镇/街道");
			tvCountry.setClickable(true);
		};
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		ViewUtils.inject(this);
		initUI();
	}
	/**
	 * 注册
	 * @param view
	 */
	public void signUp(View view)
	{
		
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
			{//性别选择

			}
		});
		llCountry.setVisibility(View.GONE);
		viewDivide.setVisibility(View.GONE);
		tvBirthDate.setOnClickListener(this);
		tvAddress.setOnClickListener(this);
		tvCountry.setOnClickListener(this);
		tvEducation.setOnClickListener(this);
		/*
		 * metLoginName.setError(""); metNickName.setError("");
		 * metPassword.setError(""); metPasswordConfirm.setError("");
		 * metPwdAnswer.setError(""); metPwdQuestion.setError("");
		 */
		mCountryAdapter = new CountryAdapter();
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
		case R.id.tv_education:
			showEducationPicker();
			break;
		}

	}

	/**
	 * 文化程度选择
	 */
	private void showEducationPicker()
	{
		// TODO Auto-generated method stub
		Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.dialog_list_view, null);
		builder.setView(view);
		((TextView) view.findViewById(R.id.tv_dialog_title)).setText("选择文化程度:");
		lvEducation = (ListView) view.findViewById(R.id.lv_dialog_content);
		lvEducation.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				tvEducation.setText(mEducation[position]);
				mEducationDialog.dismiss();
			}
		});
		lvEducation.setAdapter(new SimpleAdapter(this, getData(),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		mEducationDialog = builder.create();
		mEducationDialog.show();
	}

	private List<HashMap<String, Object>> getData()
	{
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;
		for (int i = 0; i < mEducation.length; i++)
		{
			map = new HashMap<String, Object>();
			map.put("title", mEducation[i]);
			list.add(map);
		}
		return list;
	}

	/**
	 * 地址选择器(街道/乡镇)
	 */
	private void showCountryPicker()
	{
		Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.dialog_list_view, null);
		builder.setView(view);
		((TextView) view.findViewById(R.id.tv_dialog_title))
				.setText("选择乡镇/街道:");
		lvCountries = (ListView) view.findViewById(R.id.lv_dialog_content);
		lvCountries.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				tvCountry.setText(mCountries.get(position).getAreaName());
				mCountryDialog.dismiss();
			}
		});
		lvCountries.setAdapter(mCountryAdapter);
		mCountryDialog = builder.create();
		mCountryDialog.show();
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
					int selectedCityIndex, int selectedCountyIndex)
			{
				// TODO Auto-generated method stub

				String areaId = data.get(selectedProvinceIndex).getCities()
						.get(selectedCityIndex).getCounties()
						.get(selectedCountyIndex).getAreaId();
				System.out.println("selectedCityIndex=" + selectedCityIndex
						+ "," + "selectedProvinceIndex="
						+ selectedProvinceIndex + "," + areaId);
				tvCountry.setText("查询乡镇/街道中...");
				tvCountry.setClickable(false);
				getTown(areaId);
				tvAddress.setText(province + city + county);
				llCountry.setVisibility(View.VISIBLE);
				viewDivide.setVisibility(View.VISIBLE);
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
				+ areaId.substring(2, 4)
				+ "/"
				+ areaId.substring(0, 6) + ".html";
		System.err.println(url + "  =========test");
		new Thread()
		{
			public void run()
			{
				Document connect = connect(SignUpActivity.this, url);
				Elements newsHeadlines = connect.select("tr.towntr");//
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
				mCountryAdapter.notifyDataSetChanged();
				mHandler.sendEmptyMessage(0);

			};
		}.start();
	}

	/**
	 * 连接统计局网站
	 * 
	 * @param url
	 * @return
	 */
	private static Document connect(Activity context, String url)
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
			UIUtils.showToast(context, "网络错误!");
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
			textView.setPadding(20, 5, 20, 5);
			return textView;
		}
	}
}
