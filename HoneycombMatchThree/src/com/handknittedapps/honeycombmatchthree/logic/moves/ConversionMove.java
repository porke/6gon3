package com.handknittedapps.honeycombmatchthree.logic.moves;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;

public class ConversionMove extends Move
{
	ConversionMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		int numVertices = map.getSize() * map.getSize();
		BlockColour col = src.getData().getColour();
		BlockColour finalColour = dest.getData().getColour();
		for (int i = 0; i < numVertices; ++i)
		{
			Block block = map.getBlockVertex(i).getData();
			if (block.getColour() == col && block.getType() == BlockType.Normal)
			{
				block.setColour(finalColour);
				this.affectedBlocks.add(block);
			}
		}

		return null;
	}

	@Override
	public boolean canMove(final GraphVertex<Block> src, final GraphVertex<Block> dest, int numDirections)
	{
		return (src.getData().getType() == BlockType.Normal
				&& dest.getData().getType() != BlockType.Immobile
				&& src != dest);
	}

	@Override
	public MoveType getType()
	{
		return MoveType.Conversion;
	}
}
