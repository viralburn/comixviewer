package ch.oblivion.comixviewer.engine.resolver.xpath;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to cache {@link XPathExpression}s for reuse so they do not need to be recompiled.
 * @author mark
 */
class XPathExpressionCache {
	private final XPathFactory factory;
	private final Map<String, XPathExpression> pathCache;
	
	public XPathExpressionCache() {
		factory = XPathFactory.newInstance();
		pathCache = new HashMap<String, XPathExpression>();
	}
	
	/**
	 * @param document to parse.
	 * @param expression to use as an XPathExpression, can be null.
	 * @return the string value of the evaluated expression, null if none was found.
	 * @throws XPathExpressionException if the expression is invalid.
	 */
	public String evaluateToString(Document document, String expression) throws XPathExpressionException {
		if (expression != null) {
			XPathExpression xPathExpression = getXPathExpression(expression);
			return xPathExpression.evaluate(document);
		}
		return null;
	}
	
	/**
	 * @param document to parse.
	 * @param expression to use as an XPathExpression, can be null.
	 * @return the first node that matches the given XPath expression, null if none was found.
	 * @throws XPathExpressionException if the expression is invalid.
	 */
	public Node selectFirstNode(Document document, String expression) throws XPathExpressionException {
		NodeList list = selectNodeList(document, expression);
		if (list != null && list.getLength() > 0) {
			return list.item(0);
		}
		return null;
	}

	/**
	 * @param document to parse.
	 * @param expression to use as an XPathExpression, can be null.
	 * @return the node set matching the given XPath expression. null if none were found, may be an empty node set depending on implementation.
	 * @throws XPathExpressionException if the expression is invalid.
	 */
	public NodeList selectNodeList(Document document, String expression) throws XPathExpressionException {
		if (expression != null) {
			XPathExpression xPathExpression = getXPathExpression(expression);
			Object nodeSet = xPathExpression.evaluate(document, XPathConstants.NODESET);
			if (nodeSet instanceof NodeList) {
				return (NodeList) nodeSet;
			}
		}
		return null;
	}

	/**
	 * @param expression to get the compiled XPath expression for, can not be null.
	 * @return the compiled xParh expression, this will be returned from the cache if it exists and will not be recompiled.
	 * @throws XPathExpressionException if the expression is invalid or is null.
	 */
	public XPathExpression getXPathExpression(String expression) throws XPathExpressionException {
		if (expression == null) {
			throw new XPathExpressionException("The given expression string cannot be null.");
		}
		XPathExpression xPathExpression = pathCache.get(expression);
		if (xPathExpression == null) {
			XPath xPath = factory.newXPath();
			xPathExpression = xPath.compile(expression);
			pathCache.put(expression, xPathExpression);
		}
		return xPathExpression;
	}
}