package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.badlogic.gdx.Gdx;

import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;

public class HardcoreMode extends GameMode
{
	HardcoreMode(Player play, double scoreToObtain, float timeLimit, int turnLimit)
	{
		super(play);
		this.initialNumTurns = turnLimit;
		this.numTurnsLeft = turnLimit;
		this.timeLeft = timeLimit;
		this.initialTime = timeLimit;
		this.scoreToObtain = (int) scoreToObtain;
		this.currScore = 0;
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
		int seqScore = seq.getScore();
		this.player.onScore(seqScore, this.energyMultiplier, seq);
		this.currScore += seqScore;
	}

	@Override
	public GameModeType getModeType()
	{
		return GameModeType.Hardcore;
	}

	@Override
	public float getProgress()
	{
		float ret = (float) this.currScore / (float) this.scoreToObtain;
		return ret > 1.0f ? 1.0f : ret;
	}

	/** The time lose condition.
	 * @return Time left to initial time ratio [0.0; 1.0]
	 * **/
	@Override
	public float getLoseConditionLeft()
	{
		return this.timeLeft / this.initialTime;
	}

	// The mana lose condition
	public float getLoseConditionLeftMana()
	{
		float ret = (float) this.numTurnsLeft / (float) this.initialNumTurns;
		return  (ret >= 0.0f) ? ret : 0.0f;
	}

	@Override
	public boolean hasEnded()
	{
		return this.hasWon()
				|| this.getLoseConditionLeft() <= 0.0f
				|| this.numTurnsLeft <= 0
				|| this.quit
				|| this.mapLockLose;
	}

	@Override
	public boolean hasWon()
	{
		return this.getProgress() >= 1.0f;
	}

	@Override
	public void onMove()
	{
		--this.numTurnsLeft;
	}

	@Override
	public void reset()
	{
		super.reset();
		this.currScore = 0;
		this.numTurnsLeft = this.initialNumTurns;
		this.timeLeft = this.initialTime;
	}

	@Override
	public int getWinCondition()
	{
		return this.scoreToObtain;
	}

	@Override
	public float getLoseCondition()
	{
		return this.initialTime;
	}

	public float getLoseConditionMana()
	{
		return this.initialNumTurns;
	}

	private final int initialNumTurns;
	private int numTurnsLeft;
	private final float initialTime;
	private float timeLeft;
	private final int scoreToObtain;
	private int currScore;
}
