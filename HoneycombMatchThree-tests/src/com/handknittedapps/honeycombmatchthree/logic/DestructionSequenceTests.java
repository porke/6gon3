package com.handknittedapps.honeycombmatchthree.logic;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.DestructionType;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class DestructionSequenceTests 
{
	private DestructionSequence sequence;
	
	@Test
	public void threeNormalBlocksMatchValidTest()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Red;
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
	}
	
	@Test
	public void threeNormalBlocksWithGhostMatchValidTest()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Red;
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Ghost));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
	}
	
	@Test
	public void threeNormalBlocksWithGhostMatchInvalidTest()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Red;
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(BlockColour.Blue, BlockType.Ghost));
		blocks.add(this.createBlock(color, BlockType.Normal));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		
		Assert.assertFalse("The sequence should be invalid.", this.sequence.isValid());
	}
	
	@Test
	public void fiveNormalWithSubsequenceValidTest()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Orange;
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(BlockColour.Red, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Normal));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
	}
	
	@Test
	public void ghostGhostNormalGhostValidTest()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Orange;
		blocks.add(this.createBlock(BlockColour.Red, BlockType.Ghost));
		blocks.add(this.createBlock(color, BlockType.Ghost));
		blocks.add(this.createBlock(color, BlockType.Normal));
		blocks.add(this.createBlock(color, BlockType.Ghost));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
	}
	
	@Test
	public void ghostGhostNormalInvalid()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Orange;
		blocks.add(this.createBlock(color, BlockType.Ghost));
		blocks.add(this.createBlock(BlockColour.Blue, BlockType.Ghost));
		blocks.add(this.createBlock(color, BlockType.Normal));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		
		Assert.assertFalse("The sequence should not be valid.", this.sequence.isValid());
	}
	
	@Test
	public void ghostGhostStrongValidThreeTimes()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour color = BlockColour.Orange;
		blocks.add(this.createBlock(color, BlockType.Ghost));
		blocks.add(this.createBlock(color, BlockType.Ghost));
		blocks.add(this.createBlock(color, BlockType.Strong));
		this.sequence = new DestructionSequence(color, DestructionType.Match, blocks);
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
		
		blocks.get(2).getData().destroy(this.sequence);
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
		
		blocks.get(2).getData().destroy(this.sequence);
		Assert.assertTrue("The sequence should be valid.", this.sequence.isValid());
	}
	
	@Test
	public void normalNormalGhostNormalValid()
	{
		ArrayList<GraphVertex<Block>> blocks = new ArrayList<GraphVertex<Block>>();
		BlockColour colour = BlockColour.Orange;
		blocks.add(this.createBlock(colour, BlockType.Normal));
		blocks.add(this.createBlock(colour, BlockType.Normal));
		blocks.add(this.createBlock(colour, BlockType.Ghost));
		blocks.add(this.createBlock(colour, BlockType.Normal));
		
		this.sequence = new DestructionSequence(colour, DestructionType.Match, blocks);
		Assert.assertTrue("This sequence should be valid!", this.sequence.isValid());
		
		Assert.assertEquals(blocks.size(), this.sequence.size());
	}
	
	private GraphVertex<Block> createBlock(BlockColour colour, BlockType type)
	{
		Block block = new Block(new Point(0, 0), colour, type, false);
		return new GraphVertex<Block>(block);
	}
}
