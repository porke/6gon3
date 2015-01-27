package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;

public class CasualMode extends GameMode
{
	CasualMode(final Player play, double winScoreLimit)
	{
		super(play);
		this.scoreLimit = (int) winScoreLimit;
	}

	@Override
	public void onScore(final MoveDestructionSequences seq)
	{
		int score = seq.getScore();
		this.currScore += score;
		this.player.onScore(score, this.energyMultiplier, seq);
	}

	@Override
	public float getProgress()
	{
		float ret = (float) this.currScore / (float) this.scoreLimit;
		return ret > 1.0f ? 1.0f : ret;
	}

	@Override
	public float getLoseConditionLeft()
	{
		return 0.0f;
	}

	@Override
	public boolean hasEnded()
	{
		return (this.getProgress() >= 1.0f)
				|| this.quit
				|| this.mapLockLose;
	}

	@Override
	public GameModeType getModeType()
	{
		return GameModeType.Casual;
	}

	@Override
	public boolean hasWon()
	{
		return this.hasEnded()
				&& !this.quit
				&& !this.mapLockLose;
	}

	@Override
	public int getWinCondition()
	{
		return this.scoreLimit;
	}

	@Override
	public float getLoseCondition()
	{
		return 0;
	}

	@Override
	public void reset()
	{
		super.reset();
		this.currScore =  0;
	}

	private final int scoreLimit;
	private int currScore;
}
