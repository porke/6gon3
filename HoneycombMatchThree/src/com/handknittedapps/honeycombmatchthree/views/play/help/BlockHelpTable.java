package com.handknittedapps.honeycombmatchthree.views.play.help;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;

public class BlockHelpTable extends Table
{
	private static final String[] Descriptions = new String[]
	{
		"Explodes when put into sequence, destroying all tiles around",
		"Has to be in a sequence three times before it disappears",
		"Cannot be moved, the only way to get rid of it is with explosives",
		"Disappears only when three or more, of any color, are put together"
	};

	public BlockHelpTable(Skin skin)
	{
		this.top().left();

		// Create header table
		Table headerTable = new Table();
		headerTable.setBackground(skin.getPatch("BlueCase"));
		Label header = new Label("Blocks", skin.getStyle(LabelStyle.class));
		header.setAlignment(Align.TOP, Align.CENTER);
		headerTable.add(header);
		this.add(headerTable)
			.top()
			.left()
			.expandX()
			.fillX();
		this.row();

		// Create content tables
		TextureRegion[] images = new TextureRegion[]
		{
			Resources.getBombBlock(BlockColour.Green.ordinal()),
			Resources.getStrongBlock(0),
			Resources.getImmobileBlock(0),
			Resources.getGhostBlock(BlockColour.Blue.ordinal())
		};

		for (int i = 0; i < images.length; ++i)
		{
			this.row();

			Table contentTable = new Table();
			contentTable.setBackground(skin.getPatch("YellowCase"));

			Image img = new Image(images[i]);
			contentTable.add(img)
						.width((int) img.width)
						.height((int) img.height);

			Label desc = new Label(BlockHelpTable.Descriptions[i], skin.getStyle(LabelStyle.class));
			desc.setWrap(true);
			contentTable.add(desc)
			.expandX()
			.fillX();

			this.add(contentTable)
				.prefWidth(HoneycombMatchThree.NominalWidth)
				.expand()
				.fill();
		}
	}
}
