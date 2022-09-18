package de.MarkusTieger.tac.bridge.exceptions;

public class TACException extends Exception {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000000L;

	public TACException() {
		super();
	}

	public TACException(String msg) {
		super(msg);
	}

	public TACException(Throwable cause) {
		super(cause);
	}

	public TACException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
