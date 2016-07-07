package com.xiawa.read.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.BitmapUtils;
import com.xiawa.read.R;
import com.xiawa.read.activity.OrderActivity;
import com.xiawa.read.alipay.PayResult;
import com.xiawa.read.alipay.SignUtils;
import com.xiawa.read.bean.OrderBean;

public class OrderFragment extends Fragment {
	
	// 商户PID
		public static final String PARTNER = "2088121295307264";
		// 商户收款账号
		public static final String SELLER = "fjxiawa@qq.com";
		// 商户私钥，pkcs8格式
		public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKo4ZdmdqrhTqw/K5lD78vyXdbpOx4Lb1GqcfZu5KMYjCPtufPNp6bIE33fdh7C5yvodu/JWvMxLt74++UVliWAvKbUFFyNufaOaV6d/9AHw0XMpJwFut0yhQmL7gksRkC9j0LfuWIDP7rHPx7UirUBlgcEX/2ZB25Avb4gavnp5AgMBAAECgYBi/9Icstprwh2nXbZ+O0qjJePOq6rVrMzqBIH5Y8MXGaFLuoLpfxvv8W2W5TzZx/UJaum4lEHR/+epui538gnm922oe8qm0gajYlZ493YW3hJVONx9+IXg20sXEBuLwCbGyvXZAYVBv5s0cJbmteCitXGcfsaIYfHFHB7yXs7ZEQJBAOIrdhGVvUvVAZycqv96sVWp0HDs8i9CCYhbeib6zyZpwPlDSBEK4HMg+kwG5gQ3Vta7N3/+J2fzAEvOtWDtAgsCQQDAq9LV0zabI9rOCCzYtMCUbjBVFQUjwslIKJRYEkV9yogx/yGjBF6XHzL9PiQCHz2h3+mHAXBxAvohA+LHkKwLAkAk5txz0A+7wLxrljBcUOOAS53D3xVA2rB9fBd5JrEH3ndq9CxdA35NqpLMNs/u3iygCpnqm0hIsKBavhZgAyuzAkA+zUMR86DO/ObrVXrYwEItn6UddpaQS4O0g5WnB32jPQsb0N+z9U6nz8GdDk5Kash6JTRHj06JZ8EEVfHrvtp1AkBbNK3ICk4uAYCOynnvywBe8M/zXwlEDtXln5olpQ6v6pI4Ch04m6bD4VgjmGF6Zky/B5J2V+jTQU0g/A61t/K2";
		// 支付宝公钥
		public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

		private static final int SDK_PAY_FLAG = 1;

		private static final int UPDATE_TOTAL_PRICE = 0;
	
	public static final String BUNDLE_TITLE = "title";
	private String mTitle = "DefaultValue";

	private List<OrderBean> mDatas;

	public OrderFragment(List<OrderBean> data) {
		mDatas = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			mTitle = arguments.getString(BUNDLE_TITLE);
		}
		if (mDatas.size() == 0) {
			TextView tv = new TextView(getActivity());
			tv.setText("暂无");
			tv.setGravity(Gravity.CENTER);
			return tv;
		}
		ListView listView = new ListView(getContext());
		listView.setFocusable(false);
		listView.setAdapter(new GridViewBookAdpter());
		return listView;
	}

	public static OrderFragment newInstance(List<OrderBean> data) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, "");
		OrderFragment fragment = new OrderFragment(data);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private OrderBean ob;

	class GridViewBookAdpter extends BaseAdapter {
		public static final String COVER_PIC_URL = "http://www.piaoduwang.com/pd_images/pd_BookPick/";

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public OrderBean getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView( final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			final OrderBean book = mDatas.get(position);
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.item_order,
						null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_book_title);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.iv_book_cover);
				holder.cover.setScaleType(ScaleType.FIT_XY);
				holder.price = (TextView) convertView
						.findViewById(R.id.tv_price);
				holder.tv_ordercode = (TextView) convertView
						.findViewById(R.id.tv_ordercode);
				holder.btn_check = (Button) convertView
						.findViewById(R.id.btn_check);
				holder.btn_pay = (Button) convertView
						.findViewById(R.id.btn_pay);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(book.bookname);
			holder.price.setText("¥ " + book.account);
			holder.tv_ordercode.setText(book.ordercode);
			if (book.status.equals("0") && book.paid.equals("0")) {
				holder.btn_check.setVisibility(View.GONE);
				holder.btn_pay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ob = mDatas.get(position);
						alipay(book.bookname, book.bookname, book.account);
					}
				});
			} else {
				holder.btn_pay.setVisibility(View.GONE);

			}
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
		TextView price;
		TextView tv_ordercode;
		Button btn_pay;
		Button btn_check;
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@SuppressWarnings("unused")
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case SDK_PAY_FLAG:
			{
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000"))
				{
					Toast.makeText(getContext(), "支付成功",
							Toast.LENGTH_SHORT).show();
					getActivity().finish();
					startActivity(new Intent(getContext(),OrderActivity.class));
				} else
				{
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000"))
					{
						Toast.makeText(getContext(), "支付结果确认中",
								Toast.LENGTH_SHORT).show();
						getActivity().finish();
						startActivity(new Intent(getContext(),OrderActivity.class));
					} else
					{
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(getContext(), "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case UPDATE_TOTAL_PRICE:
				break;
			}
		};
	};
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 * @param subject
	 *            商品
	 * @param body
	 *            商品详细描述
	 * @param price
	 *            价格
	 */
	public void alipay(String subject, String body, String price)
	{
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER))
		{
			new AlertDialog.Builder(getContext())
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener()
							{
								public void onClick(
										DialogInterface dialoginterface, int i)
								{
									//
								}
							}).show();
			return;
		}

		String orderInfo = getOrderInfo(subject, body, price);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try
		{
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		System.out.println("payInfo" + payInfo);
		Runnable payRunnable = new Runnable()
		{

			@Override
			public void run()
			{
				// 构造PayTask 对象
				PayTask alipay = new PayTask(getActivity());
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion()
	{
		PayTask payTask = new PayTask(getActivity());
		String version = payTask.getVersion();
		Toast.makeText(getContext(), version, Toast.LENGTH_SHORT).show();
	}

	// /**
	// * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	// *
	// * @param v
	// */
	// public void h5Pay(View v)
	// {
	// Intent intent = new Intent(this, H5PayDemoActivity.class);
	// Bundle extras = new Bundle();
	// /**
	// * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
	// * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
	// * 商户可以根据自己的需求来实现
	// */
	// String url = "http://m.taobao.com";
	// // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
	// extras.putString("url", url);
	// intent.putExtras(extras);
	// startActivity(intent);
	// }

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price)
	{

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://www.piaoduwang/mobile/alipay/return_url.php"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo()
	{
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
//				Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
		String key = ob.ordercode;
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content)
	{
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType()
	{
		return "sign_type=\"RSA\"";
	}
}
