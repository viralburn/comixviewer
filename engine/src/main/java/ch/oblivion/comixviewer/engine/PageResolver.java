package ch.oblivion.comixviewer.engine;

import java.io.InputStream;

import ch.oblivion.comixviewer.engine.domain.Page;

/**
 * Resolve the page information on a given input stream.
 * @author mark
 */
public interface PageResolver {

	/**
	 * Extract the PageInformation from an input stream.
	 * @param stream to parse.
	 * @return a {@link Page}.
	 * @throws PageResolverException if the page could not be resolved for any reason.
	 */
	public Page resolve(InputStream stream) throws PageResolverException;
	
}
