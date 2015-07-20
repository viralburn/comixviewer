package ch.oblivion.comixviewer.engine;

/**
 * Comix viewer Exceptions.
 * @author mark
 */
public class ComixException extends Exception {
	private static final long serialVersionUID = 1L;

	public ComixException() {
	}

	public ComixException(String arg0) {
		super(arg0);
	}

	public ComixException(Throwable arg0) {
		super(arg0);
	}

	public ComixException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ComixException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
