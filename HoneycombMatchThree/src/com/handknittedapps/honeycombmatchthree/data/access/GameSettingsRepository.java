package com.handknittedapps.honeycombmatchthree.data.access;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public class GameSettingsRepository extends GenericKeyValuePairAccess
{
	public GameSettingsRepository()
	{
		super(true, HoneycombMatchThree.name, "data/game_constants.xml");

		if (!this.load())
		{
			this.generateDefaults();
		}
	}

	@Override
	protected void generateDefaults()
	{
		// Init game constants
		this.keyValuePairs.put("EnergyPerBlock", 1.0f);
		this.keyValuePairs.put("ScorePerBlock", 1.0f);
		this.keyValuePairs.put("ScorePerAdditionalBlocksFactor", 1.67f);
		this.keyValuePairs.put("SpecialBlockSpawnChance", 0.12f);
		this.keyValuePairs.put("GhostInGhostModeSpawnChance", 0.15f);

		// Progressive mode constants
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeWinSpec", 100.0f);
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeLoseSpec", 60.0f);
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeWinProgress", 50.0f);
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeLoseProgress", 15.0f);

		this.keyValuePairs.put(GameModeType.Casual + "ModeWinSpec", 200.0f);
		this.keyValuePairs.put(GameModeType.Casual + "ModeLoseSpec", 0.0f);
		this.keyValuePairs.put(GameModeType.Casual + "ModeWinProgress", 50.0f);
		this.keyValuePairs.put(GameModeType.Casual + "ModeLoseProgress", 0.0f);

		this.keyValuePairs.put(GameModeType.Survival + "ModeWinSpec", 10.0f);
		this.keyValuePairs.put(GameModeType.Survival + "ModeLoseSpec", 60.0f);
		this.keyValuePairs.put(GameModeType.Survival + "ModeWinProgress", 5.0f);
		this.keyValuePairs.put(GameModeType.Survival + "ModeLoseProgress", 20.0f);

		this.keyValuePairs.put(GameModeType.Hardcore + "ModeWinSpec", 10.0f);
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeLoseSpec1", 60.0f);
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeLoseSpec2", 5.0f);
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeWinProgress", 30.0f);
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeLoseProgress1", 60.0f);
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeLoseProgress2", 30.0f);

		// Mode descriptions
		this.keyValuePairs.put(GameModeType.Casual + "ModeDesc", "The goal in the Casual mode is to gain a specified number of points. There is no time limit.");
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeDesc", "The goal of the TimeAttack mode is to gain a specified number of points within a time limit.");
		this.keyValuePairs.put(GameModeType.Survival + "ModeDesc", "The goal of the Survival mode is to survive for enough turns. Every move consumes your mana, which you can gain by matching blocks. You lose if you run out of mana. The more turns have passed, the faster your mana will be depleted every move so you will need to plan your moves well.");
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeDesc", "The goal of the Hardcore mode is to survive for enough turns. Every move consumes your mana, which you can gain by matching blocks. You lose if you run out of mana. The more turns have passed, the faster your mana will be depleted every move so you will need to plan your moves well under time pressure as an additional difficulty is the time limit.");

		// Win messages
		this.keyValuePairs.put(GameModeType.Casual + "ModeWinMsg", "Gain [win] points to win.");
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeWinMsg", "Gain [win] points to win.");
		this.keyValuePairs.put(GameModeType.Survival + "ModeWinMsg", "Survive for [win] turns to win.");
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeWinMsg", "Survive for [win] turns to win.");

		// Lose messages
		this.keyValuePairs.put(GameModeType.Casual + "ModeLoseMsg", "");
		this.keyValuePairs.put(GameModeType.TimeAttack + "ModeLoseMsg", "You lose when [lose] seconds expire.");
		this.keyValuePairs.put(GameModeType.Survival + "ModeLoseMsg", "You lose when you run out of mana. Initial mana comes to [lose].");
		this.keyValuePairs.put(GameModeType.Hardcore + "ModeLoseMsg", "You lose when [lose] seconds expire or you run out of mana.");
	}
}
