package de.MarkusTieger.tac.bridge.exceptions;

public class TACIOException extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000004L;

	public TACIOException() {
		super();
	}

	public TACIOException(String msg) {
		super(msg);
	}

	public TACIOException(Throwable cause) {
		super(cause);
	}

	public TACIOException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
