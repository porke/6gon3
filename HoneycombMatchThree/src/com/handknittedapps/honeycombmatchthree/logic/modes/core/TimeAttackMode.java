package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.badlogic.gdx.Gdx;

import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;

public class TimeAttackMode extends GameMode
{
	TimeAttackMode(Player play, double destScore, float timeLimit)
	{
		super(play);
		this.scoreLimit = (int) destScore;
		this.timeLeft = timeLimit;
		this.maxTime = timeLimit;
	}

	@Override
	public void onFrame()
	{
		super.onFrame();
		this.timeLeft -= Gdx.graphics.getDeltaTime();
	}

	@Override
	public void onScore(MoveDestructionSequences seq)
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
	public void reset()
	{
		super.reset();
		this.currScore = 0;
		this.timeLeft = this.maxTime;
	}

	@Override
	public float getLoseConditionLeft()
	{
		float ret = this.timeLeft / this.maxTime;
		return ret > 1.0f ? 1.0f : ret;
	}

	@Override
	public boolean hasEnded()
	{
		return this.getProgress() >= 1.0f
				|| this.getLoseConditionLeft() <= 0.0f
				|| this.quit
				|| this.mapLockLose;
	}

	@Override
	public GameModeType getModeType()
	{
		return GameModeType.TimeAttack;
	}

	@Override
	public int getWinCondition()
	{
		return this.scoreLimit;
	}

	@Override
	public float getLoseCondition()
	{
		return this.maxTime;
	}

	@Override
	public boolean hasWon()
	{
		return this.getProgress() >= 1.0f
				&& this.getLoseConditionLeft() > 0.0f
				&& !this.mapLockLose;
	}

	int currScore;
	private final float maxTime;
	private final int scoreLimit;
	private float timeLeft;
}
