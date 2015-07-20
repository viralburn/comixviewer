package ch.oblivion.comixviewer.engine.domain;

import java.net.URL;

/**
 * @author mark
 */
public class Page {

	private String title;
	private String description;
	private URL nextPageURL;
	private URL previousPageURL;
	private URL imageURL;
	private URL thumbURL;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public URL getNextPageURL() {
		return nextPageURL;
	}
	public void setNextPageURL(URL nextPageURL) {
		this.nextPageURL = nextPageURL;
	}
	public URL getPreviousPageURL() {
		return previousPageURL;
	}
	public void setPreviousPageURL(URL previousPageURL) {
		this.previousPageURL = previousPageURL;
	}
	public URL getImageURL() {
		return imageURL;
	}
	public void setImageURL(URL imageURL) {
		this.imageURL = imageURL;
	}
	public URL getThumbURL() {
		return thumbURL;
	}
	public void setThumbURL(URL thumbURL) {
		this.thumbURL = thumbURL;
	}

	@Override
	public String toString() {
		return super.toString() + " [" + getTitle() + "]";
	}
}
