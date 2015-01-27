package com.handknittedapps.honeycombmatchthree.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventType;
import com.handknittedapps.honeycombmatchthree.logic.graph.Graph;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.logic.graph.HexagonalGraphGenerator;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayMode;
import com.handknittedapps.honeycombmatchthree.logic.moves.Move;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveFactory;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;
import com.handknittedapps.honeycombmatchthree.utils.AtomicReference;
import com.handknittedapps.honeycombmatchthree.utils.Pair;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class Map extends Observable
{
	public static final int BlockMatchCount = 3;
	public static int GhostBlockChance;				// in percent
	public static int ImmobileBlockChance;			// in percent
	public static int BombBlockChance;				// in percent
	public static int StrongBlockChance;			// in percent
	private static int SpecialBlockChance;			// in percent

	public Map(PlayMode playMode, int gridSize)
	{
		for (MoveType moveType : playMode.getUnlockedBonuses())
		{
			this.moves.put(moveType, MoveFactory.createMove(moveType));
		}

		if (!this.moves.containsKey(MoveType.DefaultSwap))
		{
			this.moves.put(MoveType.DefaultSwap, MoveFactory.createMove(MoveType.DefaultSwap));
		}

		Map.SpecialBlockChance = Map.GhostBlockChance
								+	 Map.BombBlockChance
								+ 	 Map.ImmobileBlockChance
								+	 Map.StrongBlockChance;

		final int percent = 100;
		this.blockSpawner = new BlockSpawner(playMode.getNumColors());
		this.blockSpawner.addBlockType(percent - Map.SpecialBlockChance, BlockType.Normal);
		for (BlockType bt : playMode.getUnlockedBlocks())
		{
			switch (bt)
			{
				case Ghost:
					this.blockSpawner.addBlockType(Map.GhostBlockChance, bt);
					break;
				case Bomb:
					this.blockSpawner.addBlockType(Map.BombBlockChance, bt);
					break;
				case Immobile:
					this.blockSpawner.addBlockType(Map.ImmobileBlockChance, bt);
					break;
				case Strong:
					this.blockSpawner.addBlockType(Map.StrongBlockChance, bt);
					break;
				case Normal:
					break;
			}
		}

		this.gridSize = gridSize;
		this.fieldType = FieldType.HEXAGON;
		Block.Type = FieldType.HEXAGON;
	}

	/** Generates a random map graph and fills it with blocks.
	 * @param blockType The number of edges of a map tile (6 for a hex map).
	 * **/
	public void createRandomMap()
	{
		while(!this.areThereMovesPossible())
		{
			this.blocks.getVertices().clear();

			//Create the vertices
			for (int row = 0; row < this.gridSize; ++row)
			{
				for (int col = 0; col < this.gridSize; ++col)
				{
					this.blocks.addVertex(this.blockSpawner.spawnBlock(new Point(col, row)));
				}
			}

			new HexagonalGraphGenerator().generateGraph(this.blocks, this.gridSize);

			while (this.matchBlocks())
			{
				continue;
			}
		}

		this.unlockBlockAxes();
	}

	public void setObservers(Observer mapObserver, Observer blockObserver)
	{
		// Send notification about spawning the blocks
		for (int i = 0; i < this.blocks.numVerts(); ++i)
		{
			Block block = this.blocks.getVertex(i).getData();
			block.addObserver(blockObserver);
			block.onCreated();
		}

		this.blockObserver = blockObserver;
		this.addObserver(mapObserver);
	}

	/** Map graph operation testing only.
	 * @param size The size of the graph (column count and row count).
	 * @param blockColours An array of block color enumeration ordinal numbers for blocks (0, 0) to (size, size)
	 * **/
	public void createSpecificMap(int[] blockColours)
	{
		this.createSpecificMapWithTypes(blockColours, new int[blockColours.length]);
	}

	public void createSpecificMapWithTypes(int[] blockColours, int[] blockTypes)
	{
		for (int row = 0; row < this.gridSize; ++row)
		{
			for (int col = 0; col < this.gridSize; ++col)
			{
				Block b = new Block(new Point(col, row),
								BlockColour.values()[blockColours[row * this.gridSize + col]],
								BlockType.values()[blockTypes[row * this.gridSize + col]],
								false);
				this.blocks.addVertex(b);
			}
		}

		new HexagonalGraphGenerator().generateGraph(this.blocks, this.gridSize);
	}

	private boolean canPerformMove(GraphVertex<Block> srcVert,
									int direction,
									AtomicReference<GraphVertex<Block>> out_destVert)
	{
		// Check the source vertex which might not be valid due to move queuing
		if (srcVert == null)
		{
			return false;
		}

		// Check the destination vertex as it might be out of the map
		// The direction = -1 is reserved for the same block as source
		out_destVert.set(srcVert.getNeighbour(direction));
		if (direction == -1)
		{
			out_destVert.set(srcVert);
		}
		else if (out_destVert.get() == null)
		{
			return false;
		}

		// For the remote swap select the next block in the direction of the swap
		if (this.currentMoveMode == MoveType.RemoteSwap)
		{
			out_destVert.set(out_destVert.get().getNeighbour(direction));
		}

		Move currMove = this.moves.get(this.currentMoveMode);
		if (!currMove.canMove(srcVert, out_destVert.get(), this.fieldType.NumDirections))
		{
			return false;
		}

		return true;
	}

	private boolean isSwapValid(GraphVertex<Block> srcVert, GraphVertex<Block> destVert)
	{
		MoveDestructionSequences check = new MoveDestructionSequences(DestructionType.Match);
		this.find3At(srcVert, check);
		this.find3At(destVert, check);
		srcVert.getData().unlockAxes();
		destVert.getData().unlockAxes();

		return check.numSequences() > 0;
	}

	@SuppressWarnings("unchecked")
	public boolean performMove(int srcId, int direction)
	{
		GraphVertex<Block> srcVert = this.findBlockById(srcId);
		GraphVertex<Block> destVert = null;

		Move currMove = this.moves.get(this.currentMoveMode);
		AtomicReference<GraphVertex<Block>> out_destVert = new AtomicReference<GraphVertex<Block>>();
		if (!this.canPerformMove(srcVert, direction, out_destVert))
		{
			return false;
		}

		destVert = out_destVert.get();
		DestructionSequence destroyedBlocks = currMove.perform(srcVert, destVert, this);

		// Call an post-move, pre-destroy event
		ArrayList<Block> affectedBlocks = (ArrayList<Block>) currMove.getAffectedBlocks().clone();
		Pair<ArrayList<Block>, BlockEventType> eventArgs = new Pair<ArrayList<Block>, BlockEventType>(affectedBlocks, BlockEventType.Move);
		this.setChanged();
		this.notifyObservers(eventArgs);
		currMove.clearAffectedBlocks();

		if (this.currentMoveMode == MoveType.DefaultSwap
			&& this.blockCheckEnabled
			&& !this.isSwapValid(srcVert, destVert))
		{
			currMove.perform(srcVert, destVert, this);

			// Clear the move if it does not match any blocks
			this.currentMoveMode = MoveType.DefaultSwap;
			return false;
		}

		if (destroyedBlocks != null)
		{
			DestructionType destrType = DestructionType.Line;
			if (this.currentMoveMode == MoveType.ClearColour)
			{
				destrType = DestructionType.Colour;
			}

			MoveDestructionSequences destructibleBlocks = new MoveDestructionSequences(destrType);
			destructibleBlocks.add(destroyedBlocks);

			// Destroy the blocks and notify the renderer
			this.destroyBlocks(destructibleBlocks);
			this.setChanged();

			affectedBlocks.addAll(currMove.getAffectedBlocks());
			eventArgs.setFirst(affectedBlocks);
			eventArgs.setSecond(BlockEventType.Destroy);
			this.notifyObservers(eventArgs);

			// Fall the old blocks and drop some new ones
			this.fallBlocks();
			this.dropNewBlocks();
		}

		// Match the blocks anyway as certain bonuses might have
		// changed the map data in a way that the sequence is possible
		this.matchBlocks();

		// For the Fear bonus
		this.updateNoSpawnTime();

		// Reset the move mode back to swap move
		this.currentMoveMode = MoveType.DefaultSwap;

		// Check if there are any moves available
		this.validMovePossible = this.areThereMovesPossible();

		// Unlock block axes so as to prevent errors in finding threesomes
		this.unlockBlockAxes();

		// Send the after moved event
		this.setChanged();
		this.notifyObservers();

		return true;
	}

	private void updateNoSpawnTime()
	{
		if (this.blockSpawner.getNoSpawnColour() != null)
		{
			--this.movesLeftToRemoveNoSpawn;
			if (this.movesLeftToRemoveNoSpawn == 0)
			{
				this.blockSpawner.setNoSpawnColour(null);
			}
		}
	}

	private void unlockBlockAxes()
	{
		for (int i = 0; i < this.blocks.numVerts(); ++i)
		{
			this.blocks.getVertex(i).getData().unlockAxes();
		}
	}

	public void setMoveType(MoveType move)
	{
		this.currentMoveMode = move;
	}

	public boolean areThereMovesPossible()
	{
		Move swapMove = this.moves.get(MoveType.DefaultSwap);
		for (int i = 0; i < this.blocks.numVerts(); ++i)
		{
			GraphVertex<Block> vertex = this.blocks.getVertex(i);
			for (int j = 0; j < this.fieldType.NumDirections; ++j)
			{
				GraphVertex<Block> neighbour = vertex.getNeighbour(j);
				if (neighbour != null)
				{
					MoveDestructionSequences mds = new MoveDestructionSequences(DestructionType.Match);

					// perform the swap move
					vertex.getData().setPerformUpdates(false);
					neighbour.getData().setPerformUpdates(false);
					swapMove.perform(vertex, neighbour, this);
					this.find3At(vertex, mds);

					mds.validateSequences();
					vertex.getData().unlockAxes();
					neighbour.getData().unlockAxes();

					// restore the map state
					swapMove.perform(vertex, neighbour, this);
					vertex.getData().setPerformUpdates(true);
					neighbour.getData().setPerformUpdates(true);

					if (mds.numSequences() > 0)
					{
						mds.clear();
						return true;
					}
				}
			}
		}

		return false;
	}

	/** Matches threesomes of blocks.
	 * @return True if any sequences have been found, false otherwise.
	 *  **/
	public boolean matchBlocks()
	{
		MoveDestructionSequences destructibleBlocks = new MoveDestructionSequences(DestructionType.Match);

		destructibleBlocks.clear();
		for (int i = 0; i < this.blocks.numVerts(); ++i)
		{
			this.find3At(this.blocks.getVertex(i), destructibleBlocks);
		}

		if (destructibleBlocks.numSequences() != 0)
		{
			this.destroyBlocks(destructibleBlocks);

			// The block sequences might get removed due to validation
			if (destructibleBlocks.numSequences() > 0)
			{
				this.fallBlocks();
				this.dropNewBlocks();

				++this.sequenceCombo;
				return true;
			}
		}

		this.sequenceCombo = 0;

		return false;
	}

	private void detonateBlocks(MoveDestructionSequences destructibleBlocks)
	{
		for (int i = 0; i < destructibleBlocks.numSequences(); ++i)
		{
			DestructionSequence sequence = destructibleBlocks.getDestructionSequence(i);
			for (int j = 0; j < sequence.size(); ++j)
			{
				if (sequence.getBlock(j).getType() == BlockType.Bomb
						&& !sequence.getBlock(j).wasDetonated())
				{
					// Detonate
					GraphVertex<Block> explosionCenter = sequence.getBlockVertex(j);
					explosionCenter.getData().setDetonated(true);
					ArrayList<GraphVertex<Block>> explodedBlocks = new ArrayList<GraphVertex<Block>>();
					for (int k = 0; k < this.fieldType.NumDirections; ++k)
					{
						GraphVertex<Block> expBlockVertex = explosionCenter.getNeighbour(k);
						if (expBlockVertex != null)
						{
							Block expBlock = expBlockVertex.getData();
							expBlock.incSequenceCounter();
							explodedBlocks.add(expBlockVertex);
						}
					}

					destructibleBlocks.add(new DestructionSequence(BlockColour.Null, DestructionType.Bomb,explodedBlocks));
				}
			}
		}
	}

	/** Invokes the destroy event in each block. */
	private void notifyBlockRenderableDestroy(MoveDestructionSequences destructibleBlocks)
	{
		// Two loops because the block being destroyed
		// needs the access to all blocks within the sequence
		// So the first loop notifies the blocks about the destruction event
		final int numBlockArrays = destructibleBlocks.numSequences();
		for (int i = 0; i < numBlockArrays; ++i)
		{
			DestructionSequence sequence = destructibleBlocks.getDestructionSequence(i);
			final int numBlocks = sequence.size();
			for (int j = 0; j < numBlocks; ++j)
			{
				Block currBlock = sequence.getBlock(j);
				currBlock.destroy(sequence);
			}
		}
	}

	private void destroyBlocks(MoveDestructionSequences destructibleBlocks)
	{
		// Perform detonations
		this.detonateBlocks(destructibleBlocks);

		// Validate sequences
		destructibleBlocks.validateSequences();

		// Return if there are no sequences to destroy
		if (destructibleBlocks.numSequences() == 0)
		{
			return;
		}

		// Notify the observer (gamestate) about the destruction
		this.setChanged();
		this.notifyObservers(destructibleBlocks);

		this.notifyBlockRenderableDestroy(destructibleBlocks);

		//Then the second one deletes them from the map graph
		final int numBlockArrays = destructibleBlocks.numSequences();
		for (int i = 0; i < numBlockArrays; ++i)
		{
			DestructionSequence sequence = destructibleBlocks.getDestructionSequence(i);
			final int numBlocks = sequence.size();

			//Check if the sequence is 'ghost'
			boolean ghostSequence = destructibleBlocks.isGhost(i);
			for (int j = 0; j < numBlocks; ++j)
			{
				// The block is not getting deleted if it's a ghost
				// unless the sequence is composed entirely of ghost blocks
				GraphVertex<Block> blockVertex = sequence.getBlockVertex(j);
				if (blockVertex.getData() != null)
				{
					// Check if the block strength is 0 or negative
					// so that strong blocks are left until their strength reaches 0
					// Or a bonus has been used (holocaust)
					if (blockVertex.getData().getType() == BlockType.Ghost)
					{
						if (sequence.getType() == DestructionType.Colour
							|| sequence.getType() == DestructionType.Line
							|| ghostSequence)
						{
							blockVertex.setData(null);
						}
					}
					else if (blockVertex.getData().getStrength() <= 0)
					{
						blockVertex.setData(null);
					}
				}
			}
		}
	}

	/** Searches for any threesomes in the map graph.
	 * @param curr The block from which to begin searching.
	 * @param sequences An output structure for those blocks which are matched.
	 * **/
	private void find3At(GraphVertex<Block> curr, MoveDestructionSequences sequences)
	{
		ArrayList<GraphVertex<Block>> matchedBlocks = new ArrayList<GraphVertex<Block>>();
		for (int dir = 0; dir < this.fieldType.NumDirections / 2; ++dir)
		{
			if (curr.getData().isAxisLocked(dir))
			{
				continue;
			}

			matchedBlocks.add(curr);
			curr.getData().lockAxis(dir);

			//Search forward
			GraphVertex<Block> neighbour = curr.getNeighbour(dir);
			while (    neighbour != null
					&& curr.getData().isNeighbourValid(neighbour.getData()))
			{
				matchedBlocks.add(neighbour);
				neighbour.getData().lockAxis(dir);
				neighbour = neighbour.getNeighbour(dir);
			}

			//Search backward
			int invDir = (dir + this.fieldType.NumDirections / 2) % this.fieldType.NumDirections;
			neighbour = curr.getNeighbour(invDir);
			while (    neighbour != null
					&& curr.getData().isNeighbourValid(neighbour.getData()))
			{
				matchedBlocks.add(0, neighbour);
				neighbour.getData().lockAxis(invDir);
				neighbour = neighbour.getNeighbour(invDir);
			}

			//if there are less than 3 blocks, clear the axis data
			if (matchedBlocks.size() < Map.BlockMatchCount)
			{
				for (int i = 0; i < matchedBlocks.size(); ++i)
				{
					matchedBlocks.get(i).getData().unlockAxis(dir);
				}
			}
			// otherwise add the sequence to the list
			else
			{
				ArrayList<GraphVertex<Block>> copy = new ArrayList<GraphVertex<Block>>();
				for (int i = 0; i < matchedBlocks.size(); ++i)
				{
					matchedBlocks.get(i).getData().incSequenceCounter();
					copy.add(matchedBlocks.get(i));
				}

				sequences.add(new DestructionSequence(copy.get(0).getData().getColour(), DestructionType.Match, copy));
			}

			matchedBlocks.clear();
		}
	}

	/** Fills the empty spaces atop any columns by creating new blocks. **/
	private void dropNewBlocks()
	{
		// get the bottom top vertex
		GraphVertex<Block> vertexIterator = this.blocks.getVertex(this.gridSize * (this.gridSize - 1));
		for (int col = 0; col < this.gridSize; ++col)
		{
			//Search downwards from the top
			int row = this.gridSize - 1;
			GraphVertex<Block> down = vertexIterator.getNeighbour(this.fieldType.Down);

			// If the current node is empty and it's not the bottom, go down
			while (vertexIterator.getData() == null && down != null)
			{
				vertexIterator = down;
				down = down.getNeighbour(this.fieldType.Down);
				--row;
			}

			//Revert one block up
			if (row != this.gridSize - 1 && row != 0)
			{
				vertexIterator = vertexIterator.getNeighbour(FieldType.Up);
			}

			//Drop the proper number of blocks
			int numBlocks = this.gridSize - row - 1;

			// When the iterator reaches the last row it has to check whether it is empty or not
			if (row == 0)
			{
				// If empty, add another block
				if (vertexIterator.getData() == null)
				{
					++numBlocks;
				}
				// If not empty, start filling from the upper neighbour
				else
				{
					vertexIterator = vertexIterator.getNeighbour(FieldType.Up);
				}
			}

			for (int i = 0; i < numBlocks; ++i)
			{
				Block block = this.spawnBlock(col, i);
				vertexIterator.setData(block);
				block.fallDown(new Point(col, this.gridSize - numBlocks + i));

				GraphVertex<Block> up = vertexIterator.getNeighbour(FieldType.Up);
				vertexIterator = (up != null) ? up : vertexIterator;
			}

			//Return to the top (and one right)
			if (col < this.gridSize - 1)
			{
				//And up to the top
				vertexIterator = vertexIterator.getNeighbour(1 + (col % 2));

				GraphVertex<Block> up = vertexIterator.getNeighbour(FieldType.Up);
				while (up != null)
				{
					vertexIterator = up;
					down = down.getNeighbour(FieldType.Up);
				}
			}
		}
	}

	/** Vertically moves blocks which have empty space beneath them to fill the gaps. **/
	private void fallBlocks()
	{
		// get the bottom left vertex
		GraphVertex<Block> vertexIterator = this.blocks.getVertex(0);
		for (int col = 0; col < this.gridSize; ++col)
		{
			int row = 0;
			int gapSize = 0;
			GraphVertex<Block> upBlock = vertexIterator.getNeighbour(FieldType.Up);
			while (row < this.gridSize)
			{
				//After finishing gap processing move to the bottom and search for another gap
				//as it might be possible when using certain bonuses (conversion/holocaust)
				row = 0;

				//HACK: The vertices are arranged in such a manner that vertices from 0 to width
				//are the first vertices of subsequent columns counting from the "bottom"
				vertexIterator = this.blocks.getVertex(col);

				//The vertex is empty if the block is equal to null
				while (row < this.gridSize && vertexIterator.getData() != null)
				{
					//Bloki nie spadaj¹, idŸ dalej w górê
					GraphVertex<Block> up = vertexIterator.getNeighbour(FieldType.Up);
					vertexIterator = (up != null) ? up : vertexIterator;
					++row;
				}

				//We have a gap
				if (row < this.gridSize)
				{
					// Wyznaczona jest luka, wyznaczamy jej rozmiar
					gapSize = 0;
					upBlock = vertexIterator;
					while (upBlock != null && upBlock.getData() == null)
					{
						upBlock = upBlock.getNeighbour(FieldType.Up);
						++gapSize;
					}

					//Znajac wielkoœæ luki, opuœæ klocki
					final int initialGapSize = gapSize;
					while (gapSize > 0 && row < this.gridSize)
					{
						//The up block might be the above the the top already
						if (upBlock != null)
						{
							//Move the block from up to the bottom of the gap
							vertexIterator.setData(upBlock.getData());

							//Clear and move the one-block-above-the-gap as well
							upBlock.setData(null);
							upBlock = upBlock.getNeighbour(FieldType.Up);
						}
						else
						{
							vertexIterator.setData(null);
						}

						if (vertexIterator.getData() != null)
						{
							//Change the logical position of the block
							vertexIterator.getData().fallDown(new Point(col, vertexIterator.getData().getPosition().y - initialGapSize));
						}

						//Move the current block up
						GraphVertex<Block> up = vertexIterator.getNeighbour(FieldType.Up);
						vertexIterator = (up != null) ? up : vertexIterator;

						++row;
						--gapSize;
					}
				}
			}

			//Move left
			if (col < this.gridSize - 1)
			{
				vertexIterator = vertexIterator.getNeighbour(1 + (col % 2));

				//And down
				GraphVertex<Block> down = vertexIterator.getNeighbour(this.fieldType.Down);
				while (down != null)
				{
					vertexIterator = down;
					down = down.getNeighbour(this.fieldType.Down);
				}
			}
		}
	}

	/** Spawns a block on the top of a specified column.
	 * @param column The column in which the block will be spawned.
	 * @return The created block.
	 * **/
	private Block spawnBlock(int column, int spacesAbove)
	{
		Block ret = this.blockSpawner.spawnBlock(new Point(column, this.gridSize + spacesAbove));
		if (this.countObservers() > 0)
		{
			ret.addObserver(this.blockObserver);
			ret.onSpawned(this.findFirstBlockInColumn(column));
		}

		return ret;
	}

	private int findFirstBlockInColumn(int column)
	{
		GraphVertex<Block> foundVertex = null;
		for (int i = 0; i < this.blocks.numVerts(); ++i)
		{
			GraphVertex<Block> vertex = this.blocks.getVertex(i);
			if (vertex.getData() != null
				&& vertex.getData().getPosition().x == column)
			{
				foundVertex = vertex;
				break;
			}
		}

		if (foundVertex == null)
		{
			return -1;
		}
		else
		{
			GraphVertex<Block> upVertex = foundVertex.getNeighbour(FieldType.Up);
			while (upVertex != null
					&& upVertex.getData() != null)
			{
				foundVertex = upVertex;
				upVertex = foundVertex.getNeighbour(FieldType.Up);
			}

			return foundVertex.getData().getId();
		}
	}

	public GraphVertex<Block> findBlockById(int id)
	{
		for (int i = 0; i < this.blocks.numVerts(); ++i)
		{
			GraphVertex<Block> b = this.blocks.getVertex(i);
			if (b.getData().getId() == id)
			{
				return b;
			}
		}

		return null;
	}

	public String toString()
	{
		StringBuilder string = new StringBuilder();
		for (int row = 0; row < this.gridSize; ++row)
		{
			for (int col = 0; col < this.gridSize; ++col)
			{
				if (this.blocks.getVertex(row * this.gridSize + col).getData() != null)
				{
					GraphVertex<Block> vertex = this.blocks.getVertex(row * this.gridSize + col);
					string.append(vertex.getData().getAxisLocked() + " ");
				}
				else
				{
					string.append("  ");
				}
			}

			string.append("\n");
		}

		string.append("\n");

		return string.toString();
	}

	// Do not use this method to find a block based on its id!
	public GraphVertex<Block> getBlockVertex(int vertexIndex) { return this.blocks.getVertex(vertexIndex); }
	public void setNoSpawnColour(BlockColour colour, int durationInTurns)
	{
		this.blockSpawner.setNoSpawnColour(colour);
		this.movesLeftToRemoveNoSpawn = durationInTurns;
	}

	public FieldType getFieldType() { return this.fieldType; }
	public int getSequenceCombo() { return this.sequenceCombo; }
	public MoveType getCurrentMoveMode() { return this.currentMoveMode; }
	public int getSize() {return this.gridSize;}
	public boolean isValidMovePossible() {return this.validMovePossible;}

	private Graph<Block> blocks = new Graph<Block>();
	private boolean validMovePossible = true;
	private int gridSize;
	private FieldType fieldType;
	private int movesLeftToRemoveNoSpawn;
	private MoveType currentMoveMode = MoveType.DefaultSwap;
	private BlockSpawner blockSpawner;
	private HashMap<MoveType, Move> moves = new HashMap<MoveType, Move>();
	private int sequenceCombo = 0;

	private Observer blockObserver;

	/** If the value is false and there is no sequence, the move is not reverted. Debug purposes. */
	private boolean blockCheckEnabled = true;
}
