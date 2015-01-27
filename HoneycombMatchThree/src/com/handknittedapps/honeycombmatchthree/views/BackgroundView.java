package com.handknittedapps.honeycombmatchthree.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.IGameState;

public abstract class BackgroundView extends BaseView
{
	public BackgroundView(IGameState controller, String title, Texture background)
	{
		super(controller, title);

		// Create the background
		this.background = new Image(new TextureRegion(background, 0, 0, HoneycombMatchThree.NominalWidth, HoneycombMatchThree.NominalHeight));
		this.background.width = HoneycombMatchThree.NominalWidth;
		this.background.height = HoneycombMatchThree.NominalHeight;
		this.addActor(this.background);

		// Create the shadow for the menu screen
		Table shadow = new Table("mainShadow");
		shadow.width = HoneycombMatchThree.NominalWidth;
		shadow.height = HoneycombMatchThree.NominalHeight;
		shadow.setBackground(this.skin.getPatch("shadow"));
		this.addActor(shadow);
	}

	private Image background;
}
