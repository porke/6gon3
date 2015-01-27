package com.handknittedapps.honeycombmatchthree.graphics.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class HudTimeBasedParticleEffect extends Actor
{
	public HudTimeBasedParticleEffect(String systemName, float duration, Point position)
	{
		this.system = new ParticleEffect();
		this.system.load(Gdx.files.internal("graphics/particles/" + systemName + ".ps"), Gdx.files.internal("graphics/particles/"));
		this.system.setPosition(position.x, position.y);
		this.system.start();

		this.durationInSeconds = duration;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.system.update(delta);

		this.durationInSeconds -= delta;

		if (!dyingOut
			&& this.durationInSeconds <= 0.0f)
		{
			Array<ParticleEmitter> emitters = this.system.getEmitters();
			for (ParticleEmitter em : emitters)
			{
				em.setContinuous(false);
			}

			this.dyingOut = true;
		}

		if (this.system.isComplete())
		{
			this.markToRemove(true);
			this.system.dispose();
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		this.system.draw(batch);
	}

	public Actor hit(float x, float y) { return null; }

	private ParticleEffect system;
	private float durationInSeconds;
	private boolean dyingOut = false;
}
