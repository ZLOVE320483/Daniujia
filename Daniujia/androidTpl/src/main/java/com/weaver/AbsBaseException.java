package com.weaver;

public abstract class AbsBaseException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final int ERR_UNKOWN = 1;
	private int code = ERR_UNKOWN;
	
	public AbsBaseException(int code) {
		super("code:" + code);
		this.code = code;
	}
	
	public AbsBaseException(String msg) {
		super(msg);
	}

	public AbsBaseException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public AbsBaseException(Throwable e) {
		super(e);
	}

	public int getCode() {
		return code;
	}

}
