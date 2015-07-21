package ch.oblivion.comixviewer.app.adapters;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import ch.oblivion.comixviewer.app.model.ProfileManager;
import ch.oblivion.comixviewer.engine.domain.ProfileDescription;

public class ProfileDescriptionAdapter extends ArrayAdapter<ProfileDescription> {

	private final ProfileManager manager;
	
	public ProfileDescriptionAdapter(final Context context, final int resourceId, final ProfileManager manager) {
		super(context, resourceId);
		this.manager = manager;
	}

	public void loadDefault() {
		Log.i("ch.oblivion.comixviewer.app", "Starting download of the profiles.");
		LoadProfilesTask task = new LoadProfilesTask();
		task.execute(this);
	}
	
	public ProfileManager getManager() {
		return manager;
	}
	
	class LoadProfilesTask extends AsyncTask<ProfileDescriptionAdapter, Void, List<ProfileDescription>> {

		@Override
		protected List<ProfileDescription> doInBackground(ProfileDescriptionAdapter... adapters) {
			try {
				final ProfileDescriptionAdapter adapter = adapters[0];
				final List<ProfileDescription> list = adapter.getManager().getProfileDescriptions();
				Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {
				     public void run() {
				    	 adapter.addAll(list);
				    	 adapter.notifyDataSetChanged();
				     }
				});
				Log.i("ch.oblivion.comixviewer.app", "Downloaded the profiles.");
				return list;
			} catch (Exception e) {
				Log.e("ch.oblivion.comixviewer.app", "Could not load the profiles.", e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<ProfileDescription> result) {
		}
	}
}
