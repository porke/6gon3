package com.handknittedapps.honeycombmatchthree.views.briefing.sections;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.GameSettingsRepository;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;

public class WinTable extends Table
{
	public WinTable(GameMode gameMode, Skin skin)
	{
		this.setBackground(skin.getPatch("GreenCase"));

		GameSettingsRepository settings = new GameSettingsRepository();
		String winDesc = settings.getSetting(gameMode.getModeType() + "ModeWinMsg");
		Label lblWinSpec = new Label(winDesc.replace("[win]", String.valueOf(gameMode.getWinCondition())), skin.getStyle(LabelStyle.class));
		lblWinSpec.setWrap(true);
		lblWinSpec.setAlignment(Align.CENTER, Align.LEFT);
		this.add(lblWinSpec)
		    .prefWidth(HoneycombMatchThree.NominalWidth);
	}
}
