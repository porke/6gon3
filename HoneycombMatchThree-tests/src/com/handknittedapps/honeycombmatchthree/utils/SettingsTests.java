package com.handknittedapps.honeycombmatchthree.utils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.handknittedapps.honeycombmatchthree.data.access.GenericKeyValuePairAccess;

public class SettingsTests 
{
	private static final String TestDir = "SixGonThree-tests";
	private static final String NonExistentSettings = "no-test.xml";
	private static final String ValidSettings = "valid.xml";
	private static final String ValidContent = "<settings>" +
											"	<setting name=\"TestField\" value=\"1\" type=\"java.lang.Integer\"/>" +						
											"</settings>";
	private static final String MalformedSettings = "malformed.xml";
	private static final String MalformedContent = "<settings>" +
													"	<setting name=\"TestField\" value=\"1\" type=\"java.lang.Integer\">" +						
													"</settingsMalformed>";
	
	private class SettingsTestImpl extends GenericKeyValuePairAccess
	{
		public SettingsTestImpl(String filename) 
		{
			super(false, SettingsTests.TestDir, filename);			
		}

		@Override
		protected void generateDefaults() 
		{
			this.keyValuePairs.put("default_fld_1", "default_val_1");
			this.keyValuePairs.put("default_fld_2", "default_val_2");
		}
	}
	
	private static SettingsTestImpl settings;
	
	@Before
	public void initialize()
	{
		Gdx.files = new LwjglFiles();
		Files files = Gdx.files;

		// create valid setting
		FileHandle handle = files.local(TestDir + "/" + ValidSettings);
		handle.writeString(SettingsTests.ValidContent, false);
		
		// create malformed setting
		handle = files.local(TestDir + "/" + MalformedSettings);
		handle.writeString(SettingsTests.MalformedContent, false);
	}
	
	@After
	public void tearDown()
	{
		Gdx.files.local(TestDir).deleteDirectory();
	}
	
	@Test
	public void noFileGenerateDefaultTest()
	{			
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.NonExistentSettings);
		boolean loaded = SettingsTests.settings.load();
		Assert.assertFalse("The settings.load should fail.", loaded);
		
		SettingsTests.settings.generateDefaults();
		String value = SettingsTests.settings.getSetting("default_fld_1");
		Assert.assertEquals("default_val_1", value);
	}
	
	@Test
	public void noFieldInFileTest()
	{
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.NonExistentSettings);
		boolean loaded = SettingsTests.settings.load();
		Assert.assertFalse("The settings.load should fail.", loaded);
		
		String value = SettingsTests.settings.getSetting("default_fld_3");
		Assert.assertEquals(null, value);
	}
	
	@Test
	public void validSettingLoadTest()
	{
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.ValidSettings);
		Assert.assertTrue("Load settings should succeed.", SettingsTests.settings.load());
		
		int value = SettingsTests.settings.getSetting("TestField");
		Assert.assertEquals(1, value);
	}
	
	@Test
	public void malformedSettingLoadTest()
	{
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.MalformedSettings);
		boolean loaded = SettingsTests.settings.load();
		Assert.assertFalse("Load settings should fail.", loaded);
		
		SettingsTests.settings.generateDefaults();
		String value = SettingsTests.settings.getSetting("default_fld_1");
		Assert.assertEquals("default_val_1", value);
	}	
	
	@Test
	public void validSettingsChangeValueImmediateTest()
	{
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.ValidSettings);
		Assert.assertTrue("Load settings should succeed.", SettingsTests.settings.load());
		
		int newValue = 3;
		SettingsTests.settings.setSetting("TestField", newValue);
		int value = SettingsTests.settings.getSetting("TestField");
		Assert.assertEquals(newValue, value);
	}
	
	@Test
	public void validSettingsChangeValueReloadTest()
	{
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.ValidSettings);
		Assert.assertTrue("Load settings should succeed.", SettingsTests.settings.load());
		
		int newValue = 3;
		SettingsTests.settings.setSetting("TestField", newValue);
		SettingsTests.settings.save();
		
		SettingsTests.settings = new SettingsTestImpl(SettingsTests.ValidSettings);
		Assert.assertTrue("Load settings should succeed.", SettingsTests.settings.load());
		int value = SettingsTests.settings.getSetting("TestField");
		Assert.assertEquals(newValue, value);
	}
}
