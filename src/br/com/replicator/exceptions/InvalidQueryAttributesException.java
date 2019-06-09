package br.com.replicator.exceptions;

public class InvalidQueryAttributesException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidQueryAttributesException(String message) {
		super(message);
	}
}
