package com.handknittedapps.honeycombmatchthree.graphics;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.handknittedapps.honeycombmatchthree.graphics.events.ScoreOverlayAdvanceEvent;
import com.handknittedapps.honeycombmatchthree.graphics.events.WaiterEvent;
import com.handknittedapps.honeycombmatchthree.utils.Pair;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class ScoreOverlayManager extends Group
{
	public ScoreOverlayManager()
	{
		this.scoreFont = Resources.getDefaultFont();
	}

	@Override
	public void act(float delta)
	{
		for (int i = 0; i < this.currentOverlays.size(); ++i)
		{
			if (!this.currentOverlays.get(i).getFirst().update())
			{
				ScoreOverlay actor = this.currentOverlays.get(i).getSecond();
				this.currentOverlays.remove(i);
				this.removeActor(actor);

				--i;
			}
		}

		for (int i = 0; i < this.awaitingToBeAdded.size(); ++i)
		{
			if (!this.awaitingToBeAdded.get(i).getFirst().update())
			{
				ScoreOverlay overlay = this.awaitingToBeAdded.get(i).getSecond();
				this.awaitingToBeAdded.remove(i);
				this.addActor(overlay);

				ScoreOverlayAdvanceEvent evt = new ScoreOverlayAdvanceEvent(3.0f, true, overlay);
				this.currentOverlays.add(new Pair<ScoreOverlayAdvanceEvent, ScoreOverlay>(evt, overlay));

				--i;
			}
		}
	}

	public void onScore(ArrayList<ArrayList<BlockRenderable>> blocks, Integer score)
	{
		// For each sequence there will be an overlay
		for (int i = 0; i < blocks.size(); ++i)
		{
			ArrayList<BlockRenderable> blockList = blocks.get(i);
			Point averagePosition = new Point();

			int middleBlock = blockList.size() / 2;
			averagePosition = new Point((int) blockList.get(middleBlock).x, (int) blockList.get(middleBlock).y);
			averagePosition.x += BlockRenderable.Size / 2;
			averagePosition.y = averagePosition.y + BlockRenderable.Size / 2;


			BlockRenderable middle = blockList.get(middleBlock);
			ScoreOverlay overlay = new ScoreOverlay(String.valueOf(score / blocks.size()),
									new Point((int) middle.x + BlockRenderable.Size / 2, (int) middle.y + BlockRenderable.Size / 2),
									new Point(0, 48),
									this.scoreFont);

			WaiterEvent waiter = new WaiterEvent(middle.getEventTimeLeft(), true);
			this.awaitingToBeAdded.add(new Pair<WaiterEvent, ScoreOverlay>(waiter, overlay));
		}
	}

	private ArrayList<Pair<WaiterEvent, ScoreOverlay>> awaitingToBeAdded = new ArrayList<Pair<WaiterEvent, ScoreOverlay>>();
	private ArrayList<Pair<ScoreOverlayAdvanceEvent, ScoreOverlay>> currentOverlays = new ArrayList<Pair<ScoreOverlayAdvanceEvent,ScoreOverlay>>();
	private BitmapFont scoreFont;
}
