package com.handknittedapps.honeycombmatchthree;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class Main 
{
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = HoneycombMatchThree.name;
		cfg.useGL20 = false;
		cfg.width = HoneycombMatchThree.NominalWidth;
		cfg.height = HoneycombMatchThree.NominalHeight;
		
		new LwjglApplication(new HoneycombMatchThree(), cfg);
	}
}
