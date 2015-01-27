package com.handknittedapps.honeycombmatchthree.logic.moves;

import java.util.ArrayList;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.DestructionType;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;


public class ClearColourMove extends Move
{
	ClearColourMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest,
			Map map)
	{
		BlockColour bc = src.getData().getColour();
		ArrayList<GraphVertex<Block>> destroyedBlocks = new ArrayList<GraphVertex<Block>>();

		int numVertices = map.getSize() * map.getSize();
		for (int i = 0; i < numVertices; ++i)
		{
			GraphVertex<Block> b = map.getBlockVertex(i);
			if (b.getData().getColour() == bc)
			{
				destroyedBlocks.add(b);
				this.affectedBlocks.add(b.getData());
			}
		}
		return new DestructionSequence(bc, DestructionType.Colour, destroyedBlocks);
	}

	@Override
	public boolean canMove(final GraphVertex<Block> src, final GraphVertex<Block> dest, int numDirections)
	{
		return (src.getData().getType() != BlockType.Immobile);
	}

	@Override
	public MoveType getType()
	{
		return MoveType.ClearColour;
	}
}
