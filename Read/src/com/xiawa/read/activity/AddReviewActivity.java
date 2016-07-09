package com.xiawa.read.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookItem;
import com.xiawa.read.domain.GlobalConfig;
import com.xiawa.read.utils.URLString;

public class AddReviewActivity extends BaseActivity {

	private EditText et_content;
	
	private BookItem item;
	
	@ViewInject(R.id.tv_tip)
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setHeaderTitle("发表读后感");
		ViewUtils.inject(this);
		et_content = (EditText) findViewById(R.id.et_content);
		textView.setText("阅读完《"+item.bookname+"》有何感想？");
		item = (BookItem) getIntent().getSerializableExtra("BookItem");
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_content.getText())) {
					Toast.makeText(getApplicationContext(), "读后感内容不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				sendMsg();
			}

		});
	}
	
	private void sendMsg() {
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("loginname",GlobalConfig.username);
		params.addBodyParameter("isbn",item.isbn);
		params.addBodyParameter("suggestContent",et_content.getText().toString());
		utils.send(HttpMethod.POST, URLString.URL_ADD_REVIEW, params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject obj = new JSONObject(arg0.result);
					String resultCode = obj.getString("resultCode");
					if (resultCode.equals("0000")) {
						Toast.makeText(getApplicationContext(), "我们已收到您的读后感！", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "出错!!", Toast.LENGTH_SHORT).show();
					}
					onBackPressed();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
