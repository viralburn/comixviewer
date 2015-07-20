package ch.oblivion.comixviewer.engine.unittests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.oblivion.comixviewer.engine.domain.Profile;
import ch.oblivion.comixviewer.engine.resolver.xpath.XPathPageResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper utilities for testing.
 * @author mark
 */
public class TestUtilities {
	
	public static final String XKCD_JSON_PROFILE = "/json/Profile-XKCD.json";
	public static final String TWP_JSON_PROFILE = "/json/Profile-threewordphrase.json";

	private static final Logger logger = LoggerFactory.getLogger(TestUtilities.class);
	
	/**
	 * @param originalObject to serialize / deserialize.
	 * @param clazz of object.
	 * @return the new object.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static Profile serializeDeserialize(Profile profile, Class<Profile> clazz) throws JsonParseException, JsonMappingException, IOException {
		return serializeDeserialize(profile, clazz, null);
	}
	
	/**
	 * @param originalObject to serialize / deserialize.
	 * @param clazz of object.
	 * @param file to save to.
	 * @return the new object.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <T> T serializeDeserialize(final T originalObject, final Class<T> clazz, File file) throws JsonParseException, JsonMappingException, IOException {
		ByteArrayOutputStream output = null;
		ByteArrayInputStream input = null;
		FileOutputStream fileStream = null;
		try {
			output = new ByteArrayOutputStream();
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(output, originalObject);
			
			byte[] byteArray = output.toByteArray();
			if (file != null) {
				fileStream = new FileOutputStream(file);
				fileStream.write(byteArray);
				logger.debug("{} written to {}", clazz.getSimpleName(), file.getAbsolutePath());
			} else {
				logger.debug("{} = {}", clazz.getSimpleName(), new String(byteArray, Charsets.UTF_8));
			}
			input = new ByteArrayInputStream(byteArray);
			return mapper.readValue(input, clazz);
					
		} finally {
			IOUtils.closeQuietly(fileStream);
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * @return a test profile based on XKCD and xPath.
	 * @throws MalformedURLException 
	 */
	public static Profile createXKCDXPathProfile() throws MalformedURLException {
		XPathPageResolver resolver = new XPathPageResolver();
		resolver.setTitleExpression("/html/body//div[@id = 'ctitle']");
		resolver.setDescriptionExpression("/html/body//div[@id = 'transcript']");
		resolver.setNextPageURLExpression("concat('http://xkcd.com', /html/body//a[@rel = 'next']/@href)");
		resolver.setPreviousPageURLExpression("concat('http://xkcd.com', /html/body//a[@rel = 'prev']/@href)");
		resolver.setImageURLExpression("concat('http:', /html/body//div[@id = 'comic']//img/@src)");
		
		Profile profile = new Profile();
		profile.setName("XKCD");
		profile.setResolver(resolver);
		profile.setFirstPageURL(new URL("http://xkcd.com/1/"));
		return profile;
	}
	
	/**
	 * @return a test profile based on XKCD and xPath.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static Profile loadProfile(String path) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(TestUtilities.class.getResource(path), Profile.class);
	}

	/**
	 * @return a test profile based on threewordphrase and xPath.
	 * @throws MalformedURLException 
	 */
	public static Profile createTWPXPathProfile() throws MalformedURLException {
		XPathPageResolver resolver = new XPathPageResolver();
		resolver.setTitleExpression("/html/head/title");
		resolver.setDescriptionExpression("/html/body/div[@align = 'center'][1]/table[2]/tr/td/img/@alt");
		resolver.setNextPageURLExpression("concat('http://threewordphrase.com/', /html/body/div[@align = 'center'][1]/table[1]/tr/td[4]/a/@href)");
		resolver.setPreviousPageURLExpression("concat('http://threewordphrase.com/', /html/body/div[@align = 'center'][1]/table[1]/tr/td[2]/a/@href)");
		resolver.setImageURLExpression("concat('http://threewordphrase.com/', /html/body/div[@align = 'center'][1]/table[2]/tr/td/img/@src)");
		
		Profile profile = new Profile();
		profile.setName("Three Word Phrase");
		profile.setResolver(resolver);
		profile.setFirstPageURL(new URL("http://threewordphrase.com/goneatya.htm"));
		return profile;
	}
}
