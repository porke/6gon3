package com.handknittedapps.honeycombmatchthree.views.briefing.sections;

import java.text.DecimalFormat;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.GameSettingsRepository;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.HardcoreMode;

public class LoseTable extends Table
{
	public LoseTable(GameMode gameMode, Skin skin)
	{
		this.setBackground(skin.getPatch("RedCase"));

		GameSettingsRepository settings = new GameSettingsRepository();
		String loseDesc = settings.getSetting(gameMode.getModeType() + "ModeLoseMsg");

		DecimalFormat fmt = new DecimalFormat("#.#");
		loseDesc = loseDesc.replace("[lose]", fmt.format(gameMode.getLoseCondition()));

		if (gameMode.getModeType() == GameModeType.Hardcore)
		{
			loseDesc = loseDesc.replace("[lose2]", String.valueOf( (int)((HardcoreMode)gameMode).getLoseConditionMana()));
		}

		Label lblLoseSpec = new Label(loseDesc, skin.getStyle(LabelStyle.class));
		lblLoseSpec.setAlignment(Align.CENTER, Align.LEFT);
		lblLoseSpec.setWrap(true);
		this.add(lblLoseSpec)
		    .prefWidth(HoneycombMatchThree.NominalWidth);
	}
}
