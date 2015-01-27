package com.handknittedapps.honeycombmatchthree.data.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeFactory;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class MissionRepository
{
	private void loadMission(Element data, Player player, HashMap<Integer, Mission> missions) throws IOException
	{
		GameMode gameMode = null;
		GameModeType mode = GameModeType.fromString(data.getAttribute("mode"));
		double winSpec = Double.valueOf(data.getAttribute("winSpec"));
		double loseSpec = Double.valueOf(data.getAttribute("loseSpec"));
		int missionId = Integer.valueOf(data.getAttribute("id"));
		ThemeType theme = ThemeType.fromString(data.getAttribute("tileset"));
		String name = data.getAttribute("name");
		String briefing = data.getChildByName("briefing").getText();
		int colors = data.getIntAttribute("colors");

		double loseSpec2 = (mode == GameModeType.Hardcore) ? Float.valueOf(data.getAttribute("loseSpec2")) : 0.0;
		double[] loseSpecArray = new double[]{loseSpec, loseSpec2};
		gameMode = GameModeFactory.createGameMode(mode, player, winSpec, loseSpecArray);

		// Prerequisites
		// Important!
		// The prerequisite missions must be placed before the current one in the
		// xml data file or else they will not work correctly
		Element preElem = data.getChildByName("prerequisites");
		int numPrerequsistes = preElem.getChildCount();
		ArrayList<Mission> prereqs = new ArrayList<Mission>();
		for (int i = 0; i < numPrerequsistes; ++i)
		{
			int prereq = Integer.valueOf(preElem.getChild(i).getAttribute("id"));
			prereqs.add(missions.get(prereq));
		}

		// Unlocked
		Element unlockElem = data.getChildByName("unlocks");
		MoveType um  = MoveType.fromString(unlockElem.getAttribute("bonus"));
		GameModeType ut = GameModeType.fromString(unlockElem.getAttribute("mode"));
		BlockType ub = BlockType.fromString(unlockElem.getAttribute("block"));
		ThemeType uh = ThemeType.fromString(unlockElem.getAttribute("theme"));

		Mission mission = new Mission(missionId, name, briefing, theme, gameMode);
		mission.setUnlockedData(ub, um, ut, uh);
		mission.setPrereqs(prereqs);
		mission.setAvailableColors(colors);

		missions.put(missionId, mission);
	}

	/** Loads the campaign structure from a file. **/
	public HashMap<Integer, Mission> loadCampaignFromFile(Player player)
	{
		HashMap<Integer, Mission> missions = new HashMap<Integer, Mission>();

		XmlReader read = new XmlReader();
		Element root;
		try
		{
			root = read.parse(Gdx.files.internal("data/campaign.xml"));
			int numMissions = root.getChildCount();
			for (int i = 0; i < numMissions; ++i)
			{
				Element missionData = root.getChild(i);
				this.loadMission(missionData, player, missions);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return missions;
	}
}
