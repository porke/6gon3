package com.handknittedapps.honeycombmatchthree.graphics.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class HudTurnBasedParticleEffect extends Actor implements Disposable
{
	public HudTurnBasedParticleEffect(String systemName, int duration, Point position)
	{
		super(systemName);
		this.system = new ParticleEffect();
		this.system.load(Gdx.files.internal("graphics/particles/" + systemName + ".ps"), Gdx.files.internal("graphics/particles/"));
		this.system.setPosition(position.x, position.y);
		this.system.start();

		this.durationInTurns = duration;
	}

	public int getDurationInTurns()
	{
		return this.durationInTurns;
	}

	public void setDurationInTurns(int newTurns)
	{
		this.durationInTurns = newTurns;

		if (this.durationInTurns <= 0)
		{
			Array<ParticleEmitter> emitters = this.system.getEmitters();
			for (ParticleEmitter em : emitters)
			{
				em.setContinuous(false);
			}
		}
	}

	// Inherited from Actor
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		this.system.draw(batch);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.system.update(delta);

		if (this.system.isComplete())
		{
			this.markToRemove(true);
		}
	}

	@Override
	public void dispose()
	{
		this.system.dispose();
		this.system = null;
	}

	public ParticleEffect getSystem() {return this.system; }

	// Unused
	@Override
	public boolean touchDown(float x, float y, int pointer) {	return false;	}

	@Override
	public void touchUp(float x, float y, int pointer) {		}

	@Override
	public void touchDragged(float x, float y, int pointer) {		}

	@Override
	public Actor hit(float x, float y) { return null; }

	private ParticleEffect system;
	private int durationInTurns;
}
