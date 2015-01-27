package com.handknittedapps.honeycombmatchthree.data.score;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.handknittedapps.honeycombmatchthree.data.access.ProgressiveScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;

public class ProgressiveScoreControllerTests 
{
	private static final String TestDir = "SixGonThree-tests";
	private static final String TestCasual = GameModeType.Casual.displayedName + "_scores" + ".xml";
	private ProgressiveScoreRepository controller;
	
	@Before
	public void initializeEach()
	{
		Gdx.files = new LwjglFiles();
		this.controller = new ProgressiveScoreRepository(GameModeType.Casual, ProgressiveScoreControllerTests.TestDir);
		
		// create valid setting
		Files files = Gdx.files;
		FileHandle handle = files.local(TestDir + "/" + TestCasual);
		
		// Create casual highscores
		StringBuilder casualScores = new StringBuilder();		
		casualScores.append("<settings>\n");
		for (int i = 0; i < this.controller.getNumHighscores(); ++i)
		{
			String id = String.valueOf(i + 1);
			String value = String.valueOf((110 - (i + 1) * 10));
			casualScores.append("\t<setting name=\"totalBonusesUsed_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"date_" + id + "\"" + " type=\"java.lang.Long\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"totalMoves_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"totalTimeSpent_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"totalScore_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
		}
		casualScores.append("</settings>");
		handle.writeString(casualScores.toString(), false);
	}
	
	@After
	public void tearDown()
	{
		Gdx.files.local(TestDir).deleteDirectory();
	}
	
	@Test
	public void generateDefaultsTest()
	{
		this.controller = new ProgressiveScoreRepository(GameModeType.Casual, "");
		this.controller.loadGameScores();
		for(int  i = 0; i < this.controller.getNumHighscores(); ++i)
		{
			GameStats model = this.controller.getScore(i);
			Assert.assertTrue("The scores should be zero by default", model.isZero());
		}
	}
	
	@Test
	public void nonZeroHighscoreLoadPositiveTest()
	{
		this.controller.loadGameScores();
		for(int  i = 0; i < this.controller.getNumHighscores(); ++i)
		{
			GameStats model = this.controller.getScore(i);
			Assert.assertFalse("The score should not be zero", model.isZero());
		}
	}
	
	@Test
	public void worstScoreOverwriteTest()
	{
		this.controller.loadGameScores();
		GameStats score = new GameStats();
		score.setTotalScore(1000);
		this.controller.onGameFinished(score, 0);
		Assert.assertEquals(score.getTotalScore(), this.controller.getScore(0).getTotalScore());
	}
	
	@Test
	public void resetScorePositiveTest()
	{
		this.controller.loadGameScores();
		this.controller.resetGameScores();
		for(int  i = 0; i < this.controller.getNumHighscores(); ++i)
		{
			GameStats model = this.controller.getScore(i);
			Assert.assertTrue("The score should be zero", model.isZero());
		}
	}
	
	@Test
	public void saveScorePositiveTest()
	{
		// Overwrite one of the scores
		this.controller.loadGameScores();
		GameStats score = new GameStats();
		score.setTotalScore(1000);
		this.controller.onGameFinished(score, 0);
		
		// Save the scores
		this.controller.saveGameScores();
		
		// Reload them and check if the result is the same
		this.controller.loadGameScores();
		Assert.assertEquals(score.getTotalScore(), this.controller.getScore(0).getTotalScore());
	}
}
