package com.handknittedapps.honeycombmatchthree.logic;

import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.utils.Point;

import java.util.ArrayList;

public class DestructionSequence
{
	public DestructionSequence(BlockColour col, DestructionType t, ArrayList<GraphVertex<Block>> b)
	{
		this.blockVertices = b;
		this.type = t;
		this.colour = col;
		for (GraphVertex<Block> gv : this.blockVertices)
		{
			this.blocks.add(gv.getData());
		}
	}

	public boolean isGhost()
	{
		boolean isGhost = true;
		for (Block b : this.blocks)
		{
			if (b.getType() != BlockType.Ghost)
			{
				isGhost = false;
				break;
			}
		}
		return isGhost;
	}

	/** List incorrect sequences.
	 * Incorrect sequences:
	 * - sequences consisting mixed non triplets of ghosts and normal blocks
	 * - sequences consisting only strong blocks and ghosts
	 * @return True if valid, false otherwise.
	 */
	public boolean isValid()
	{
		if (!this.isGhost()
				&& this.type == DestructionType.Match)
		{
			// Check if the sequence has a correct colour subsequence
			Point inData = this.getValidSubsequence();
			int subsequentOfColour = inData.x;
			int sameOfColourBegin = inData.y;

			// The sequence is not entirely correct, but it has a correct subsequence
			int subsequentGhosts = 0;
			if (subsequentOfColour >= Block.Sequence
				&& subsequentOfColour != this.blocks.size())
			{
				this.trimSequence(sameOfColourBegin, subsequentOfColour);
			}
			else if (subsequentOfColour < Block.Sequence)
			{
				// Check is the sequence has a correct ghost subsequence
				// in case its length is still lower than minimum
				subsequentGhosts = this.getSubsequentGhostCount();
			}

			return (subsequentOfColour >= Block.Sequence)
					|| (subsequentGhosts >= Block.Sequence);
		}

		return true;
	}

	public void clear()
	{
		for (Block block : this.blocks)
		{
			block.resetSequenceCounter();
		}
	}

	/**
	 * Returns a valid subsequence.
	 * outData.x - number of subsequent blocks of the same color
	 * outData.y - the index of the subsequence start
	 * */
	private Point getValidSubsequence()
	{
		Point outData = new Point();
		BlockColour currColour = this.blocks.get(0).getColour();
		int subsequentOfColour = 1;
		int sameOfColourBegin = 0;
		for (int j = 1; j < this.blocks.size(); ++j)
		{
			Block block = this.blocks.get(j);
			if (block.getColour() != currColour
					&& subsequentOfColour < Block.Sequence)
			{
				sameOfColourBegin = j;
				subsequentOfColour = 1;
				currColour = block.getColour();
			}
			else
			{
				++subsequentOfColour;
			}
		}

		outData.x = subsequentOfColour;
		outData.y = sameOfColourBegin;
		return outData;
	}

	/** Finds the maximum subsequent ghost block count.
	 * */
	private int getSubsequentGhostCount()
	{
		int subsequentGhosts = 0;
		int ghostBegin = -1;
		for (int j = 0; j < this.blocks.size(); ++j)
		{
			Block block = this.blocks.get(j);
			if (block.getType() == BlockType.Ghost)
			{
				if (ghostBegin == -1)
				{
					ghostBegin = j;
				}
				++subsequentGhosts;
			}
			else
			{
				subsequentGhosts = 0;
				ghostBegin = -1;
			}

			//Select the ghosts
			if (subsequentGhosts >= Block.Sequence
					&& subsequentGhosts != this.blocks.size())
			{
				this.trimSequence(ghostBegin, subsequentGhosts);
			}
		}

		return subsequentGhosts;
	}

	private void trimSequence(int beginIdx, int elements)
	{
		final int end = beginIdx + elements;
		ArrayList<GraphVertex<Block>> newVerts = new ArrayList<GraphVertex<Block>>();
		ArrayList<Block> newBlocks = new ArrayList<Block>();
		for (int i = beginIdx; i < end; ++i)
		{
			newVerts.add(this.blockVertices.get(i));
			newBlocks.add(this.blocks.get(i));
		}

		this.blockVertices = newVerts;
		this.blocks = newBlocks;
	}

	public BlockColour getColour() { return this.colour; }
	public DestructionType getType() { return this.type; }
	public Block getBlock(int index) { return this.blocks.get(index); }
	public GraphVertex<Block> getBlockVertex(int index) { return this.blockVertices.get(index); }
	public int size() { return this.blockVertices.size(); }

	private BlockColour colour;
	private DestructionType type;
	private ArrayList<GraphVertex<Block>> blockVertices;
	private ArrayList<Block> blocks = new ArrayList<Block>();
}
