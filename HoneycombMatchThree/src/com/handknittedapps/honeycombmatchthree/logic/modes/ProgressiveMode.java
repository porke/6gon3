package com.handknittedapps.honeycombmatchthree.logic.modes;

import java.util.Random;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.GameSettingsRepository;
import com.handknittedapps.honeycombmatchthree.data.access.ProgressiveScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeFactory;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public class ProgressiveMode extends PlayMode
{
	public ProgressiveMode(GameModeType gmt, Player play)
	{
		super(play);
		this.stageId = 1;

		GameSettingsRepository settings = new GameSettingsRepository();
		this.baseWinSpec = settings.getSetting(gmt + "ModeWinSpec");
		this.baseLoseSpec = settings.getSetting(gmt + "ModeLoseSpec");
		this.winProgress = settings.getSetting(gmt + "ModeWinProgress");
		this.loseProgress  = settings.getSetting(gmt + "ModeLoseProgress");

		Random rand = new Random();
		this.currentTheme = ThemeType.values()[rand.nextInt(ThemeType.values().length)];

		if (gmt == GameModeType.Hardcore)
		{
			this.baseLoseSpec2 = settings.getSetting(gmt + "ModeLoseSpec2");
			this.loseProgress2 = settings.getSetting(gmt + "ModeLoseProgress2");
		}

		this.createGameMode(gmt, this.stageId);
		this.resetScore();
	}

	public void nextStage(GameStats stageScore)
	{
		++this.stageId;
		this.createGameMode(this.mode.getModeType(), this.stageId);
		this.scoreModel = this.scoreModel.add(stageScore);

		this.getPlayer().resetEnergy();
	}

	public void onEndgame(GameStats stageScore)
	{
		this.scoreModel = this.scoreModel.add(stageScore);
	}

	private void createGameMode(GameModeType gmt, int stage)
	{
		float loseSpec = this.baseLoseSpec + this.loseProgress * (stage - 1);
		float winSpec = this.baseWinSpec + this.winProgress * (stage - 1);
		float loseSpec2 = this.baseLoseSpec2 + this.loseProgress2 * (stage - 1);

		double[] loseSpecArray = new double[]{loseSpec, loseSpec2};
		this.mode = GameModeFactory.createGameMode(gmt, this.getPlayer(), (int)winSpec, loseSpecArray);
	}

	public ThemeType getCurrentTheme() { return this.currentTheme; }
	public void resetScore() {this.scoreModel = new GameStats();}

	@Override
	public void onStageFinished(GameStats finalScore)
	{
		if (this.mode.hasWon())
		{
			this.nextStage(finalScore);
		}
		else
		{
			this.onEndgame(finalScore);

			// Save the highscore
			ProgressiveScoreRepository controller = new ProgressiveScoreRepository(this.mode.getModeType(), HoneycombMatchThree.name);
			controller.loadGameScores();
			controller.onGameFinished(finalScore, this.stageId);
			controller.saveGameScores();
		}
	}

	@Override
	public ThemeType getTheme()
	{
		return this.currentTheme;
	}

	@Override
	public PlayModeType getPlayModeType()
	{
		return PlayModeType.Progressive;
	}

	@Override
	public GameMode getGameMode()
	{
		return this.mode;
	}

	@Override
	public GameStats getScore()
	{
		return this.scoreModel;
	}

	/** The game score model used to carry through stages. */
	private GameStats scoreModel;
	private ThemeType currentTheme;

	private GameMode mode;
	private float baseWinSpec;
	private float baseLoseSpec;
	private float winProgress;
	private float loseProgress;

	private float baseLoseSpec2;		// Used by Hardcore mode
	private float loseProgress2;		// ...
}
