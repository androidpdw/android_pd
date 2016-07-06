package com.xiawa.read.domain;

import java.util.ArrayList;

import android.app.Activity;

public class GlobalConfig {
	
	//是否是服务站登录
	public static boolean isAdmin=false;
	
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
	
	public static ArrayList<Activity> list = new ArrayList<Activity>();
	
	public static void add(Activity activity){
		list.add(activity);
	}
	
	public static void remove(Activity activity){
		list.remove(activity);
	}
	
	public static void backToHome() {
		for (int i = 0; i < list.size(); i++) {
			Activity activity = list.get(i);
			activity.finish();
		}
		list.clear();
	}
}
