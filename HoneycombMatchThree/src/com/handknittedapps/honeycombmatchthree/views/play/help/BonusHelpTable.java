package com.handknittedapps.honeycombmatchthree.views.play.help;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class BonusHelpTable extends Table
{
	private static final String[] Descriptions = new String[]
	{
		"Allows swapping tiles that are two spaces away",
		"Rotates tiles around the selected one clockwise or anticlockwise",
		"Allows you to change the color of the selected block to another color",
		"Changes standard tile into a bomb",
		"Boosts your energy income for the next few moves",
		"Erases a line off the grid",
		"Destroys blocks of the selected color",
		"Stops tiles of a certain color from falling for a few moves",
		"Changes every tile of a single color into another color",
	};

	public BonusHelpTable(Skin skin)
	{
		this.top().left();

		// Create header table
		Table headerTable = new Table();
		headerTable.setBackground(skin.getPatch("BlueCase"));
		Label header = new Label("Bonuses", skin.getStyle(LabelStyle.class));
		header.setAlignment(Align.CENTER);
		headerTable.add(header);
		this.add(headerTable)
			.colspan(2)
			.expandX()
			.fillX();
		this.row();

		// Create content tables
		for (int i = 0; i < MoveType.values().length; ++i)
		{
			MoveType mt = MoveType.values()[i];
			if (mt == MoveType.DefaultSwap)
			{
				continue;
			}

			this.row();

			String path = "graphics/hud/bonus/";
			switch(mt.type)
			{
				case 0:
					path += "Red";
					break;
				case 1:
					path += "Green";
					break;
				case 2:
					path += "Blue";
					break;
			}

			path += mt.name;
			path += "Normal.png";

			// Fill the content table
			Table contentTable = new Table();
			contentTable.setBackground(skin.getPatch("YellowCase"));

			Image icon = new Image(new TextureRegion(new Texture(path)));
			contentTable.add(icon)
				.width((int) icon.width)
				.height((int) icon.height);

			// - 1 because the default swap is omitted
			Label desc = new Label(BonusHelpTable.Descriptions[i - 1], skin.getStyle(LabelStyle.class));
			desc.setWrap(true);
			contentTable.add(desc)
						.expandX()
						.fillX();

			this.add(contentTable)
				.width(HoneycombMatchThree.NominalWidth);
		}
	}
}
