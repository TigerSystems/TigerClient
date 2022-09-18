package de.MarkusTieger.tac.bridge.exceptions.reader;

import de.MarkusTieger.tac.bridge.exceptions.TACException;

public class TACReaderAlreadyEnabled extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000005L;

	public static final String DEFAULT = "TAC-Reader is already Enabled!";

	public TACReaderAlreadyEnabled() {
		super();
	}

	public TACReaderAlreadyEnabled(String msg) {
		super(msg);
	}

	public TACReaderAlreadyEnabled(Throwable cause) {
		super(cause);
	}

	public TACReaderAlreadyEnabled(String msg, Throwable cause) {
		super(msg, cause);
	}

}
