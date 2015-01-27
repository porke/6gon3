package com.handknittedapps.honeycombmatchthree.handlers;

import android.app.Activity;

import com.flurry.android.FlurryAgent;
import com.handknittedapps.honeycombmatchthree.external.StatsHandler;

public class FlurryStatsHandler implements StatsHandler 
{
	public FlurryStatsHandler(Activity parentActivity)
	{
		this.parentActivity = parentActivity;
	}
	
	@Override
	public void logEvent(String event) 
	{
		FlurryAgent.logEvent(event);
	}
	
	public void start()
	{
		FlurryAgent.setUseHttps(true);
    	FlurryAgent.setCaptureUncaughtExceptions(true);
    	FlurryAgent.onStartSession(this.parentActivity, this.FlurryApiKey);
	}
	
	public void shutdown()
	{
		FlurryAgent.onEndSession(this.parentActivity);
	}
	
	private Activity parentActivity;
	private final String FlurryApiKey = "DKYWN3FM2HPHB6GHF287";
}
