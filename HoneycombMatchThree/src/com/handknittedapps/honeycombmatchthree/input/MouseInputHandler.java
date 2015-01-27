package com.handknittedapps.honeycombmatchthree.input;

import com.handknittedapps.honeycombmatchthree.utils.Point;

public interface MouseInputHandler
{
	public void onMouseDown(Point pos, boolean left);
	public void onMouseUp(Point pos, boolean left);
	public void onMouseMove(Point delta);
	public void onDrag(Point src, Point curr);
}
