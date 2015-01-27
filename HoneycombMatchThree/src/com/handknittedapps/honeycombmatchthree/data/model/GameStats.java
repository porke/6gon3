package com.handknittedapps.honeycombmatchthree.data.model;

import java.util.Comparator;

public class GameStats
{
	/** The comparer compares scores based on total score value. */
	public static class TotalScoreInverseComparer implements Comparator<GameStats>
	{
		/** The comparison is inverted because the scores are to be sorted from the highest
		 * to the lowest. */
		@Override
		public int compare(GameStats l, GameStats r)
		{
			if (l.totalScore > r.totalScore)
			{
				return -1;
			}
			else if (l.totalScore < r.totalScore)
			{
				return 1;
			}

			return 0;
		}
	}

	public static class TotalMoveCountComparer implements Comparator<GameStats>
	{
		@Override
		public int compare(GameStats l, GameStats r)
		{
			if (l.totalMoves > r.totalMoves)
			{
				return 1;
			}
			else if (l.totalMoves > r.totalMoves)
			{
				return -1;
			}

			return 0;
		}
	}

	public static class TotalTimeComparer implements Comparator<GameStats>
	{
		@Override
		public int compare(GameStats l, GameStats r)
		{
			if (l.totalTimeSpent > r.totalTimeSpent)
			{
				return 1;
			}
			else if (l.totalTimeSpent < r.totalTimeSpent)
			{
				return -1;
			}

			return 0;
		}

	}

	/** The date is being set to the earlier one. */
	public GameStats add(GameStats add)
	{
		GameStats ret = new GameStats();
		ret.setTotalBonusesUsed(this.totalBonusesUsed + add.totalBonusesUsed);
		ret.setTotalMoves(this.totalMoves + add.totalMoves);
		ret.setTotalScore(this.totalScore + add.totalScore);
		ret.setTotalTimeSpent(this.totalTimeSpent + add.totalTimeSpent);
		ret.setDate(this.date > add.date ? this.date : add.date);

		return ret;
	}

	public boolean isZero()
	{
		return 	   totalScore == 0
				&& totalTimeSpent == 0
				&& totalBonusesUsed == 0
				&& totalMoves == 0
				&& date == 0;
	}

	public int getTotalScore()  {return this.totalScore; }
	public int getTotalTimeSpent()  { return this.totalTimeSpent; }
	public int getTotalMoves() { return this.totalMoves; }
	public long getDate() { return this.date; }
	public int getTotalBonusesUsed() { return this.totalBonusesUsed; }

	public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
	public void setTotalTimeSpent(int totalTimeSpent) { this.totalTimeSpent = totalTimeSpent; }
	public void setTotalMoves(int totalMoves) { this.totalMoves = totalMoves; }
	public void setDate(long date) { this.date = date; }
	public void setTotalBonusesUsed(int totalBonusesUsed) { this.totalBonusesUsed = totalBonusesUsed;}

	private int totalScore;
	private int totalTimeSpent;					// In seconds
	private int totalBonusesUsed;				// In seconds
	private int totalMoves;						// In seconds
	private long date;
}
