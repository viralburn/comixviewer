package ch.oblivion.comixviewer.engine.integration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.oblivion.comixviewer.engine.ComixException;
import ch.oblivion.comixviewer.engine.domain.Profile;
import ch.oblivion.comixviewer.engine.domain.ProfileDescription;
import ch.oblivion.comixviewer.engine.domain.ProfileDescriptionList;
import ch.oblivion.comixviewer.engine.services.ComixService;

public class OnlineProviderTest {

	private static final Logger logger = LoggerFactory.getLogger(OnlineProviderTest.class);
	
	private ComixService service = new ComixService();
	
	@Test
	public void onlineProviderTest() throws IOException, ComixException {
		URL url = new URL("http://www.oblivion.ch/comixviewer/ProfileDescriptionList.json");
		InputStream stream = null;
		try {
			stream = url.openStream();
			ProfileDescriptionList list = service.deserialiseProfileDescriptionList(stream);
			for (ProfileDescription description : list.getProfileDescriptions()) {
				logger.debug("Found {}", description.getName());
				downloadProfile(description);
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
	
	}

	private void downloadProfile(ProfileDescription description) throws IOException, ComixException {
		URL url = new URL("http://www.oblivion.ch/comixviewer/" + description.getId().toString() + ".json");
		InputStream stream = null;
		try {
			stream = url.openStream();
			Profile profile = service.deserialiseProfile(stream);
			logger.debug("Found {} pages for {}", profile.getPages().size(), profile.getName());
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
