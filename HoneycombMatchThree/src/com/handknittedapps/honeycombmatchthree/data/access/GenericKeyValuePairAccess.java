package com.handknittedapps.honeycombmatchthree.data.access;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public abstract class GenericKeyValuePairAccess
{
	public GenericKeyValuePairAccess(boolean readOnly, String rootSettingDir, String settingsFile)
	{
		this.settingsFilename = settingsFile;
		this.rootSettingsDir = rootSettingDir;
		this.readOnly = readOnly;
	}

	/** Output setting to a file. **/
	public final void save()
	{
		if (this.readOnly)
		{
			return;
		}

		FileHandle outFile = null;
		try
		{
			String completePath = this.rootSettingsDir + "/" + this.settingsFilename;
			outFile = Gdx.files.local(completePath);

			if (outFile.exists())
			{
				outFile.delete();
			}
			else
			{
				Gdx.files.local(this.rootSettingsDir).mkdirs();
				outFile = Gdx.files.local(completePath);
			}

			String path = Gdx.files.getLocalStoragePath() + completePath;
			StringWriter writer = new StringWriter();
			XmlWriter xml = new XmlWriter(writer);
			PrintWriter pw = new PrintWriter(new FileWriter(path));

			XmlWriter root = xml.element("settings");

			// Saving unlocked modes
			XmlWriter inputElem = null;
			for (Entry<String, Object> e : this.keyValuePairs.entrySet())
			{
				inputElem = root.element("setting");
				inputElem.attribute("name", e.getKey());
				inputElem.attribute("type", e.getValue().getClass().getName());
				inputElem.attribute("value", e.getValue());
				inputElem.pop();
			}

			if (inputElem != null)
			{
				inputElem.pop();
			}

			xml.close();
			root.close();
			pw.print(writer.toString());
			pw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Loads key value pairs from a file.
	 * @return True on success, false otherwise. **/
	public final boolean load()
	{
		FileHandle inFile = null;
		try
		{
			if (this.readOnly)
			{
				inFile = Gdx.files.internal(this.settingsFilename);
			}
			else
			{
				String filename = this.rootSettingsDir + "/" + this.settingsFilename;
				inFile = Gdx.files.local(filename);
			}

			XmlReader read = new XmlReader();

			if (!inFile.exists())
			{
				throw new IOException("File does not exist.");
			}

			Element root = read.parse(inFile);

			if (root != null)
			{
				for (int i = 0; i < root.getChildCount(); ++i)
				{
					Element child = root.getChild(i);
					Object setting = this.parseSetting(child.getAttribute("value"), child.getAttribute("type"));
					this.keyValuePairs.put(child.getAttribute("name"), setting);
				}
			}
		}
		catch (IOException e)
		{
			return false;
		}
		catch (SerializationException e)
		{
			return false;
		}

		return true;
	}

	protected abstract void generateDefaults();

	private Object parseSetting(String value, String type)
	{
		try
		{
			Class<?> classType = Class.forName(type);
			Constructor<?> ctor = classType.getConstructor(String.class);
			Object ret = ctor.newInstance(value);
			return ret;
		}
		catch (Throwable thr)
		{
			thr.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public final <T> T getSetting(String key)
	{
		return (T) this.keyValuePairs.get(key);
	}

	public final <T> void setSetting(String key, T value)
	{
		this.keyValuePairs.put(key, value);
	}

	protected HashMap<String, Object> keyValuePairs = new HashMap<String, Object>();
	protected String settingsFilename;
	protected String rootSettingsDir;
	protected boolean readOnly;
}
