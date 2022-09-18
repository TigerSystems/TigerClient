package de.MarkusTieger.tac.bridge.exceptions;

public class TACAlreadyEnabledException extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000001L;

	public static final String DEFAULT = "TAC-Reader is already Enabled!";

	public TACAlreadyEnabledException() {
		super();
	}

	public TACAlreadyEnabledException(String msg) {
		super(msg);
	}

	public TACAlreadyEnabledException(Throwable cause) {
		super(cause);
	}

	public TACAlreadyEnabledException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
