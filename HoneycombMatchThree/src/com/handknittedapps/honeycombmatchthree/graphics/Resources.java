package com.handknittedapps.honeycombmatchthree.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Resources
{
	private static final int GhostFrameSize = 64;

	private static Texture background;
	private static Texture endgameOverlay;
	private static Texture darkenTexture;
	private static Texture ghostBlockTexture;
	private static Texture bombBlockTexture;
	private static Texture immobileBlockTexture;
	private static TextureRegion pushedMarker;
	private static TextureRegion hoverMarker;
	private static List<TextureRegion> ghostBlocks = new ArrayList<TextureRegion>();
	private static List<TextureRegion> bombBlocks = new ArrayList<TextureRegion>();
	private static List<TextureRegion> immobileBlocks = new ArrayList<TextureRegion>();
	private static List<TextureRegion> strongIcons = new ArrayList<TextureRegion>();
	private static List<TextureRegion> ghostBlinkAnimationFrames;
	private static BitmapFont font;
	private static Skin skin;

	public static void load()
	{
		// Load graphics
		// Load the background
		Resources.background = new Texture("graphics/hud/MainBackground.jpg");

		// Load the endgame overlay
		Resources.endgameOverlay = new Texture("graphics/hud/EndgameOverlay.png");

		// Create the darken texture
		Pixmap pm = new Pixmap(2, 2, Format.RGB888);
		pm.setColor(Color.BLACK);
		pm.fill();
		Resources.darkenTexture = new Texture(pm);

		// Create the ghost animation
		Texture animationTexture = new Texture("graphics/special/GhostBlinkAnimation.png");
		TextureRegion[][] frames = TextureRegion.split(animationTexture, Resources.GhostFrameSize, Resources.GhostFrameSize);
		Resources.ghostBlinkAnimationFrames = new ArrayList<TextureRegion>();
		for (int r = 0; r < frames.length; ++r)
		{
			for (int c = 0; c < frames[r].length; ++c)
			{
				Resources.ghostBlinkAnimationFrames.add(frames[r][c]);
			}
		}

		// Load ghost blocks
		Resources.ghostBlockTexture = new Texture("graphics/special/GhostBlocks.png");
		Resources.loadBlocks(Resources.ghostBlockTexture, 8, Resources.ghostBlocks);

		// Load bomb blocks
		Resources.bombBlockTexture = new Texture("graphics/special/BombBlocks.png");
		Resources.loadBlocks(Resources.bombBlockTexture, 8, Resources.bombBlocks);

		// Load immobile blocks
		Resources.immobileBlockTexture = new Texture("graphics/special/ImmobileBlocks.png");
		Resources.loadBlocks(Resources.immobileBlockTexture, 2, Resources.immobileBlocks);

		// Create the selection markers
		Resources.hoverMarker = new TextureRegion(Resources.immobileBlockTexture, 0, 80, 96, 84);
		Resources.pushedMarker = new TextureRegion(Resources.immobileBlockTexture, 97, 80, 96, 84);

		// Create the strong icons
		Resources.strongIcons.add(new TextureRegion(Resources.immobileBlockTexture, 0, 164, 79, 79));
		Resources.strongIcons.add(new TextureRegion(Resources.immobileBlockTexture, 79, 164, 79, 79));

		Resources.skin = new Skin(Gdx.files.internal("graphics/hud/uiskin.json"), Gdx.files.internal("graphics/hud/uiskin.png"));
		Resources.font = new BitmapFont(Gdx.files.internal("graphics/hud/DefaultFont.fnt"), false);
	}

	private static void loadBlocks(Texture allBlocks, int numBlocks, List<TextureRegion> out_destArray)
	{
		final int blocksPerRow = 3;
		for (int i = 0; i < numBlocks; ++i)
		{
			int col = i % blocksPerRow;
			int row = i / blocksPerRow;
			int space = 1;
			int x = col * (BlockRenderable.Size + space);
			int y = row * (BlockRenderable.Size + space);
			TextureRegion blockRegion = new TextureRegion(allBlocks, x, y, BlockRenderable.Size, BlockRenderable.Size);
			out_destArray.add(blockRegion);
		}
	}

	public static void dispose()
	{
		Resources.background.dispose();
		Resources.endgameOverlay.dispose();
		Resources.darkenTexture.dispose();

		Resources.ghostBlinkAnimationFrames.clear();

		Resources.skin.dispose();
		Resources.font.dispose();

		Resources.bombBlockTexture.dispose();
		Resources.bombBlocks.clear();
		Resources.immobileBlockTexture.dispose();
		Resources.immobileBlocks.clear();
		Resources.ghostBlockTexture.dispose();
		Resources.ghostBlocks.clear();
		Resources.strongIcons.clear();
	}

	public static TextureRegion getStrongBlock(int colour) {return Resources.strongIcons.get(colour);}
	public static TextureRegion getBombBlock(int colour) { return Resources.bombBlocks.get(colour); }
	public static TextureRegion getImmobileBlock(int colour) { return Resources.immobileBlocks.get(colour); }
	public static TextureRegion getGhostBlock(int colour) { return Resources.ghostBlocks.get(colour); }
	public static TextureRegion getPushedMarker() { return Resources.pushedMarker; }
	public static TextureRegion getHoverMarker() { return Resources.hoverMarker; }
	public static Texture getBackground() { return Resources.background; }
	public static Texture getEndgameOverlay() { return Resources.endgameOverlay; }
	public static Texture getDarkenTexture() { return Resources.darkenTexture; }
	public static List<TextureRegion> getGhostBlinkAnimation() { return Resources.ghostBlinkAnimationFrames; }
	public static Skin getSkin() {return Resources.skin;}
	public static BitmapFont getDefaultFont() {return Resources.font;}
}
