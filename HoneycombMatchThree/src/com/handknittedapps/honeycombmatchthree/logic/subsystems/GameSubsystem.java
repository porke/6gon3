package com.handknittedapps.honeycombmatchthree.logic.subsystems;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;


public abstract class GameSubsystem
{
	public GameSubsystem(GameSubsystem nextSubsystem)
	{
		this.setNext(nextSubsystem);
	}

	public abstract void onGameStart(PlayModeType pmt, GameModeType gmt, Object data);
	public abstract void onEndgame(GameMode gm);
	public abstract void onMove(GameMode gm);
	public abstract void onBonusUsed(Player player, MoveType mt, Block targetBlock);
	public abstract void onScore(GameMode gm, MoveDestructionSequences mts);

	public void setNext(GameSubsystem next)
	{
		this.next = next;
	}

	protected GameSubsystem next;
}
