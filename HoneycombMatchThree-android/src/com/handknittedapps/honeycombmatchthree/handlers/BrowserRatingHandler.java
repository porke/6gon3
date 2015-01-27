package com.handknittedapps.honeycombmatchthree.handlers;

import com.handknittedapps.honeycombmatchthree.external.RatingHandler;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;


public class BrowserRatingHandler implements RatingHandler 
{
	private final String GameUrl = "market://details?id=com.handknittedapps.honeycombmatchthree";
	
	private Activity parentActivity;
	
	public BrowserRatingHandler(Activity parentActivity)
	{
		this.parentActivity = parentActivity;
	}
	
	@Override
	public void navigateToRatingPage()
	{
		Intent marketIntent = new Intent(Intent.ACTION_VIEW);
		marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		marketIntent.setData(Uri.parse(GameUrl));		
		
		boolean googlePlayAppSupported = false;
		try
		{
			parentActivity.startActivity(marketIntent);
		}
		catch (ActivityNotFoundException ex)
		{
			googlePlayAppSupported = false;
		}
		
		// If the google play default app is not supported, try a browser
		if (!googlePlayAppSupported)
		{
			Uri browserUrl = Uri.parse("https://play.google.com/store/apps/details?id=com.handknittedapps.honeycombmatchthree");
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUrl);
			marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);			
			try
			{
				parentActivity.startActivity(browserIntent);
			}
			catch (ActivityNotFoundException ex)
			{
				// Well, looks like we're not gonna get a rating from this user
			}
		}
	}
}

