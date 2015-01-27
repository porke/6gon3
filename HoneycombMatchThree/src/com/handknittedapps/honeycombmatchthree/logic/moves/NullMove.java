package com.handknittedapps.honeycombmatchthree.logic.moves;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;

public class NullMove extends Move
{
	@Override
	public boolean canMove(GraphVertex<Block> src, GraphVertex<Block> dest, int numDirections)
	{
		return false;
	}

	@Override
	public DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map)
	{
		return null;
	}

	@Override
	public MoveType getType()
	{
		return null;
	}
}
