package ch.oblivion.comixviewer.engine.resolver.regex;

import java.io.InputStream;
import java.util.regex.Pattern;

import ch.oblivion.comixviewer.engine.domain.Page;
import ch.oblivion.comixviewer.engine.resolver.PageResolver;
import ch.oblivion.comixviewer.engine.resolver.PageResolverException;

public class RegexPageResolver implements PageResolver {

	private Pattern titlePattern;
	private Pattern descriptionPattern;
	private Pattern nextPageURLPattern;
	private Pattern previousPageURLPattern;
	private Pattern imageURLPattern;
	private Pattern thumbURLPattern;
	
	public Page resolve(InputStream stream) throws PageResolverException {
		Page page = new Page();
		return page;
	}

	public Pattern getTitlePattern() {
		return titlePattern;
	}

	public void setTitlePattern(Pattern titlePattern) {
		this.titlePattern = titlePattern;
	}

	public Pattern getDescriptionPattern() {
		return descriptionPattern;
	}

	public void setDescriptionPattern(Pattern descriptionPattern) {
		this.descriptionPattern = descriptionPattern;
	}

	public Pattern getNextPageURLPattern() {
		return nextPageURLPattern;
	}

	public void setNextPageURLPattern(Pattern nextPageURLPattern) {
		this.nextPageURLPattern = nextPageURLPattern;
	}

	public Pattern getPreviousPageURLPattern() {
		return previousPageURLPattern;
	}

	public void setPreviousPageURLPattern(Pattern previousPageURLPattern) {
		this.previousPageURLPattern = previousPageURLPattern;
	}

	public Pattern getImageURLPattern() {
		return imageURLPattern;
	}

	public void setImageURLPattern(Pattern imageURLPattern) {
		this.imageURLPattern = imageURLPattern;
	}

	public Pattern getThumbURLPattern() {
		return thumbURLPattern;
	}

	public void setThumbURLPattern(Pattern thumbURLPattern) {
		this.thumbURLPattern = thumbURLPattern;
	}

}
