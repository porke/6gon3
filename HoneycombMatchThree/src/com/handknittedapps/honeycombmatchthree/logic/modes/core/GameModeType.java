package com.handknittedapps.honeycombmatchthree.logic.modes.core;


// GameModeType refers to a single map
public enum GameModeType
{
	Casual("Casual"),
	TimeAttack("Time attack"),
	Survival("Survival"),
	Hardcore("Hardcore"),
	Classic("Classic");

	public static GameModeType fromString(String str)
	{
		GameModeType[] modes = GameModeType.values();
		for (GameModeType m : modes)
		{
			if (m.name().equals(str))
			{
				return m;
			}
		}

		return null;
	}

	private GameModeType(String type)
	{
		this.displayedName = type;
	}

	public final String displayedName;
}
