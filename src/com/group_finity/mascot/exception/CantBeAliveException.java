package com.group_finity.mascot.exception;

public class CantBeAliveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CantBeAliveException(final String message) {
		super(message);
	}

	public CantBeAliveException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
