package com.portfolio.www.exception;

public class WrongPasswordException extends RuntimeException{

	public WrongPasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WrongPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public WrongPasswordException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public WrongPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public WrongPasswordException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
