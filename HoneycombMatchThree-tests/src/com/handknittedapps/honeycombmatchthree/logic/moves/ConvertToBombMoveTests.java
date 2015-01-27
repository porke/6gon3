package com.handknittedapps.honeycombmatchthree.logic.moves;

import org.junit.Assert;
import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.logic.moves.ConvertToBombMove;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;

public class ConvertToBombMoveTests
{
	private ConvertToBombMove move = new ConvertToBombMove();

	@Test
	public void typeTest()
	{
		Assert.assertEquals(this.move.getType(), MoveType.BombChange);
	}

	@Test
	public void canMoveNullSrc()
	{
		Assert.assertEquals(this.move.canMove(null, null, 6), false);
	}

	@Test
	public void canMoveBlockNormal()
	{
		Block block = new Block(null, BlockColour.Blue, BlockType.Normal, true);
		GraphVertex<Block> src = new GraphVertex<Block>(block);
		Assert.assertEquals(this.move.canMove(src, null, 6), true);
	}

	@Test
	public void canMoveBlockGhost()
	{
		Block block = new Block(null, BlockColour.Blue, BlockType.Ghost, true);
		GraphVertex<Block> src = new GraphVertex<Block>(block);
		Assert.assertEquals(this.move.canMove(src, null, 6), false);
	}

	@Test
	public void canMoveBlockUnmovable()
	{
		Block block = new Block(null, BlockColour.Blue, BlockType.Immobile, true);
		GraphVertex<Block> src = new GraphVertex<Block>(block);
		Assert.assertEquals(this.move.canMove(src, null, 6), false);
	}

	@Test
	public void canMoveBlockStrong()
	{
		Block block = new Block(null, BlockColour.Blue, BlockType.Strong, true);
		GraphVertex<Block> src = new GraphVertex<Block>(block);
		Assert.assertEquals(this.move.canMove(src, null, 6), false);
	}
}
