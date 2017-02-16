package com.xiaojia.daniujia.db;

/**
 * Created by ZLOVE on 2016/4/8
 */
public class DatabaseConstants {

    public static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME_FORMAT = "dnj_db_%s.db";

    public static class Tables {
        public static final String TABLE_NAME_CONVERSATION = "_conversation";
        public static final String TABLE_NAME_MESSAGE = "_message";
    }

    public class ConversationColumn {
        public static final String COLUMN_NAME_TARGET_NAME = "_target_name";
        public static final String COLUMN_NAME_NAME = "_name";
        public static final String COLUMN_NAME_TARGET_IDENTITY = "_target_identity";
        public static final String COLUMN_NAME_TARGET_AVATAR = "_target_avatar";
        public static final String COLUMN_NAME_TARGET_USER_ID = "_target_user_id";
        public static final String COLUMN_NAME_CONVERSATION_TYPE = "_conversation_type";
        public static final String COLUMN_NAME_IS_TO_WAITER = "_is_to_waiter";
        public static final String COLUMN_NAME_CREATE_AT = "_create_at";
        public static final String COLUMN_NAME_UNREAD_COUNT = "_unread_count";
        public static final String COLUMN_NAME_MSG_ID = "_msg_id";
        public static final String COLUMN_NAME_MSG_TYPE = "_msg_type";
        public static final String COLUMN_NAME_FROM = "_from";
        public static final String COLUMN_NAME_TO = "_to";
        public static final String COLUMN_NAME_MSG_STAMP = "_msg_stamp";
        public static final String COLUMN_NAME_MSG_CONTENT_TYPE = "_msg_content_type";
        public static final String COLUMN_NAME_MSG_CONTENT = "_msg_content";
        public static final String COLUMN_NAME_SERVICE_STATUS = "_service_status";
    }

    public class MessageColumn {
        public static final String COLUMN_NAME_MSG_ID = "_msg_id";
        public static final String COLUMN_NAME_MSG_TYPE = "_msg_type";
        public static final String COLUMN_NAME_FROM = "_from";
        public static final String COLUMN_NAME_TO = "_to";
        public static final String COLUMN_NAME_CONVERSATION_TYPE = "_conversation_type";
        public static final String COLUMN_NAME_MSG_STAMP = "_msg_stamp";
        public static final String COLUMN_NAME_MSG_CODE = "_msg_code";
        public static final String COLUMN_NAME_MSG_CONTENT_TYPE = "_msg_content_type";
        public static final String COLUMN_NAME_MSG_EVENT_TYPE = "_msg_event_type";
        public static final String COLUMN_NAME_MSG_EVENT_TARGET = "_msg_event_target";
        public static final String COLUMN_NAME_MSG_CONTENT = "_msg_content";
        public static final String COLUMN_NAME_MSG_IS_READ = "_is_read";
        public static final String COLUMN_NAME_MSG_STATE = "_msg_state";
        public static final String COLUMN_NAME_IMG_HEIGHT = "_img_height";
        public static final String COLUMN_NAME_IMG_WIDTH = "_img_width";
        public static final String COLUMN_NAME_URL = "_url";
        public static final String COLUMN_NAME_DURATION = "_duration";
        public static final String COLUMN_NAME_QUESTION_ID = "_question_id";
        public static final String COLUMN_NAME_QUESTION_CONTENT = "_question_content";
        public static final String COLUMN_NAME_IS_DOWN = "_is_down";
        public static final String COLUMN_NAME_VOICE_IS_PLAY = "_voice_is_play";
        public static final String COLUMN_NAME_ATTACH_FILE_NAME = "_file_name";
        public static final String COLUMN_NAME_ATTACH_SIZE = "_file_size";
        public static final String COLUMN_NAME_MIME_TYPE = "_mime_type";
        public static final String COLUMN_NAME_LOCAL_PATH = "_local_path";
        public static final String COLUMN_NAME_ATTACH_SOURCE = "_attach_source";
        public static final String COLUMN_NAME_IMG_UPLOAD_PROGRESS = "_upload_progress";
        public static final String COLUMN_NAME_DEMAND_ID = "_demand_id";
        public static final String COLUMN_NAME_DEMAND_NOTICE = "_demand_notice";
        public static final String COLUMN_NAME_FILE_PRE_VIEW_URL = "_pre_view_url";
    }
}
