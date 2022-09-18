package de.MarkusTieger.tac.bridge.exceptions;

public class TACNotAktiveException extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000003L;

	public static final String DEFAULT = "TAC is not Active!";

	public TACNotAktiveException() {
		super();
	}

	public TACNotAktiveException(String msg) {
		super(msg);
	}

	public TACNotAktiveException(Throwable cause) {
		super(cause);
	}

	public TACNotAktiveException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
