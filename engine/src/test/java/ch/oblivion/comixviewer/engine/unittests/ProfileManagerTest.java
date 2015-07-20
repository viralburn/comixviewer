package ch.oblivion.comixviewer.engine.unittests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.oblivion.comixviewer.engine.ComixProfileManager;
import ch.oblivion.comixviewer.engine.PageResolverException;
import ch.oblivion.comixviewer.engine.domain.Page;
import ch.oblivion.comixviewer.engine.domain.Profile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ProfileManagerTest {
	
	private final transient Logger logger = LoggerFactory.getLogger(ProfileManagerTest.class);
	private final transient ComixProfileManager manager = new ComixProfileManager();
	
	@Before
	public void loadTestProfiles() throws JsonParseException, JsonMappingException, IOException {
		List<Profile> profiles = new ArrayList<Profile>();
		profiles.add(TestUtilities.loadProfile(TestUtilities.XKCD_JSON_PROFILE));
		profiles.add(TestUtilities.loadProfile(TestUtilities.TWP_JSON_PROFILE));
		manager.setProfiles(profiles);
	}
	
	@Test
	public void testProfilePages() throws PageResolverException, JsonParseException, JsonMappingException, IOException, InterruptedException {
		for (Profile profile : manager.getProfiles()) {
			testProfile(profile, 2);	
		}
	}

	private void testProfile(Profile profile, int limit) throws PageResolverException, IOException, InterruptedException {
		long millis = System.currentTimeMillis();
		int pages = cacheNextPages(profile, limit);
		logger.debug("Processed {} pages from profile {} in {}", pages, profile.getName(), System.currentTimeMillis() - millis);
		millis = System.currentTimeMillis();
		File file = File.createTempFile("XKCD-JSON", ".json");
		Profile reloadedProfile = TestUtilities.serializeDeserialize(profile, Profile.class, file);
		logger.debug("Reloaded and read {} pages from profile {} in {}", reloadedProfile.getPages().size(), reloadedProfile.getName(), System.currentTimeMillis() - millis);
	}
	
	private int cacheNextPages(Profile profile, int limit) throws PageResolverException, IOException, InterruptedException {
		int index = 0;
		boolean pageNotFound = false;
		while (profile.nextPageURL() != null && !pageNotFound && (limit == 0 || index < limit)) {
			boolean pageFound = false;
			long millis = System.currentTimeMillis();
			HttpURLConnection connection = null;
			InputStream stream = null;
			try {
				URL url = profile.nextPageURL();
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int responseCode = connection.getResponseCode();
				if (responseCode == 200) {
					stream = connection.getInputStream();
					Page page = profile.resolve(stream);
					if (page != null) {
						Page lastPage = profile.lastPage();
						if (lastPage == null || !ObjectUtils.equals(page.getImageURL(), lastPage.getImageURL())) {
							profile.addPage(page);
							index++;
							logger.debug("Processed page {} in {}", index, System.currentTimeMillis() - millis);
							pageFound = true;
						}
					}
				}		
			} catch (IOException e) {
				throw new PageResolverException("Could not resolve the page for profile " + profile.getName(), e);
				
			} finally {
				IOUtils.closeQuietly(stream);
				IOUtils.close(connection);
			}
			pageNotFound = !pageFound;
		}
		return index;
	}

}
