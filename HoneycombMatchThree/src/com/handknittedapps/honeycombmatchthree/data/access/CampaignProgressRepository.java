package com.handknittedapps.honeycombmatchthree.data.access;

import java.util.ArrayList;
import java.util.Collection;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;

public class CampaignProgressRepository extends GenericKeyValuePairAccess
{
	public CampaignProgressRepository()
	{
		super(false, HoneycombMatchThree.name, "user_progress.xml");

		if (!this.load())
		{
			this.generateDefaults();
		}
	}

	public void saveState(CampaignMode playMode)
	{
		this.keyValuePairs.clear();
		ArrayList<Mission> missions = playMode.getCompletedMissions();
		for(Mission mission : missions)
		{
			this.keyValuePairs.put(String.valueOf(mission.getId()), mission.getId());
		}

		this.save();
	}

	public void loadState(CampaignMode playMode)
	{
		this.load();

		Collection<Object> values = this.keyValuePairs.values();
		for(Object completedId : values)
		{
			int missionId = Integer.valueOf(completedId.toString());
			playMode.setMissionAsCompleted(missionId);
		}

		playMode.refreshUnlockables();
	}

	public void completeMission(int missionId, CampaignMode playMode)
	{
		this.keyValuePairs.put(String.valueOf(missionId), missionId);
		playMode.setMissionAsCompleted(missionId);
		this.save();
	}

	@Override
	protected void generateDefaults()
	{
		// No missions unlocked by default
	}
}
