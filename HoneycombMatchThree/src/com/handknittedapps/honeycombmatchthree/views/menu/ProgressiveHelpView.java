package com.handknittedapps.honeycombmatchthree.views.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.MenuState;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.views.LogoView;
import com.handknittedapps.honeycombmatchthree.views.VerticalScrollView;
import com.handknittedapps.honeycombmatchthree.views.menu.help.ProgressiveHelpTable;

public class ProgressiveHelpView extends LogoView
{


	public ProgressiveHelpView(MenuState controller)
	{
		super(controller, "help-view");
		this.bottom();

		ProgressiveHelpTable helpContent = new ProgressiveHelpTable();
		VerticalScrollView helpPane = new VerticalScrollView(helpContent);

		final int VisibleIcons = 4;
		final int TotalIcons = GameModeType.values().length;
		this.add(helpPane)
			.maxHeight((int) (helpContent.getPrefHeight() * (float) VisibleIcons / TotalIcons))
			.bottom()
			.expandX()
			.fillX();
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
		((MenuState) this.controller).showProgressiveView();
	}

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			ProgressiveHelpView.this.onBackPressed();
		}
	};
}
