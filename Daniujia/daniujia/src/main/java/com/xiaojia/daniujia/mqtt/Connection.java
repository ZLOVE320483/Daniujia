/*
 * Licensed Materials - Property of IBM
 *
 * 5747-SM3
 *
 * (C) Copyright IBM Corp. 1999, 2012 All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 *
 */
package com.xiaojia.daniujia.mqtt;

import android.content.Context;

import com.xiaojia.daniujia.mqtt.service.MqttClientAndroidService;
import com.xiaojia.daniujia.utils.ApplicationUtil;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * 
 * Represents a {@link MqttClientAndroidService} and the actions it has
 * performed
 * 
 * refactored  @{@author leeks} 
 */
public class Connection {

	private static Connection instance = null;
	/*
	 * Basic Information about the client
	 */
	/** ClientHandle for this Connection Object **/
	private String clientHandle = null;
	/**
	 * The clientId of the client associated with this <code>Connection</code>
	 * object
	 **/
	private String clientId = null;
	/**
	 * The host that the {@link MqttClientAndroidService} represented by this
	 * <code>Connection</code> is represented by
	 **/
	private String host = null;
	/** The {@link MqttClientAndroidService} instance this class represents **/
	private MqttClientAndroidService client = null;
	/** The {@link Context} of the application this object is part of **/
	private Context context = null;

	/** The {@link MqttConnectOptions} that were used to connect this client **/
	private MqttConnectOptions conOpt;

	/** True if this connection is secured using SSL **/
	private boolean sslConnection = false;

	/** Persistence id, used by {@link} **/
	private long persistenceId = -1;

	
	private Connection(){
		
	}
	public void setClient(MqttClientAndroidService client) {
		this.client = client;
	}

	public synchronized static Connection getInstance(){
		if(instance == null){
			instance = new Connection();
		}
		return instance;
	}
	
	/**
	 * invoked when connect
	 * @param serverConfig
	 */
	public Connection init(ServerConfig serverConfig, MqttConnectOptions option){
		instance.host = serverConfig.host;
		instance.sslConnection = true;
		instance.clientId = serverConfig.clientId;
		instance.clientHandle = serverConfig.getClientHandle();
		instance.context = ApplicationUtil.getApplicationContext();
		instance.conOpt = option;
		instance.client = new MqttClientAndroidService(context, serverConfig.getUri(), clientId);
		return instance;
	}
	
	public void connect(IMqttActionListener callback) throws MqttException{
		if(client == null){
			throw new IllegalStateException("client has not been initialized");
		}
		client.connect(conOpt, context, callback);
	}
	
	public void disConnect() throws MqttException{
		if(client != null && client.isConnected()){
			client.disconnect();
		}
	}
	
	public void setClientCallback(MqttCallback callback){
		if(client != null){
			client.setCallback(callback);
		}
	}
	
	/**
	 * Gets the client handle for this connection
	 * 
	 * @return client Handle for this connection
	 */
	public String handle() {
		return clientHandle;
	}

	/**
	 * Determines if the client is connected
	 * 
	 * @return is the client connected
	 */
	public boolean isConnected() {
		if(client != null){
			return client.isConnected();
		}
		return false;
	}

	/**
	 * Determines if a given handle refers to this client
	 * 
	 * @param handle
	 *            The handle to compare with this clients handle
	 * @return true if the handles match
	 */
	public boolean isHandle(String handle) {
		return clientHandle.equals(handle);
	}

	/**
	 * Compares two connection objects for equality this only takes account of
	 * the client handle
	 * 
	 * @param o
	 *            The object to compare to
	 * @return true if the client handles match
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Connection)) {
			return false;
		}

		Connection c = (Connection) o;

		return clientHandle.equals(c.clientHandle);

	}

	/**
	 * Get the client Id for the client this object represents
	 * 
	 * @return the client id for the client this object represents
	 */
	public String getId() {
		return clientId;
	}

	/**
	 * Get the host name of the server that this connection object is associated
	 * with
	 * 
	 * @return the host name of the server this connection object is associated
	 *         with
	 */
	public String getHostName() {

		return host;
	}

	/**
	 * Gets the client which communicates with the android service.
	 * 
	 * @return the client which communicates with the android service
	 */
	public MqttClientAndroidService getClient() {
		if(client == null){
			return null;
		}
		return client;
	}

	/**
	 * Add the connectOptions used to connect the client to the server
	 * 
	 * @param connectOptions
	 *            the connectOptions used to connect to the server
	 */
	public void addConnectionOptions(MqttConnectOptions connectOptions) {
		conOpt = connectOptions;

	}

	/**
	 * Get the connectOptions used to connect this client to the server
	 * 
	 * @return The connectOptions used to connect the client to the server
	 */
	public MqttConnectOptions getConnectionOptions() {
		return conOpt;
	}


	/**
	 * Determines if the connection is secured using SSL, returning a C style
	 * integer value
	 * 
	 * @return 1 if SSL secured 0 if plain text
	 */
	public int isSSL() {
		return sslConnection ? 1 : 0;
	}

	/**
	 * Assign a persistence ID to this object
	 * 
	 * @param id
	 *            the persistence id to assign
	 */
	public void assignPersistenceId(long id) {
		persistenceId = id;
	}

	/**
	 * Returns the persistence ID assigned to this object
	 * 
	 * @return the persistence ID assigned to this object
	 */
	public long persistenceId() {
		return persistenceId;
	}
}
