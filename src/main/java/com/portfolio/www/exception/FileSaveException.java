package com.portfolio.www.exception;

/**
 * 첨부파일을 저장하다가 발생한 예외들을 감싸서 던지는 용도의 예외
 */
public class FileSaveException extends RuntimeException {

	public FileSaveException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FileSaveException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FileSaveException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FileSaveException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	



}
