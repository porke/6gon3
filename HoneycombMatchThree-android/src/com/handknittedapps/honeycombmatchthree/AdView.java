package com.handknittedapps.honeycombmatchthree;

import java.lang.ref.WeakReference;

import com.eqgmyaulbyshmnhcveyf.AdController;
import com.eqgmyaulbyshmnhcveyf.AdListener;
import com.handknittedapps.honeycombmatchthree.external.AdHandler;
import com.handknittedapps.honeycombmatchthree.external.types.AdHandlerMessages;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class AdView extends View implements AdHandler
{
	private AdController leadboltAdController;
	private AdListener adListener = new AdListener() 
	{
		public void onAdProgress() 
		{
			Log.i("AdView.AdListener", "Ad progress...");
		}
		
		public void onAdLoaded() 
		{
			Log.i("AdView.AdListener", "Ad loaded successfully.");
			AdView.this.leadboltAdController.resumeAd();
		}
		
		public void onAdFailed() 
		{			
			Log.i("AdView.AdListener", "Ad loading failed.");
			AdView.this.leadboltAdController.destroyAd();
		}
		
		public void onAdCompleted() 
		{
			// Empty
		}
		
		public void onAdClosed() 
		{
			// Empty
		}
		
		public void onAdClicked() 
		{
			// Empty
		}
		
		public void onAdAlreadyCompleted() 
		{
			// Empty
		}

		public void onAdPaused() 
		{
			// Empty
		}

		public void onAdResumed() 
		{
			// Empty
		}
	};
	
	private Handler handler;	
	private static class AdHandler extends Handler
	{
		private WeakReference<AdView> adView;
		
		public AdHandler(AdView hostView)
		{
			adView = new WeakReference<AdView>(hostView);
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			AdHandlerMessages msgId = AdHandlerMessages.values()[msg.what];
			switch (msgId)
			{
				case LoadAd:
				{
					String section = (String)msg.obj;
					adView.get().leadboltAdController = new AdController((Activity) adView.get().getContext(), section, adView.get().adListener);
					adView.get().leadboltAdController.setOnProgressInterval(1);
					adView.get().leadboltAdController.loadAd();
					
					Log.i("AdView.handler", "LoadAd: " + section);
					break;
				}
				case DestroyAd:
				{
					adView.get().leadboltAdController.destroyAd();
					Log.i("AdView.handler", "DestroyAd");
					break;
				}
			}
		}
	}
	
	public AdView(Context context) 
	{
		super(context);
		handler = new AdHandler(this);
	}

	public void loadAd(String section) 
	{
		Message msg = new Message();
		msg.what = AdHandlerMessages.LoadAd.ordinal();
		msg.obj = section;
		this.handler.sendMessage(msg);
	}

	public void destroyAd() 
	{
		Message msg = new Message();
		msg.what = AdHandlerMessages.DestroyAd.ordinal();
		this.handler.sendMessage(msg);
	}
}
