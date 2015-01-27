package com.handknittedapps.honeycombmatchthree.logic;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.ProgressiveMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public class MapTests 
{
	private Map map;
	
	@Before
	public void initialize()
	{
		this.map = new Map(new ProgressiveMode(GameModeType.Casual, new Player(0)), 4);
	}
	
	@Test
	public void movesPossiblePositiveTest()
	{
		int[] colors = new int[]{1, 3, 4, 5,
								 2, 1, 2, 4, 
								 5, 3, 2, 5,
								 4, 1, 1, 4};
		this.map.createSpecificMap(colors);
		
		boolean movesPossible = this.map.areThereMovesPossible();
		Assert.assertTrue("There should be moves possible for this map!", movesPossible);
	}
	
	@Test
	public void movesPossibleNegativeTest()
	{
		int[] colors = new int[]{1, 2, 3, 4,
								 5, 6, 1, 2, 
								 3, 4, 5, 6,
								 1, 2, 3, 4};
		this.map.createSpecificMap(colors);
		
		boolean movesPossible = this.map.areThereMovesPossible();
		Assert.assertFalse("There should not be any moves possible for this map!", movesPossible);
	}
	
	@Test
	public void validOddRandomMapPositiveTest()
	{
		this.map.createRandomMap();
		
		boolean movesPossible = this.map.areThereMovesPossible();
		Assert.assertTrue("There should be moves possible for this map!", movesPossible);
	}
	
	@Test
	public void validEvenRandomMapPositiveTest()
	{
		this.map.createRandomMap();
		
		boolean movesPossible = this.map.areThereMovesPossible();
		Assert.assertTrue("There should be moves possible for this map!", movesPossible);
	}
	
	@Test
	public void largeMapWithValidSequenceMatchTest()
	{
		this.map = new Map(new ProgressiveMode(GameModeType.Casual, new Player(0)), 6);
		int[] colors = new int[]{5, 1, 6, 2, 4, 6,
								 1, 1, 6, 0, 5, 2, 
								 2, 3, 6, 1, 4, 2,
								 5, 4, 0, 1, 3, 4,
								 2, 4, 6, 5, 3, 0,
								 0, 5, 3, 0, 5, 1};
		this.map.createSpecificMap(colors);
		
		boolean matched = this.map.matchBlocks();
		Assert.assertTrue("There should be blocks matched for this map!", matched);
	}
	
	@Test
	public void twoSubsequentSequencesMatchTest()
	{
		int[] colors = new int[]{1, 2, 3, 4, 
								 2, 5, 3, 6,  
								 0, 0, 3, 0, 
								 0, 0, 2, 4};
		this.map.createSpecificMap(colors);
		
		boolean matched = this.map.matchBlocks();
		Assert.assertTrue("There should be blocks matched for the first time for this map!", matched);
		
		matched = this.map.matchBlocks();
		Assert.assertTrue("There should be blocks matched for the second time for this map!", matched);
	}
}
