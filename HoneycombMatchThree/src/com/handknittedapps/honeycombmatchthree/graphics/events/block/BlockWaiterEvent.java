package com.handknittedapps.honeycombmatchthree.graphics.events.block;

public class BlockWaiterEvent extends BlockEvent
{
	public BlockWaiterEvent(float time, boolean activate)
	{
		super(time, activate, null);
	}

	@Override
	public void onEnded()
	{
		// Do nothing...
	}
}
