package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.utils.ScreenUtils;

public class LoadingDlg extends BaseDialog {

	private ImageView imageView;
	private Animation animation;
	private TextView msg;
	public LoadingDlg(Activity context) {
		super(context, R.layout.dlg_loading);
		AbsBaseActivity activity = (AbsBaseActivity) context;
		imageView = (ImageView) mView.findViewById(R.id.load_image);
		msg = (TextView) mView.findViewById(R.id.msg);
		animation = AnimationUtils.loadAnimation(context, R.anim.anim_loading);
		activity.getList().add(this);
	}

	@Override
	public void show() {
		super.show();
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0f;
		lp.width = ScreenUtils.getDisplayMetrics(mActivity).widthPixels;
		lp.height = ScreenUtils.getDisplayMetrics(mActivity).heightPixels - 60 - ScreenUtils.dip2px(45);
		window.setAttributes(lp);
		window.setBackgroundDrawable(new ColorDrawable());

		window.setGravity(Gravity.BOTTOM);

		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				imageView.startAnimation(animation);
			}
		});
	}

	public void show(String msg) {
		this.msg.setText(msg);
		show();
	}

//	public void dismissDelay() {//加延迟，延迟可以掩盖界面的突然变化
//		if (System.currentTimeMillis() - mStartTime > MIN_DELAY_TIME_LEN) {
//			super.dismiss();
//			return;
//		}
//			@Override
//			public void run() {
//				LoadingDlg.super.dismiss();
//			}
//		}, MIN_DELAY_TIME_LEN);
//	}
	
	@Override
	public void dismiss() {
		if(super.isShowing()) {
			super.dismiss();
		}
		if (imageView != null) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					imageView.clearAnimation();
				}
			});
		}
	}
}
