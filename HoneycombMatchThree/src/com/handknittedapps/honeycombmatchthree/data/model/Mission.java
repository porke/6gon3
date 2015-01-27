package com.handknittedapps.honeycombmatchthree.data.model;

import java.util.ArrayList;

import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class Mission
{
	public Mission(int id, String name, String briefing, ThemeType theme, GameMode gm)
	{
		this.id = id;
		this.briefing = briefing;
		this.name = name;
		this.scenario = gm;
		this.theme = theme;
	}

	public void setUnlockedData(BlockType ub, MoveType um, GameModeType ut, ThemeType uh)
	{
		this.unlocksBlock = ub;
		this.unlocksMove = um;
		this.unlocksMode = ut;
		this.unlocksTheme = uh;
	}

	public void setPrereqs(ArrayList<Mission> prereqs)
	{
		this.prereqs = prereqs;
	}

	public void setAvailableColors(int colors)
	{
		this.availableColors = colors;
	}

	public String getName() { return this.name; }
	public String getBriefing() { return this.briefing; }
	public BlockType getUnlockedBlock() { return this.unlocksBlock; }
	public MoveType getUnlockedMove() { return this.unlocksMove; }
	public GameMode getGameMode() { return this.scenario; }
	public GameModeType getUnlockedMode() { return this.unlocksMode; }
	public ThemeType getUnlockedTheme() { return this.unlocksTheme; }
	public int getAvailableColors() {return this.availableColors; }
	public int getId() { return this.id; }
	public boolean isCompleted() { return this.completed; }
	public ThemeType getTheme() { return this.theme; }
	public boolean isAvailable()
	{
		for (Mission m : this.prereqs)
		{
			if (!m.isCompleted())
			{
				return false;
			}
		}

		return true;
	}

	public void reset()
	{
		this.completed = false;
		this.scenario.reset();
	}

	public void setCompleted(boolean comp) { this.completed = comp; }

	private int id;
	private String briefing;
	private String name;
	private GameMode scenario;
	private ThemeType theme;
	private int availableColors = BlockColour.NumColours;
	private boolean completed;
	private ArrayList<Mission> prereqs = new ArrayList<Mission>();

	/** Block unlocked by the mission. **/
	private BlockType unlocksBlock;
	/** Move unlocked by the mission. **/
	private MoveType unlocksMove;
	/** Mode unlocked by the mission. **/
	private GameModeType unlocksMode;
	/** Theme unlocked by the mission. **/
	private ThemeType unlocksTheme;
}
