package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.modes.core.HardcoreMode;

public class HardcoreModeTests 
{
	private HardcoreMode mode;
	
	@Before
	public void initialize()
	{
		this.mode = new HardcoreMode(null, 2, 1, 1);
	}
	
	@Test
	public void hasEndedOnStart()
	{
		Assert.assertFalse(this.mode.hasEnded());
	}
	
	@Test
	public void hasEndedOnQuitTest()
	{
		this.mode.quit();
		Assert.assertTrue(this.mode.hasEnded());
	}
	
	@Test
	public void hasEndedOnMapLockTest()
	{
		this.mode.mapLockLose();
		Assert.assertTrue(this.mode.hasEnded());
	}
}
