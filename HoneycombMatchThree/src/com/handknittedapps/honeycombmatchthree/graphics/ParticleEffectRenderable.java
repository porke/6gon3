package com.handknittedapps.honeycombmatchthree.graphics;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectRenderable extends Actor
{
	public ParticleEffectRenderable(ParticleEffect eff)
	{
		this.system = eff;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		this.system.draw(batch);
	}

	@Override
	public void act(float delta)
	{
		this.system.update(delta);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) { return false; }

	@Override
	public void touchUp(float x, float y, int pointer) {	}

	@Override
	public void touchDragged(float x, float y, int pointer)  {	}

	@Override
	public Actor hit(float x, float y) { return null;	}

	public ParticleEffect getSystem() { return this.system; }

	private ParticleEffect system;
}
