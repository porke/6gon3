package com.handknittedapps.honeycombmatchthree;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class Main_600x1024 
{
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = HoneycombMatchThree.name;
		cfg.useGL20 = false;
		cfg.width = 600;
		cfg.height = 1024;
		
		new LwjglApplication(new HoneycombMatchThree(), cfg);
	}
}
