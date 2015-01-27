package com.handknittedapps.honeycombmatchthree.logic.subsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class MoveDebugOutput
{
	public MoveDebugOutput(boolean active)
	{
		this.isActive = active;
		Gdx.files.external(HoneycombMatchThree.name + "/debug.txt").delete();

		if (this.isActive)
		{
			this.debugFile = Gdx.files.external(HoneycombMatchThree.name + "/debug.txt");
		}
	}

	public void preMove(Map map, Point moveSource, int direction)
	{
		if (this.isActive)
		{
			++this.moveNumber;
			this.debugFile.writeString("Move number: " + this.moveNumber, true);
			this.debugFile.writeString("\nMap state:\n", true);
			this.debugFile.writeString(map.toString(), true);

			this.debugFile.writeString("Move from: " + moveSource + ", dir: " + direction + "\n", true);
			this.debugFile.writeString("Move type: " + map.getCurrentMoveMode() + "\n\n", true);
		}
	}

	public void postMove(Map map, Point moveSource, int direction)
	{
		if (this.isActive)
		{
			this.debugFile.writeString("Post move map state:\n", true);
			this.debugFile.writeString(map.toString(), true);
		}
	}

	private int moveNumber;
	private FileHandle debugFile;
	private boolean isActive;
}
