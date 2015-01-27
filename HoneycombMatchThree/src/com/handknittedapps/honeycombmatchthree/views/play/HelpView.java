package com.handknittedapps.honeycombmatchthree.views.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.IGameState;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.views.BaseView;
import com.handknittedapps.honeycombmatchthree.views.VerticalScrollView;
import com.handknittedapps.honeycombmatchthree.views.play.help.BasicHelpTable;
import com.handknittedapps.honeycombmatchthree.views.play.help.BlockHelpTable;
import com.handknittedapps.honeycombmatchthree.views.play.help.BonusHelpTable;

public class HelpView extends BaseView
{
	public static final String ViewId = "help";

	public HelpView(IGameState controller)
	{
		super(controller, HelpView.ViewId);

		this.setBackground(this.skin.getPatch("shadow"));

		Table helpContainer = new Table();
		helpContainer.add(new BasicHelpTable(this.skin));
		helpContainer.row();

		helpContainer.add(new BlockHelpTable(this.skin));
		helpContainer.row();

		helpContainer.add(new BonusHelpTable(this.skin));
		helpContainer.layout();

		VerticalScrollView scrollView = new VerticalScrollView(helpContainer);
		this.add(scrollView)
			.expand()
			.fill();

		if (HoneycombMatchThree.IsDesktopVersion)
		{
			ImageButton retButton = new ImageButton(Resources.getSkin().getStyle("return-button", ImageButtonStyle.class));
			retButton.setClickListener(this.backButtonClick);

			this.row();
			this.add(retButton)
				.left()
				.bottom();
		}
	}

	private ClickListener backButtonClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			HelpView.this.onBackPressed();
		}
	};

	@Override
	public void onBackPressed()
	{
		this.controller.onBackPressed();
	}
}
