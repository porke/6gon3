package com.handknittedapps.honeycombmatchthree.views.menu.help;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;

public class MenuHelpTable extends Table
{
	private static final String CampaignDesc = "Here you can get to know the game while playing missions that increase in difficulty";
	private static final String InfiniteDesc = "Choose one of the modes and try to achieve the best score beating an infinite number of stages";
	private static final String ScoreDesc = "Click here to check out your stats";
	private static final String ExitDesc = "If you wish to quit, touch exit";

	public MenuHelpTable()
	{
		this.top();
		Skin skin = Resources.getSkin();

		Table header = new Table();
		header.setBackground(skin.getPatch("BlueCase"));

		Label help = new Label("Menu options", skin);
		help.setAlignment(Align.CENTER);
		header.add(help)
			  .expandX()
			  .fillX();

		this.add(header)
			.expandX()
			.fillX();
		this.row();

		this.createHelpEntry(SpecialCharacters.CAMPAIGN.toString(), MenuHelpTable.CampaignDesc, skin);
		this.createHelpEntry(SpecialCharacters.INFINITY.toString(), MenuHelpTable.InfiniteDesc, skin);
		this.createHelpEntry(SpecialCharacters.SCORE.toString(), MenuHelpTable.ScoreDesc, skin);
		this.createHelpEntry(SpecialCharacters.EXIT.toString(), MenuHelpTable.ExitDesc, skin);
	}

	private void createHelpEntry(String character, String description, Skin skin)
	{
		Table content = new Table();
		content.setBackground(skin.getPatch("YellowCase"));

		Label icon = new Label(character, skin.getStyle("menu-label", LabelStyle.class));
		icon.setAlignment(Align.CENTER);
		content.add(icon)
			   .padLeft(7)
			   .padRight(7);
		Label label = new Label(description, skin);
		label.setWrap(true);
		content.add(label)
			   .expandX()
			   .fillX();

		this.add(content)
			.expandX()
			.fillX();
		this.row();
	}
}
