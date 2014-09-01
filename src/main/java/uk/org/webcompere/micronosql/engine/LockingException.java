package uk.org.webcompere.micronosql.engine;

/**
 * Exception that gets thrown when optimistic locking
 * violation events occur
 */
public class LockingException extends Exception {
	private static final long serialVersionUID = 1L;

	public LockingException(String message) {
		super(message);
	}
}
