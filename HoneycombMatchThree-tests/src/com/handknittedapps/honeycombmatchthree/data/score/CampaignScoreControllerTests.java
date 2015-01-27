package com.handknittedapps.honeycombmatchthree.data.score;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignProgressRepository;
import com.handknittedapps.honeycombmatchthree.data.access.CampaignScoreRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;

public class CampaignScoreControllerTests 
{
	private static final String TestDir = "SixGonThree-tests";
	private static CampaignMode campaign;
	private CampaignScoreRepository controller;
	
	@BeforeClass
	public static void initialize()
	{
		Gdx.files = new LwjglFiles();
		CampaignScoreControllerTests.campaign = new CampaignMode(new Player(0));
		CampaignProgressRepository repo = new CampaignProgressRepository();
		repo.loadState(CampaignScoreControllerTests.campaign);
	}
	
	@Before
	public void initializeEach()
	{
		// create valid setting
		Files files = Gdx.files;
		FileHandle handle = files.local(TestDir + "/campaign_score.xml");
		
		// Create campaign mission scores
		StringBuilder casualScores = new StringBuilder();		
		casualScores.append("<settings>\n");
		final int numMissions = CampaignScoreControllerTests.campaign.getMissionCount();
		for (int i = 0; i < numMissions; ++i)
		{
			String id = String.valueOf(i + 1);
			String value = String.valueOf((numMissions * 10 + 10 - (i + 1) * 10));
			casualScores.append("\t<setting name=\"totalBonusesUsed_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"date_" + id + "\"" + " type=\"java.lang.Long\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"totalMoves_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"totalTimeSpent_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
			casualScores.append("\t<setting name=\"totalScore_" + id + "\"" + " type=\"java.lang.Integer\" value=\"" + value + "\"/>\n");
		}
		casualScores.append("</settings>");
		handle.writeString(casualScores.toString(), false);

		this.controller = new CampaignScoreRepository(CampaignScoreControllerTests.TestDir, CampaignScoreControllerTests.campaign.getMissionCount());		
	}
	
	@Test
	public void generateDefaultsTest()
	{
		int missionCount = CampaignScoreControllerTests.campaign.getMissionCount();
		this.controller = new CampaignScoreRepository("", missionCount);
		this.controller.loadGameScores();
		for(int  i = 0; i < this.controller.getNumHighscores(); ++i)
		{
			int rank = i + 1;
			GameStats model = this.controller.getScore(rank);
			Assert.assertTrue("The scores should be zero by default", model.isZero());
		}
	}
	
	@After
	public void tearDown()
	{
		Gdx.files.local(TestDir).deleteDirectory();
	}
	
	@Test
	public void nonZeroHighscoreLoadPositiveTest()
	{
		this.controller.loadGameScores();
		
		Assert.assertTrue("No missions loaded!", this.controller.getNumHighscores() > 0);
		for(int i = 0; i < this.controller.getNumHighscores(); ++i)
		{
			int missionId = i + 1;
			GameStats model = this.controller.getScore(missionId);
			Assert.assertFalse("The score should not be zero", model.isZero());
		}
	}
	
	@Test
	public void onGameFinishScoreOverwriteTest()
	{
		this.controller.loadGameScores();
		GameStats score = new GameStats();
		score.setTotalScore(1000);
		this.controller.onGameFinished(score, 1);
		Assert.assertEquals(score.getTotalScore(), this.controller.getScore(1).getTotalScore());
	}
	
	@Test
	public void onGameFinishScoreNotOverwriteTest()
	{
		this.controller.loadGameScores();

		GameStats score = new GameStats();
		score.setTotalScore(5);
		this.controller.onGameFinished(score, 1);
		
		GameStats prevScore = this.controller.getScore(1);
		Assert.assertEquals(prevScore.getTotalScore(), this.controller.getScore(1).getTotalScore());
	}
	
	@Test
	public void gameSaveReloadTest()
	{
		this.controller.loadGameScores();
		GameStats score = new GameStats();
		score.setTotalScore(1000);
		this.controller.onGameFinished(score, 1);
		
		// Save the score and reset the controller
		this.controller.saveGameScores();
		this.controller.resetGameScores();
		this.controller.loadGameScores();
		Assert.assertEquals(score.getTotalScore(), this.controller.getScore(1).getTotalScore());
	}
}
