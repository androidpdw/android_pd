package com.xiawa.read.domain;

import android.R.bool;

public class GlobalConfig {
	
	//是否认证
	public static boolean isAuth = false;

	/**
	 *认证类型
	 *1.教师会员
	 *2.学生会员
	 *3.家长会员
	 *4.普通会员
	 */
	public static String authType="普通会员";
	//手机号码
	public static String phone = "";
	
	//用户名
	public static String username = "";
	
	//是否登录
	public static boolean isLogin=false;
}
