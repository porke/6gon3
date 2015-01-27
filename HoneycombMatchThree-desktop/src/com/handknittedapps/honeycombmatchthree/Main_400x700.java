package com.handknittedapps.honeycombmatchthree;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class Main_400x700 
{
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = HoneycombMatchThree.name;
		cfg.useGL20 = false;
		cfg.width = 400;
		cfg.height = 700;
		
		new LwjglApplication(new HoneycombMatchThree(), cfg);
	}
}
