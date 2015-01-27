package com.handknittedapps.honeycombmatchthree.views;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.IGameState;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public abstract class BaseView extends Table
{
	public static int MainButtonHeight = 64;

	protected BaseView(IGameState controller, String title)
	{
		super(title);

		this.skin = Resources.getSkin();
		this.controller = controller;

		this.width = HoneycombMatchThree.NominalWidth;
		this.height = HoneycombMatchThree.NominalHeight;
		this.top().left();
	}

	public abstract void onBackPressed();

	protected IGameState controller;
	protected Skin skin;
}
