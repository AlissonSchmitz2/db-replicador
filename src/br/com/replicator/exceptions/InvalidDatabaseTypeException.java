package br.com.replicator.exceptions;

public class InvalidDatabaseTypeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidDatabaseTypeException(String message) {
		super(message);
	}
}
