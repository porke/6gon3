package com.handknittedapps.honeycombmatchthree.logic.moves;


import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class RotateMove extends Move
{
	RotateMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		boolean rotateRight = true;
		int numDir = map.getFieldType().NumDirections;

		for (int i = 0; i < numDir; ++i)
		{
			if (src.getNeighbour(i) == dest)
			{
				rotateRight = (i <= numDir / 2);
				break;
			}
		}

		//store the blocks and clear the graph
		Block[] blocks = new Block[numDir];
		for (int i = 0; i < numDir; ++i)
		{
			blocks[i] = src.getNeighbour(i).getData();
			this.affectedBlocks.add(blocks[i]);
			src.getNeighbour(i).setData(null);
		}

		//Update the block position
		if (rotateRight)
		{
			Point firstPos = new Point(blocks[0].getPosition().x, blocks[0].getPosition().y);
			for (int i = 0; i < numDir - 1; ++i)
			{
				blocks[i].moveTo(blocks[i + 1].getPosition());
			}
			blocks[map.getFieldType().NumDirections - 1].moveTo(firstPos);

			//Place the blocks in the graph
			for (int i = 0; i < map.getFieldType().NumDirections; ++i)
			{
				src.getNeighbour(i).setData(blocks[(i + numDir - 1) % numDir]);
			}
		}
		//Rotate left
		else
		{
			Point lastPos = new Point(blocks[numDir - 1].getPosition().x, blocks[numDir - 1].getPosition().y);
			for (int i = numDir - 1; i > 0; --i)
			{
				blocks[i].moveTo(blocks[i - 1].getPosition());
			}
			blocks[0].moveTo(lastPos);

			//Place the blocks in the graph
			for (int i = 0; i < numDir; ++i)
			{
				src.getNeighbour(i).setData(blocks[(i + 1) % numDir]);
			}
		}

		return null;
	}

	@Override
	public boolean canMove(final GraphVertex<Block> src, final GraphVertex<Block> dest, int numDirections)
	{
		if (src.numNeighbours() != numDirections)
		{
			return false;
		}

		boolean isNeighbour = false;
		boolean hasImmobileBlocks = false;
		for (int i = 0; i < numDirections; ++i)
		{
			if (src.getNeighbour(i) == dest)
			{
				isNeighbour = true;
			}

			if (src.getNeighbour(i).getData().getType() == BlockType.Immobile)
			{
				hasImmobileBlocks = true;
			}
		}

		return (isNeighbour && !hasImmobileBlocks);
	}

	@Override
	public MoveType getType()
	{
		return MoveType.RotateSwap;
	}
}
