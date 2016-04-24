package com.xiawa.read.utils;

import android.content.Context;


/**
 * 操作安装包中的“assets”目录下的文件
 *
 */
public class AssetsUtils {

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static String readText(Context context, String assetPath) {
        try {
            return ConvertUtils.toString(context.getAssets().open(assetPath));
        } catch (Exception e) {
            return "";
        }
    }

}
