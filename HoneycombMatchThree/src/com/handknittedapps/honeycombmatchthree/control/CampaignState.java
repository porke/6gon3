package com.handknittedapps.honeycombmatchthree.control;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignProgressRepository;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.external.ExternalServiceRegistry;
import com.handknittedapps.honeycombmatchthree.external.types.Leaderboards;
import com.handknittedapps.honeycombmatchthree.logic.CampaignAchievementUnlocker;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.views.campaign.CampaignView;
import com.handknittedapps.honeycombmatchthree.views.campaign.EndCampaignView;


public class CampaignState extends IGameState
{
	public CampaignState(ExternalServiceRegistry serviceRegistry)
	{
		this.userProgress = new CampaignProgressRepository();
		this.serviceRegistry = serviceRegistry;
	}

	public CampaignState(CampaignMode prevState, ExternalServiceRegistry serviceRegistry)
	{
		this(serviceRegistry);
		this.campaign = prevState;
	}

	@Override
	public void onEnter()
	{
		// Occurs when finishing a mission
		if (this.campaign != null)
		{
			Mission playedMission = this.campaign.getMission(this.campaign.getStageId());

			// Check if the mission has been won recently
			if (playedMission != null && playedMission.getGameMode().hasWon())
			{
				CampaignAchievementUnlocker achievementService = new CampaignAchievementUnlocker(this.campaign, this.serviceRegistry.getAchievementHandler());
				achievementService.onMissionCompleted();

				boolean isLastMission = this.campaign.getStageId() == this.campaign.getMissionCount();
				if (this.campaign.isCompleted())
				{
					CampaignScoreRepository scoreRepository = new CampaignScoreRepository(HoneycombMatchThree.name, this.campaign.getMissionCount());
					scoreRepository.loadGameScores();
					GameStats campaignStats = scoreRepository.getTotals();

					this.serviceRegistry.getAchievementHandler().submitScore(campaignStats.getTotalScore(), Leaderboards.CampaignScore);
					this.serviceRegistry.getAchievementHandler().submitScore(campaignStats.getTotalMoves(), Leaderboards.CampaignLeastMoves);
					this.serviceRegistry.getAchievementHandler().submitScore(campaignStats.getTotalTimeSpent(), Leaderboards.CampaignLeastTime);

					if (isLastMission)
					{
						this.showEndCampaignView();
						playedMission.getGameMode().reset();
						return;
					}
				}
			}
		}
		// Occurs when entering the campaign from MenuState
		// or returning from a mission
		else
		{
			this.campaign = new CampaignMode(new Player(0));
			this.userProgress.loadState(this.campaign);
		}

		this.showCampaignView();
	}

	@Override
	public void onPause()
	{
		this.stage.clear();
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
	public void onResume()
	{
		this.onEnterBase();
	}

	@Override
	public void onExit()
	{
		this.userProgress.saveState(this.campaign);
	}

	@Override
	public boolean onFrame()
	{
		this.stage.draw();
		this.stage.act(Gdx.graphics.getDeltaTime());
		return this.isActive;
	}

	//////////////////////////////
	//Campaign view controller functions
	//////////////////////////////
	public void enterMission(int missionId)
	{
		List<Mission> avm = this.campaign.getAvailableMissions(true);
		Mission mission = this.campaign.getMission(missionId);
		if (avm.contains(mission))
		{
			this.nextState = new BriefingState(this.campaign, mission.getId(), this.serviceRegistry);
			this.isActive = false;
		}
	}

	public void returnFromState()
	{
		this.nextState = new MenuState(this.serviceRegistry);
		this.isActive = false;
	}

	public void showCampaignView()
	{
		if (this.activeView != null)
		{
			this.stage.removeActor(this.activeView);
		}

		this.activeView = new CampaignView(this, this.campaign, this.userProgress);
		this.stage.addActor(this.activeView);
	}

	public void showEndCampaignView()
	{
		if (this.activeView != null)
		{
			this.stage.removeActor(this.activeView);
		}

		this.activeView = new EndCampaignView(this);
		this.stage.addActor(this.activeView);
	}
	//////////////////////////////

	// Logic data
	private CampaignMode campaign;
	private CampaignProgressRepository userProgress;

	/** Service registry handle. */
	private ExternalServiceRegistry serviceRegistry;
}
