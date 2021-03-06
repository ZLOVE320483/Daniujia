package com.xiaojia.daniujia;

/**
 * message used inside the application
 */
public interface InsideMsg {

	int BASE_MSG = 100;
	
	//chat message
	int BASE_CHAT_MSG = BASE_MSG * 1;
	int LOAD_MSG_FROM_DB = BASE_CHAT_MSG + 1;
	int LOAD_MSG_FROM_MEM = BASE_CHAT_MSG + 2;
	int LOAD_OFFLINE_MSG_COMPLETE = BASE_CHAT_MSG + 3;
	int NOTIFY_UNREAD_MSG_CHANGED = BASE_CHAT_MSG + 4;
	int NOTIFY_USER_UPDATE_CHAT_ORDER = BASE_CHAT_MSG + 5;
	int NOTIFY_GET_CONVERSATION = BASE_CHAT_MSG + 6;
	int NOTIFY_PUSH_NEW_MESSAGE = BASE_CHAT_MSG + 7;
	int NOTIFY_CONVERSATION_ARRIVED = BASE_CHAT_MSG + 8;
	int NOTIFY_CONVERSATION_EMPTY = BASE_CHAT_MSG + 9;
	int NOTIFY_VERSION_CHANGE = BASE_CHAT_MSG + 10;
	int NOTIFY_MQTT_CONNECTING = BASE_CHAT_MSG + 11;
	int NOTIFY_MQTT_CONNECTED = BASE_CHAT_MSG + 12;
	int NOTIFY_MQTT_UNCONNECT = BASE_CHAT_MSG + 13;
	int NOTIFY_JUMP_TO_ME = BASE_CHAT_MSG + 14;

	//pay
	int BASE_PAY_MSG = BASE_MSG * 2;
	int PAY_SUCCESS = BASE_PAY_MSG + 1;
	int PAY_BALANCE_SUCCESS = BASE_PAY_MSG + 2;
	int PAY_ALI_SUCCESS = BASE_PAY_MSG + 3;
	int PAY_WEIXIN_SUCCESS = BASE_PAY_MSG + 4;
	int PAY_WEIXIN_FAIL = BASE_PAY_MSG + 5;
	
	//UI
	int BASE_UI_MSG = BASE_MSG * 3;
	int UI_JUMP_TO_MSG = BASE_UI_MSG + 1;
	int UI_MAIN_CLOSE = BASE_UI_MSG + 2;
	int UI_JUMP_TO_CONSULT = BASE_UI_MSG + 3;
	int UI_NOTIFY_FINISH = BASE_UI_MSG + 4;
	int UI_LOGIN_FINISH = BASE_UI_MSG + 5;
	int RELATIVE_STATUS_CHANGE = BASE_UI_MSG + 6;
	int NOTIFY_SHARE_STATUS = BASE_UI_MSG + 7;
	//other
	int BASE_OTHER_MSG = BASE_MSG * 4;
	int OTHER_MIRACLE_MSG = BASE_OTHER_MSG + 1;
	int ILLEGAL_LOGOUT_MSG = BASE_OTHER_MSG + 2;
	
	
	//argument passed between activity
	int PAY_WAY_CHAT = 1;
	int PAY_WAY_QUESTION = 2;
	int CONTINUE_PLAY_VOICE = 100;
	int STOP_CONTINUE_PLAY_VOICE = 101;

	int VOICE_PLAYING = 102;
}
