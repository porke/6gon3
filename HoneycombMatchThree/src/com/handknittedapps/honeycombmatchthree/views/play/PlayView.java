package com.handknittedapps.honeycombmatchthree.views.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.PlayState;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.logic.Gamestate;
import com.handknittedapps.honeycombmatchthree.views.BaseView;

public class PlayView extends BaseView
{
	public static final String ViewId = "play";

	public PlayView(PlayState controller, Gamestate gamestate)
	{
		super(controller, PlayView.ViewId);
		this.gamestate = gamestate;
		this.bottom().left().width(HoneycombMatchThree.NominalWidth);

		// Add the help view
		final ImageButton helpButton = new ImageButton(Resources.getSkin().getStyle("help-button", ImageButtonStyle.class));
		helpButton.setClickListener(this.helpClick);

		if (HoneycombMatchThree.IsDesktopVersion)
		{
			ImageButton retButton = new ImageButton(Resources.getSkin().getStyle("return-button", ImageButtonStyle.class));
			retButton.setClickListener(this.backClick);
			this.add(retButton);
		}

		this.add()
			.expandX()
			.fill();

		this.add(helpButton)
			.right();
		this.pack();
	}

	@Override
	public void act(float delta)
	{
		this.gamestate.onFrame();
	}

	private ClickListener helpClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((PlayState) PlayView.this.controller).setActiveView(HelpView.ViewId);
		}
	};

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			PlayView.this.onBackPressed();
		}
	};

	@Override
	public void onBackPressed()
	{
		this.controller.onBackPressed();
	}

	private Gamestate gamestate;
}
