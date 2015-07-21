package ch.oblivion.comixviewer.engine.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.oblivion.comixviewer.engine.ComixException;
import ch.oblivion.comixviewer.engine.domain.Profile;
import ch.oblivion.comixviewer.engine.domain.ProfileDescriptionList;

/**
 * Provide methods to serialise / deserialise and collect default profiles.
 * @author mark
 */
public class ComixService {
	
	private ObjectMapper mapper = new ObjectMapper();

	public void serialiseProfile(OutputStream outputStream, Profile profile) throws ComixException {
		serialiseObject(outputStream, profile);
	}
	
	public Profile deserialiseProfile(InputStream inputStream) throws ComixException {
		return deserialiseObject(inputStream, Profile.class);
	}
	
	public void serialiseProfileDescriptionList(OutputStream outputStream, ProfileDescriptionList profileDescriptionList) throws ComixException {
		serialiseObject(outputStream, profileDescriptionList);
	}
	
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
	
	public List<Profile> loadDefaultProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();
		return profiles;
	}
	
}
