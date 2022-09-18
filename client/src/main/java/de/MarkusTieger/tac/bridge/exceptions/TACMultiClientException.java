package de.MarkusTieger.tac.bridge.exceptions;

public class TACMultiClientException extends TACException {

	@java.io.Serial
	static final long serialVersionUID = 1000000000000000007L;

	public static final String DEFAULT = "TAC has already a connected Client";

	public TACMultiClientException() {
		super();
	}

	public TACMultiClientException(String msg) {
		super(msg);
	}

	public TACMultiClientException(Throwable cause) {
		super(cause);
	}

	public TACMultiClientException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
