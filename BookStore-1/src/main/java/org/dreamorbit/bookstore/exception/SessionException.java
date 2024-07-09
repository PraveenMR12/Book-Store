package org.dreamorbit.bookstore.exception;

public class SessionException extends RuntimeException {
	private String msg;
	public SessionException(String msg) {
		super(msg);
		this.msg = msg;
	}

}
