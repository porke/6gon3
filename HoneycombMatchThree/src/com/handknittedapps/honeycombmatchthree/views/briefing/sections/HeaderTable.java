package com.handknittedapps.honeycombmatchthree.views.briefing.sections;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;

public class HeaderTable extends Table
{
	public HeaderTable(Skin skin, GameMode gameMode, ThemeType themeType, String headerText)
	{
		this.width(HoneycombMatchThree.NominalWidth);
		this.setBackground(skin.getPatch("YellowCase"));

		// Mission type image
		Image imgMissionType = new Image(new Texture("graphics/modes/" + gameMode.getModeType().name() + "Normal.png"));
		this.add(imgMissionType)
			.pad(4)
			.left()
			.minWidth((int) imgMissionType.getPrefWidth());

		// Mission name
		Label lblMissionName = new Label(headerText, skin.getStyle(LabelStyle.class));
		this.add(lblMissionName)
			.expandX()
			.minHeight(HoneycombMatchThree.NominalHeight / 12)
			.center();

		// Mission theme image
		Image imgMissionTheme = new Image(new Texture("graphics/stages/" + themeType.colour + "StageNormal.png"));
		this.add(imgMissionTheme)
			.pad(4)
			.right();
	}
}
