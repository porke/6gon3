package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.handknittedapps.honeycombmatchthree.logic.Player;

public class SurvivalMode extends CasualMode
{
	SurvivalMode(Player play, double scoreLimit, int initMana)
	{
		super(play, scoreLimit);
		this.initialNumTurns = initMana;
		this.turnsLeftCount = initMana;
	}

	@Override
	public void onMove()
	{
		--this.turnsLeftCount;
	}

	@Override
	public GameModeType getModeType()
	{
		return GameModeType.Survival;
	}

	@Override
	public float getLoseConditionLeft()
	{
		float ret = (float) this.turnsLeftCount / (float) this.initialNumTurns;
		return  (ret >= 0.0f) ? ret : 0.0f;
	}

	@Override
	public boolean hasEnded()
	{
		return this.getProgress() >= 1.0f
				|| this.turnsLeftCount <= 0
				|| this.quit
				|| this.mapLockLose;
	}

	@Override
	public void reset()
	{
		super.reset();
		this.turnsLeftCount = this.initialNumTurns;
	}

	@Override
	public float getLoseCondition()
	{
		return this.initialNumTurns;
	}

	@Override
	public boolean hasWon()
	{
		return this.turnsLeftCount > 0
				&& this.getProgress() >= 1.0f
				&& !this.mapLockLose;
	}

	private final int initialNumTurns;
	private int turnsLeftCount;
}
