package de.MarkusTieger.tac.bridge.exceptions.reader;

import de.MarkusTieger.tac.bridge.exceptions.TACException;

public class TACReaderNotEnabled extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000006L;

	public static final String DEFAULT = "TAC-Reader is not enabled!";

	public TACReaderNotEnabled() {
		super();
	}

	public TACReaderNotEnabled(String msg) {
		super(msg);
	}

	public TACReaderNotEnabled(Throwable cause) {
		super(cause);
	}

	public TACReaderNotEnabled(String msg, Throwable cause) {
		super(msg, cause);
	}

}
