package com.handknittedapps.honeycombmatchthree.logic;

import java.util.Observable;

import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventType;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class Block extends Observable
{
	/** The number of blocks needed to form a sequence. **/
	public static final int Sequence = 3;
	/** The default amount of time a strong block needs to be destroyed. **/
	public static final int DefaultStrongBlockStrength = 3;

	public static FieldType Type;
	private static int GUID = 0;

	public Block(Point gridPosition, BlockColour colour, BlockType type, boolean spawned)
	{
		this.colourIdx = colour;
		this.type = type;
		if (this.type == BlockType.Strong)
		{
			this.type = BlockType.Normal;
			this.strength = Block.DefaultStrongBlockStrength;
		}
		else
		{
			this.strength = 1;
		}

		if (this.type == BlockType.Immobile)
		{
			this.colourIdx = BlockColour.Null;
		}

		this.logicalPosition = gridPosition;
		this.prevPosition = gridPosition;
		this.id = ++Block.GUID;
	}

	/** Block event notification (comes from Map). Note: Created != Spawned **/
	public void onCreated()
	{
		this.setChanged();
		this.notifyObservers(BlockEventType.Create);
	}

	/** Block event notification (comes from Map). Note: Created != Spawned **/
	public void onSpawned(int firstBlockInColumnId)
	{
		this.firstBlockInColumnId = firstBlockInColumnId;

		this.setChanged();
		this.notifyObservers(BlockEventType.Spawn);
	}

	public boolean isNeighbourValid(Block neighbour)
	{
		if (this.type != BlockType.Immobile)
		{
			if (this.type == BlockType.Ghost && neighbour.type == BlockType.Ghost)
			{
				return true;
			}
			else
			{
				return this.colourIdx == neighbour.colourIdx;
			}
		}

		return false;
	}

	public void destroy(DestructionSequence sequence)
	{
		if (this.type != BlockType.Ghost)
		{
			--this.strength;
		}

		this.setChanged();

		// Ghost blocks disappear only if all blocks in the sequence are ghost
		// Or a holocaust bonus has been used
		if (this.type == BlockType.Ghost)
		{
			if (sequence.isGhost()
					|| sequence.getType() == DestructionType.Colour
					|| sequence.getType() == DestructionType.Line)
			{
				this.notifyDestroy(sequence);
			}
			else
			{
				this.notifyObservers(BlockEventType.Link);
			}
		}
		else if (this.strength <= 0)
		{
			this.notifyDestroy(sequence);
		}
		else
		{
			this.notifyObservers(BlockEventType.StrengthChange);
		}
	}

	private void notifyDestroy(DestructionSequence sequence)
	{
		this.destroyedBlocks = sequence;
		--this.numSequences;
		this.notifyObservers(BlockEventType.Destroy);
		this.deleteObservers();
	}

	public void moveTo(Point newPos)
	{
		this.prevPosition = this.logicalPosition;
		this.logicalPosition = newPos;
		this.setChanged();
		this.notifyObservers(BlockEventType.Move);
	}

	public void fallDown(Point bottomPos)
	{
		this.prevPosition = this.logicalPosition;
		this.logicalPosition = bottomPos;
		this.setChanged();
		this.notifyObservers(BlockEventType.Fall);
	}

	public void setColour(BlockColour col)
	{
		this.colourIdx = col;
		this.setChanged();
		this.notifyObservers(BlockEventType.ColourChange);
	}

	public void setType(BlockType newType)
	{
		this.type = newType;
		this.setChanged();
		this.notifyObservers(BlockEventType.TypeChange);
	}

	public void setDetonated(boolean det)
	{
		this.detonated = det;
	}

	public void setFirstBlockInColumnId(int value)
	{
		this.firstBlockInColumnId = value;
	}

	public void clearState()
	{
		this.setChanged();
		this.notifyObservers(BlockEventType.None);
	}

	@Override
	protected void setChanged()
	{
		if (this.performUpdates)
		{
			super.setChanged();
		}
	}

	//Accessors
	public Point getPosition() { return this.logicalPosition; }
	public BlockColour getColour() { return this.colourIdx; }
	public int getId() { return this.id; }
	public BlockType getType() { return this.type; }
	public int getStrength() { return this.strength; }
	public boolean wasDetonated() { return this.detonated; }
	public int getFirstBlockInColumnId() {return this.firstBlockInColumnId;}

	public void incSequenceCounter()
	{
		++this.numSequences;
	}

	public void resetSequenceCounter()
	{
		this.numSequences = 0;
	}

	//sequence detection helpers (axis locking and unlocking)
	public void lockAxis(int direction)
	{
		this.lockedAxes |= (1 << (direction % (Block.Type.NumDirections / 2)));
	}

	public boolean isAxisLocked(int direction)
	{
		return (this.lockedAxes & (1 << (direction % (Block.Type.NumDirections / 2)))) != 0;
	}

	public void unlockAxis(int direction)
	{
		this.lockedAxes ^= (1 << (direction % (Block.Type.NumDirections / 2)));
	}

	public void unlockAxes()
	{
		this.lockedAxes = 0;
	}

	public int getAxisLocked()
	{
		return this.lockedAxes;
	}

	//used by the observer
	public Point getPrevPosition() { return this.prevPosition; }
	public DestructionSequence getDestroyedBlocks() { return this.destroyedBlocks; }
	public int getNumSequences() { return this.numSequences; }
	public void setPerformUpdates(boolean update) {this.performUpdates = update;}

	private int id;
	private int strength;
	private boolean detonated;
	private BlockColour colourIdx;
	private BlockType type;
	private Point logicalPosition;
	private int lockedAxes;			// if the block is in a sequence the axis is being locked
	// to ensure that the same sequence isn't detected multiple times

	// used by the observer
	private boolean performUpdates = true;
	private Point prevPosition;
	private DestructionSequence destroyedBlocks;
	private int numSequences;		// the block can be a part of multiple sequences
	private int firstBlockInColumnId;
}
