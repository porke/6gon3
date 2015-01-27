package com.handknittedapps.honeycombmatchthree.data.access;

import java.util.HashMap;
import java.util.Map;

import com.handknittedapps.honeycombmatchthree.data.model.GameStats;

public class CampaignScoreRepository implements GameScoreAccess
{
	private class SettingsImpl extends GenericKeyValuePairAccess
	{
		public SettingsImpl(boolean readOnly, String rootSettingDir, String settingsFile)
		{
			super(readOnly, rootSettingDir, settingsFile);
		}

		@Override
		protected void generateDefaults()
		{
			for (int i = 0; i < CampaignScoreRepository.this.missionCount; ++i)
			{
				int rank = i + 1;
				this.setSetting("date_" + rank, 0L);
				this.setSetting("totalBonusesUsed_" + rank, 0);
				this.setSetting("totalMoves_" + rank, 0);
				this.setSetting("totalScore_" + rank, 0);
				this.setSetting("totalTimeSpent_" + rank, 0);
			}
		}
	};

	public CampaignScoreRepository(String rootSettingDir, int numMissions)
	{
		this.missionCount = numMissions;
		this.settings = new SettingsImpl(false, rootSettingDir, "campaign_score.xml");
	}

	@Override
	public void saveGameScores()
	{
		for (int i = 0; i < this.missionCount; ++i)
		{
			int rank = i + 1;

			long date = this.scores.get(rank).getDate();
			int totalBonusesUsed = this.scores.get(rank).getTotalBonusesUsed();
			int totalMoves = this.scores.get(rank).getTotalMoves();
			int totalScore = this.scores.get(rank).getTotalScore();
			int totalTimeSpent = this.scores.get(rank).getTotalTimeSpent();

			this.settings.setSetting("date_" + rank, date);
			this.settings.setSetting("totalBonusesUsed_" + rank, totalBonusesUsed);
			this.settings.setSetting("totalMoves_" + rank, totalMoves);
			this.settings.setSetting("totalScore_" + rank, totalScore);
			this.settings.setSetting("totalTimeSpent_" + rank, totalTimeSpent);
		}

		this.settings.save();
	}

	@Override
	public void loadGameScores()
	{
		this.scores.clear();

		if(!this.settings.load())
		{
			this.settings.generateDefaults();
		}

		for (int i = 0; i < this.missionCount; ++i)
		{
			int rank = i + 1;
			long date = this.settings.getSetting("date_" + rank);
			int totalBonusesUsed = this.settings.getSetting("totalBonusesUsed_" + rank);
			int totalMoves = this.settings.getSetting("totalMoves_" + rank);
			int totalScore = this.settings.getSetting("totalScore_" + rank);
			int totalTimeSpent = this.settings.getSetting("totalTimeSpent_" + rank);

			GameStats model = new GameStats();
			model.setDate(date);
			model.setTotalBonusesUsed(totalBonusesUsed);
			model.setTotalMoves(totalMoves);
			model.setTotalScore(totalScore);
			model.setTotalTimeSpent(totalTimeSpent);

			this.scores.put(rank, model);
		}
	}

	@Override
	public void resetGameScores()
	{
		this.scores.clear();

		for (int i = 0; i < this.missionCount; ++i)
		{
			int rank = i + 1;
			this.scores.put(rank, new GameStats());
		}
	}

	@Override
	public void onGameFinished(GameStats score, int stageOrMission)
	{
		GameStats bestMissionScore = this.getScore(stageOrMission);

		// If the score is greater than the current one, overwrite it
		// Note: Inverted comparer so -1 is being checked
		if (new GameStats.TotalScoreInverseComparer().compare(score, bestMissionScore) == -1)
		{
			bestMissionScore.setTotalScore(score.getTotalScore());
		}

		// If the time is shorter than the current one, overwrite it
		if (new GameStats.TotalTimeComparer().compare(score, bestMissionScore) == -1)
		{
			bestMissionScore.setTotalTimeSpent(score.getTotalTimeSpent());
		}

		// If the move count is smaller than the current one, overwrite it
		if (new GameStats.TotalMoveCountComparer().compare(score, bestMissionScore) == -1)
		{
			bestMissionScore.setTotalMoves(score.getTotalMoves());
		}

		this.scores.remove(stageOrMission);
		this.scores.put(stageOrMission, score);
	}

	@Override
	public GameStats getScore(int missionId)
	{
		return this.scores.get(missionId);
	}

	public int getNumHighscores()
	{
		return this.scores.size();
	}

	public GameStats getTotals()
	{
		GameStats totalScore = new GameStats();

		for (int i = 0; i < this.missionCount; ++i)
		{
			int rank = i + 1;
			totalScore = totalScore.add(this.getScore(rank));
		}

		return totalScore;
	}

	private Map<Integer, GameStats> scores = new HashMap<Integer, GameStats>();
	private int missionCount;
	private GenericKeyValuePairAccess settings;
}
