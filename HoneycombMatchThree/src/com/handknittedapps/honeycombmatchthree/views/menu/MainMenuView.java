package com.handknittedapps.honeycombmatchthree.views.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.control.MenuState;
import com.handknittedapps.honeycombmatchthree.views.LogoView;
import com.handknittedapps.honeycombmatchthree.views.menu.main.MainButtonTable;

public class MainMenuView extends LogoView
{
	public static final String ViewId = "main-menu";
	public static int HexButtonWidth = 140;
	public static int HexButtonHeight = 120;

	public MainMenuView(final MenuState controller)
	{
		super(controller, MainMenuView.ViewId);

		Table topButtons = new Table();
		topButtons.add()
				  .fill()
				  .expand();

		Button showAchievements = new ImageButton(this.skin.getStyle("highscore-button", ImageButtonStyle.class));
		showAchievements.setClickListener(this.showAchievementsClick);
		topButtons.add(showAchievements)
				  .top()
				  .right();

		this.add(topButtons)
			.expandX()
			.fillX();
		this.row();

		this.add()
			.fill()
			.expand();
		this.row();

		this.add(new MainButtonTable(this.enterCampaignClick, this.progressiveClick, this.highScoreClick, this.quitClick));
		this.row();

		Table bottomButtons = new Table();
		this.add(bottomButtons)
			.expandX()
			.fillX();

		// Create rate it button
		Button rateIt = new ImageButton(this.skin.getStyle("rate-button", ImageButtonStyle.class));
		rateIt.setClickListener(this.rateClick);
		bottomButtons.add(rateIt)
					 .left()
					 .center();

		bottomButtons.add()
					 .expandX()
					 .fillX();

		// Create high score button
		Button help = new ImageButton(this.skin.getStyle("help-button", ImageButtonStyle.class));
		help.setClickListener(this.helpClick);
		bottomButtons.add(help)
					 .right();
	}

	@Override
	public void onBackPressed()
	{
		((MenuState) (MainMenuView.this.controller)).exitFromState();
	}

	private ClickListener quitClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			MainMenuView.this.onBackPressed();
		}
	};

	private ClickListener enterCampaignClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (MainMenuView.this.controller)).enterGame();
		}
	};

	private ClickListener progressiveClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (MainMenuView.this.controller)).showProgressiveView();
		}
	};

	private ClickListener highScoreClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (MainMenuView.this.controller)).showHighscoreView();
		}
	};

	private ClickListener helpClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (MainMenuView.this.controller)).showHelpView();
		}
	};

	private ClickListener rateClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (MainMenuView.this.controller)).openRatingPage();
		}
	};

	private ClickListener showAchievementsClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (MainMenuView.this.controller)).showAchievements();
		}
	};
}
