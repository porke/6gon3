package com.handknittedapps.honeycombmatchthree.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public final class ViewportUtils
{
	private ViewportUtils()
	{
		// Intentionally left empty
	}

	/** Scales the display size to a custom size retaining the aspect ratio.
	 * @param displaySize The actual display size in pixels.
	 * @param outSize The calculated viewport size in pixels.
	 * @param outViewportPos The coordinates of the start point of the viewport.
	 * @return The scaling factor.
	 * */
	public static float calculateViewport(Vector2 displaySize, float desiredAspectRatio, Vector2 outSize, Vector2 outViewportPos)
	{
		float aspectRatio = displaySize.x / displaySize.y;
		float scaleFactor = HoneycombMatchThree.NominalAspectRatio / aspectRatio;

		outSize.x = displaySize.x;
		outSize.y = displaySize.y;

		if(scaleFactor > 1.0f)
        {
			outSize.y = displaySize.y / scaleFactor;
        }
        else if(scaleFactor < 1.0f)
        {
        	outSize.x = displaySize.x * scaleFactor;
        }

		if (scaleFactor < 1.0f)
		{
			outViewportPos.x = (displaySize.x - outSize.x) / 2;
		}
		else if (scaleFactor > 1.0f)
		{
			outViewportPos.y = (displaySize.y - outSize.y) / 2;
		}

		return scaleFactor;
	}

	public static Vector2 transformToViewport(Rectangle destViewport, Rectangle srcViewport, Vector2 point)
	{
		point.x -= srcViewport.x;
		point.y -= srcViewport.y;
		point.x *= destViewport.width / srcViewport.width;
		point.y *= destViewport.height / srcViewport.height;
		return point;
	}
}
