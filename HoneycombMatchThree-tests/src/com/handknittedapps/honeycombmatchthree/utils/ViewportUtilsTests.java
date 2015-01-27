package com.handknittedapps.honeycombmatchthree.utils;

import junit.framework.Assert;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.utils.ViewportUtils;

public class ViewportUtilsTests 
{
	@Test
	public void sameDisplaySize_ShouldReturn1AndDisplaySize()
	{
		Vector2 displaySize = new Vector2(480, 840);
		float desiredAspectRatio = HoneycombMatchThree.NominalAspectRatio;
		Vector2 outSize = new Vector2();
		Vector2 outCrop = new Vector2();
		
		Assert.assertEquals(1.0f, ViewportUtils.calculateViewport(displaySize, desiredAspectRatio, outSize, outCrop), 0.001f);
		Assert.assertEquals(displaySize.x, outSize.x, 0.5f);
		Assert.assertEquals(displaySize.y, outSize.y, 0.5f);
		Assert.assertEquals(0, outCrop.x, 0.5f);
		Assert.assertEquals(0, outCrop.y, 0.5f);
	}
	
	@Test
	public void aspectRatioLessThan1_shouldScaleWidth()
	{
		Vector2 displaySize = new Vector2(480, 800);
		float desiredAspectRatio = HoneycombMatchThree.NominalAspectRatio;
		Vector2 outSize = new Vector2();
		Vector2 outCrop = new Vector2();
		
		float expectedScale = 0.952f;
		Vector2 expectedSize = new Vector2(457, 800);
		Vector2 expectedCrop = new Vector2(11.5f, 0);
		Assert.assertEquals(expectedScale, ViewportUtils.calculateViewport(displaySize, desiredAspectRatio, outSize, outCrop), 0.001f);
		Assert.assertEquals(expectedSize.x, outSize.x, 0.5f);
		Assert.assertEquals(expectedSize.y, outSize.y, 0.5f);
		Assert.assertEquals(expectedCrop.x, outCrop.x, 0.5f);
		Assert.assertEquals(expectedCrop.y, outCrop.y, 0.5f);	
	}
	
	@Test
	public void aspectRatioMoreThan1_shouldScaleHeight()
	{
		Vector2 displaySize = new Vector2(400, 840);
		float desiredAspectRatio = HoneycombMatchThree.NominalAspectRatio;
		Vector2 outSize = new Vector2();
		Vector2 outCrop = new Vector2();
		
		float expectedScale = 1.2f;
		Vector2 expectedSize = new Vector2(400, 700);
		Vector2 expectedCrop = new Vector2(0, 70);
		Assert.assertEquals(expectedScale, ViewportUtils.calculateViewport(displaySize, desiredAspectRatio, outSize, outCrop), 0.001f);
		Assert.assertEquals(expectedSize.x, outSize.x, 0.5f);
		Assert.assertEquals(expectedSize.y, outSize.y, 0.5f);
		Assert.assertEquals(expectedCrop.x, outCrop.x, 0.5f);
		Assert.assertEquals(expectedCrop.y, outCrop.y, 0.5f);
	}
	
	@Test
	public void verySmallResolution_shouldScaleWidth()
	{
		Vector2 displaySize = new Vector2(320, 480);
		float desiredAspectRatio = HoneycombMatchThree.NominalAspectRatio;
		Vector2 outSize = new Vector2();
		Vector2 outCrop = new Vector2();
		
		float expectedScale = 0.8571f;
		Vector2 expectedSize = new Vector2(274, 480);
		Vector2 expectedCrop = new Vector2(23, 0);
		Assert.assertEquals(expectedScale, ViewportUtils.calculateViewport(displaySize, desiredAspectRatio, outSize, outCrop), 0.001f);
		Assert.assertEquals(expectedSize.x, outSize.x, 0.5f);
		Assert.assertEquals(expectedSize.y, outSize.y, 0.5f);
		Assert.assertEquals(expectedCrop.x, outCrop.x, 0.5f);
		Assert.assertEquals(expectedCrop.y, outCrop.y, 0.5f);
	}
}
