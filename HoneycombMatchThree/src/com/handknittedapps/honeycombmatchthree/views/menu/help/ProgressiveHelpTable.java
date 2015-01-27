package com.handknittedapps.honeycombmatchthree.views.menu.help;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.data.access.GameSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public class ProgressiveHelpTable extends Table
{
	public ProgressiveHelpTable()
	{
		this.bottom();
		Skin skin = Resources.getSkin();

		Table header = new Table();
		header.setBackground(skin.getPatch("BlueCase"));

		Label help = new Label("Infinite modes", skin);
		help.setAlignment(Align.CENTER);
		header.add(help)
			  .expandX()
			  .fillX();

		this.add(header)
			.expandX()
			.fillX();
		this.row();

		GameModeType[] values = GameModeType.values();
		String[] displayedNames = new String[] {SpecialCharacters.CASUAL.toString(),
												SpecialCharacters.TIME_ATTACK.toString(),
												SpecialCharacters.SURVIVAL.toString(),
												SpecialCharacters.HARDCORE.toString(),
												SpecialCharacters.CLASSIC.toString()};

		GameSettingsRepository settings = new GameSettingsRepository();
		String[] contentValues = new String[] {settings.getSetting(GameModeType.Casual + "ModeDesc"),
											   settings.getSetting(GameModeType.TimeAttack + "ModeDesc"),
											   settings.getSetting(GameModeType.Survival + "ModeDesc"),
											   settings.getSetting(GameModeType.Hardcore + "ModeDesc"),
											   settings.getSetting(GameModeType.Classic + "ModeDesc")};

		for (int i = 0; i < values.length; ++i)
		{
			Table content = new Table();
			content.setBackground(skin.getPatch("YellowCase"));

			Label logoLbl = new Label(displayedNames[i].toString(), skin.getStyle("menu-label", LabelStyle.class));
			logoLbl.setAlignment(Align.CENTER);
			content.add(logoLbl)
				   .padRight(5);

			Label contentLbl = new Label(contentValues[i], skin);
			contentLbl.setWrap(true);
			content.add(contentLbl)
				   .expandX()
				   .fillX();

			this.add(content)
				.expandX()
				.fillX();

			this.row();
		}
	}
}
