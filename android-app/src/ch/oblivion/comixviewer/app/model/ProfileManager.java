package ch.oblivion.comixviewer.app.model;

import java.util.List;
import java.util.Map;

import android.util.Log;
import ch.oblivion.comixviewer.engine.ComixException;
import ch.oblivion.comixviewer.engine.domain.ProfileDescription;
import ch.oblivion.comixviewer.engine.domain.ProfileDescriptionList;
import ch.oblivion.comixviewer.engine.services.ComixService;

public class ProfileManager {

	private static final ProfileManager MANAGER = new ProfileManager();
	
	private final ComixService service;
	private final ProfileDescriptionList profileList;
	
	private ProfileManager() {
		service = new ComixService();
		profileList = new ProfileDescriptionList();
	}
	
	public static ProfileManager getProfileManager() {
		return MANAGER;
	}

	public List<ProfileDescription> getProfileDescriptions() {
		try {
			if (profileList.getProfileDescriptions().isEmpty()) {
				List<ProfileDescription> descriptions = service.loadProfileDescriptions();
				for (ProfileDescription profileDescription : descriptions) {
					profileList.addProfileDescription(profileDescription);
				}
			}
		} catch (ComixException e) {
			Log.e("comix", e.getLocalizedMessage(), e);
		}
		return profileList.getProfileDescriptions();
	}

	public ProfileDescription getProfileDescription(int position) {
		return getProfileDescriptions().get(position);
	}

	public ProfileDescription getProfileDescription(String id) {
		for (ProfileDescription description : getProfileDescriptions()) {
			if (description.getId().toString().equals(id)) {
				return description;
			}
		}
		return null;
	}
}
