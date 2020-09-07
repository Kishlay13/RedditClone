package com.RedditClone.RedditClone.exceptions;

public class NonVerifiedTokenException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonVerifiedTokenException(String exMessage, Exception exception) {
		super(exMessage, exception);
	}

	public NonVerifiedTokenException(String exMessage) {
		super(exMessage);
	}

}
