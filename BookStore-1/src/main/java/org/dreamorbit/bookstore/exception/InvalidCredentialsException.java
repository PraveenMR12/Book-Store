package org.dreamorbit.bookstore.exception;

public class InvalidCredentialsException extends RuntimeException {
	private String cause;
	private String causedBy;

	public InvalidCredentialsException(String causedBy, String cause) {
		super(String.format("%s : %s",causedBy, cause));
	}
	
	

}
