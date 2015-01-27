package com.handknittedapps.honeycombmatchthree.logic.moves;

import java.util.ArrayList;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.DestructionType;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;

public class DeleteLineMove extends Move
{
	DeleteLineMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src,
			GraphVertex<Block> dest, Map map)
	{
		ArrayList<GraphVertex<Block>> destroyedBlocks = new ArrayList<GraphVertex<Block>>();
		int searchDir = 0;
		int invSearchDir = 0;
		int numDir = map.getFieldType().NumDirections;
		for (int i = 0; i < map.getFieldType().NumDirections; ++i)
		{
			if (src.getNeighbour(i) == dest)
			{
				searchDir = i;
				invSearchDir = (i + numDir / 2) % numDir;
			}
		}

		//Fwd search
		GraphVertex<Block> iterator = src.getNeighbour(searchDir);
		while (iterator != null)
		{
			destroyedBlocks.add(iterator);
			this.affectedBlocks.add(iterator.getData());
			iterator = iterator.getNeighbour(searchDir);
		}

		//Bwd search
		iterator = src.getNeighbour(invSearchDir);
		while (iterator != null)
		{
			destroyedBlocks.add(iterator);
			this.affectedBlocks.add(iterator.getData());
			iterator = iterator.getNeighbour(invSearchDir);
		}
		destroyedBlocks.add(src);
		this.affectedBlocks.add(src.getData());

		return new DestructionSequence(BlockColour.Null, DestructionType.Line, destroyedBlocks);
	}

	@Override
	public boolean canMove(final GraphVertex<Block> src, final GraphVertex<Block> dest, int numDirections)
	{
		boolean isNeighbour = false;
		for (int i = 0; i < src.numNeighbours(); ++i)
		{
			if (src.getNeighbour(i) == dest)
			{
				isNeighbour = true;
				break;
			}
		}

		return (src != dest) && isNeighbour;
	}

	@Override
	public MoveType getType()
	{
		return MoveType.DeleteLine;
	}
}
