package com.handknittedapps.honeycombmatchthree.views.campaign;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.CampaignState;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignProgressRepository;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.views.BackgroundView;

public class CampaignView extends BackgroundView
{
	private static final int ShowcaseHeight = 82;

	public CampaignView(final CampaignState controller, CampaignMode campaign, CampaignProgressRepository progress)
	{
		super(controller, "campaign-view", Resources.getBackground());

		// Create the showcase
		final int stageId = campaign.getStageId();
		campaign.setStageId(0);
		FlickScrollPane showcase = new FlickScrollPane(new ShowcaseTable(campaign));
		showcase.setOverscroll(false);
		this.add(showcase)
 		    .fillX()
		    .minHeight(CampaignView.ShowcaseHeight);
		this.row();

		// Create the campaign tree
		CampaignMissionTreeTable missionTree = new CampaignMissionTreeTable(campaign, missionClick);
		FlickScrollPane missionScrollPane = new FlickScrollPane(missionTree);
		missionScrollPane.setOverscroll(false);
		this.add(missionScrollPane)
			.expand()
			.fill();

		// Scroll the pane to the previous mission
		Cell<?> scroll = missionTree.getCell(String.valueOf(stageId));
		if (scroll != null)
		{
			missionScrollPane.setScrollX(scroll.getColumn() * CampaignMissionTreeTable.MinMissionBtnWidth);
		}
		this.row();

		////////////////////////////////////////
		// Bottom buttons only on desktop
		////////////////////////////////////////
		if (HoneycombMatchThree.IsDesktopVersion)
		{
			Table bottomTable = new Table();

			Button btnReturn = new ImageButton(this.skin.getStyle("return-button", ImageButtonStyle.class));
			btnReturn.setClickListener(this.backClick);

			bottomTable.add(btnReturn)
				.left();

			bottomTable.add()
					   .fillX()
					   .expandX();

			this.add(bottomTable)
				.fillX()
				.expandX();
		}
	}

	@Override
	public void onBackPressed()
	{
		((CampaignState) this.controller).returnFromState();
	}

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			CampaignView.this.onBackPressed();
		}
	};

	private ClickListener missionClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			((CampaignState)CampaignView.this.controller).enterMission(Integer.valueOf(actor.name));
		}
	};
}
