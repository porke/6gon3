package com.handknittedapps.honeycombmatchthree.views.briefing.sections;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class ScoreTable extends Table
{
	public ScoreTable(Skin skin, String scoreHeader, int score, ClickListener backClickListener)
	{
		this.width(HoneycombMatchThree.NominalWidth);

		// Back button only for desktop
		if (HoneycombMatchThree.IsDesktopVersion)
		{
			Button btnBack = new ImageButton(skin.getStyle("return-button", ImageButtonStyle.class));
			btnBack.setClickListener(backClickListener);
			this.add(btnBack)
				.left();
		}

		// The score labels table
		Table scoreTable = new Table();
		scoreTable.setBackground(skin.getPatch("default-window"));

		// Score label
		Label scoreLabel = new Label(scoreHeader, skin.getStyle(LabelStyle.class));
		scoreLabel.setAlignment(Align.CENTER);
		scoreTable.add(scoreLabel)
				  .expandX();

		// Score value
		Label scoreValue = new Label(String.valueOf(score), skin.getStyle(LabelStyle.class));
		scoreValue.setAlignment(Align.CENTER);
		scoreTable.add(scoreValue)
				  .expandX();
		this.add(scoreTable)
			.expandX()
			.fillX();
	}
}
