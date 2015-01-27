package com.handknittedapps.honeycombmatchthree.graphics.events;

public class WaiterEvent extends EventBase
{
	public WaiterEvent(float time, boolean activate)
	{
		super(time, activate);
	}

	@Override
	public void onEnded()
	{
		// Do nothing...
	}
}
