package ch.oblivion.comixviewer.engine.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread safe Manager for the list of profiles.
 * Does not provide any mechanism for loading/saving. This should be handled by the consumer.
 * 
 * In order to keep the object memory footprint small, we will only ever keep a list
 * of ProfileDescriptors.
 * It is up to the consumer to load the appropriate profile at runtime.
 * @author mark
 */
public class ProfileDescriptionList {
	
	private final ReentrantLock profileLock;
	private List<ProfileDescription> profileDescriptions;
	
	public ProfileDescriptionList() {
		profileLock = new ReentrantLock();
	}

	public List<ProfileDescription> getProfileDescriptions() {
		profileLock.lock();
		try {
			if (profileDescriptions == null) {
				profileDescriptions = new ArrayList<ProfileDescription>();
			}
			return Collections.unmodifiableList(profileDescriptions);
		} finally {
			profileLock.unlock();
		}
	}
	
	public void setProfileDescriptions(final List<ProfileDescription> profileList) {
		profileLock.lock();
		try {
			this.profileDescriptions = profileList;
		} finally {
			profileLock.unlock();
		}
	}
	
	public void addProfileDescription(final ProfileDescription profileDescription) {
		getProfileDescriptions();
		profileDescriptions.add(profileDescription);
	}
	
	public void removeProfileDescription(final ProfileDescription profileDescription) {
		getProfileDescriptions();
		profileDescriptions.remove(profileDescription);
	}
}
