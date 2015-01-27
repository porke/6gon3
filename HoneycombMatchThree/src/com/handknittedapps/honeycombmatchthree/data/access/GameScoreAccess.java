package com.handknittedapps.honeycombmatchthree.data.access;

import com.handknittedapps.honeycombmatchthree.data.model.GameStats;


public interface GameScoreAccess
{
	public void saveGameScores();
	public void loadGameScores();
	public void resetGameScores();
	public void onGameFinished(GameStats score, int stageOrMission);
	public GameStats getScore(int scoreRank);
	public int getNumHighscores();
}
