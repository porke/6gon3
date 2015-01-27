package com.handknittedapps.honeycombmatchthree.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Sprite extends Actor
{
	public Sprite(TextureRegion textureRgn)
	{
		this.textureRgn = textureRgn;
		this.width = textureRgn.getRegionWidth();
		this.height = textureRgn.getRegionHeight();
	}

	public Sprite(Texture tex)
	{
		this(new TextureRegion(tex));
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		batch.draw(this.textureRgn, this.x, this.y, this.width, this.height);
	}

	@Override
	public Actor hit(float x, float y)
	{
		return null;
	}

	private TextureRegion textureRgn;
}
