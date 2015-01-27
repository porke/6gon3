package com.handknittedapps.honeycombmatchthree.graphics.events;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateDecelerateInterpolator;

import com.handknittedapps.honeycombmatchthree.graphics.hud.ProgressBar;

public class ProgressBarEvent extends EventBase
{
	private static float ProgressBarFillPerSecond = 0.4f;

	public ProgressBarEvent(ProgressBar pbr, float currValue, float destValue, boolean activate)
	{
		super(Math.abs(destValue - currValue) / ProgressBarEvent.ProgressBarFillPerSecond, activate);
		this.pbr = pbr;
		this.initialValue = currValue;
		this.destValue = destValue;
		this.delta = destValue - this.initialValue;
		this.interpolator = AccelerateDecelerateInterpolator.$(1.5f);
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			this.pbr.updateProgress(this.initialValue + this.delta * this.interpolator.getInterpolation(this.getTimeElapsedToTime()));
		}

		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.pbr.updateProgress(this.destValue);
	}

	public float getDestValue() { return this.destValue; }

	private ProgressBar pbr;
	private Interpolator interpolator;
	private float initialValue;
	private float destValue;
	private float delta;
}
