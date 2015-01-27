package com.handknittedapps.honeycombmatchthree.graphics.events;

import com.badlogic.gdx.Gdx;

public abstract class EventBase
{
	protected EventBase(float time, boolean activate)
	{
		if (time < Float.MIN_VALUE)
		{
			this.time = 1.0f;
			this.timeElapsed = 1.0f;
		}
		else
		{
			this.time = time;
			this.timeElapsed = 0;
		}
		this.isActive = activate;
	}

	public boolean update()
	{
		return this.update(Gdx.graphics.getDeltaTime());
	}

	public boolean update(float deltaTime)
	{
		if (this.isActive)
		{
			this.timeElapsed += deltaTime;
			if (this.timeElapsed > this.time)
			{
				this.isActive = false;
				this.timeElapsed = this.time;
				this.onEnded();
			}
		}

		return this.isActive;
	}

	public abstract void onEnded();
	public void stop() { this.isActive = false; }
	public void start() { this.isActive = true; }
	public boolean isActive() { return this.isActive; }
	public float getTimeLeft() { return (this.time - this.timeElapsed); }
	public float getTimeElapsedToTime() { return this.timeElapsed / this.time; }

	private float time;
	protected float timeElapsed;
	protected boolean isActive;
}
