package com.handknittedapps.honeycombmatchthree.external.types;

import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public enum Leaderboards
{
	TimeAttackScore(11743),
	SurvivalScore(11751),
	HardcoreScore(11753),
	ClassicScore(11755),
	CasualScore(11833),
	CampaignLeastMoves(11757),
	CampaignLeastTime(11759),
	CampaignScore(11761);

	public final int Id;

	public static Leaderboards fromGameModeType(GameModeType type)
	{
		Leaderboards leaderboardType;
		switch (type)
		{
			case Classic:
				leaderboardType = Leaderboards.ClassicScore;
				break;
			case Hardcore:
				leaderboardType = Leaderboards.HardcoreScore;
				break;
			case TimeAttack:
				leaderboardType = Leaderboards.TimeAttackScore;
				break;
			case Survival:
				leaderboardType = Leaderboards.SurvivalScore;
				break;
			case Casual:
				leaderboardType = Leaderboards.CasualScore;
				break;
			default:
				leaderboardType = Leaderboards.ClassicScore;
		}

		return leaderboardType;
	}

	private Leaderboards(int id)
	{
		this.Id = id;
	}
}
