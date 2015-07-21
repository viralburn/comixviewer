package ch.oblivion.comixviewer.engine.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import ch.oblivion.comixviewer.engine.unittests.TestUtilities;

public class ProfileLoadingTest {

	private static final String JSON_PROFILE_DESCRIPTION_LIST_JSON = "/json/ProfileDescriptionList.json";
	private static final Logger logger = LoggerFactory.getLogger(ProfileLoadingTest.class);
	
	@Test
	public void createDeafultProfileDescriptionList() throws IOException, ComixException {
		Profile profile1 = TestUtilities.loadProfile(TestUtilities.XKCD_JSON_PROFILE);
		Profile profile2 = TestUtilities.loadProfile(TestUtilities.TWP_JSON_PROFILE);
		
		ProfileDescriptionList list = new ProfileDescriptionList();
		list.addProfileDescription(createProfileDescription("XKCD", "A webcomic of romance, sarcasm, math, and language.", "http://imgs.xkcd.com/static/terrible_small_logo.png"));
		list.addProfileDescription(createProfileDescription("Three Word Phrase", "Three Word Phrase", "http://threewordphrase.com/header.gif"));
		
		File fileDesc = File.createTempFile("ProfileDescriptions", ".json");
		File fileXkcd = File.createTempFile("ProfileDescriptions", ".json");
		File fileTwp = File.createTempFile("ProfileDescriptions", ".json");
		FileOutputStream outputStreamDesc = null;
		FileOutputStream outputStreamXkcd = null;
		FileOutputStream outputStreamTwp = null;
		try {
			ComixService services = new ComixService();
			
			outputStreamDesc = new FileOutputStream(fileDesc);
			services.serialiseProfileDescriptionList(outputStreamDesc, list);
			logger.debug("Profile description list written to : {}", fileDesc.getAbsolutePath());
			
			outputStreamXkcd = new FileOutputStream(fileXkcd);
			profile1.setId(list.getProfileDescriptions().get(0).getId());
			services.serialiseProfile(outputStreamXkcd, profile1);
			logger.debug("Profile1 written to : {}", fileXkcd.getAbsolutePath());
			
			outputStreamTwp = new FileOutputStream(fileTwp);
			profile2.setId(list.getProfileDescriptions().get(1).getId());
			services.serialiseProfile(outputStreamTwp, profile2);
			logger.debug("Profile2 written to : {}", fileTwp.getAbsolutePath());
			
		} finally {
			IOUtils.closeQuietly(outputStreamDesc);
			IOUtils.closeQuietly(outputStreamXkcd);
			IOUtils.closeQuietly(outputStreamTwp);
		}
	}
	
	@Test
	public void loadProfileDescriptionList() throws ComixException {
		ComixService service = new ComixService();
		InputStream inputStream = getClass().getResourceAsStream(JSON_PROFILE_DESCRIPTION_LIST_JSON);
		try {
			ProfileDescriptionList list = service.deserialiseProfileDescriptionList(inputStream);
			for (ProfileDescription description : list.getProfileDescriptions()) {
				logger.debug("Found {}", description);
			}
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private ProfileDescription createProfileDescription(String name, String description, String imageUrl) throws MalformedURLException {
		ProfileDescription profileDescription = new ProfileDescription();
		profileDescription.setName(name);
		profileDescription.setDescription(description);
		profileDescription.setImageUrl(new URL(imageUrl));
		return profileDescription;
	}
}
