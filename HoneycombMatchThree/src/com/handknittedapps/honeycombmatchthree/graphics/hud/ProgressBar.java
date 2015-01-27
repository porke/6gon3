package com.handknittedapps.honeycombmatchthree.graphics.hud;


import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.handknittedapps.honeycombmatchthree.graphics.events.EventBase;
import com.handknittedapps.honeycombmatchthree.graphics.events.ProgressBarEvent;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class ProgressBar extends Displayable
{
	public static class Builder
	{
		private ProgressBar pbr;

		public Builder(String name, Texture tex)
		{
			pbr = new ProgressBar(name, tex);
		}

		public Builder size(Point size)
		{
			pbr.width = size.x;
			pbr.height = size.y;
			return this;
		}

		public Builder position(Point pos)
		{
			pbr.x = pos.x;
			pbr.y = pos.y;
			return this;
		}

		public Builder yAxis(boolean yAxis)
		{
			pbr.yAxis = yAxis;
			return this;
		}

		public Builder inverted(boolean inverted)
		{
			pbr.inverted = inverted;
			return this;
		}

		public Builder normalizedMappingMin(float normMapping)
		{
			pbr.mappingMin = normMapping;
			return this;
		}

		public Builder normalizedMappingMax(float normMapping)
		{
			pbr.mappingMax = normMapping;
			return this;
		}

		public Builder currProgress(float currProgress)
		{
			pbr.currProgress = currProgress;
			return this;
		}

		public ProgressBar build()
		{
			return pbr;
		}
	}

	private ProgressBar(String name, Texture tex)
	{
		super(name, "", new Point(0, 0), new Point(0, 0), null);
		this.texture = tex;
	}

	@Override
	public void act(float delta)
	{
		if (!this.events.isEmpty())
		{
			if (!this.events.peek().update())
			{
				this.events.removeFirst();

				if (!this.events.isEmpty())
				{
					this.events.peek().start();
				}
			}
		}
	}

	@Override
	public void draw(SpriteBatch renderer, float alpha)
	{
		if (this.visible)
		{
			// The y axis is not subject to texCoordMapping
			if (this.yAxis)
			{
				renderer.draw(this.texture, this.x, this.y, this.width, this.height * this.currProgress,
						0.0f, 1.0f, 1.0f, 1.0f - this.currProgress);
			}
			else
			{
				if (this.inverted)
				{
					float invP = (1.0f - this.currProgress);
					float xt = this.width * invP;
					float u1 = this.mappingMin + (this.mappingMax - this.mappingMin) * invP;
					renderer.draw(this.texture, this.x + xt, this.y, this.width * this.currProgress,
							this.height, u1, 1.0f, this.mappingMax, 0.0f);
				}
				else
				{
					renderer.draw(this.texture, this.x, this.y, this.width * this.currProgress,
							this.height, 0, 1.0f, this.mappingMax * this.currProgress, 0.0f);
				}
			}
		}
	}

	public void addProgressUpdateEvent(float newProgress)
	{
		if (newProgress >= 1.0f)
		{
			newProgress = 1.0f;
		}

		if (this.events.peek() != null)
		{
			this.events.add(new ProgressBarEvent(this, ((ProgressBarEvent) this.events.peek()).getDestValue(), newProgress, false));
		}
		else
		{
			this.events.add(new ProgressBarEvent(this, this.currProgress, newProgress, false));
		}

		if (!this.events.peek().isActive())
		{
			this.events.peek().start();
		}
	}

	public void updateProgress(float progress)
	{
		this.currProgress = progress;
	}

	public boolean isEnqueued()
	{
		return !this.events.isEmpty();
	}

	public void setTexture(Texture t)
	{
		this.texture = t;
	}

	public float getCurrProgress()
	{
		return this.currProgress;
	}

	@Override
	public ProgressBar hit(float arg0, float arg1)
	{
		// Progress bar does not handle input
		return null;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2)
	{
		// Progress bar does not handle input
		return false;
	}

	@Override
	public void touchDragged(float arg0, float arg1, int arg2)
	{
		// Progress bar does not handle input
	}

	@Override
	public void touchUp(float arg0, float arg1, int arg2)
	{
		// Progress bar does not handle input
	}

	private LinkedList<EventBase> events = new LinkedList<EventBase>();
	private float mappingMin;
	private float mappingMax;
	private Texture texture;
	private float currProgress;
	private boolean yAxis;
	private boolean inverted = false;
}
