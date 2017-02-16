package com.xiaojia.daniujia.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;

import java.io.File;

public class DisplayUtil {

	public static void display(ImageView imageView, String url) {
		if (TextUtils.isEmpty(url)) {
			imageView.setImageResource(R.drawable.def_icon);
			return;
		}
		Picasso.with(App.get()).load(url).error(R.drawable.def_icon).into(imageView);
	}

	public static void display(ImageView imageView, File file) {
		imageView.setImageResource(R.drawable.def_icon);
		if (!file.exists()) {
			return;
		}
		Picasso.with(App.get()).load(file).error(R.drawable.consult_icon).into(imageView);
	}

	public static RequestCreator display(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		RequestCreator reqCreator = Picasso.with(App.get()).load(url);
		return reqCreator;

	}

	public static int getVoiceItemWidth(int duration) {
		if (duration > 0) {
			double width = (Math.log(duration) / Math.log(8)) * 200 + 180;
			return (int) width;
		}
		return 0;
	}
}
