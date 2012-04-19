package com.group_finity.mascot.exception;

public class ConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationException(final String message) {
		super(message);
	}

	public ConfigurationException(final Throwable cause) {
		super(cause);
	}

	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
