package com.handknittedapps.honeycombmatchthree.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class ScoreOverlay extends Actor
{
	public ScoreOverlay(String txt, Point basePos, Point displacement, BitmapFont font)
	{
		this.text = txt;
		this.basePos = basePos;
		this.x = basePos.x;
		this.y = basePos.y;
		this.destPos = new Point((int) this.x + displacement.x, (int) this.y + displacement.y);
		this.alpha = 1.0f;
		this.font = font;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) 
	{
		this.font.setColor(0, 1, 0, this.alpha);
		this.font.draw(batch, this.text, this.x, this.y);
	}

	@Override
	public Actor hit(float x, float y) 
	{
		return null;
	}
	
	public String getText() { return this.text; }
	public Point getBasePos() {	return this.basePos; }
	public Point getDestPos() { return this.destPos;	}
	public float getAlpha() {	return this.alpha;	}

	public void setText(String text) { this.text = text; }
	public void setBasePos(Point basePos) { this.basePos = basePos; }
	public void setDestPos(Point destPos) { this.destPos = destPos; }
	public void setAlpha(float alpha) { this.alpha = alpha; }

	private String text;
	private Point basePos;
	private Point destPos;
	private float alpha;
	private BitmapFont font;
}
