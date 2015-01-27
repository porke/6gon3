package com.handknittedapps.honeycombmatchthree.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.handknittedapps.honeycombmatchthree.external.ExternalServiceRegistry;
import com.handknittedapps.honeycombmatchthree.views.splash.SplashView;

public class SplashState extends IGameState
{
	private static final long SplashDurationInMilliseconds = 3000;

	public SplashState(ExternalServiceRegistry serviceRegistry)
	{
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public void onBackPressed()
	{
		this.isActive = false;
	}

	@Override
	public void onExit()
	{
		// Empty
	}

	@Override
	public void onPause()
	{
		// Empty
	}

	@Override
	public void onResume()
	{
		this.onEnterBase();
	}

	@Override
	public boolean onFrame()
	{
		if (this.startTime == -1)
		{
			this.startTime = System.currentTimeMillis();
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.stage.draw();

		this.isActive = (System.currentTimeMillis() < this.startTime + SplashState.SplashDurationInMilliseconds);
		return this.isActive;
	}

	@Override
	public void onEnter()
	{
		this.nextState = new MenuState(this.serviceRegistry);
		this.activeView = new SplashView(this);

		this.stage.addActor(this.activeView);
	}

	private long startTime = -1;

	/** Service registry handle. */
	private ExternalServiceRegistry serviceRegistry;
}
