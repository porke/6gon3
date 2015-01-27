package com.handknittedapps.honeycombmatchthree.utils;

public class Point 
{
	public Point(int px, int py)
	{
		this.x = px;
		this.y = py;
	}
	
	public Point() 
	{
		this.x = this.y = 0;
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
	public int x;
	public int y;
}
