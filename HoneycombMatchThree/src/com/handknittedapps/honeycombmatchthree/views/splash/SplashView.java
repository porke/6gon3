package com.handknittedapps.honeycombmatchthree.views.splash;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.IGameState;
import com.handknittedapps.honeycombmatchthree.views.BaseView;

public class SplashView extends BaseView
{
	private static final int LogoWidth = 425;
	private static final int LogoHeight = 607;

	public SplashView(IGameState controller)
	{
		super(controller, "splash-view");

		Texture logoTex = new Texture("graphics/hud/HandKnittedApps.jpg");
		Image logo = new Image(new TextureRegion(logoTex));
		logo.x = (HoneycombMatchThree.NominalWidth - LogoWidth) / 2;
		logo.y = (-LogoHeight) / 2;
		this.addActor(logo);
	}

	@Override
	public void onBackPressed()
	{
		this.controller.onBackPressed();
	}
}
