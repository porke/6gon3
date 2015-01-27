package com.handknittedapps.honeycombmatchthree.graphics.hud;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public abstract class Displayable extends Actor
{
	private String text;
	private int active;

	protected Displayable(String name, String txt, Point pos, Point size, TextureRegion tex)
	{
		super(name);
		this.x = pos.x;
		this.y = pos.y;
		this.width = size.x;
		this.height = size.y;
		this.text = txt;
	}

	public void setText(String txt) { this.text = txt; }
	public void setActive(int active) { this.active = active; }
	public String getText() { return this.text; }
	public String getName() { return this.name; }
	public int getActive() { return this.active; }
}
