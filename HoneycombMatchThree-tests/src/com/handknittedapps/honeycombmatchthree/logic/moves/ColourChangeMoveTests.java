package com.handknittedapps.honeycombmatchthree.logic.moves;

import junit.framework.Assert;

import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.logic.moves.ColourChangeMove;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class ColourChangeMoveTests 
{
	private ColourChangeMove move = new ColourChangeMove();

	@Test
	public void typeTest()
	{
		Assert.assertEquals(this.move.getType(), MoveType.ColourChange);
	}
	
	@Test
	public void nullBlockCanMoveTest()
	{
		Assert.assertFalse(this.move.canMove(null, null, 1));
	}
	
	@Test
	public void immobileBlockCanMoveTest()
	{
		GraphVertex<Block> normal = new GraphVertex<Block>(
									new Block(
									new Point(0, 0), BlockColour.Red, BlockType.Normal, false));
		GraphVertex<Block> immobile = new GraphVertex<Block>(
									  new Block(
									  new Point(0, 0), BlockColour.Red, BlockType.Immobile, false));
		Assert.assertFalse(this.move.canMove(normal, immobile, 1));
	}
	
	@Test
	public void normalBlockCanMoveTest()
	{
		GraphVertex<Block> normal = new GraphVertex<Block>(
									new Block(
									new Point(0, 0), BlockColour.Red, BlockType.Normal, false));
		GraphVertex<Block> otherNormal = new GraphVertex<Block>(
									  new Block(
									  new Point(0, 0), BlockColour.Red, BlockType.Normal, false));
		Assert.assertTrue(this.move.canMove(normal, otherNormal, 1));
	}
	
	@Test
	public void normalColourChangePositiveTest()
	{
		GraphVertex<Block> normal = new GraphVertex<Block>(
				new Block(
				new Point(0, 0), BlockColour.Blue, BlockType.Normal, false));
		GraphVertex<Block> otherNormal = new GraphVertex<Block>(
				  new Block(
				  new Point(0, 1), BlockColour.Red, BlockType.Normal, false));
		
		this.move.perform(normal, otherNormal, null);
		Assert.assertTrue(normal.getData().getColour().equals(otherNormal.getData().getColour()));
	}
	
}
