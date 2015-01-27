package com.handknittedapps.honeycombmatchthree.views.play.help;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class BasicHelpTable extends Table
{
	private final static String Content =
			"Connect three of more blocks of the same colour. " +
			"The longer connections, the more points you get. " +
			"Each sequence gives you additional points that you can spend on special moves.";

	private final static String ProgressBarWin = "The golden bar shows you how close you are to winning the mission.";
	private final static String ProgressBarMoves = "The red bar shows you how many moves you can make.";
	private final static String ProgressBarTime = "The blue bar shows you how much time you have left.";

	public BasicHelpTable(Skin skin)
	{
		this.top().left();

		// Create header table
		Table headerTable = new Table();
		headerTable.setBackground(skin.getPatch("BlueCase"));
		Label header = new Label("The basics", skin.getStyle(LabelStyle.class));
		header.setAlignment(Align.TOP, Align.CENTER);
		headerTable.add(header);
		this.add(headerTable)
			.expandX()
			.fillX();
		this.row();

		// Create content table
		Table contentTable = new Table();
		contentTable.setBackground(skin.getPatch("YellowCase"));
		contentTable.top();

		// Basic help
		Label helpContent = new Label(BasicHelpTable.Content, skin.getStyle(LabelStyle.class));
		helpContent.setAlignment(Align.TOP, Align.LEFT);
		helpContent.setWrap(true);
		contentTable.add(helpContent)
			.width(HoneycombMatchThree.NominalWidth);
		contentTable.row();

		//Progress bar reference
		Table progressBarInfo = new Table();
		Texture progressIcons = new Texture("graphics/hud/ProgressBarHelp.png");

		Image victoryImg = new Image(new TextureRegion(progressIcons, 1, 1, 20, 23));
		progressBarInfo.add(victoryImg);
		Label winLabel = new Label(BasicHelpTable.ProgressBarWin, skin.getStyle(LabelStyle.class));
		winLabel.setAlignment(Align.TOP, Align.LEFT);
		winLabel.setWrap(true);
		progressBarInfo.add(winLabel)
					   .expand()
					   .fill();
		progressBarInfo.row();


		Image movesImg = new Image(new TextureRegion(progressIcons, 1, 24, 20, 23));
		progressBarInfo.add(movesImg);
		Label movesLabel = new Label(BasicHelpTable.ProgressBarMoves, skin.getStyle(LabelStyle.class));
		movesLabel.setAlignment(Align.TOP, Align.LEFT);
		movesLabel.setWrap(true);
		progressBarInfo.add(movesLabel)
					   .expand()
					   .fill();
		progressBarInfo.row();

		Image timeImg = new Image(new TextureRegion(progressIcons, 21, 1, 20, 23));
		progressBarInfo.add(timeImg);
		Label timeLabel = new Label(BasicHelpTable.ProgressBarTime, skin.getStyle(LabelStyle.class));
		timeLabel.setAlignment(Align.TOP, Align.LEFT);
		timeLabel.setWrap(true);
		progressBarInfo.add(timeLabel)
					   .expand()
					   .fill();
		progressBarInfo.row();

		contentTable.add(progressBarInfo)
					.expand()
					.fill();

		this.add(contentTable)
			.width(HoneycombMatchThree.NominalWidth);
	}
}
