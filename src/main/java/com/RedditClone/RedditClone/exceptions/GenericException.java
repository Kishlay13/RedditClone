package com.RedditClone.RedditClone.exceptions;

public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GenericException(String exMessage, Exception exception) {
		super(exMessage, exception);
	}

	public GenericException(String exMessage) {
		super(exMessage);
	}

}
