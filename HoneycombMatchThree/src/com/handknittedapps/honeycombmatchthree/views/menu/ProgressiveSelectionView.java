package com.handknittedapps.honeycombmatchthree.views.menu;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.MenuState;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.LogoView;

public class ProgressiveSelectionView extends LogoView
{
	public static final String ViewId = "infinite-selection";

	public ProgressiveSelectionView(MenuState controller)
	{
		super(controller, ProgressiveSelectionView.ViewId);
		this.bottom();

		this.add()
			.fill()
			.expand();
		this.row();

		GameModeType[] gameModes = GameModeType.values();
		ArrayList<Button> buttons = new ArrayList<Button>();
		String[] displayedNames = new String[] {SpecialCharacters.CASUAL.toString(),
												SpecialCharacters.TIME_ATTACK.toString(),
												SpecialCharacters.SURVIVAL.toString(),
												SpecialCharacters.HARDCORE.toString(),
												SpecialCharacters.CLASSIC.toString()};

		for (int i = 0; i < gameModes.length; ++i)
		{
			final Button btn = new TextButton(displayedNames[i],
											this.skin.getStyle("menu_button", TextButtonStyle.class),
											gameModes[i].name());
			btn.setClickListener(this.modeClick);
			buttons.add(btn);
		}

		Table buttonTable = new Table();
		buttonTable.add();
		buttonTable.add(buttons.get(0))
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth);
		buttonTable.add();
		buttonTable.row();

		buttonTable.add(buttons.get(1))
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth);
		buttonTable.add(buttons.get(2))
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth)
			.pad(5);
		buttonTable.add(buttons.get(3))
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth);
		buttonTable.row();

		buttonTable.add();
		buttonTable.add(buttons.get(4))
			.height(MainMenuView.HexButtonHeight)
			.width(MainMenuView.HexButtonWidth);
		buttonTable.add();

		this.add(buttonTable)
			.fillX()
			.expandX();
		this.row();


		Table bottomTable = new Table();
		if (HoneycombMatchThree.IsDesktopVersion)
		{
			Button back = new ImageButton(this.skin.getStyle("return-button", ImageButtonStyle.class));
			back.setClickListener(this.backClick);
			bottomTable.add(back)
					   .left();
		}

		bottomTable.add()
					.expandX()
					.fillX();

		Button help = new ImageButton(this.skin.getStyle("help-button", ImageButtonStyle.class));
		help.setClickListener(this.helpClick);
		bottomTable.add(help)
				   .right();

		this.add(bottomTable)
			.expandX()
			.fillX();
	}

	@Override
	public void onBackPressed()
	{
		((MenuState) this.controller).showMainMenuView();
	}

	private ClickListener modeClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			GameModeType mode = GameModeType.fromString(actor.name);
			((MenuState) (ProgressiveSelectionView.this.controller)).enterProgressiveGame(mode);
		}
	};

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			ProgressiveSelectionView.this.onBackPressed();
		}
	};

	private ClickListener helpClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) (ProgressiveSelectionView.this.controller)).showProgressiveHelpWindow();
		}
	};
}
