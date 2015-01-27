package com.handknittedapps.honeycombmatchthree.external;

import com.handknittedapps.honeycombmatchthree.external.types.Achievements;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;

public interface AchievementHandler
{
	void activateService();
	void showLeaderboard(Leaderboards type);
	void showAllLeaderboards();
	void unlockAchievement(Achievements achievement);
	void showAchievements();
	void submitScore(int score, Leaderboards type);
	boolean isLoggedIn();
}
