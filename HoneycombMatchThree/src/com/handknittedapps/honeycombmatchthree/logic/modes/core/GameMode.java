package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.badlogic.gdx.Gdx;

import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;

public abstract class GameMode
{
	protected GameMode(Player play)
	{
		this.player = play;
		this.energyMultiplier = 1.0f;
	}

	// Real time update (ret = is endgame)
	public void onFrame()
	{
		if (this.energyMultiplier > 1.0f)
		{
			this.energyMultiplierDuration -= Gdx.graphics.getDeltaTime();
			if (this.energyMultiplierDuration <= 0.0f)
			{
				this.energyMultiplier = 1.0f;
			}
		}
	}

	public void reset()
	{
		this.energyMultiplier = 1.0f;
		this.energyMultiplierDuration = 0.0f;
		this.quit = false;
		this.mapLockLose = false;
		this.player.resetEnergy();
	}

	// Turn based update
	public void onMove()
	{
		// Does nothing in this mode
	}

	public void enableEnergyMultiplication(float factor, float timeInSeconds)
	{
		this.energyMultiplierDuration = timeInSeconds;
		this.energyMultiplier = factor;
	}

	public abstract void onScore(MoveDestructionSequences seq);
	public abstract GameModeType getModeType();
	public abstract float getProgress();
	public abstract float getLoseConditionLeft();
	public abstract boolean hasEnded();
	public abstract boolean hasWon();
	public abstract int getWinCondition();
	public abstract float getLoseCondition();

	public boolean hasQuit() { return this.quit; }
	public boolean isMapLocked() {return this.mapLockLose;}
	public Player getPlayer() { return this.player; }
	public float getEnergyMultiplier() { return this.energyMultiplier; }

	public void quit() { this.quit = true; }
	public void mapLockLose() {this.mapLockLose = true;}

	protected Player player;
	protected float energyMultiplier;
	protected float energyMultiplierDuration;
	protected boolean quit = false;
	protected boolean mapLockLose = false;
}
