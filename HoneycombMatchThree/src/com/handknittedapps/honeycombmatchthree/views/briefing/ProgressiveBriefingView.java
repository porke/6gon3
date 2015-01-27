package com.handknittedapps.honeycombmatchthree.views.briefing;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.BriefingState;
import com.handknittedapps.honeycombmatchthree.data.access.GameScoreAccess;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.logic.modes.ProgressiveMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.BackgroundView;
import com.handknittedapps.honeycombmatchthree.views.BaseView;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.LoseTable;
import com.handknittedapps.honeycombmatchthree.views.briefing.sections.WinTable;
import com.handknittedapps.honeycombmatchthree.views.menu.MainMenuView;
import com.handknittedapps.honeycombmatchthree.views.menu.score.ScoreDataGrid;

public class ProgressiveBriefingView extends BackgroundView
{
	public ProgressiveBriefingView(BriefingState controller, ProgressiveMode progressive, GameScoreAccess scoreControl)
	{
		super(controller, "infinite-briefing-view", Resources.getBackground());
		this.bottom();

		// Top - empty section
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

		// Mission type
		GameMode gameMode = progressive.getGameMode();

		// Win spec
		this.add(new WinTable(gameMode, this.skin));
		this.row();

		// Lose spec
		// A Casual game essentially cannot be lost
		if (gameMode.getModeType() != GameModeType.Casual)
		{
			this.add(new LoseTable(gameMode, this.skin));
			this.row();
		}

		this.createCurrentStatsTable(progressive);
		this.createBestScoreTable(scoreControl);

		// Global leaderboards
		TextButton globalLadderButton = new TextButton("View global leaderboard", this.skin);
		globalLadderButton.setClickListener(this.globalLeaderboardClicked);
		this.add(globalLadderButton)
			.expandX()
			.fillX()
			.height(BaseView.MainButtonHeight);
		this.row();

		if (HoneycombMatchThree.IsDesktopVersion)
		{
			// Construct bottom section
			Table bottomTable = new Table();
			Button btnBack = new ImageButton(skin.getStyle("return-button", ImageButtonStyle.class));
			btnBack.setClickListener(backClickListener);
			bottomTable.add(btnBack)
					  .left();

			bottomTable.add()
					   .expandX()
					   .fillX();

			bottomTable.add();

			this.add(bottomTable)
				.expandX()
				.fillX();
		}
	}

	private void createCurrentStatsTable(ProgressiveMode progressive)
	{
		Table currentScore = new Table();
		currentScore.setBackground(this.skin.getPatch("YellowCase"));

		Label currentStatsHeader = new Label("Your stats", this.skin);
		currentStatsHeader.setAlignment(Align.CENTER);
		currentScore.add(currentStatsHeader)
					.expandX()
					.fillX();
		currentScore.row();

		currentScore.add(new ScoreDataGrid(progressive.getScore()))
			   .expandX()
			   .fillX();
		this.add(currentScore)
			.fillX()
			.expandX();
		this.row();
	}

	private void createBestScoreTable(GameScoreAccess scoreControl)
	{
		Table bestScore = new Table();
		bestScore.setBackground(this.skin.getPatch("BlueCase"));
		Label bestStatsHeader = new Label("Best stats", this.skin);
		bestStatsHeader.setAlignment(Align.CENTER);
		bestScore.add(bestStatsHeader)
					.expandX()
					.fillX();
		bestScore.row();

		ScoreDataGrid scoreData = new ScoreDataGrid(scoreControl.getScore(0));
		SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy");
		long date = scoreControl.getScore(0).getDate();
		String dateText = date != 0 ? dateFmt.format(new Date(date)) : "-";
		Label dateLbl = new Label("Date", this.skin);
		dateLbl.setAlignment(Align.LEFT);
		Label dateContent = new Label(dateText, this.skin);
		dateContent.setAlignment(Align.RIGHT);
		scoreData.add(dateLbl).expandX().fillX().pad(0, ScoreDataGrid.Padding, 0, 0);
		scoreData.add(dateContent).expandX().fillX().pad(0, 0, 0, ScoreDataGrid.Padding);

		bestScore.add(scoreData)
		   .expandX()
		   .fillX();

		this.add(bestScore)
			.fillX()
			.expandX();
		this.row();
	}

	@Override
	public void onBackPressed()
	{
		((BriefingState) ProgressiveBriefingView.this.controller).returnFromState();
	}

	private ClickListener goClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((BriefingState) ProgressiveBriefingView.this.controller).enterPlayState();
		}
	};

	private ClickListener globalLeaderboardClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((BriefingState) ProgressiveBriefingView.this.controller).showGlobalLeaderboard();
		}
	};

	private ClickListener backClickListener = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			ProgressiveBriefingView.this.onBackPressed();
		}
	};
}
