package com.handknittedapps.honeycombmatchthree.logic.modes;

import java.util.ArrayList;
import java.util.HashMap;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignProgressRepository;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.access.MissionRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class CampaignMode extends PlayMode
{
	public CampaignMode(Player play)
	{
		super(play);

		MissionRepository missionAccess = new MissionRepository();
		this.missions = missionAccess.loadCampaignFromFile(this.getPlayer());
	}

	public void setMissionAsCompleted(int id)
	{
		Mission m = this.missions.get(id);
		m.setCompleted(true);

		this.refreshUnlockables();
	}

	public void refreshUnlockables()
	{
		this.calculateUnlockedBonusTiers();
		this.calculateUnlockedSpecialBlocks();
		this.calculateUnlockedModes();
		this.calculateUnlockedThemes();
	}

	public ArrayList<Mission> getCompletedMissionsWithCurrent()
	{
		ArrayList<Mission> ret = this.getCompletedMissions();

		if (this.stageId > 0)
		{
			Mission currentMission = this.missions.get(this.stageId);
			if (!ret.contains(currentMission))
			{
				ret.add(currentMission);
			}
		}

		return ret;
	}

	public ArrayList<Mission> getAvailableMissions(boolean includeCompleted)
	{
		ArrayList<Mission> avail = new ArrayList<Mission>();
		for (Mission m : this.missions.values())
		{
			if (m.isAvailable())
			{
				if (includeCompleted || !m.isCompleted())
				{
					avail.add(m);
				}
			}
		}
		return avail;
	}

	public ArrayList<Mission> getCompletedMissions()
	{
		ArrayList<Mission> comp = new ArrayList<Mission>();
		for (Mission m : this.missions.values())
		{
			if (m.isCompleted())
			{
				comp.add(m);
			}
		}
		return comp;
	}

	public boolean isCompleted()
	{
		for (Mission m : this.missions.values())
		{
			if (!m.isCompleted())
			{
				return false;
			}
		}

		return true;
	}

	/** Reset all the missions states. **/
	public void reset()
	{
		for (Mission m : this.missions.values())
		{
			if (m.isCompleted())
			{
				m.reset();
			}
		}
	}

	@Override
	public void onStageFinished(GameStats score)
	{
		Mission m = this.missions.get(this.stageId);
		if (m.getGameMode().hasWon())
		{
			CampaignProgressRepository progressController = new CampaignProgressRepository();
			progressController.completeMission(m.getId(), this);

			CampaignScoreRepository controller = new CampaignScoreRepository(HoneycombMatchThree.name, this.getMissionCount());
			controller.loadGameScores();
			controller.onGameFinished(score, this.stageId);
			controller.saveGameScores();
		}
	}

	private void calculateUnlockedBonusTiers()
	{
		ArrayList<MoveType> unlocked = new ArrayList<MoveType>();
		unlocked.add(MoveType.DefaultSwap);

		ArrayList<Mission> completedMissions = this.getCompletedMissionsWithCurrent();
		for (Mission m : completedMissions)
		{
			if (m.getUnlockedMove() != null)
			{
				unlocked.add(m.getUnlockedMove());
			}
		}

		this.unlockedBonuses = unlocked.toArray(new MoveType[0]);
	}

	private void calculateUnlockedSpecialBlocks()
	{
		ArrayList<BlockType> unlocked = new ArrayList<BlockType>();
		ArrayList<Mission> completedMissions = this.getCompletedMissionsWithCurrent();
		for (Mission m : completedMissions)
		{
			if (m.getUnlockedBlock() != null)
			{
				unlocked.add(m.getUnlockedBlock());
			}
		}

		this.unlockedBlocks = unlocked.toArray(new BlockType[0]);
	}

	private void calculateUnlockedThemes()
	{
		ArrayList<ThemeType> unlocked = new ArrayList<ThemeType>();
		unlocked.add(ThemeType.Neutral);

		ArrayList<Mission> completedMissions = this.getCompletedMissionsWithCurrent();
		for (Mission m : completedMissions)
		{
			if (m.getUnlockedTheme() != null)
			{
				unlocked.add(m.getUnlockedTheme());
			}
		}

		this.unlockedThemes = unlocked.toArray(new ThemeType[0]);
	}

	private void calculateUnlockedModes()
	{
		ArrayList<GameModeType> unlocked = new ArrayList<GameModeType>();
		unlocked.add(GameModeType.Casual);

		ArrayList<Mission> completedMissions = this.getCompletedMissionsWithCurrent();
		for (Mission m : completedMissions)
		{
			if (m.getUnlockedMode() != null)
			{
				unlocked.add(m.getUnlockedMode());
			}
		}

		this.unlockedModes = unlocked.toArray(new GameModeType[0]);
	}

	@Override
	public void setStageId(int stageId)
	{
		super.setStageId(stageId);
		this.refreshUnlockables();
	}

	@Override
	public ThemeType getTheme()
	{
		return this.missions.get(this.stageId).getTheme();
	}

	@Override
	public PlayModeType getPlayModeType()
	{
		return PlayModeType.Campaign;
	}

	@Override
	public GameMode getGameMode()
	{
		return this.missions.get(this.stageId).getGameMode();
	}

	@Override
	public GameStats getScore()
	{
		CampaignScoreRepository score = new CampaignScoreRepository(HoneycombMatchThree.name, this.getMissionCount());
		score.loadGameScores();
		return score.getScore(this.stageId);
	}

	// Accessors
	public Mission getMission(int id) { return this.missions.get(id); }
	public int getMissionCount() {return this.missions.size(); }

	private HashMap<Integer, Mission> missions = new HashMap<Integer, Mission>();
}
