package com.xiaojia.daniujia.utils;

import com.xiaojia.daniujia.base.App;

import cn.sharesdk.sina.weibo.SinaWeibo;
import sharesdk.onekeyshare.OnekeyShare;
import sharesdk.onekeyshare.OnekeyShareTheme;

public class ShareSDKUtil {

	public static void share(String title, String desc, String imgUrl, String linkUrl,String platform) {

		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(false);
		// ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC 第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setTitle(title);// 微信、QQ空间
		oks.setTitleUrl(linkUrl);// QQ空间
		oks.setSiteUrl(linkUrl);// QQ空间
		oks.setComment("分享"); // QQ空间
		oks.setSite("大牛家"); // QQ空间
		oks.setUrl(linkUrl); // 微信不绕过审核分享链接
		oks.setPlatform(platform);
//		if (imgFile != null && imgFile.exists()) {
//			oks.setImagePath(imgFile.getAbsolutePath());// ALL support
//		}
		if (platform.equals(SinaWeibo.NAME)) {
			oks.setSilent(true);
			oks.setImagePath(imgUrl);
			oks.setText(getFirst50Words(desc) + linkUrl);// ALL support
		} else {
			oks.setText(getFirst50Words(desc));// ALL support
			if (imgUrl != null) {
				imgUrl += "?imageView2/2/w/240";
				oks.setImageUrl(imgUrl);// 新浪微博、QQ空间//http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg
			}
		}
		// 启动分享
		oks.show(App.get().getApplicationContext());
	}


	public static void SettingShare(String title, String desc, String imgUrl, String linkUrl) {

		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(false);
		// ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC 第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setTitle(title );// 微信、QQ空间
		oks.setTitleUrl(linkUrl);// QQ空间
		oks.setSiteUrl(linkUrl);// QQ空间
		oks.setComment("分享"); // QQ空间
		oks.setSite("大牛家"); // QQ空间
		oks.setUrl(linkUrl); // 微信不绕过审核分享链接
		oks.setText(desc);// ALL support
//		if (imgFile != null && imgFile.exists()) {
//			oks.setImagePath(imgFile.getAbsolutePath());// ALL support
//		}
		if (imgUrl != null) {
			imgUrl += "?imageView2/2/w/240";
			//resetImageUrl(imgUrl);
			oks.setImageUrl(imgUrl);// 新浪微博、QQ空间//http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg
		}
		// 启动分享
		oks.show(App.get().getApplicationContext());
	}
	private static String getFirst50Words(String s) {
		if (s != null && s.length() > 50) {
			s = s.substring(0, 50) + "...";
		}
		return s;
	}
}
