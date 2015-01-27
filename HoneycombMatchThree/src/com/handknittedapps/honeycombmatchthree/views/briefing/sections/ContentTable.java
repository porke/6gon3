package com.handknittedapps.honeycombmatchthree.views.briefing.sections;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.access.GameSettingsRepository;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;

public class ContentTable extends Table
{
	public ContentTable(Skin skin, GameMode gameMode, PlayModeType playMode, Mission mission)
	{
		GameSettingsRepository settings = new GameSettingsRepository();
		String missionDesc = settings.getSetting(gameMode.getModeType().toString() + "ModeDesc");
		Label lblMissionDesc = new Label(playMode == PlayModeType.Campaign ? mission.getBriefing() : missionDesc, skin.getStyle(LabelStyle.class));

		lblMissionDesc.setAlignment(Align.TOP, Align.LEFT);
		lblMissionDesc.setWrap(true);

		this.add(lblMissionDesc)
			.top()
			.expand()
			.fill()
			.width(HoneycombMatchThree.NominalWidth);
	}
}
