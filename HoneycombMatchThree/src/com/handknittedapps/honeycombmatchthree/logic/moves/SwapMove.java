package com.handknittedapps.honeycombmatchthree.logic.moves;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.utils.Point;


//This is the simple swap move, it swaps two adjacent blocks
public class SwapMove extends Move
{
	SwapMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		// Swap the position
		Point tmpPos = src.getData().getPosition();
		Block srcBlock = src.getData();
		Block destBlock = dest.getData();
		srcBlock.moveTo(destBlock.getPosition());
		destBlock.moveTo(tmpPos);

		// Swap the data
		Block tmp = srcBlock;
		src.setData(destBlock);
		dest.setData(tmp);

		// Save the affected blocks
		this.affectedBlocks.add(srcBlock);
		this.affectedBlocks.add(destBlock);
		return null;
	}

	@Override
	public boolean canMove(final GraphVertex<Block> src,
			final GraphVertex<Block> dest,
			final int numDirections)
	{
		boolean isNeighbour = false;
		for (int i = 0; i < numDirections; ++i)
		{
			if (src.getNeighbour(i) == dest)
			{
				isNeighbour = true;
				break;
			}
		}
		return isNeighbour
				&& src.getData().getType() != BlockType.Immobile
				&& dest.getData().getType() != BlockType.Immobile;
	}

	@Override
	public MoveType getType()
	{
		return null;
	}
}
