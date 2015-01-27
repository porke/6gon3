package com.handknittedapps.honeycombmatchthree.logic.moves;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;

public class ColourChangeMove extends Move
{
	ColourChangeMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		BlockColour end = dest.getData().getColour();
		src.getData().setColour(end);

		// Save the affected blocks
		this.affectedBlocks.add(src.getData());
		this.affectedBlocks.add(dest.getData());
		return null;
	}

	@Override
	public boolean canMove(final GraphVertex<Block> src, final GraphVertex<Block> dest, int numDirections)
	{
		return (src != null
				&& dest != null
				&& src.getData().getType() == BlockType.Normal
				&& dest.getData().getType() != BlockType.Immobile
				&& src != dest);
	}

	@Override
	public MoveType getType()
	{
		return MoveType.ColourChange;
	}

}
