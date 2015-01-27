package com.handknittedapps.honeycombmatchthree.views.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.MenuState;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.BackgroundView;
import com.handknittedapps.honeycombmatchthree.views.BaseView;
import com.handknittedapps.honeycombmatchthree.views.YesNoDialog;
import com.handknittedapps.honeycombmatchthree.views.menu.score.CampaignScoreTable;
import com.handknittedapps.honeycombmatchthree.views.menu.score.GlobalCampaignScores;

public class HighscoreView extends BackgroundView
{
	public HighscoreView(MenuState controller)
	{
		super(controller, "HighScoreWindow", Resources.getBackground());

		this.add()
			.expand()
			.fill();
		this.row();

		Button resetScrollButton = new TextButton(SpecialCharacters.SCORE.toString(), this.skin.getStyle("menu_button", TextButtonStyle.class));
		resetScrollButton.setClickListener(this.resetScrollClick);
		this.add(resetScrollButton)
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth);
		this.row();

		this.add()
			.expand()
			.fill();
		this.row();

		this.highscoreData = new CampaignScoreTable(controller.getCampaignScoreRepository(), controller.getCampaignMode());
		this.add(this.highscoreData)
			.expandX()
			.fillX();
		this.row();

		// Add the reset buttons
		Table resetTable = new Table();
		Button resetCampaign = new TextButton("Reset campaign", skin);
		resetCampaign.setClickListener(resetCampaignClick);
		resetTable.add(resetCampaign)
			.expandX()
			.fillX()
			.height(BaseView.MainButtonHeight);

		// Add global campaign score buttons
		this.add(new GlobalCampaignScores(this.showGlobalScoreClick,
										  this.showGlobalTimeClick,
										  this.showGlobalMovesClick))
			.expandX()
			.fillX();
		this.row();

		Button resetHighscores = new TextButton("Reset infinite", this.skin);
		resetHighscores.setClickListener(this.resetProgressivePrompt);
		resetTable.add(resetHighscores)
			.expandX()
			.fillX()
			.height(BaseView.MainButtonHeight);
		this.add(resetTable);
		this.row();

		if (HoneycombMatchThree.IsDesktopVersion)
		{
			Button back = new ImageButton(this.skin.getStyle("return-button", ImageButtonStyle.class));
			back.setClickListener(this.backClick);
			this.add(back)
			  	.left();
		}
	}

	@Override
	public void onBackPressed()
	{
		// That was a No response - hide the dialog
		if (this.visibleDialog != null)
		{
			this.visibleDialog.markToRemove(true);
			this.visibleDialog = null;
		}
		// Otherwise hide the window and return to the menu
		else
		{
			((MenuState) this.controller).showMainMenuView();
		}
	}

	private void refreshCampaignScores()
	{
		CampaignScoreRepository scoreControl = ((MenuState) this.controller).getCampaignScoreRepository();
		CampaignMode mode = ((MenuState) this.controller).getCampaignMode();
		((CampaignScoreTable) this.highscoreData).updateScoreData(mode, scoreControl);
	}

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			HighscoreView.this.onBackPressed();
		}
	};

	private ClickListener resetProgressivePrompt = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			HighscoreView.this.visibleDialog = new YesNoDialog("Are you sure you want to reset the infinite mode scores?"
												, HighscoreView.this.resetProgressiveExecute);
			HighscoreView.this.addActor(HighscoreView.this.visibleDialog);
		}
	};

	private ClickListener resetProgressiveExecute = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			if (HighscoreView.this.visibleDialog.wasYesClicked())
			{
				for (GameModeType gmt : GameModeType.values())
				{
					((MenuState) HighscoreView.this.controller).resetScores(gmt);
				}
			}

			HighscoreView.this.removeActor(HighscoreView.this.visibleDialog);
			HighscoreView.this.visibleDialog = null;
		}
	};

	private ClickListener resetCampaignClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			HighscoreView.this.visibleDialog = new YesNoDialog("Are you sure that you want to clear the campaign progress? (also resets the scores)"
									, HighscoreView.this.resetCampaignExecute);

			HighscoreView.this.addActor(HighscoreView.this.visibleDialog);
		}
	};

	private ClickListener resetCampaignExecute = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			if (HighscoreView.this.visibleDialog .wasYesClicked())
			{
				((MenuState) HighscoreView.this.controller).resetCampaign();
			}

			HighscoreView.this.removeActor(HighscoreView.this.visibleDialog);
			HighscoreView.this.visibleDialog = null;
			HighscoreView.this.refreshCampaignScores();
		}
	};

	private ClickListener showGlobalTimeClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) HighscoreView.this.controller).showGlobalTimeLeaderboard();
		}
	};

	private ClickListener showGlobalScoreClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) HighscoreView.this.controller).showGlobalScoreLeaderboard();
		}
	};

	private ClickListener showGlobalMovesClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) HighscoreView.this.controller).showGlobalMoveLeaderboard();
		}
	};

	private ClickListener resetScrollClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((CampaignScoreTable) HighscoreView.this.highscoreData).resetMissionScroll();
		}
	};

	private Table highscoreData;
	private YesNoDialog visibleDialog;
}
