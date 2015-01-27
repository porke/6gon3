package com.handknittedapps.honeycombmatchthree.logic.moves;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;


public class ConvertToBombMove extends Move
{
	ConvertToBombMove()
	{
		// Disable instantiation
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src,
			GraphVertex<Block> dest, Map map)
	{
		src.getData().setType(BlockType.Bomb);
		this.affectedBlocks.add(src.getData());
		return null;
	}

	@Override
	public boolean canMove(GraphVertex<Block> src, GraphVertex<Block> dest, int numDirections)
	{
		return (src != null
				&& src.getData().getType() == BlockType.Normal
				&& src.getData().getStrength() == 1);
	}

	@Override
	public MoveType getType()
	{
		return MoveType.BombChange;
	}
}
