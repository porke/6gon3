package com.handknittedapps.honeycombmatchthree.handlers;

import android.app.Activity;

import com.handknittedapps.honeycombmatchthree.external.AchievementHandler;
import com.handknittedapps.honeycombmatchthree.external.types.Achievements;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmAchievement;
import com.swarmconnect.SwarmLeaderboard;
import com.swarmconnect.SwarmLeaderboard.GotLeaderboardCB;

public class SwarmStatsHandler implements AchievementHandler 
{

	public SwarmStatsHandler(Activity parent)
	{
		parentActivity = parent;
		Swarm.preload(this.parentActivity, this.SwarmAppId, this.SwarmAppKey);
	}
	
	@Override
	public void showLeaderboard(Leaderboards type)
	{
		activateService();
		SwarmLeaderboard.getLeaderboardById(type.Id, new GotLeaderboardCB() 
		{
			public void gotLeaderboard(SwarmLeaderboard leaderboard)
			{
				leaderboard.showLeaderboard();				
			}
		});
	}

	@Override
	public void showAllLeaderboards() 
	{
		Swarm.showLeaderboards();
	}

	@Override
	public void unlockAchievement(Achievements achievement) 
	{
		SwarmAchievement.unlock(achievement.Id);
	}

	@Override
	public void showAchievements() 
	{
		activateService();		
		Swarm.showAchievements();
	}

	@Override
	public void submitScore(int score, Leaderboards type) 
	{				
		SwarmLeaderboard.submitScore(type.Id, score);
	}

	@Override
	public void activateService()
	{
		Swarm.setAllNotificationsEnabled(true);
		this.setActive(true);		
		Swarm.init(this.parentActivity, this.SwarmAppId, this.SwarmAppKey);		
	}
	
	@Override
	public boolean isLoggedIn()
	{
		return Swarm.isLoggedIn();
	}
	
	public void setActive(boolean active)
	{
		if (active)
		{
			Swarm.setActive(this.parentActivity);
		}
		else
		{
			Swarm.setInactive(this.parentActivity);			
		}
	}
	
	private Activity parentActivity;
	
	private final int SwarmAppId = 7603;
	private final String SwarmAppKey = "6dd29ccc69b46cd7cc11b6ce0e413fbb";
}

