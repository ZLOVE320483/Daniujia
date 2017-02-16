package com.xiaojia.daniujia.mqtt;


public class ServerConfig {
	public String user;
	public String pwd;
	public String host;
	public String port;
	public String clientId;
	private boolean ssl = true;

	@Override
	public String toString() {
		return "ServerConfig [user=" + user + ", pwd=" + pwd + ", host=" + host + ", port=" + port + ", clientId="
				+ clientId + ", ssl=" + ssl + "]";
	}

	public String getUri() {
		String uri = null;
		if (ssl) {
			uri = "ssl://";
		} else {
			uri = "tcp://";
		}
		uri = uri + host + ":" + port;
		return uri;
	}

	public String getClientHandle() {
		return getUri() + clientId;
	}

	public static final int CONN_QOS = 0;
	public static final boolean CONN_RETAINED = false;
	public static final int CONN_TIMEOUT = 30;
	public static final int CONN_KEEPALIVE = 60;
	
}
