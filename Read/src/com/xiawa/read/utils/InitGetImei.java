package com.xiawa.read.utils;

import android.os.Build;
import android.telephony.TelephonyManager;

/*
 * @author zhengwei
 * 获取手机Imei号
 */

public class InitGetImei {
	TelephonyManager mTelephonyMgr = null;
	
	public InitGetImei(TelephonyManager t){
		mTelephonyMgr = t;
	}
	
    public String getImei(){
    	String szImei="";
		if((szImei = getImeiTrue()).matches("0000000000000")) szImei = getPseudoImei();
    	return szImei;
    }	

    private String getImeiTrue(){          //获取手机Imei号
    	//作为手机来讲，IMEI是唯一的，它应该类似于 359881030314356
    	//除非你有一个没有量产的手机（水货）它可能有无效的IMEI，如：0000000000000
    	return mTelephonyMgr.getDeviceId();  	
 	}    
    	    
    private String getPseudoImei(){   //获取手机伪Imei号
       	//这个在任何Android手机中都有效有一些特殊的情况，一些如平板电脑的设置没有通话功能
       	//或者你不愿加入READ_PHONE_STATE许可。而你仍然想获得唯一序列号之类的东西。
       	//这时你可以通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。
       	//这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）。
       	//但应当明白的是，出现类似情况的可能性基本可以忽略。
        return "35" + Build.BOARD.length()%10 + Build.BRAND.length()%10 + Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + Build.DISPLAY.length()%10 + Build.HOST.length()%10 + Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + Build.TAGS.length()%10 + Build.TYPE.length()%10 + Build.USER.length()%10 ;
    }
}