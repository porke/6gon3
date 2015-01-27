package com.handknittedapps.honeycombmatchthree.graphics.events;

import com.handknittedapps.honeycombmatchthree.graphics.ScoreOverlay;

public class ScoreOverlayAdvanceEvent extends EventBase
{
	public ScoreOverlayAdvanceEvent(float time, boolean activate, ScoreOverlay overlay)
	{
		super(time, activate);
		this.overlay = overlay;
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			float tm = this.getTimeElapsedToTime();
			this.overlay.setAlpha(1.0f - tm);

			this.overlay.x = this.overlay.getBasePos().x + (int) ((this.overlay.getDestPos().x - this.overlay.getBasePos().x) * tm);
			this.overlay.y = this.overlay.getBasePos().y + (int) ((this.overlay.getDestPos().y - this.overlay.getBasePos().y) * tm);
		}

		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.overlay.setBasePos(this.overlay.getDestPos());
		this.overlay.setAlpha(0.0f);
	}

	public ScoreOverlay getOverlay()
	{
		return this.overlay;
	}

	private ScoreOverlay overlay;
}
