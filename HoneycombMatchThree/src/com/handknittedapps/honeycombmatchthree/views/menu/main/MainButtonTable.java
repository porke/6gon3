package com.handknittedapps.honeycombmatchthree.views.menu.main;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.views.menu.MainMenuView;

public class MainButtonTable extends Table
{
	public MainButtonTable(ClickListener campaignClick, ClickListener progressiveClick, ClickListener highscoreClick, ClickListener quitClick)
	{
		Skin skin = Resources.getSkin();

		Button campaignMode = new TextButton(SpecialCharacters.CAMPAIGN.toString(), skin.getStyle("menu_button", TextButtonStyle.class));
		campaignMode.setClickListener(campaignClick);

		Button quit = new TextButton(SpecialCharacters.EXIT.toString(), skin.getStyle("menu_button", TextButtonStyle.class));
		quit.setClickListener(quitClick);

		Button progressiveMode = new TextButton(SpecialCharacters.INFINITY.toString(), skin.getStyle("menu_button", TextButtonStyle.class));
		progressiveMode.setClickListener(progressiveClick);

		Button highscores = new TextButton(SpecialCharacters.SCORE.toString(), skin.getStyle("menu_button", TextButtonStyle.class));
		highscores.setClickListener(highscoreClick);

		// First row - one button
		this.add();
		this.add(campaignMode)
			.width(MainMenuView.HexButtonWidth)
			.height(MainMenuView.HexButtonHeight);
		this.add();
		this.row();

		// Second row - two buttons
		this.add(progressiveMode)
			.width(MainMenuView.HexButtonWidth)
			.height(MainMenuView.HexButtonHeight);
		this.add();
		this.add(highscores)
			.width(MainMenuView.HexButtonWidth)
			.height(MainMenuView.HexButtonHeight);
		this.row();

		// Third row - one button
		this.add();
		this.add(quit)
			.width(MainMenuView.HexButtonWidth)
			.height(MainMenuView.HexButtonHeight);
		this.add();
	}
}
