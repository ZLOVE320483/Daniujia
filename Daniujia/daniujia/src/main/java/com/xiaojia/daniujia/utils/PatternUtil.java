package com.xiaojia.daniujia.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternUtil {
	public static final String IMG_REGEX = "<img([^<]*) src=\"([\\S]*)\"([^<]*)>";

	public static boolean isPhoneNum(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return false;
		}
		String regex = "1[3578]\\d{9}$";
		return Pattern.matches(regex, phone);
	}

	public static String checkUser(String user, String password) {
		if (TextUtils.isEmpty(user)) {
			return "用户名不能为空";
		}
		if(user.length() < 4 || user.length() > 14){
			return "用户名长度4-14位";
		}
		if(containsChinese(user)){
			return "用户名不能包含中文";
		}

		if(!PatternUtil.isRegisterString(user)){
			return "用户名只能包含字母、数字、下划线";
		}

		if (user.startsWith("_") || user.endsWith("_")){
			return "用户名开头或结尾不能有下划线";
		}

		if (TextUtils.isEmpty(password)) {
			return "密码不能为空";
		}
		if (password.length() < 6 || password.length() > 14) {
			return "密码长度为6-14位";
		}
		return null;
	}

	public static String checkPwd(String password, String passwordConfirm){
		if(TextUtils.isEmpty(password)){
			return "密码不能为空";
		}
		if(!password.equals(passwordConfirm)){
			return "两次密码不一致";
		}
		if(password.length() < 6 || password.length() > 14){
			return "密码长度为6-14位";
		}
		return null;
	}
	
	/**
	 * @param checkString
	 * @return true if checkString contains Chinese 
	 */
	public static boolean containsChinese(String checkString) {
		boolean isChinese = false;
		for (int i = 0; i < checkString.length(); i++) {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkString.charAt(i));
			if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
					|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
					|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
					|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
				isChinese = true;
				break;
			}
		}
		return isChinese;
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		str=str.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!");//替换中文标号
		String regEx="[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String isContainsIllegalString(String str){
		String result = "";
		if (str != null){
			String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			while (m.find()){
				return m.group();
			}
		}

		return result;
	}

	public static String isContainsIllegalStringDnj(String str){
		String result = "";
		if (str != null){
			String regEx="[%￥$＄@#^~]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			while (m.find()){
				return m.group();
			}
		}
		return result;
	}

	public static boolean isRegisterString(String str){
		boolean bl = false;
		//首先,使用Pattern解释要使用的正则表达式，其中^表是字符串的开始，$表示字符串的结尾。
		Pattern pt = Pattern.compile("^[0-9a-zA-Z_]+$");
		//然后使用Matcher来对比目标字符串与上面解释得结果
		Matcher mt = pt.matcher(str);
		//如果能够匹配则返回true。实际上还有一种方法mt.find()，某些时候，可能不是比对单一的一个字符串，
		//可能是一组，那如果只要求其中一个字符串符合要求就可以用find方法了.
		if(mt.matches()){
			bl = true;
		}
		return bl;

	}

	public static String replaceString(String str,String regex){
		String newStr = str;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()){
			newStr = newStr.replace(matcher.group(),"[图片]");
		}

		return newStr;
	}

	public static String replaceString2(String str,String regex){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("[图片]");
	}

	public static boolean isMonthNum(String num) {
		if (TextUtils.isEmpty(num)) {
			return false;
		}
		String regex = "[0][1-9]||[1][0,1,2]||[1-9]";
		return Pattern.matches(regex, num);
	}

}
