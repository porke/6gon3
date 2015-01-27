package com.handknittedapps.honeycombmatchthree.views.campaign;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.Scaling;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class ShowcaseTable extends Table
{
	public ShowcaseTable(CampaignMode campaign)
	{
		super("showcaseIcons");

		this.skin = Resources.getSkin();

		Table blocks = this.createShowcaseUnlockedBlocks(campaign.getUnlockedBlocks());
		Table moves = this.createShowcaseUnlockedMoves(campaign.getUnlockedBonuses());
		Table modes = this.createShowcaseUnlockedModes(campaign.getUnlockedModes());
		Table themes = this.createShowcaseUnlockedThemes(campaign.getUnlockedThemes());

		this.add(blocks);
		this.add(themes);
		this.add(modes);
		this.add(moves);
	}

	private Table createShowcaseUnlockedThemes(ThemeType[] unlockedThemes)
	{
		Table themes = new Table("themes");
		themes.setBackground(this.skin.getPatch("GreenCase"));

		// Unlocked themes
		themes.add(new Image(new TextureRegion(new Texture("graphics/stages/WhiteStageNormal.png"))));

		if (Arrays.binarySearch(unlockedThemes, ThemeType.Water) >= 0)
		{
			themes.add(new Image(new TextureRegion(new Texture("graphics/stages/BlueStageNormal.png"))))
				  .padLeft(4);
		}
		else
		{
			themes.add(new Image(new TextureRegion(new Texture("graphics/stages/BlueStageInactive.png"))))
				  .padLeft(4);
		}

		if (Arrays.binarySearch(unlockedThemes, ThemeType.Industrial) >= 0)
		{
			themes.add(new Image(new TextureRegion(new Texture("graphics/stages/RedStageNormal.png"))))
				  .padLeft(4);
		}
		else
		{
			themes.add(new Image(new TextureRegion(new Texture("graphics/stages/RedStageInactive.png"))))
				  .padLeft(4);
		}

		if (Arrays.binarySearch(unlockedThemes, ThemeType.Nature) >= 0)
		{
			themes.add(new Image(new TextureRegion(new Texture("graphics/stages/GreenStageNormal.png"))))
				  .padLeft(4);
		}
		else
		{
			themes.add(new Image(new TextureRegion(new Texture("graphics/stages/GreenStageInactive.png"))))
			      .padLeft(4);
		}

		themes.add(new Image(new TextureRegion(new Texture("graphics/stages/YellowStageNormal.png"))))
		  		  .padLeft(4);

		return themes;
	}

	private Table createShowcaseUnlockedModes(GameModeType[] gameModes)
	{
		Table modes = new Table("modes");
		modes.setBackground(this.skin.getPatch("YellowCase"));

		GameModeType[] modeTypes = GameModeType.values();
		for (GameModeType mt : modeTypes)
		{
			String path = "graphics/modes/" + mt.name() + (Arrays.binarySearch(gameModes, mt) >= 0 ?  "Normal" : "Inactive") + ".png";
			modes.add(new Image(new TextureRegion(new Texture(path))))
				 .padLeft(4);
		}

		return modes;
	}

	private Table createShowcaseUnlockedMoves(MoveType[] unlockedMoves)
	{
		Table moves = new Table("moves");
		moves.setBackground(this.skin.getPatch("BlueCase"));

		MoveType[] allMoves = MoveType.values();
		for (MoveType mt : allMoves)
		{
			if (mt == MoveType.DefaultSwap)
			{
				continue;
			}

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

			boolean isUnlocked = false;
			for (MoveType cmt : unlockedMoves)
			{
				if (mt.equals(cmt))
				{
					isUnlocked = true;
					break;
				}
			}

			path += mt.name;
			path += (isUnlocked) ? "Normal" : "Inactive";
			path += ".png";

			moves.add(new Image(new TextureRegion(new Texture(path))))
				 .padLeft(4);
		}

		return moves;
	}

	private Table createShowcaseUnlockedBlocks(BlockType[] unlockedBlocksArray)
	{
		Table blocks = new Table("blocks");
		blocks.setBackground(this.skin.getPatch("RedCase"));
		List<BlockType> unlockedBlocks = (List<BlockType>) Arrays.asList(unlockedBlocksArray);

		// Active blocks
		for (BlockType blockType : BlockType.values())
		{
			boolean contains = unlockedBlocks.contains(blockType);
			TextureRegion textureReg = null;
			switch (blockType)
			{
				case Ghost:
					textureReg = Resources.getGhostBlock(contains ? BlockColour.Blue.ordinal() : BlockColour.Null.ordinal());
					break;
				case Bomb:
					textureReg = Resources.getBombBlock(contains ? BlockColour.Green.ordinal() : BlockColour.Null.ordinal());
					break;
				case Immobile:
					textureReg = Resources.getImmobileBlock(contains ? 0 : 1);
					break;
				case Strong:
					textureReg = Resources.getStrongBlock(contains ? 0 : 1);
					break;
				case Normal:
					break;
			}

			Image addedImage = new Image(textureReg);
			addedImage.setScaling(Scaling.fit);

			blocks.add(addedImage)
				  .height(64);
		}

		return blocks;
	}

	private Skin skin;
}
