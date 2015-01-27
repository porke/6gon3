package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.badlogic.gdx.Gdx;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;

public class ClassicMode extends GameMode
{
	ClassicMode(final Player play, double winScoreLimit, float pointsLostPerSecond)
	{
		super(play);
		this.scoreLimit = (int) winScoreLimit;
		this.currScore = (int) winScoreLimit / 2;
		this.pointsLostPerSecond = pointsLostPerSecond;
	}

	@Override
	public void onScore(final MoveDestructionSequences seq)
	{
		int score = seq.getScore();
		this.currScore += score;
		this.player.onScore(score, this.energyMultiplier, seq);
	}

	@Override
	public void onFrame()
	{
		super.onFrame();
		this.currScore -= Gdx.graphics.getDeltaTime() * this.pointsLostPerSecond;
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
		return this.getProgress();
	}

	@Override
	public boolean hasEnded()
	{
		return (this.getProgress() >= 1.0f)
				|| this.getLoseConditionLeft() <= 0.0f
				|| this.quit
				|| this.mapLockLose;
	}

	@Override
	public GameModeType getModeType()
	{
		return GameModeType.Classic;
	}

	@Override
	public boolean hasWon()
	{
		return this.hasEnded()
				&& this.getProgress() >= 1.0f
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
		return this.pointsLostPerSecond;
	}

	@Override
	public void reset()
	{
		super.reset();
		this.currScore =  this.scoreLimit / 2;
	}

	private final int scoreLimit;
	private float currScore;
	private float pointsLostPerSecond;
}
