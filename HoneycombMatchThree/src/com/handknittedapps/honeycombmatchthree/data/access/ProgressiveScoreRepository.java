package com.handknittedapps.honeycombmatchthree.data.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public class ProgressiveScoreRepository implements GameScoreAccess
{
	private static final int NumHighscores = 1;

	private class SettingsImpl extends GenericKeyValuePairAccess
	{
		public SettingsImpl(boolean readOnly, String rootSettingDir, String settingsFile)
		{
			super(readOnly, rootSettingDir, settingsFile);
		}

		@Override
		protected void generateDefaults()
		{
			for (int i = 0; i < ProgressiveScoreRepository.NumHighscores; ++i)
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

	public ProgressiveScoreRepository(GameModeType gameMode, String dir)
	{
		this.settings = new SettingsImpl(false, dir, gameMode.displayedName + "_scores" + ".xml");
	}

	@Override
	public void saveGameScores()
	{
		for (int i = 0; i < ProgressiveScoreRepository.NumHighscores; ++i)
		{
			long date = this.scores.get(i).getDate();
			int totalBonusesUsed = this.scores.get(i).getTotalBonusesUsed();
			int totalMoves = this.scores.get(i).getTotalMoves();
			int totalScore = this.scores.get(i).getTotalScore();
			int totalTimeSpent = this.scores.get(i).getTotalTimeSpent();

			int rank = i + 1;
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
		if(!this.settings.load())
		{
			this.settings.generateDefaults();
		}

		this.scores.clear();

		for (int i = 0; i < ProgressiveScoreRepository.NumHighscores; ++i)
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

			this.scores.add(model);
		}

		Collections.sort(this.scores, new GameStats.TotalScoreInverseComparer());
	}

	@Override
	public void resetGameScores()
	{
		this.scores.clear();

		for (int i = 0; i < ProgressiveScoreRepository.NumHighscores; ++i)
		{
			this.scores.add(new GameStats());
		}
	}

	@Override
	public void onGameFinished(GameStats score, int stageOrMission)
	{
		this.scores.add(score);
		Collections.sort(this.scores, new GameStats.TotalScoreInverseComparer());
		this.scores.remove(this.scores.size() - 1);
	}

	public int getNumHighscores()
	{
		return ProgressiveScoreRepository.NumHighscores;
	}

	@Override
	public GameStats getScore(int scoreRank)
	{
		return this.scores.get(scoreRank);
	}

	private List<GameStats> scores = new ArrayList<GameStats>();
	private GenericKeyValuePairAccess settings;
}
