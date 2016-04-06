package com.shizhefei.guide;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BaseDialog extends Dialog implements DialogInterface.OnDismissListener {
	private Activity activity;

	public BaseDialog(Activity context) {
		this(context, R.style.base_dialog);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public BaseDialog(Activity context, int style) {
		super(context, style);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
		}
		this.activity = context;
		super.setOnDismissListener(this);
	}

	protected Context getApplicationContext() {
		return getContext().getApplicationContext();
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		onDismissListener = listener;
	}

	private OnDismissListener onDismissListener;

	public Activity getActivity() {
		return activity;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (onDismissListener != null)
			onDismissListener.onDismiss(dialog);
	}

	public void showAtBottom() {
		show();
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels;
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.x = 0;
		params.y = height - params.height;
		getWindow().setAttributes(params);
	}
}
