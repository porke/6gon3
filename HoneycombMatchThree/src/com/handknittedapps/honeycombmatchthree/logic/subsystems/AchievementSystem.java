package com.handknittedapps.honeycombmatchthree.logic.subsystems;

import com.handknittedapps.honeycombmatchthree.external.AchievementHandler;
import com.handknittedapps.honeycombmatchthree.external.types.Achievements;
import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.DestructionType;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class AchievementSystem extends GameSubsystem
{
	private AchievementHandler handler;

	public AchievementSystem(AchievementHandler handler, GameSubsystem next)
	{
		super(next);
		this.handler = handler;
	}

	@Override
	public void onGameStart(PlayModeType pmt, GameModeType gmt, Object data)
	{
		if (this.next != null)
		{
			this.next.onGameStart(pmt, gmt, data);
		}
	}

	@Override
	public void onEndgame(GameMode gm)
	{
		if (this.next != null)
		{
			this.next.onEndgame(gm);
		}
	}

	@Override
	public void onMove(GameMode gm)
	{
		if (this.next != null)
		{
			this.next.onMove(gm);
		}
	}

	@Override
	public void onBonusUsed(Player player, MoveType mt, Block targetBlock)
	{
		if (this.next != null)
		{
			this.next.onBonusUsed(player, mt, targetBlock);
		}
	}

	@Override
	public void onScore(GameMode gm, MoveDestructionSequences mts)
	{
		unlockBlockAchievements(mts);
		if (this.next != null)
		{
			this.next.onScore(gm, mts);
		}
	}

	private void unlockBlockAchievements(MoveDestructionSequences mts)
	{
		if (mts.getCurrentCombo() >= 4)
		{
			this.handler.unlockAchievement(Achievements.FiveSequencees);
		}
		else if (mts.getCurrentCombo() >= 2)
		{
			this.handler.unlockAchievement(Achievements.ThreeSequences);
		}

		for(int i = 0; i < mts.numSequences(); ++i)
		{
			DestructionSequence seq = mts.getDestructionSequence(i);
			if (seq.getType() == DestructionType.Match)
			{
				if (seq.size() > 4)
				{
					this.handler.unlockAchievement(Achievements.FiveBlocksInSequence);
					break;
				}
			}
		}
	}
}
