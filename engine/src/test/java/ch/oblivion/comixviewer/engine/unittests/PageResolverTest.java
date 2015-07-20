package ch.oblivion.comixviewer.engine.unittests;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ch.oblivion.comixviewer.engine.domain.Page;
import ch.oblivion.comixviewer.engine.domain.Profile;
import ch.oblivion.comixviewer.engine.resolver.PageResolverException;

public class PageResolverTest {
	
	private static final String PAGES_XKCD_1000_HTML = "/pages/xkcd-1000.html";
	
	private final transient Logger logger = LoggerFactory.getLogger(PageResolverTest.class);
	
	@Test
	public void testXPathResolver() throws PageResolverException, JsonParseException, JsonMappingException, IOException {
		Profile profile = TestUtilities.createXKCDXPathProfile();
		InputStream stream = null;
		try {
			stream = getClass().getResourceAsStream(PAGES_XKCD_1000_HTML);
			Assert.assertNotNull(stream);
			profile = TestUtilities.serializeDeserialize(profile, Profile.class);
			Page page = profile.resolve(stream);
			page = TestUtilities.serializeDeserialize(page, Page.class, null);
			
			logger.debug("Page = {}", page);
			
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
