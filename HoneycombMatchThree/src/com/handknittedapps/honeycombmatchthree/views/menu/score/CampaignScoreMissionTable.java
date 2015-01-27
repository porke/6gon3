package com.handknittedapps.honeycombmatchthree.views.menu.score;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class CampaignScoreMissionTable extends Table
{
	/** The view consists of 2 columns. */
	public CampaignScoreMissionTable(Mission missionModel, CampaignScoreRepository scoreControl)
	{
		super(String.valueOf(missionModel.getId()));
		this.top();
		Skin skin = Resources.getSkin();
		this.setBackground(skin.getPatch("YellowCase"));

		// Mission header
		this.header = new Label(missionModel.getName(), skin.getStyle(LabelStyle.class));
		this.header.setAlignment(Align.CENTER);
		this.header.setWrap(true);
		this.add(this.header)
			.colspan(2)
			.expandX()
			.fillX();
		this.row();

		// Mission data grid
		this.scoreDataGrid = new ScoreDataGrid(scoreControl.getScore(missionModel.getId()));
		this.add(this.scoreDataGrid)
			.colspan(2)
			.expandX()
			.fillX();
		this.row();

		// Add date
		this.dateHeader = new Label("", skin.getStyle(LabelStyle.class));
		this.dateHeader.setAlignment(Align.RIGHT);
		this.add(new Label("Date completed", skin))
			.expandX()
			.fillX()
			.pad(0, ScoreDataGrid.Padding, 0, 0);
		this.add(this.dateHeader)
			.expandX()
			.fillX()
			.pad(0, 0, 0, ScoreDataGrid.Padding);

		this.updateScoreData(missionModel, scoreControl);
		this.pack();
	}

	public void updateScoreData(Mission missionModel, CampaignScoreRepository scoreControl)
	{
		this.header.setText("Mission - " + missionModel.getName());

		GameStats scoreModel = scoreControl.getScore(missionModel.getId());
		SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date(scoreModel.getDate());

		String dateString = date.getTime() != 0 ? dateFmt.format(date) : "-";
		this.dateHeader.setText(dateString);

		this.scoreDataGrid.updateScoreData(scoreModel);
	}

	private Label header;
	private Label dateHeader;
	private ScoreDataGrid scoreDataGrid;
}
