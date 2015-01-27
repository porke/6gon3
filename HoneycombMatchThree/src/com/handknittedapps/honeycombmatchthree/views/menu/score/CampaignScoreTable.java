package com.handknittedapps.honeycombmatchthree.views.menu.score;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;


public class CampaignScoreTable extends Table
{
	public CampaignScoreTable(CampaignScoreRepository scoreController, CampaignMode campaign)
	{
		super("campaign-score-view");
		Skin skin = Resources.getSkin();
		this.top();

		// Add the header
		Table highscoreHeader = new Table();
		highscoreHeader.setBackground(skin.getPatch("RedCase"));
		Label header = new Label("Highscores - campaign", skin.getStyle(LabelStyle.class));
		header.setAlignment(Align.CENTER);
		highscoreHeader.add(header);
		this.add(highscoreHeader)
			.expandX()
			.fillX();
		this.row();

		// Add missions to a table
		this.missionScoreGridContainer = new Table();
		for (int i = 1; i < campaign.getMissionCount() + 1; ++i)
		{
			Mission missionModel = campaign.getMission(i);
			this.missionScoreGridContainer.add(new CampaignScoreMissionTable(missionModel, scoreController))
					  .top()
					  .expandX()
					  .fillX()
					  .minHeight(200)
					  .minWidth(HoneycombMatchThree.NominalWidth);
		}

		// Add the table with the missions to the scroll pane
		this.missionScrollPane = new FlickScrollPane(this.missionScoreGridContainer);
		this.missionScrollPane.setScrollingDisabled(false, true);
		this.missionScrollPane.setOverscroll(false);
		this.add(this.missionScrollPane)
			.top()
			.minHeight((int) this.missionScoreGridContainer.getMinHeight())
			.minWidth(HoneycombMatchThree.NominalWidth)
			.expandX()
			.fillX();
		this.row();

		// Add navigation buttons
		this.slider = new Slider(0, 1, 1, skin.getStyle(SliderStyle.class));
		this.slider.touchable = false;
		this.add(this.slider)
			.top()
			.width(HoneycombMatchThree.NominalWidth)
			.height(30);
		this.row();

		// Add the totals view
		this.scoreTotalsGrid = new CampaignScoreTotalsTable(scoreController);
		this.add(this.scoreTotalsGrid)
			.top()
			.expandX()
			.fillX();
		this.row();

		this.pack();
	}

	public void updateScoreData(CampaignMode mode, CampaignScoreRepository scoreControl)
	{
		this.scoreTotalsGrid.updateTotalsData(scoreControl);
		List<Actor> missionTables = this.missionScoreGridContainer.getActors();
		for (Actor missionTable : missionTables)
		{
			Mission missionModel = mode.getMission(Integer.valueOf(missionTable.name));
			((CampaignScoreMissionTable) missionTable).updateScoreData(missionModel, scoreControl);
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.slider.setValue(this.missionScrollPane.getScrollPercentX());
	}

	public void resetMissionScroll()
	{
		this.missionScrollPane.setScrollX(0);
		this.missionScrollPane.setVelocityX(0);
	}

	private Slider slider;
	private FlickScrollPane missionScrollPane;
	private CampaignScoreTotalsTable scoreTotalsGrid;
	private Table missionScoreGridContainer;
}
