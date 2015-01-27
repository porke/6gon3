package com.handknittedapps.honeycombmatchthree.logic;

public enum BlockColour
{
	Red(0),
	Green(1),
	Blue(2),
	Cyan(3),
	Magenta(4),
	Yellow(5),
	Orange(6),
	Null(-1);

	private BlockColour(int val)
	{
		this.value = val;
	}

	public final int value;
	public static final int NumColours = BlockColour.values().length - 1;
}
