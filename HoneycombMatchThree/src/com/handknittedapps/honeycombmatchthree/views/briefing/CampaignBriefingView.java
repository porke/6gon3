package com.handknittedapps.honeycombmatchthree.views.briefing;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.control.BriefingState;
import com.handknittedapps.honeycombmatchthree.data.access.GameScoreAccess;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.BackgroundView;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.ContentTable;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.HeaderTable;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.LoseTable;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.ScoreTable;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.UnlocksTable;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.WinTable;
import com.handknittedapps.honeycombmatchthree.views.menu.MainMenuView;

public class CampaignBriefingView extends BackgroundView
{
	public CampaignBriefingView(BriefingState controller, Mission mission, GameScoreAccess campaignScore)
	{
		super(controller, "campaign-briefing-view", Resources.getBackground());
		this.bottom();

		// Create the play button
		this.add()
			.expand()
			.fill();
		this.row();

		Button playIcon = new TextButton(SpecialCharacters.CAMPAIGN.toString(), this.skin.getStyle("menu_button", TextButtonStyle.class));
		playIcon.setClickListener(this.goClicked);
		this.add(playIcon)
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth);
		this.row();

		this.add()
			.expand()
			.fill();
		this.row();

		// Gather the header data
		GameMode gameMode = mission.getGameMode();
		String headerContent = mission.getName();
		String scoreHeader = "Mission score";
		int score = campaignScore.getScore(mission.getId()).getTotalScore();

		// Create the header
		this.add(new HeaderTable(this.skin, gameMode, mission.getTheme(), headerContent));
		this.row();

		// Mission desc field
		Table contentTable = new Table();
		contentTable.setBackground(this.skin.getPatch("BlueCase"));
		contentTable.add(new ContentTable(this.skin, gameMode, PlayModeType.Campaign, mission));
		contentTable.row();

		contentTable.add(new UnlocksTable(this.skin, mission));
		contentTable.row();

		this.add(contentTable);
		this.row();

		// Win spec
		this.add(new WinTable(gameMode, this.skin));
		this.row();

		// Lose spec
		// A Casual game essentially cannot be lost
		if (gameMode.getModeType() != GameModeType.Casual)
		{
			this.add(new LoseTable(gameMode, skin));
			this.row();
		}

		// Construct score section
		this.add(new ScoreTable(this.skin, scoreHeader, score, this.backClickListener));
		this.row();
	}

	@Override
	public void onBackPressed()
	{
		((BriefingState) CampaignBriefingView.this.controller).returnFromState();
	}

	private ClickListener goClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((BriefingState) CampaignBriefingView.this.controller).enterPlayState();
		}
	};

	private ClickListener backClickListener = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			CampaignBriefingView.this.onBackPressed();
		}
	};
}
