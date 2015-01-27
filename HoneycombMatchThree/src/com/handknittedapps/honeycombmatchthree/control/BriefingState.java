package com.handknittedapps.honeycombmatchthree.control;

import com.badlogic.gdx.Gdx;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.access.GameScoreAccess;
import com.handknittedapps.honeycombmatchthree.data.access.ProgressiveScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.external.ExternalServiceRegistry;
import com.handknittedapps.honeycombmatchthree.external.types.Achievements;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.ProgressiveMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.briefing.CampaignBriefingView;
import com.handknittedapps.honeycombmatchthree.views.briefing.ProgressiveBriefingView;
import com.handknittedapps.honeycombmatchthree.views.menu.ProgressiveSelectionView;

public class BriefingState extends IGameState
{
	// Used only for the campaign
	public BriefingState(CampaignMode campaign, int missionId, ExternalServiceRegistry serviceRegistry)
	{
		this.playMode = campaign;
		this.playMode.setStageId(missionId);
		this.serviceRegistry = serviceRegistry;

		this.scoreRepository = new CampaignScoreRepository(HoneycombMatchThree.name, campaign.getMissionCount());
		this.scoreRepository.loadGameScores();
	}

	// Used for progressive mode
	public BriefingState(ProgressiveMode prog, ExternalServiceRegistry serviceRegistry)
	{
		this.playMode = prog;
		this.serviceRegistry = serviceRegistry;

		this.scoreRepository = new ProgressiveScoreRepository(this.playMode.getGameMode().getModeType(), HoneycombMatchThree.name);
		this.scoreRepository.loadGameScores();
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
	public void onEnter()
	{
		if (this.playMode.getPlayModeType() == PlayModeType.Progressive)
		{
			this.activeView = new ProgressiveBriefingView(this, (ProgressiveMode) this.playMode, this.scoreRepository);

			if (this.playMode.getStageId() >= 5
				&& this.playMode.getGameMode().getModeType() != GameModeType.Casual)
			{
				this.serviceRegistry.getAchievementHandler().unlockAchievement(Achievements.FifthStageInProgressive);
			}
		}
		else
		{
			Mission mission = ((CampaignMode) this.playMode).getMission(this.playMode.getStageId());
			this.activeView = new CampaignBriefingView(this, mission, this.scoreRepository);
		}

		this.stage.addActor(this.activeView);
	}

	@Override
	public void onExit()
	{
		// Save the score in case this is the progressive mode
		if (this.playMode.getPlayModeType() == PlayModeType.Progressive)
		{
			ProgressiveScoreRepository scoreController = new ProgressiveScoreRepository(this.playMode.getGameMode().getModeType(), HoneycombMatchThree.name);
			scoreController.loadGameScores();
			scoreController.onGameFinished(this.playMode.getScore(), this.playMode.getStageId());
			scoreController.saveGameScores();

			int totalScore = this.playMode.getScore().getTotalScore();
			if (totalScore > 0)
			{
				GameMode mode = this.playMode.getGameMode();

				// only submit score if the game is definitely finished
				// that is, the player has lost
				if (this.nextState != null && (this.nextState instanceof MenuState))
				{
					Leaderboards type = Leaderboards.fromGameModeType(mode.getModeType());
					this.serviceRegistry.getAchievementHandler().submitScore(totalScore, type);
				}
			}
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
	//Briefing view controller functions
	//////////////////////////////
	public void enterPlayState()
	{
		this.nextState = new PlayState(BriefingState.this.playMode,
									   BriefingState.this.serviceRegistry);
		this.isActive = false;
	}

	public void returnFromState()
	{
		switch (BriefingState.this.playMode.getPlayModeType())
		{
			case Campaign:
				this.nextState = new CampaignState((CampaignMode) this.playMode, this.serviceRegistry);
				break;
			case Progressive:
				this.nextState = new MenuState(ProgressiveSelectionView.ViewId, this.serviceRegistry);
				break;
		}

		this.isActive = false;
	}

	public void showGlobalLeaderboard()
	{
		Leaderboards leaderboardType = Leaderboards.fromGameModeType(this.playMode.getGameMode().getModeType());
		this.serviceRegistry.getAchievementHandler().showLeaderboard(leaderboardType);
	}

	//////////////////////////////

	/** Score controller used to obtain the best score. */
	private GameScoreAccess scoreRepository;

	/** Play mode data. */
	private PlayMode playMode;

	/** Service registry handle. */
	private ExternalServiceRegistry serviceRegistry;
}
