package com.handknittedapps.honeycombmatchthree.logic.modes.core;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.modes.core.TimeAttackMode;

public class TimeAttackModeTests 
{
	private TimeAttackMode mode;
	
	@Before
	public void initialize()
	{
		this.mode = new TimeAttackMode(null, 1, 1);
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
