package com.handknittedapps.honeycombmatchthree.views.briefing.sections;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class UnlocksTable extends Table
{
	public UnlocksTable(Skin skin, Mission mission)
	{
		this.width(HoneycombMatchThree.NominalWidth);

		String unlockablePath = "";
		String unlockedName = "";
		String unlockedType = "";
		TextureRegion tex = null;

		if (mission.getUnlockedBlock() != null)
		{
			unlockedName = mission.getUnlockedBlock().toString();
			unlockedType =  "block";

			if (mission.getUnlockedBlock() == BlockType.Bomb)
			{
				tex = Resources.getBombBlock(BlockColour.Blue.ordinal());
			}
			else if (mission.getUnlockedBlock() == BlockType.Ghost)
			{
				tex = Resources.getGhostBlock(BlockColour.Green.ordinal());
			}
			else if (mission.getUnlockedBlock() == BlockType.Strong)
			{
				tex = Resources.getStrongBlock(0);
			}
			else
			{
				tex = Resources.getImmobileBlock(0);
			}
		}
		else if (mission.getUnlockedMove() != null)
		{
			MoveType move = mission.getUnlockedMove();
			unlockedName = move.name;
			unlockedType = "move";
			unlockablePath = "graphics/hud/bonus/" + move.getColour() + move.toString() + "Normal.png";
		}
		else if (mission.getUnlockedTheme() != null)
		{
			unlockedName = mission.getUnlockedTheme().toString();
			unlockedType = "theme";
			unlockablePath = "graphics/stages/" + mission.getUnlockedTheme().colour + "StageNormal.png";
		}
		else if (mission.getUnlockedMode() != null)
		{
			unlockedName = mission.getUnlockedMode().displayedName;
			unlockedType = "mode";
			unlockablePath = "graphics/modes/" + mission.getUnlockedMode().toString() + "Normal.png";
		}

		// Do not add any controls if the mission does not unlock anything
		if (unlockablePath.length() != 0)
		{
			Label lblUnlocks = new Label("Unlocks " + unlockedType + ":\n" + unlockedName, skin.getStyle(LabelStyle.class));
			Image imgUnlocks = new Image(new Texture(unlockablePath));
			lblUnlocks.setWrap(true);
			lblUnlocks.setAlignment(Align.CENTER, Align.CENTER);
			this.add(imgUnlocks)
				.pad(4);
			this.add(lblUnlocks)
				.expandX()
				.fillX();
		}
		else if (tex != null)
		{
			Label lblUnlocks = new Label("Unlocks " + unlockedType + ":\n" + unlockedName, skin.getStyle(LabelStyle.class));
			Image imgUnlocks = new Image(tex);
			lblUnlocks.setWrap(true);
			lblUnlocks.setAlignment(Align.CENTER, Align.CENTER);
			this.add(imgUnlocks)
				.pad(4);
			this.add(lblUnlocks)
				.expandX()
				.fillX();
		}
	}
}
