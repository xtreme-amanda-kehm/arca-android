package com.rottentomatoes.app.application;

import android.app.Application;

public class RottenTomatoesApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		com.xtreme.rest.utils.Logger.setup(true, "Xtreme-Rest");
		com.xtreme.rest.service.Logger.setup(true, "Xtreme-Rest-Service");
		com.xtreme.rest.sync.Logger.setup(true, "Xtreme-Rest-Sync");
	}
}
