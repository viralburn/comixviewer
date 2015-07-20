package ch.oblivion.comixviewer.engine.domain;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ch.oblivion.comixviewer.engine.resolver.PageResolver;
import ch.oblivion.comixviewer.engine.resolver.PageResolverException;
import ch.oblivion.comixviewer.engine.resolver.regex.RegexPageResolver;
import ch.oblivion.comixviewer.engine.resolver.xpath.XPathPageResolver;

/**
 * A Profile is a heavy weight object containing both the page resolver and list of pages.
 * Profiles are given a unique ID so that they can be globally differentiated. 
 */
public class Profile {
	
	private final List<Page> pages = new ArrayList<Page>();
	
	private UUID id;
	private String name;
	private URL firstPageURL;
	
	@JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = XPathPageResolver.class, name = "xpath"),
        @JsonSubTypes.Type(value = RegexPageResolver.class, name = "regex")})
	private PageResolver resolver;
	
	public Page resolve(InputStream stream) throws PageResolverException {
		return resolver.resolve(stream);
	}
	
	public UUID getId() {
		if (id == null) {
			id = UUID.randomUUID();
		}
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public PageResolver getResolver() {
		return resolver;
	}
	
	public void setResolver(PageResolver resolver) {
		this.resolver = resolver;
	}
	
	public List<Page> getPages() {
		return Collections.unmodifiableList(pages);
	}
	
	public void addPage(Page page) {
		pages.add(page);
	}
	
	public URL getFirstPageURL() {
		return firstPageURL;
	}
	
	public void setFirstPageURL(URL firstPageURL) {
		this.firstPageURL = firstPageURL;
	}
	
	@Override
	public String toString() {
		return super.toString() + " [" + getName() + "]";
	}

	public Page firstPage() {
		if (pages.size() > 0) {
			return pages.get(0);
		}
		return null;
	}
	
	public Page lastPage() {
		if (pages.size() > 0) {
			return pages.get(pages.size() - 1);
		}
		return null;
	}

	public URL nextPageURL() {
		Page lastPage = lastPage();
		if (lastPage != null) {
			return lastPage.getNextPageURL();
		}
		return getFirstPageURL();
	}
}
