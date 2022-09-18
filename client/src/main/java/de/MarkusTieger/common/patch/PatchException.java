package de.MarkusTieger.common.patch;

import java.io.IOException;

public class PatchException extends IOException {

	private static final long serialVersionUID = 1L;

	public PatchException() {
		super();
	}

	public PatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public PatchException(String message) {
		super(message);
	}

	public PatchException(Throwable cause) {
		super(cause);
	}

}
