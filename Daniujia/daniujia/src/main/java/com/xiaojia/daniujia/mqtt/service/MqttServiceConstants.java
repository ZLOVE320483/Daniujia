/*
============================================================================ 
Licensed Materials - Property of IBM

5747-SM3
 
(C) Copyright IBM Corp. 1999, 2012 All Rights Reserved.
 
US Government Users Restricted Rights - Use, duplication or
disclosure restricted by GSA ADP Schedule Contract with
IBM Corp.
============================================================================
 */
package com.xiaojia.daniujia.mqtt.service;

/**
 * Various strings used to identify operations or data in the Android MQTT
 * service, mainly used in Intents passed between Activities and the Service.
 */
public interface MqttServiceConstants {

    /*
     * Attibutes of messages <p> Used for the column names in the database
     */
    String DUPLICATE = "duplicate";
    String RETAINED = "retained";
    String QOS = "qos";
    String PAYLOAD = "payload";
    String DESTINATION_NAME = "destinationName";
    String CLIENT_HANDLE = "clientHandle";
    String MESSAGE_ID = "messageId";

    /* Tags for actions passed between the Activity and the Service */
    String SEND_ACTION = "send";
    String UNSUBSCRIBE_ACTION = "unsubscribe";
    String SUBSCRIBE_ACTION = "subscribe";
    String DISCONNECT_ACTION = "disconnect";
    String CONNECT_ACTION = "connect";
    String MESSAGE_ARRIVED_ACTION = "messageArrived";
    String MESSAGE_DELIVERED_ACTION = "messageDelivered";
    String ON_CONNECTION_LOST_ACTION = "onConnectionLost";
    String TRACE_ACTION = "trace";

    /* Identifies an Intent which calls back to the Activity */
    String CALLBACK_TO_ACTIVITY = MqttService.TAG
            + ".callbackToActivity";

    /* Identifiers for extra data on Intents broadcast to the Activity */
    String CALLBACK_ACTION = MqttService.TAG + ".callbackAction";
    String CALLBACK_STATUS = MqttService.TAG + ".callbackStatus";
    String CALLBACK_CLIENT_HANDLE = MqttService.TAG + "."
            + CLIENT_HANDLE;
    String CALLBACK_ERROR_MESSAGE = MqttService.TAG
            + ".errorMessage";
    String CALLBACK_EXCEPTION_STACK = MqttService.TAG
            + ".exceptionStack";
    String CALLBACK_INVOCATION_CONTEXT = MqttService.TAG + "."
            + "invocationContext";
    String CALLBACK_ACTIVITY_TOKEN = MqttService.TAG + "."
            + "activityToken";
    String CALLBACK_DESTINATION_NAME = MqttService.TAG + '.'
            + DESTINATION_NAME;
    String CALLBACK_MESSAGE_ID = MqttService.TAG + '.'
            + MESSAGE_ID;
    String CALLBACK_MESSAGE_PARCEL = MqttService.TAG + ".PARCEL";
    String CALLBACK_TRACE_SEVERITY = MqttService.TAG
            + ".traceSeverity";
    String CALLBACK_ERROR_NUMBER = MqttService.TAG
            + ".ERROR_NUMBER";

    String CALLBACK_EXCEPTION = MqttService.TAG + ".exception";

    //exception code for non mqttexceptions
    int NON_MQTT_EXCEPTION = -1;

    String PULL_CONVERSATIONS_REQUEST = "request_conversation_get";
    String PULL_CONVERSATIONS_RESPONSE = "response_conversation_get";

    String DELETE_CONVERSATION_REQUEST = "request_conversation_delete";
    String DELETE_CONVERSATION_RESPONSE = "response_conversation_delete";

    String NEW_CONVERSATION_RECEIVE = "conversation_new";

    String MESSAGE_GET_REQUEST = "request_message_get";
    String MESSAGE_GET_RESPONSE = "response_message_get";

    String MESSAGE_SEND_REQUEST = "request_message_send";
    String MESSAGE_SEND_RESPONSE = "response_message_send";

    String MESSGAE_RESPONSE_ERROR = "response_error";

    String MESSAGE_RECEIVE = "message_new";

    String USER_LOGOUT_REQUEST = "request_user_logout";
    String USER_LOGOUT_RESPONSE = "response_user_logout";

    String STATE_CHANGE = "user_presence_changed";

    String REMOTE_LOGIN = "user_login_otherPlace";

    String MESSAGE_VERSION_CHANGE = "client_version_changed";

    String USER_LOGIN_REQUEST = "request_user_login";
    String USER_LOGIN_RESPONSE = "response_user_login";

    String CUSTOMER_SERVICE_DISPATCH_REQUEST = "request_customer_servicer_dispatch";
    String CUSTOMER_SERVICE_DISPATCH_RESPONSE = "response_customer_servicer_dispatch";

}