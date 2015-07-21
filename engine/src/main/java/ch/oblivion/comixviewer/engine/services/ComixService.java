package ch.oblivion.comixviewer.engine.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;

import ch.oblivion.comixviewer.engine.ComixException;
import ch.oblivion.comixviewer.engine.domain.Profile;
import ch.oblivion.comixviewer.engine.domain.ProfileDescription;
import ch.oblivion.comixviewer.engine.domain.ProfileDescriptionList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provide methods to serialise / deserialise and collect default profiles.
 * Currently uses the pages available at http://www.oblivion.ch/comixviewer to get initial Profiles.
 * @author mark
 */
public class ComixService {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private String defaultProfileDescriptionUrl = "http://www.oblivion.ch/comixviewer/ProfileDescriptionList.json";
	private String defaultProfileUrl = "http://www.oblivion.ch/comixviewer/{0}.json";


	/**
	 * @param outputStream to serialise the profile to.
	 * @param profile to serialise.
	 * @throws ComixException if the {@link Profile} could not be serialised.
	 */
	public void serialiseProfile(OutputStream outputStream, Profile profile) throws ComixException {
		serialiseObject(outputStream, profile);
	}
	
	/**
	 * @param inputStream of {@link Profile} to deserialise.
	 * @return the {@link Profile} represented by the input stream.
	 * @throws ComixException if the {@link Profile} could not be deserialized.
	 */
	public Profile deserialiseProfile(InputStream inputStream) throws ComixException {
		return deserialiseObject(inputStream, Profile.class);
	}
	
	/**
	 * @param outputStream to serialise to.
	 * @param profileDescriptionList to serialise.
	 * @throws ComixException if the {@link ProfileDescriptionList} could not be serialised.
	 */
	public void serialiseProfileDescriptionList(OutputStream outputStream, ProfileDescriptionList profileDescriptionList) throws ComixException {
		serialiseObject(outputStream, profileDescriptionList);
	}
	
	/**
	 * @param inputStream of {@link ProfileDescriptionList} to deserialise.
	 * @return the {@link ProfileDescriptionList} represented in the input stream.
	 * @throws ComixException if the object could not be deserialised.
	 */
	public ProfileDescriptionList deserialiseProfileDescriptionList(InputStream inputStream) throws ComixException {
		return deserialiseObject(inputStream, ProfileDescriptionList.class);
	}
	
	private void serialiseObject(OutputStream outputStream, Object object) throws ComixException {
		try {
			mapper.writeValue(outputStream, object);
		} catch (JsonGenerationException e) {
			throw new ComixException(e);
		} catch (JsonMappingException e) {
			throw new ComixException(e);
		} catch (IOException e) {
			throw new ComixException(e);
		}
	}
	
	private <T> T deserialiseObject(InputStream inputStream, Class<T> clazz) throws ComixException {
		try {
			return mapper.readValue(inputStream, clazz);
		} catch (JsonParseException e) {
			throw new ComixException(e);
		} catch (JsonMappingException e) {
			throw new ComixException(e);
		} catch (IOException e) {
			throw new ComixException(e);
		}
	}
	
	/**
	 * @return the default profile description.
	 * @throws ComixException if the default profile description could not be returned.
	 */
	public List<ProfileDescription> loadProfileDescriptions() throws ComixException {
		try {
			return loadProfileDescriptions(new URL(defaultProfileDescriptionUrl));
		} catch (MalformedURLException e) {
			throw new ComixException(e);
		}
	}
	
	/**
	 * @param url to get the profile description from.
	 * @return the profile description.
	 * @throws ComixException if the profile description could not be returned.
	 */
	public List<ProfileDescription> loadProfileDescriptions(final URL url) throws ComixException {
		InputStream stream = null;
		try {
			stream = url.openStream();
			return deserialiseProfileDescriptionList(stream).getProfileDescriptions();
		} catch (IOException e) {
			throw new ComixException(e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
	/**
	 * Uses the default service to get existing profiles.
	 * @param description to get the profile id from.
	 * @return the profile.
	 * @throws ComixException if the profile could not be retrieved.
	 */
	public Profile loadProfile(ProfileDescription description) throws ComixException {
		try {
			return loadProfile(new URL(MessageFormat.format(defaultProfileUrl, description.getId())));
		} catch (MalformedURLException e) {
			throw new ComixException(e);
		}
	}
	
	/**
	 * @param url to get the profile from.
	 * @return the profile.
	 * @throws ComixException if the profile could not be retrieved.
	 */
	public Profile loadProfile(URL url) throws ComixException {
		InputStream stream = null;
		try {
			stream = url.openStream();
			return deserialiseProfile(stream);
		} catch (IOException e) {
			throw new ComixException(e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
}
