package com.handknittedapps.honeycombmatchthree.control;

import com.badlogic.gdx.Gdx;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignProgressRepository;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.access.ProgressiveScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.access.UserSettingsRepository;
import com.handknittedapps.honeycombmatchthree.external.ExternalServiceRegistry;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.ProgressiveMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.BaseView;
import com.handknittedapps.honeycombmatchthree.views.DialogReturnValue;
import com.handknittedapps.honeycombmatchthree.views.LogoView;
import com.handknittedapps.honeycombmatchthree.views.menu.HighscoreView;
import com.handknittedapps.honeycombmatchthree.views.menu.MainHelpView;
import com.handknittedapps.honeycombmatchthree.views.menu.MainMenuView;
import com.handknittedapps.honeycombmatchthree.views.menu.ProgressiveHelpView;
import com.handknittedapps.honeycombmatchthree.views.menu.ProgressiveSelectionView;

public class MenuState extends IGameState
{
	public MenuState(ExternalServiceRegistry serviceRegistry)
	{
		this(MainMenuView.ViewId, serviceRegistry);
	}

	public MenuState(String startView, ExternalServiceRegistry serviceRegistry)
	{
		this.startViewId = startView;
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public void onEnter()
	{
		if (this.startViewId.equals(MainMenuView.ViewId))
		{
			this.showMainMenuView();
		}
		else if (this.startViewId.equals(ProgressiveSelectionView.ViewId))
		{
			this.showProgressiveView();
		}
	}

	@Override
	public void onExit()
	{
		// Empty
	}

	@Override
	public void onBackPressed()
	{
		if (this.activeView != null)
		{
			this.activeView.onBackPressed();
		}
	}

	@Override
	public void onPause()
	{
		this.stage.clear();
	}

	@Override
	public void onResume()
	{
		this.onEnterBase();
	}

	@Override
	public boolean onFrame()
	{
		this.stage.draw();
		this.stage.act(Gdx.graphics.getDeltaTime());

		return this.isActive;
	}

	//////////////////////////////
	//Menu view controller functions
	//////////////////////////////
	public void exitFromState()
	{
		this.isActive = false;
		this.nextState = null;
	}

	public void enterGame()
	{
		checkAchievementLogin();

		if (this.gameModeSelectedBeforeRatingView == null)
		{
			this.nextState = new CampaignState(this.serviceRegistry);
			this.serviceRegistry.getStatsHandler().logEvent("Playing campaign mode.");
		}
		else
		{
			this.enterProgressiveGame(this.gameModeSelectedBeforeRatingView);
		}
	}

	public void showGlobalTimeLeaderboard()
	{
		this.serviceRegistry.getAchievementHandler().showLeaderboard(Leaderboards.CampaignLeastTime);
	}

	public void showGlobalScoreLeaderboard()
	{
		this.serviceRegistry.getAchievementHandler().showLeaderboard(Leaderboards.CampaignScore);
	}

	public void showGlobalMoveLeaderboard()
	{
		this.serviceRegistry.getAchievementHandler().showLeaderboard(Leaderboards.CampaignLeastMoves);
	}

	public void openRatingPage()
	{
		this.serviceRegistry.getRatingHandler().navigateToRatingPage();
	}

	public void loginToSwarm()
	{
		this.serviceRegistry.getAchievementHandler().activateService();
	}

	public void showAchievements()
	{
		this.serviceRegistry.getAchievementHandler().showAchievements();
	}

	public void enterProgressiveGame(GameModeType gm)
	{
		checkAchievementLogin();

		this.nextState = new BriefingState(new ProgressiveMode(gm, new Player()), this.serviceRegistry);
		this.serviceRegistry.getStatsHandler().logEvent("Playing progressive mode: " + gm);
	}

	public void showMainMenuView()
	{
		this.swapCurrentView(new MainMenuView(this));
	}

	public void showHelpView()
	{
		this.swapCurrentView(new MainHelpView(this));
	}

	public void showProgressiveView()
	{
		this.swapCurrentView(new ProgressiveSelectionView(this));
	}

	public void showHighscoreView()
	{
		this.swapCurrentView(new HighscoreView(this));
	}

	public void showProgressiveHelpWindow()
	{
		this.swapCurrentView(new ProgressiveHelpView(this));
	}

	public ProgressiveScoreRepository getProgressiveScoreRepository(GameModeType gmt)
	{
		ProgressiveScoreRepository progressiveScore = new ProgressiveScoreRepository(gmt, HoneycombMatchThree.name);
		progressiveScore.loadGameScores();
		return progressiveScore;
	}

	public CampaignMode getCampaignMode()
	{
		return new CampaignMode(new Player(0));
	}

	public CampaignScoreRepository getCampaignScoreRepository()
	{
		CampaignMode mode = this.getCampaignMode();
		CampaignScoreRepository campaignScore = new CampaignScoreRepository(HoneycombMatchThree.name, mode.getMissionCount());
		campaignScore.loadGameScores();
		return campaignScore;
	}

	public void resetCampaign()
	{
		// Reset the campaign state
		CampaignMode campaign = this.getCampaignMode();
		CampaignProgressRepository repo = new CampaignProgressRepository();
		campaign.reset();
		repo.saveState(campaign);

		// Reset the campaign scores
		CampaignScoreRepository campaignScore = this.getCampaignScoreRepository();
		campaignScore.loadGameScores();
		campaignScore.resetGameScores();
		campaignScore.saveGameScores();
	}

	public void handleAchievementLoginPromptResponse(DialogReturnValue dialogReturnValue)
	{
		// Save the users response and don't trouble him later
		// if he responds no, then don't do anything
		UserSettingsRepository settings = new UserSettingsRepository();
		if (dialogReturnValue == DialogReturnValue.Yes)
		{
			this.serviceRegistry.getAchievementHandler().activateService();
		}
		else if (dialogReturnValue == DialogReturnValue.Never)
		{
			settings.setSetting("swarmLogin", "Never");
		}
		else if (dialogReturnValue == DialogReturnValue.Always)
		{
			settings.setSetting("swarmLogin", "Always");
			this.serviceRegistry.getAchievementHandler().activateService();
		}

		settings.save();
		this.isActive = false;
	}

	public void resetScores(GameModeType gameMode)
	{
		ProgressiveScoreRepository progressiveScore = this.getProgressiveScoreRepository(gameMode);
		progressiveScore.loadGameScores();
		progressiveScore.resetGameScores();
		progressiveScore.saveGameScores();
	}
	//////////////////////////////

	private void swapCurrentView(BaseView newView)
	{
		if (this.activeView != null)
		{
			this.activeView.markToRemove(true);
		}

		this.activeView = newView;
		this.stage.addActor(this.activeView);
	}

	private void checkAchievementLogin()
	{
		if (this.serviceRegistry.getAchievementHandler().isLoggedIn())
		{
			this.isActive = false;
			return;
		}

		UserSettingsRepository settings = new UserSettingsRepository();
		String swarmLogin = settings.getSetting("swarmLogin");

		if (swarmLogin.equals("Prompt"))
		{
			((LogoView)this.activeView).showAchievementLoginPrompt();
		}
		else if (swarmLogin.equals("Always"))
		{
			this.serviceRegistry.getAchievementHandler().activateService();
			this.isActive = false;
		}
		else if (swarmLogin.equals("Never"))
		{
			this.isActive = false;
		}
	}

	private String startViewId;
	private GameModeType gameModeSelectedBeforeRatingView;

	/** Service registry handle. */
	private ExternalServiceRegistry serviceRegistry;
}
