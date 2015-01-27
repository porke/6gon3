package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.handknittedapps.honeycombmatchthree.graphics.BlockObserverState;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;

public class BlockEventChangeState extends BlockEvent
{
	public BlockEventChangeState(float time, BlockRenderable block, BlockObserverState destState)
	{
		super(time, false, block);
		this.state = destState;
	}

	@Override
	public void onEnded()
	{
		this.block.setObserverState(this.state);
	}

	private BlockObserverState state;
}
