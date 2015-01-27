package com.handknittedapps.honeycombmatchthree.logic.modes;

import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public abstract class PlayMode
{
	PlayMode(Player play)
	{
		this.player = play;
	}

	public void setStageId(int id) {this.stageId = id;}

	public Player getPlayer() {return this.player;}
	public int getStageId() {return this.stageId;}

	public abstract ThemeType getTheme();
	public abstract PlayModeType getPlayModeType();
	public abstract GameMode getGameMode();
	public abstract GameStats getScore();

	public abstract void onStageFinished(GameStats finalScore);

	public BlockType[] getUnlockedBlocks() { return this.unlockedBlocks; }
	public MoveType[] getUnlockedBonuses() { return this.unlockedBonuses; }
	public ThemeType[] getUnlockedThemes() { return this.unlockedThemes; }
	public GameModeType[] getUnlockedModes() { return this.unlockedModes; }
	public int getNumColors() {return this.unlockedColours;}

	Player player;
	int stageId;
	MoveType[] unlockedBonuses = MoveType.values();
	BlockType[] unlockedBlocks = BlockType.values();
	ThemeType[] unlockedThemes = ThemeType.values();
	GameModeType[] unlockedModes = GameModeType.values();
	int unlockedColours = BlockColour.NumColours;
}
