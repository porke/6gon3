package com.handknittedapps.honeycombmatchthree.control;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.external.ExternalServiceRegistry;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.input.GamestateInputProcessor;
import com.handknittedapps.honeycombmatchthree.input.UIInputProcessor;
import com.handknittedapps.honeycombmatchthree.logic.Gamestate;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.ProgressiveMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.views.BaseView;
import com.handknittedapps.honeycombmatchthree.views.play.EndgameView;
import com.handknittedapps.honeycombmatchthree.views.play.HelpView;
import com.handknittedapps.honeycombmatchthree.views.play.PlayView;


public class PlayState extends IGameState
{
	private static final String BannerSectionCode = "564654922";

	public PlayState(PlayMode mode, ExternalServiceRegistry serviceRegistry)
	{
		this.playMode = mode;
		this.serviceRegistry = serviceRegistry;

		if (mode.getPlayModeType() == PlayModeType.Progressive)
		{
			this.nextState = new BriefingState((ProgressiveMode) mode, this.serviceRegistry);
		}
		else
		{
			this.nextState = new CampaignState((CampaignMode) mode, this.serviceRegistry);
		}
	}

	///////////////////////////////////
	// Controller functions
	///////////////////////////////////
	public void setActiveView(String viewName)
	{
		this.stage.removeActor(this.activeView);
		for (BaseView viewTable : this.views)
		{
			if (viewTable.name.equals(viewName))
			{
				this.activeView = viewTable;
				break;
			}
		}

		this.stage.addActor(this.activeView);

		// Configure the input processor
		Rectangle properViewport = this.setupViewport();
		if (this.activeView.name.equals(PlayView.ViewId))
		{
			Gdx.input.setInputProcessor(new GamestateInputProcessor(this.gamestate, properViewport));
		}
		else
		{
			Gdx.input.setInputProcessor(new UIInputProcessor(this.stage, properViewport));
		}

		// If this is the endgame view, set the proper label
		if (this.activeView.name.equals(EndgameView.ViewId))
		{
			((EndgameView) this.activeView).setResult(this.gamestate.hasWon());
		}
	}
	///////////////////////////////////

	@Override
	public void onEnter()
	{
		GameMode mode = this.playMode.getGameMode();
		ThemeType theme = this.playMode.getTheme();

		// ensure that the mode is reset
		boolean hasBeenPlayed = mode.getProgress() > 0.0f
							|| mode.getLoseConditionLeft() < 1.0f;
		if (hasBeenPlayed && !this.paused)
		{
			mode.reset();
		}

		this.gamestate = new Gamestate(this, theme);
		this.gamestate.startGame(this.playMode,
								 this.stage,
								 this.serviceRegistry.getAchievementHandler(),
							 	 HoneycombMatchThree.IsDesktopVersion);

		// Load the ad
		this.serviceRegistry.getAdHandler().loadAd(PlayState.BannerSectionCode);

		// Create the views
		this.views.add(new HelpView(this));
		this.views.add(new PlayView(this, this.gamestate));
		this.views.add(new EndgameView(this));
		this.setActiveView(PlayView.ViewId);
	}

	@Override
	public void onPause()
	{
		// Perform any final updates (eg. highscores)
		if (this.gamestate.isEndgame())
		{
			this.gamestate.processEndgame();
			this.playMode.onStageFinished(this.gamestate.getScore());
		}

		this.gamestate.dispose();

		this.stage.clear();
		this.views.clear();

		this.paused = true;
	}

	@Override
	public void onResume()
	{
		this.onEnterBase();
		this.gamestate.resumeGame();

		this.paused = false;
	}

	@Override
	public void onExit()
	{
		// Perform any final updates (eg. highscores)
		if (!this.gamestate.isEndgame())
		{
			this.gamestate.processEndgame();
		}

		this.playMode.onStageFinished(this.gamestate.getScore());

		if (!this.gamestate.hasWon())
		{
			if (this.playMode.getPlayModeType() == PlayModeType.Progressive)
			{
				this.nextState = new MenuState(this.serviceRegistry);
				submitProgressiveScore();
			}
		}

		this.gamestate.dispose();
		this.serviceRegistry.getAdHandler().destroyAd();
	}

	@Override
	public boolean onFrame()
	{
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();

		return this.isActive;
	}

	@Override
	public void onBackPressed()
	{
		if (this.activeView != null
			&& this.activeView.name.equals(HelpView.ViewId))
		{
			this.setActiveView(PlayView.ViewId);
			Rectangle properViewport = this.setupViewport();
			Gdx.input.setInputProcessor(new GamestateInputProcessor(this.gamestate, properViewport));
		}
		else if (this.gamestate != null)
		{
			if (!this.gamestate.hasWon())
			{
				switch (this.playMode.getPlayModeType())
				{
					case Campaign:
						this.nextState = new CampaignState((CampaignMode) this.playMode, this.serviceRegistry);
						break;
					case Progressive:
						this.nextState = new MenuState(this.serviceRegistry);
						break;
				}
			}

			this.isActive = false;
		}
	}

	private void submitProgressiveScore()
	{
		int totalScore = this.playMode.getScore().getTotalScore();
		Leaderboards leaderboardType = Leaderboards.fromGameModeType(this.playMode.getGameMode().getModeType());
		this.serviceRegistry.getAchievementHandler().submitScore(totalScore, leaderboardType);
	}

	/** The current play mode (progressive or campaign) */
	private PlayMode playMode;

	/** The gamestate */
	private Gamestate gamestate;

	/** The currently active view. */
	private BaseView activeView;

	/** The list of views so that they will not need to be recreated every switch */
	private ArrayList<BaseView> views = new ArrayList<BaseView>();

	private boolean paused = false;

	/** Service registry handle. */
	private ExternalServiceRegistry serviceRegistry;
}
