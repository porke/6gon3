package com.handknittedapps.honeycombmatchthree.views.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.MenuState;
import com.handknittedapps.honeycombmatchthree.views.LogoView;
import com.handknittedapps.honeycombmatchthree.views.menu.help.MenuHelpTable;

public class MainHelpView extends LogoView
{
	public MainHelpView(MenuState controller)
	{
		super(controller, "help-view");
		this.bottom();

		this.add(new MenuHelpTable())
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
		((MenuState) this.controller).showMainMenuView();
	}

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			MainHelpView.this.onBackPressed();
		}
	};
}
