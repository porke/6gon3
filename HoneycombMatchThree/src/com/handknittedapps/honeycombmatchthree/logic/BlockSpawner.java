package com.handknittedapps.honeycombmatchthree.logic;

import java.util.AbstractMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.handknittedapps.honeycombmatchthree.utils.Point;

public class BlockSpawner
{
	private static final int Percent = 100;

	public BlockSpawner(int numColors)
	{
		this.numColors = numColors;
		this.rand = new Random();
	}

	public Block spawnBlock(Point position)
	{
		Block block = new Block(position, this.selectBlockColor(), this.selectBlockType(), true);
		return block;
	}

	public void addBlockType(int probability, BlockType blockType)
	{
		int probSum = probability;
		probSum += this.selectMax();
		this.availableBlocks.put(probSum, blockType);
	}

	private BlockType selectBlockType()
	{
		int value = rand.nextInt(BlockSpawner.Percent);
		Set<Integer> probabilities = this.availableBlocks.keySet();
		for (Integer key : probabilities)
		{
			if (value < key)
			{
				return this.availableBlocks.get(key);
			}
		}

		return BlockType.Normal;
	}

	private BlockColour selectBlockColor()
	{
		BlockColour color = BlockColour.values()[this.rand.nextInt(this.numColors)];
		if (color == this.noSpawnColour)
		{
			int finalColourVal = (color.value + this.rand.nextInt(this.numColors - 1)) % this.numColors;
			color = BlockColour.values()[finalColourVal];
		}

		return color;
	}

	private int selectMax()
	{
		int max = 0;

		Set<Integer> keys = this.availableBlocks.keySet();
		int numKeys = keys.size();

		if (numKeys > 0)
		{
			max = keys.toArray(new Integer[0])[numKeys - 1];
		}

		return max;
	}

	public void setNoSpawnColour(BlockColour col) { this.noSpawnColour = col; }

	public BlockColour getNoSpawnColour() {return this.noSpawnColour; }

	private AbstractMap<Integer, BlockType> availableBlocks = new TreeMap<Integer, BlockType>();
	private int numColors;
	private BlockColour noSpawnColour;
	private Random rand;
}
