package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;

public class BlockEventInsertParallel extends BlockEvent
{
	public BlockEventInsertParallel(float time, boolean activate, BlockRenderable block, BlockEvent insertedEvent)
	{
		super(time, activate, block);
		this.insertEvent = insertedEvent;
	}

	@Override
	public void onEnded()
	{
		this.block.insertEventToParallelQueue(this.insertEvent);
	}

	private BlockEvent insertEvent;
}
