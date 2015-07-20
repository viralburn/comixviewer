package ch.oblivion.comixviewer.engine.domain;

import java.net.URL;
import java.util.UUID;

/**
 * Light weight representation of a profile.
 * This allows the list of profiles to be maintained independently of the profile with the page list.
 * @author mark
 */
public class ProfileDescription {

	private UUID id;
	private String name;
	private String description;
	private URL imageUrl;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public URL getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(URL imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	

}
