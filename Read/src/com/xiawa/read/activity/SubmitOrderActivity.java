package com.xiawa.read.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiawa.read.R;
import com.xiawa.read.alipay.PayResult;
import com.xiawa.read.alipay.SignUtils;
import com.xiawa.read.bean.BookDetailItem;
import com.xiawa.read.bean.BookRankItem;
import com.xiawa.read.utils.URLString;
import com.xiawa.read.wxpay.Constants;
import com.xiawa.read.wxpay.Util;
import com.xiawa.read.wxpay.WXPayActivity;

public class SubmitOrderActivity extends BaseActivity
{
	public static final String COVER_PIC_URL = "http://www.piaoduwang.com/mobile/images/up_cover_0619/";

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	// 商户PID
	public static final String PARTNER = "2088121295307264";
	// 商户收款账号
	public static final String SELLER = "fjxiawa@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = ""
			+ "QU0g/A61t/K2";
	// 支付宝公钥
	public static final String RSA_PUBLIC = ""
			+ "F/9mQduQL2+IGr56eQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int UPDATE_TOTAL_PRICE = 0;

	private float mTotalPrice = 0;
	// 订单总价格
	@ViewInject(R.id.tv_price_all)
	private TextView tvPriceAll;

	// 订单只有一个条目
	@ViewInject(R.id.rl_single_item)
	private RelativeLayout rlSingleItem;

	// 订单有多个条目
	@ViewInject(R.id.ll_multity_items)
	private LinearLayout llMultityItems;
	// 支付方式
	@ViewInject(R.id.rg_pay_method)
	private RadioGroup rgPayMethod;

	// 订单只有单个条目时，书的价格
	@ViewInject(R.id.tv_price)
	private TextView tvPrice;
	// 订单只有单个条目时，书名
	@ViewInject(R.id.tv_book_title)
	private TextView tvBookTitle;
	// 订单只有单个条目时，书的封面
	@ViewInject(R.id.iv_book_cover)
	private ImageView ivBookCover;

	// 订单有多个条目，前四个封面
	@ViewInject(R.id.imageView1)
	private ImageView imageView1;
	@ViewInject(R.id.imageView2)
	private ImageView imageView2;
	@ViewInject(R.id.imageView3)
	private ImageView imageView3;
	@ViewInject(R.id.imageView4)
	private ImageView imageView4;
	// 订单有多个条目时，条目总数。
	@ViewInject(R.id.tv_total_items)
	private TextView tvTotalItems;

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
					Toast.makeText(SubmitOrderActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else
				{
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000"))
					{
						Toast.makeText(SubmitOrderActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else
					{
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(SubmitOrderActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case UPDATE_TOTAL_PRICE:
				tvPriceAll.setText(mTotalPrice + "");
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		ViewUtils.inject(this);
		setHeaderTitle("提交订单");
		Intent intent = getIntent();
		if (intent != null)
			initData(intent);

	}

	/**
	 * 提交订单通过Intent接受数据
	 * 
	 * @param intent
	 * 
	 */
	private void initData(Intent intent)
	{
		Bundle extras = intent.getExtras();
		if (extras == null)
			return;
		ArrayList<BookRankItem> items = (ArrayList<BookRankItem>) extras
				.getSerializable("BOOKS");
		if (items.size() == 1)// 订单只有一个条目
		{
			BookRankItem bookRankItem = items.get(0);
			rlSingleItem.setVisibility(View.VISIBLE);
			llMultityItems.setVisibility(View.INVISIBLE);
			tvPrice.setText(bookRankItem.price);
			tvPriceAll.setText(bookRankItem.price);
			tvBookTitle.setText(bookRankItem.bookname);
			BitmapUtils bitmapUtils = new BitmapUtils(SubmitOrderActivity.this);
			try
			{
				String url = URLEncoder.encode(bookRankItem.coverpic, "utf-8");
				bitmapUtils.display(ivBookCover, COVER_PIC_URL + url);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		} else
		// 订单有多个条目
		{
			rlSingleItem.setVisibility(View.INVISIBLE);
			llMultityItems.setVisibility(View.VISIBLE);
			tvTotalItems.setText("共" + items.size() + "件");
			for (int i = 0; i < items.size(); i++)
				mTotalPrice += Float.parseFloat(items.get(i).price);
			mHandler.sendEmptyMessage(UPDATE_TOTAL_PRICE);
			BitmapUtils bitmapUtils = new BitmapUtils(SubmitOrderActivity.this);
			try
			{
				String url = URLEncoder.encode(items.get(0).coverpic, "utf-8");
				bitmapUtils.display(imageView1, COVER_PIC_URL + url);
				System.out.println("debug1 " + COVER_PIC_URL + url);

				if (items.size() >= 2)
				{
					url = URLEncoder.encode(items.get(1).coverpic, "utf-8");
					bitmapUtils.display(imageView2, COVER_PIC_URL + url);
					System.out.println("debug2 " + COVER_PIC_URL + url);
				}

				if (items.size() >= 3)
				{
					url = URLEncoder.encode(items.get(2).coverpic, "utf-8");
					bitmapUtils.display(imageView3, COVER_PIC_URL + url);
					System.out.println("debug3 " + COVER_PIC_URL + url);
				}
				if (items.size() >= 4)
				{
					url = URLEncoder.encode(items.get(3).coverpic, "utf-8");
					bitmapUtils.display(imageView4, COVER_PIC_URL + url);
					System.out.println("debug4 " + COVER_PIC_URL + url);
				}
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void onSubmitClick(View view)
	{
		String title = tvBookTitle.getText().toString();
		String price = tvPriceAll.getText().toString();
		int checkedRadioButtonId = rgPayMethod.getCheckedRadioButtonId();
		if (checkedRadioButtonId == R.id.rb_aipay)
			alipay(title, "", price);
		if (checkedRadioButtonId == R.id.rb_wxpay)
			wxpay();
		// Toast.makeText(this, "微信支付敬请期待", Toast.LENGTH_SHORT).show();
	}

	private void wxpay()
	{
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
		api.registerApp(Constants.APP_ID);
		String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
		Toast.makeText(SubmitOrderActivity.this, "获取订单中...", Toast.LENGTH_SHORT)
				.show();
		PayReq req = new PayReq();
		req.appId=Constants.APP_ID;
//		req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
//		req.appId = "wxd930ea5d5a258f4f";
		req.partnerId = "1900000109";
		req.prepayId= "1101000000140415649af9fc314aa427";
		req.packageValue = "Sign=WXPay";
		req.nonceStr= "1101000000140429eb40476f8896f4c9";
		req.timeStamp= "1398746574";
		req.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
		api.sendReq(req);
		Toast.makeText(SubmitOrderActivity.this, "正常调起支付",
				Toast.LENGTH_SHORT).show();
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//		api.sendReq(req);
//		startActivity(new Intent(SubmitOrderActivity.this, WXPayActivity.class));
//        finish();
		
		/*
		try
		{
			byte[] buf = Util.httpGet(url);
			if (buf != null && buf.length > 0)
			{
				String content = new String(buf);
				Log.e("get server pay params:", content);
				JSONObject json = new JSONObject(content);
				if (null != json && !json.has("retcode"))
				{
					PayReq req = new PayReq();
					req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
					//req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					Toast.makeText(SubmitOrderActivity.this, "正常调起支付",
							Toast.LENGTH_SHORT).show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					api.sendReq(req);
					startActivity(new Intent(SubmitOrderActivity.this, WXPayActivity.class));
			        finish();
				} else
				{
					Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
					Toast.makeText(SubmitOrderActivity.this,
							"返回错误" + json.getString("retmsg"),
							Toast.LENGTH_SHORT).show();
				}
			} else
			{
				Log.d("PAY_GET", "服务器请求错误");
				Toast.makeText(SubmitOrderActivity.this, "服务器请求错误",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e)
		{
			Log.e("PAY_GET", "异常：" + e.getMessage());
			Toast.makeText(SubmitOrderActivity.this, "异常：" + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		*/
	}

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
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener()
							{
								public void onClick(
										DialogInterface dialoginterface, int i)
								{
									//
									finish();
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
				PayTask alipay = new PayTask(SubmitOrderActivity.this);
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
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
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
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
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
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
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
