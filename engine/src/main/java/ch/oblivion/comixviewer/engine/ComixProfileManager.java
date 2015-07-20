package ch.oblivion.comixviewer.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import ch.oblivion.comixviewer.engine.domain.Profile;

/**
 * Thread safe Manager for the list of profiles.
 * Does not provide any mechanism for loading/saving. This should be handled by the consumer.
 * @author mark
 */
public class ComixProfileManager {
	
	private final ReentrantLock profileLock;
	private List<Profile> profiles;
	
	public ComixProfileManager() {
		profileLock = new ReentrantLock();
	}

	public List<Profile> getProfiles() {
		profileLock.lock();
		try {
			if (profiles == null) {
				profiles = new ArrayList<Profile>();
			}
			return Collections.unmodifiableList(profiles);
		} finally {
			profileLock.unlock();
		}
	}
	
	public void setProfiles(final List<Profile> profileList) {
		profileLock.lock();
		try {
			this.profiles = profileList;
		} finally {
			profileLock.unlock();
		}
	}
	
	public void addProfile(final Profile profile) {
		List<Profile> list = getProfiles();
		list.add(profile);
	}
	
	public void removeProfile(final Profile profile) {
		List<Profile> list = getProfiles();
		list.remove(profile);
	}
}
