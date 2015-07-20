package ch.oblivion.comixviewer.engine.resolver.xpath;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import ch.oblivion.comixviewer.engine.PageResolver;
import ch.oblivion.comixviewer.engine.PageResolverException;
import ch.oblivion.comixviewer.engine.domain.Page;

/**
 * Parse an XML input stream.
 * @author mark
 */
public class XPathPageResolver implements PageResolver{

	
	/**
	 * This needs to be transient to avoid serialization issues.
	 */
	private final transient XPathExpressionCache expressionCache;
	
	private String titleExpression;
	private String descriptionExpression;
	private String nextPageURLExpression;
	private String previousPageURLExpression;
	private String imageURLExpression;
	private String thumbURLExpression;
	
	public XPathPageResolver() {
		expressionCache = new XPathExpressionCache();
	}
	
	/* (non-Javadoc)
	 * @see ch.oblivion.comixviewer.engine.PageResolver#resolver(java.io.InputStream)
	 */
	public Page resolve(final InputStream stream) throws PageResolverException {
		try {
			Tidy tidy = new Tidy();
			tidy.setOnlyErrors(true);
			tidy.setForceOutput(true);
			tidy.setQuiet(true);
			tidy.setShowWarnings(false);
			tidy.setShowErrors(0);
			Document document = tidy.parseDOM(stream, null);
			Page page = new Page();
			page.setTitle(toString(document, titleExpression));
			page.setDescription(toString(document, descriptionExpression));
			page.setNextPageURL(toURL(document, nextPageURLExpression));
			page.setPreviousPageURL(toURL(document, previousPageURLExpression));
			page.setImageURL(toURL(document, imageURLExpression));
			page.setThumbURL(toURL(document, thumbURLExpression));
			if (page.getImageURL() == null) {
				return null;
			}
			return page;
			
		} catch (XPathExpressionException e) {
			throw new PageResolverException(e);
			
		} catch (MalformedURLException e) {
			throw new PageResolverException(e);
			
		} finally {
		}
	}

	private String toString(Document document, String expression) throws XPathExpressionException {
		String value = expressionCache.evaluateToString(document, expression);
		if (value != null) {
			return value;
		}
		return null;
	}

	private URL toURL(Document document, String expression) throws XPathExpressionException, MalformedURLException {
		String nodeValue = toString(document, expression);
		if (nodeValue != null) {
			return new URL(nodeValue);
		}
		return null;
	}

	public String getTitleExpression() {
		return titleExpression;
	}

	public void setTitleExpression(String titleExpression) {
		this.titleExpression = titleExpression;
	}

	public String getDescriptionExpression() {
		return descriptionExpression;
	}

	public void setDescriptionExpression(String descriptionExpression) {
		this.descriptionExpression = descriptionExpression;
	}

	public String getNextPageURLExpression() {
		return nextPageURLExpression;
	}

	public void setNextPageURLExpression(String nextPageURLExpression) {
		this.nextPageURLExpression = nextPageURLExpression;
	}

	public String getPreviousPageURLExpression() {
		return previousPageURLExpression;
	}

	public void setPreviousPageURLExpression(String previousPageURLExpression) {
		this.previousPageURLExpression = previousPageURLExpression;
	}

	public String getImageURLExpression() {
		return imageURLExpression;
	}

	public void setImageURLExpression(String imageURLExpression) {
		this.imageURLExpression = imageURLExpression;
	}

	public String getThumbURLExpression() {
		return thumbURLExpression;
	}

	public void setThumbURLExpression(String thumbURLExpression) {
		this.thumbURLExpression = thumbURLExpression;
	}
}
