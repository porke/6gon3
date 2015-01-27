package com.handknittedapps.honeycombmatchthree.logic;

import java.util.Arrays;
import java.util.List;

import com.handknittedapps.honeycombmatchthree.external.AchievementHandler;
import com.handknittedapps.honeycombmatchthree.external.types.Achievements;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class CampaignAchievementUnlocker
{
	private CampaignMode campaign;
	private AchievementHandler handler;

	public CampaignAchievementUnlocker(CampaignMode campaign, AchievementHandler handler)
	{
		this.campaign = campaign;
		this.handler = handler;
	}

	public void onMissionCompleted()
	{
		unlockBonusesAchievements();
		unlockGameModesAchievements();
		unlockMissionsCompletedAchievements();
	}

	private void unlockBonusesAchievements()
	{
		List<MoveType> unlockedBonuses = Arrays.asList(this.campaign.getUnlockedBonuses());

		List<MoveType> redBonuses = Arrays.asList(MoveType.BombChange, MoveType.ClearColour, MoveType.RemoteSwap);
		List<MoveType> greenBonuses = Arrays.asList(MoveType.IncomeBoost, MoveType.RotateSwap, MoveType.Fear);
		List<MoveType> blueBonuses = Arrays.asList(MoveType.Conversion, MoveType.ColourChange, MoveType.DeleteLine);

		if (unlockedBonuses.containsAll(redBonuses))
		{
			this.handler.unlockAchievement(Achievements.AllRedBonuses);
		}

		if (unlockedBonuses.containsAll(greenBonuses))
		{
			this.handler.unlockAchievement(Achievements.AllGreenBonuses);
		}

		if (unlockedBonuses.containsAll(blueBonuses))
		{
			this.handler.unlockAchievement(Achievements.AllBlueBonuses);
		}
	}

	private void unlockGameModesAchievements()
	{
		if (this.campaign.getUnlockedModes().length == GameModeType.values().length)
		{
			this.handler.unlockAchievement(Achievements.AllGameModes);
		}
	}

	private void unlockMissionsCompletedAchievements()
	{
		final int missionsCompleted = this.campaign.getCompletedMissionsWithCurrent().size();

		if (this.campaign.isCompleted())
		{
			this.handler.unlockAchievement(Achievements.CampaignCompleted);
		}
		else if (missionsCompleted >= 15)
		{
			this.handler.unlockAchievement(Achievements.FifteenMissionCompleted);
		}
		else if (missionsCompleted >= 10)
		{
			this.handler.unlockAchievement(Achievements.TenMissionsCompleted);
		}
		else if (missionsCompleted >= 5)
		{
			this.handler.unlockAchievement(Achievements.FiveMissionsCompleted);
		}
	}
}
