package de.MarkusTieger.tac.bridge.exceptions;

public class TACNotEnabledException extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000002L;

	public static final String DEFAULT = "TAC is not Enabled!";

	public TACNotEnabledException() {
		super();
	}

	public TACNotEnabledException(String msg) {
		super(msg);
	}

	public TACNotEnabledException(Throwable cause) {
		super(cause);
	}

	public TACNotEnabledException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
