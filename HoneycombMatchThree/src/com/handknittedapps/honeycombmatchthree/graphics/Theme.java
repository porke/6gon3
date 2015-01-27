package com.handknittedapps.honeycombmatchthree.graphics;

import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;

public class Theme
{
	private final static int BackgroundWidth = 480;
	private final static int BackgroundHeight = 651;

	// Standard, bombs and ghosts
	private ArrayList<TextureRegion> normalBlocks = new ArrayList<TextureRegion>();
	private Texture background;
	private TextureRegion backgroundRegion;
	private ThemeType type;

	private Theme()
	{

	}

	public static Theme create(ThemeType type)
	{
		Theme theme = new Theme();
		theme.type = type;

		// Load the background and the track
		String path = String.format("graphics/%s/", type.toString().toLowerCase(Locale.US));
		theme.background = new Texture(path + "Background.jpg");
		theme.backgroundRegion = new TextureRegion(theme.background, 0, 0, Theme.BackgroundWidth, Theme.BackgroundHeight);

		// Load the normal blocks

		// The other block types are loaded by Resources
		final int blocksPerRow = 3;
		Texture allBlocks = new Texture(path + "Blocks.png");
		for (int i = 0; i < BlockColour.NumColours; ++i)
		{
			int col = i % blocksPerRow;
			int row = i / blocksPerRow;
			int space = 1;
			int x = col * (BlockRenderable.Size + space);
			int y = row * (BlockRenderable.Size + space);
			TextureRegion blockRegion = new TextureRegion(allBlocks, x, y, BlockRenderable.Size, BlockRenderable.Size);
			theme.normalBlocks.add(blockRegion);
		}

		return theme;
	}

	public void dispose()
	{
		this.normalBlocks.get(0).getTexture().dispose();
		this.background.dispose();
	}

	public TextureRegion getBlockTexture(int colour)
	{
		return this.normalBlocks.get(colour);
	}

	public ThemeType getType() { return this.type; }
	public TextureRegion getBackground() { return this.backgroundRegion; }
}
