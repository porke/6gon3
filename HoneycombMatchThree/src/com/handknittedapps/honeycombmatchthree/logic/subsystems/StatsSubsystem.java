package com.handknittedapps.honeycombmatchthree.logic.subsystems;

import java.util.Date;

import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class StatsSubsystem extends GameSubsystem
{
	public StatsSubsystem(GameSubsystem next, GameModeType gmt, PlayModeType pmt)
	{
		super(next);
		this.currentScore = new GameStats();
	}

	@Override
	public void onGameStart(PlayModeType pmt, GameModeType gmt, Object data)
	{
		this.gameStartTime = System.nanoTime();

		if (this.next != null)
		{
			this.next.onGameStart(pmt, gmt, data);
		}
	}

	@Override
	public void onEndgame(GameMode gm)
	{
		final int nano = 1000000000;
		int gameDuration = (int) ((System.nanoTime() - this.gameStartTime) / nano);
		this.currentScore.setTotalTimeSpent(gameDuration);
		Date currDate = new Date();
		this.currentScore.setDate(currDate.getTime());

		if (this.next != null)
		{
			this.next.onEndgame(gm);
		}
	}

	@Override
	public void onMove(GameMode gm)
	{
		int numMoves = this.currentScore.getTotalMoves();
		this.currentScore.setTotalMoves(numMoves + 1);
	}

	@Override
	public void onBonusUsed(Player player, MoveType mt, Block targetBlock)
	{
		int numBonusesUsed = this.currentScore.getTotalBonusesUsed();
		this.currentScore.setTotalBonusesUsed(numBonusesUsed + 1);

		if (this.next != null)
		{
			this.next.onBonusUsed(player, mt, targetBlock);
		}
	}

	@Override
	public void onScore(GameMode gm, MoveDestructionSequences mts)
	{
		if (mts != null)
		{
			int currScore = this.currentScore.getTotalScore();
			this.currentScore.setTotalScore(currScore + mts.getScore());
		}

		if (this.next != null)
		{
			this.next.onScore(gm, mts);
		}
	}

	public GameStats getScore() { return this.currentScore; }

	private long gameStartTime;
	private GameStats currentScore = new GameStats();
}
