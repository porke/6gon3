package com.handknittedapps.honeycombmatchthree.logic;

public enum FieldType
{
	HEXAGON(6);

	FieldType(int directions)
	{
		this.NumDirections = directions;
		this.Down = this.NumDirections / 2;
	}

	public static final int Up = 0;
	public final int NumDirections;
	public final int Down;
}
