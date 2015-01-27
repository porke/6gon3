package com.handknittedapps.honeycombmatchthree.views.menu.score;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class CampaignScoreTotalsTable extends Table
{
	/** @param controller An initialized score controller. */
	public CampaignScoreTotalsTable(CampaignScoreRepository controller)
	{
		super("campaign-totals");
		Skin skin = Resources.getSkin();
		this.setBackground(skin.getPatch("BlueCase"));

		Label totals = new Label("Campaign totals", skin.getStyle(LabelStyle.class));
		totals.setAlignment(Align.CENTER);
		this.add(totals).colspan(2).expandX().fillX();
		this.row();

		GameStats scoreTotal = controller.getTotals();
		this.totalsGrid = new ScoreDataGrid(scoreTotal);
		this.add(this.totalsGrid).expandX().fillX();
	}

	public void updateTotalsData(CampaignScoreRepository scoreControl)
	{
		this.totalsGrid.updateScoreData(scoreControl.getTotals());
	}

	private ScoreDataGrid totalsGrid;
}
