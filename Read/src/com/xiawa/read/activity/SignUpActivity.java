package com.xiawa.read.activity;

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
}
