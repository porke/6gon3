package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import com.handknittedapps.honeycombmatchthree.logic.Player;

public class GameModeFactory
{
	private GameModeFactory()
	{
		// Locked class
	}

	public static GameMode createGameMode(GameModeType type, Player player, double winSpec, double loseSpec[])
	{
		switch (type)
		{
			case Casual:
				return new CasualMode(player, winSpec);
			case Hardcore:
				return new HardcoreMode(player, winSpec, (float) loseSpec[1], (int) loseSpec[0]);
			case Survival:
				return new SurvivalMode(player, winSpec, (int) loseSpec[0]);
			case TimeAttack:
				return new TimeAttackMode(player, winSpec, (float) loseSpec[0]);
			case Classic:
				return new ClassicMode(player, winSpec, (float) loseSpec[0]);
			default:
				throw new IllegalArgumentException("Invalid game mode type.");
		}
	}
}
