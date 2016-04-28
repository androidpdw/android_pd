package com.xiawa.read.view;

import com.xiawa.read.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

/*
 * 自定义progress dialog
 * @author zhengwei
 */

public class CommonProgressDialog extends Dialog {
	private static CommonProgressDialog m_progrssDialog;

	private CommonProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CommonProgressDialog createProgrssDialog(Context context) {
		m_progrssDialog = new CommonProgressDialog(context, R.style.SF_pressDialogCustom);
		m_progrssDialog.setContentView(R.layout.common_view_custom_progress_dialog);
		m_progrssDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return m_progrssDialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (null == m_progrssDialog)
			return;
		ImageView loadingImageView = (ImageView) m_progrssDialog.findViewById(R.id.sf_iv_progress_dialog_loading);
		AnimationDrawable animationDrawable = (AnimationDrawable) loadingImageView.getBackground();
		animationDrawable.start();
	}

	public CommonProgressDialog setMessage(String msg) {
		
		return m_progrssDialog;
	}
}
