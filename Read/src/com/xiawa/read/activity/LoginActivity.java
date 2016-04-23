package com.xiawa.read.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiawa.read.R;

public class LoginActivity extends Activity
{

    @ViewInject(R.id.et_user_name)
    private EditText etUserName;
    @ViewInject(R.id.et_password)
    private EditText etPwd;

    /**
     * 使用ViewUtils给顶部返回添加事件
     *
     * @param v
     */
    @OnClick(R.id.iv_back_top)
    public void backTopOnClick(View v)
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
    }


    /**
     * 登录
     *
     * @param view
     */
    public void login(View view)
    {

    }

    /**
     * 注册
     *
     * @param view
     */
    public void signUp(View view)
    {
        System.out.println("   signup");
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
