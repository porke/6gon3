package com.handknittedapps.honeycombmatchthree.input;

import com.handknittedapps.honeycombmatchthree.utils.Point;

public class NullMouseInputProcessor implements MouseInputHandler
{
	@Override
	public void onMouseDown(Point pos, boolean left)
	{
		// Intentionally left empty
	}

	@Override
	public void onMouseUp(Point pos, boolean left)
	{
		// Intentionally left empty
	}

	@Override
	public void onMouseMove(Point delta)
	{
		// Intentionally left empty
	}

	@Override
	public void onDrag(Point src, Point curr)
	{
		// Intentionally left empty
	}
}
