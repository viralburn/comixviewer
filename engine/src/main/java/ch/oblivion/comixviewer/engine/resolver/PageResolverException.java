package ch.oblivion.comixviewer.engine.resolver;

import ch.oblivion.comixviewer.engine.ComixException;


/**
 * Exceptions generated via any {@link PageResolver}.
 * @author mark
 *
 */
public class PageResolverException extends ComixException {
	private static final long serialVersionUID = 1L;

	public PageResolverException() {
		super();
	}

	public PageResolverException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PageResolverException(String arg0) {
		super(arg0);
	}

	public PageResolverException(Throwable arg0) {
		super(arg0);
	}
}
