package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.graphics.events.EventBase;

public abstract class BlockEvent extends EventBase
{
	protected BlockEvent(float time, boolean activate, BlockRenderable block)
	{
		super(time, activate);
		this.block = block;
	}

	protected BlockRenderable block;
}
