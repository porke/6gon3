package com.handknittedapps.honeycombmatchthree.data.score;

import java.util.Comparator;
import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.data.model.GameStats;

import junit.framework.Assert;

public class GameScoreModelTests 
{
	@Test
	public void scoreComparerGreaterTest()
	{
		GameStats more = new GameStats();
		more.setTotalScore(100);
		
		GameStats less = new GameStats();
		less.setTotalScore(98);
		
		Comparator<GameStats> comparer =  new GameStats.TotalScoreInverseComparer();
		Assert.assertEquals(-1, comparer.compare(more, less));
	}
	
	@Test
	public void scoreComparerLesserTest()
	{
		GameStats more = new GameStats();
		more.setTotalScore(100);
		
		GameStats less = new GameStats();
		less.setTotalScore(98);
		
		Comparator<GameStats> comparer =  new GameStats.TotalScoreInverseComparer();
		Assert.assertEquals(1, comparer.compare(less, more));
	}
	
	@Test
	public void scoreComparerEqualTest()
	{
		GameStats r = new GameStats();
		r.setTotalScore(100);
		
		GameStats l = new GameStats();
		l.setTotalScore(100);
		
		Comparator<GameStats> comparer =  new GameStats.TotalScoreInverseComparer();
		Assert.assertEquals(0, comparer.compare(l, r));
	}
}
