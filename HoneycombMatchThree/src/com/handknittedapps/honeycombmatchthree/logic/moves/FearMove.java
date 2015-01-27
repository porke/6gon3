package com.handknittedapps.honeycombmatchthree.logic.moves;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;

public class FearMove extends Move
{
	FearMove()
	{
		// Disable instantiation
	}

	public static int DurationInTurns;

	@Override
	public boolean canMove(GraphVertex<Block> src, GraphVertex<Block> dest, int numDirections)
	{
		return (src.getData().getType() != BlockType.Immobile);
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		this.affectedBlocks.add(src.getData());
		map.setNoSpawnColour(src.getData().getColour(), FearMove.DurationInTurns);
		return null;
	}

	@Override
	public MoveType getType()
	{
		return MoveType.Fear;
	}
}
