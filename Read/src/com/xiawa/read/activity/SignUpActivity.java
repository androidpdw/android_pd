package com.xiawa.read.activity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.utils.AssetsUtils;
import com.xiawa.read.utils.UIUtils;
import com.xiawa.read.utils.URLString;
import com.xiawa.read.view.CommonProgressDialog;
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

	private String mSex = "0"; //记录性别,默认为男生
	private ListView lvCountries;
	private ListView lvEducation;
	private List<AddressPicker.Country> mCountries;
	private CountryAdapter mCountryAdapter;
	private AlertDialog mCountryDialog;
	private AlertDialog mEducationDialog;
	
	private CommonProgressDialog mCustomProgrssDialog;
	
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
	 * @throws NoSuchAlgorithmException 
	 * @throws JSONException 
	 */
	public void signUp(View view) throws NoSuchAlgorithmException, JSONException
	{
		if(isRequiredValid()){
		
    	String metPwd=metPassword.getText().toString().trim();
    	String metPwdConfirm=metPasswordConfirm.getText().toString().trim();
		if (!metPwd.equals(metPwdConfirm)) {
			Toast.makeText(getApplicationContext(), "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
			return;
		}
    	//metPwd = CommonFunction.getMD5(metPwd);
		//metPwdConfirm = CommonFunction.getMD5(metPwdConfirm);
		String date=tvBirthDate.getText().toString();
//		String year=date.substring(0,4);
//		String month=date.substring(5,7);
//		String day=date.substring(8,10);
//		String birthDate=year+month+day;
		String country = tvCountry.getText().toString();
		if (country.equals("点击以选择乡镇/街道")) {
			country="";
		}
		String address = tvAddress.getText().toString()+tvCountry.getText().toString();
		
		Log.i("data", metPwdQuestion.getText()+" "+metPwdAnswer.getText()+" "+date+" "+address+" "+tvEducation.getText().toString());
		RequestParams params = new RequestParams();
		params.addBodyParameter("chkRegUserName", metLoginName.getText().toString().trim());
		params.addBodyParameter("regUserName", metLoginName.getText().toString().trim());
		params.addBodyParameter("regPwd", metPwd);
		params.addBodyParameter("regNickName", metNickName.getText().toString().trim());
		params.addBodyParameter("regPwdQuestion", metPwdQuestion.getText().toString().trim());
		params.addBodyParameter("regPwdAnswer", metPwdAnswer.getText().toString().trim());
		params.addBodyParameter("regSex", mSex);
		params.addBodyParameter("regAddress", address);
		params.addBodyParameter("regBirthDate", date);
		params.addBodyParameter("regEducation", "0");
//		params.addBodyParameter("regEducation", tvEducation.getText().toString());
	    
		String URL = URLString.URL_DOMAIN + URLString.URL_REGISTER;
		
		sendPOST(URL, params);
		}
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
				if(left){
					Log.i("zheng", "left");
					mSex="0";
				}else{
					Log.i("zheng", "right");
					mSex="1";
				}
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
	
	/*
	 * 向服务器提交数据
	 */
	public void sendPOST(String url, RequestParams params) {
		
		HttpUtils httpUtils = new HttpUtils();

		httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			 @Override
	          public void onStart() {
				 Log.i("onstart", "onstart");
				 showCustomProgrssDialog();
	          }

			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.i("onfailure", "onfailure");
				hideCustomProgressDialog();
				Toast.makeText(getApplicationContext(), "网络连接错误！请重试", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Log.i("onsuccess", "onsuccess");
				hideCustomProgressDialog();
				try {
					JSONObject obj = new JSONObject(arg0.result);
					
					String result = new String(obj.getString("resultCode"));
					String msg=new String(obj.getString("resultMsg"));
					if (result.equals("0000")) {  //注册成功
						resSuccessActivity();
					} else {
						Toast.makeText(getApplicationContext(), msg, 0).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 跳转到注册成功页面
	 */
	public void resSuccessActivity() {
		startActivity(new Intent(this, RegSuccessActivity.class));
		finish();
	}
	
	final void showCustomProgrssDialog() {
		if (null == mCustomProgrssDialog)
			mCustomProgrssDialog = CommonProgressDialog.createProgrssDialog(this);
		if (null != mCustomProgrssDialog) {
			//mCustomProgrssDialog.setMessage(msg);
			mCustomProgrssDialog.show();
			mCustomProgrssDialog.setCancelable(false);
		}
	}

	final void hideCustomProgressDialog() {
		if (null != mCustomProgrssDialog) {
			mCustomProgrssDialog.dismiss();
			mCustomProgrssDialog = null;
		}
	}

	
	public boolean isRequiredValid() { 	//判断必填项是否为空
		
		if (TextUtils.isEmpty(metLoginName.getText())) {

			Toast.makeText(this, getString(R.string.loginName_empty), Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(metNickName.getText())) {

			Toast.makeText(this, getString(R.string.nickName_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (TextUtils.isEmpty(metPassword.getText())) {

			Toast.makeText(this, getString(R.string.loginPwd_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (TextUtils.isEmpty(metPasswordConfirm.getText())) {

			Toast.makeText(this, getString(R.string.confirmPwd_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (TextUtils.isEmpty(metPwdQuestion.getText())) {

			Toast.makeText(this, getString(R.string.questionPwd_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (TextUtils.isEmpty(metPwdAnswer.getText())) {

			Toast.makeText(this, getString(R.string.answerPwd_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (tvAddress.getText().toString().isEmpty()) {

			Toast.makeText(this, getString(R.string.address_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (tvEducation.getText().toString().equals("请选择文化程度")) {

			Toast.makeText(this, getString(R.string.education_empty), Toast.LENGTH_SHORT).show();
			return false;
		}else if (tvCountry.getText().toString().equals("点击以选择乡镇/街道")) {

			Toast.makeText(this, getString(R.string.address_empty), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	/**
	 * 顶部返回按钮
	 * @param view
	 */
	public void back(View view) {
		onBackPressed();
	}
}
