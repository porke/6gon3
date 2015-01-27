package com.handknittedapps.honeycombmatchthree.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.IGameState;
import com.handknittedapps.honeycombmatchthree.control.MenuState;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public abstract class LogoView extends BackgroundView
{
	public LogoView(IGameState controller, String title)
	{
		super(controller, title, Resources.getBackground());

		// Create the logo
		String logoPath = "graphics/hud/Logo.png";
		Texture logoTex = new Texture(logoPath);
		this.logo = new Image(logoTex);
		this.logo.width = logoTex.getWidth();
		this.logo.height = logoTex.getHeight();

		this.logo.x = (HoneycombMatchThree.NominalWidth - this.logo.width) / 2;
		this.logo.y = HoneycombMatchThree.NominalHeight * 0.95f - this.logo.height;

		this.addActor(this.logo);
	}

	public void showAchievementLoginPrompt()
	{
		this.dialog = new YesNoAlwaysNeverDialog
		("Do you want to login to the Swarm achievement service? If you select Never, " +
				"you can still login by selecting the Achievement button in the upper right-hand " +
				"corner of the main menu screen."
		 , this.achievementLoginExecute);

		this.addActor(this.dialog);
	}

	private ClickListener achievementLoginExecute = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((MenuState) LogoView.this.controller).handleAchievementLoginPromptResponse(LogoView.this.dialog.getReturnValue());
			LogoView.this.removeActor(LogoView.this.dialog);
			LogoView.this.dialog = null;
		}
	};

	private YesNoAlwaysNeverDialog dialog;

	private Image logo;
}
