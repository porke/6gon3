package com.handknittedapps.honeycombmatchthree.external;

import com.handknittedapps.honeycombmatchthree.external.types.Achievements;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;

public class NullAchievementHandler implements AchievementHandler
{
	@Override
	public void activateService()
	{
		System.out.println("Activating achievements!");
		// Do nothing
	}

	@Override
	public void showLeaderboard(Leaderboards type)
	{
		// Do nothing
	}

	@Override
	public void showAllLeaderboards()
	{
		// Do nothing
	}

	@Override
	public void unlockAchievement(Achievements achievement)
	{
		// Do nothing
	}

	@Override
	public void showAchievements()
	{
		// Do nothing
	}

	@Override
	public void submitScore(int score, Leaderboards type)
	{
		// Do nothing
	}

	@Override
	public boolean isLoggedIn()
	{
		// Do nothing
		return true;
	}
}
