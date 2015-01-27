package com.handknittedapps.honeycombmatchthree.graphics.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class HudBonusPushedParticleEffect extends Actor
{
	public HudBonusPushedParticleEffect(String id, Point position)
	{
		super(id);
		this.system = new ParticleEffect();
		this.system.load(Gdx.files.internal("graphics/particles/bonus_active.ps"), Gdx.files.internal("graphics/particles/"));
		this.system.setPosition(position.x, position.y);
		this.system.start();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.system.update(delta);

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
}
