package com.handknittedapps.honeycombmatchthree;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.external.AdHandler;
import com.handknittedapps.honeycombmatchthree.handlers.BrowserRatingHandler;
import com.handknittedapps.honeycombmatchthree.handlers.FlurryStatsHandler;
import com.handknittedapps.honeycombmatchthree.handlers.SwarmStatsHandler;

public class MainActivity extends AndroidApplication
{
	private HoneycombMatchThree game;
	private SwarmStatsHandler achievementHandler;
	private FlurryStatsHandler statsHandler;
	
    public void onCreate(Bundle savedStateInstance)
    {
    	super.onCreate(savedStateInstance);
    	
    	createViews();
    }
    
    @Override
    public void onStart()
    {
    	super.onStart();
    	this.statsHandler.start();
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();    	
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	this.achievementHandler.setActive(true);
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	this.achievementHandler.setActive(false);
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	this.statsHandler.shutdown();    	
    }
    
    @Override
    public void onBackPressed() 
    {
    	this.game.onBackPressed();
    }

    private void createViews()
    {
        // Do the stuff that initialize() would do for you
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                
        AdView adView = new AdView(this);
        View gameView = createGameView(adView);

        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(adView, createAdViewLayout());
        layout.addView(gameView);

        // Hook it all up
        this.setContentView(layout);
    }
    
    private View createGameView(AdHandler adHandler)
    {    	
    	AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;        
        this.achievementHandler = new SwarmStatsHandler(this);
        this.statsHandler = new FlurryStatsHandler(this);
        this.game = new HoneycombMatchThree(adHandler,
        									new BrowserRatingHandler(this),
											this.achievementHandler,
											this.statsHandler);
        
        return this.initializeForView(this.game, cfg);
    }
    
    private RelativeLayout.LayoutParams createAdViewLayout()
    {
    	RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);        
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        return adParams;
    }
}
