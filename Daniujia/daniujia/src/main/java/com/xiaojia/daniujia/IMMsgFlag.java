package com.xiaojia.daniujia;

public class IMMsgFlag {
	//transport message first level 
	public static final int MSG_CHAT = 1;
	public static final int MSG_CHAT_READ_CONFIRM = 3;
	public static final int MSG_MULTIPORT_LOGIN = 10;
	public static final int MSG_LOGIN_ERROR = 11;
	public static final int MSG_RELATIVE_ONLINE = 15;
	public static final int MSG_WEB_RTC_CHAT = 100;
	
	public static final int MSG_WEB_RTC_CHAT_START = 101;
	public static final int MSG_WEB_RTC_CHAT_END = 102;
	
	//chat message second level
	public static final int MSG_CHAT_TEXT = 1;
	public static final int MSG_CHAT_IMAGE = 2;
	public static final int MSG_CHAT_SERVICE_QUESTION = 3;
	public static final int MSG_CHAT_EXPERT_QUESTION = 4;
	public static final int MSG_CHAT_CUSTODY_FEE_END = 10;
	public static final int MSG_CHAT_SEVER_END = 11;
	public static final int MSG_CHAT_VOICE = 12;
	public static final int MSG_CHAT_EVENT = 15;
	public static final int MSG_CHAT_ATTACH = 16;
	public static final int MSG_CHAT_REQUIREMENT = 17;
	
	public static final int MSG_SERVICE_TEXT = 1;
	public static final int MSG_SERVICE_PHONE = 2;
	public static final int MSG_SERVICE_OFFLINE = 3;
	
	/**
	 * 0.Alice发送失败 1.Alice已发送（显示：已发送） 2.Bob消息未读（显示：红点及未读消息数）
	 * 3.Bob消息已读（红点及未读消息数消失） 4.Alice消息已读确认（不显示） 5.Alice 最后一条消息已读（显示已读）
	 */

	//message state
	public static final int MSG_SEND_FAIL = 7;
	public static final int MSG_SEND_SUCCESS = 0;
	public static final int MSG_NOT_READ = 2;
	public static final int MSG_READ = 3;
	public static final int MSG_READ_CONFIRM = 4;
	public static final int LAST_MSG_READ = 5;
	public static final int MSG_SENDING = 6;
	
	/**
	 * message type and position
	 */
	public static final int MSG_VIEW_TYPE_COUNT = 10;
	public static final int MSG_RIGHT_TEXT = 0;
	public static final int MSG_LEFT_TEXT = 1;
	public static final int MSG_RIGHT_IMAGE = 2;
	public static final int MSG_LEFT_IMAGE = 3;
	public static final int MSG_RIGHT_SYSTEM = 4;
	public static final int MSG_LEFT_SYSTEM = 5;
	public static final int MSG_LEFT_QUESTION_SERVICE = 6;
	public static final int MSG_LEFT_QUESTION_EXPERT = 7;
	public static final int MSG_RIGHT_VOICE = 8;
	public static final int MSG_LEFT_VOICE = 9;
	
	/**
	 * server type
	 */
	public static final int SERVER_TYPE_COMMON = 1;
	public static final int SERVER_TYPE_PHONE = 2;
	public static final int SERVER_TYPE_OFFLINE = 3;
	
	/**
	 * chat entry
	 */
	public static final int CHAT_ENTRY_BUY_COMMON = 1;
	public static final int CHAT_ENTRY_BUY_PHONE = 2;
	public static final int CHAT_ENTRY_BUY_OFFLINE = 3;
	public static final int CHAT_ENTRY_RECORD = 4;
	public static final int CHAT_ENTRY_ANSWER_QUESTION = 5;

}
