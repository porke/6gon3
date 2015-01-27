package com.handknittedapps.honeycombmatchthree.logic.moves;

import java.util.ArrayList;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;

public abstract class Move
{
	public abstract boolean canMove(GraphVertex<Block> src, GraphVertex<Block> dest, int numDirections);
	public abstract DestructionSequence perform(GraphVertex<Block> src, GraphVertex<Block> dest, Map map);
	public ArrayList<Block> getAffectedBlocks() { return this.affectedBlocks; };
	public void clearAffectedBlocks() { this.affectedBlocks.clear(); };

	protected ArrayList<Block> affectedBlocks = new ArrayList<Block>();
	public abstract MoveType getType();
}
