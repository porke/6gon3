package com.handknittedapps.honeycombmatchthree.logic.moves;


import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class RemoteSwap extends Move
{
	RemoteSwap()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		// Swap the position
		Point tmpPos = src.getData().getPosition();
		src.getData().moveTo(dest.getData().getPosition());
		dest.getData().moveTo(tmpPos);

		// Swap the data
		Block tmp = src.getData();
		src.setData(dest.getData());
		dest.setData(tmp);

		// Save the affected blocks
		this.affectedBlocks.add(src.getData());
		this.affectedBlocks.add(dest.getData());
		return null;
	}

	@Override
	public boolean canMove(GraphVertex<Block> src, GraphVertex<Block> dest, int numDirections)
	{
		boolean isNeighbour = false;
		for (int i = 0; i < numDirections; ++i)
		{
			if (src.getNeighbour(i) != null)
			{
				if (src.getNeighbour(i) == dest || src.getNeighbour(i).getNeighbour(i) == dest)
				{
					isNeighbour = true;
					break;
				}
			}
		}
		return isNeighbour
				&& dest != null
				&& src.getData().getType() != BlockType.Immobile
				&& dest.getData().getType() != BlockType.Immobile;
	}

	@Override
	public MoveType getType()
	{
		return MoveType.RemoteSwap;
	}
}
