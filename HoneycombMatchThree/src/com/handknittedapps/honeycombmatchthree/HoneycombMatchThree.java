package com.handknittedapps.honeycombmatchthree;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import com.handknittedapps.honeycombmatchthree.control.SplashState;
import com.handknittedapps.honeycombmatchthree.control.StateMachine;
import com.handknittedapps.honeycombmatchthree.external.AdHandler;
import com.handknittedapps.honeycombmatchthree.external.ExternalServiceRegistry;
import com.handknittedapps.honeycombmatchthree.external.NullAdHandler;
import com.handknittedapps.honeycombmatchthree.external.NullRatingHandler;
import com.handknittedapps.honeycombmatchthree.external.NullAchievementHandler;
import com.handknittedapps.honeycombmatchthree.external.NullStatsHandler;
import com.handknittedapps.honeycombmatchthree.external.RatingHandler;
import com.handknittedapps.honeycombmatchthree.external.AchievementHandler;
import com.handknittedapps.honeycombmatchthree.external.StatsHandler;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class HoneycombMatchThree implements ApplicationListener
{
	public static final String name = "Honeycomb Match 3";
	public static boolean IsDesktopVersion = false;
	public static final int NominalWidth = 480;
	public static final int NominalHeight = 840;
	public static final float NominalAspectRatio = (float)NominalWidth / (float)NominalHeight;

	private StateMachine stateMachine = new StateMachine();
	private ExternalServiceRegistry serviceRegistry = new ExternalServiceRegistry();

	public HoneycombMatchThree()
	{
		this.serviceRegistry.setAdHandler(new NullAdHandler());
		this.serviceRegistry.setRatingHandler(new NullRatingHandler());
		this.serviceRegistry.setAchievementHandler(new NullAchievementHandler());
		this.serviceRegistry.setStatsHandler(new NullStatsHandler());
		HoneycombMatchThree.IsDesktopVersion = true;
	}

	public HoneycombMatchThree(AdHandler adControl,
							   RatingHandler ratingControl,
							   AchievementHandler achievementHandler,
							   StatsHandler statsHandler)
	{
		this.serviceRegistry.setAdHandler(adControl);
		this.serviceRegistry.setRatingHandler(ratingControl);
		this.serviceRegistry.setAchievementHandler(achievementHandler);
		this.serviceRegistry.setStatsHandler(statsHandler);
	}

	@Override
	public void create()
	{
		Resources.load();
		this.stateMachine.changeState(new SplashState(this.serviceRegistry));
	}

	@Override
	public void dispose()
	{
		Resources.dispose();
	}

	@Override
	public void pause()
	{
		this.stateMachine.onPause();
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (!this.stateMachine.onFrame())
		{
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height)
	{
		// Resize will not be implemented (currently)
	}

	@Override
	public void resume()
	{
		this.stateMachine.onResume();
	}

	public void onBackPressed()
	{
		this.stateMachine.onBackPressed();
	}
}
