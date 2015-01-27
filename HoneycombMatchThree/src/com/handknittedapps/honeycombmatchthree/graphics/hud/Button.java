package com.handknittedapps.honeycombmatchthree.graphics.hud;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class Button extends Displayable
{
	public static class Builder
	{
		private Button button;

		/***
		 * @param buttonName Actor string id.
		 * @param textures Array of size 4.
		 * <ul>
		 * <li>0 - inactive tex</li>
		 * <li>1 - active  tex</li>
		 * <li>2 - checked tex</li>
		 * <li>3 - hidden tex</li>
		 * </ul>
		 */
		public Builder(String buttonName, Texture[] textures)
		{
			button = new Button(buttonName, textures);
		}

		public Builder position(Point pos)
		{
			button.x = pos.x;
			button.y = pos.y;
			return this;
		}

		public Builder size(Point size)
		{
			button.width = size.x;
			button.height = size.y;
			return this;
		}

		public Builder inputRadius(float rad)
		{
			button.inputRadius = rad;
			return this;
		}

		public Builder active(int active)
		{
			button.setActive(active);
			return this;
		}

		public Button build()
		{
			return button;
		}
	}

	public Texture[] textures = new Texture[4];
	private float inputRadius;

	private Button(String name, Texture[] textures)
	{
		super(name, "", new Point(0, 0), new Point(0, 0), new TextureRegion(textures[1]));
		this.textures = textures;
		this.setActive(1);
	}

	@Override
	public void draw(SpriteBatch renderer, float alpha)
	{
		renderer.draw(this.textures[this.getActive()], this.x, this.y);
	}

	@Override
	public Button hit(float xp, float yp)
	{
		Vector2 in = new Vector2(xp, yp);
		this.toLocalCoordinates(in);
		Vector2 curr = new Vector2(this.x, this.y);
		this.toLocalCoordinates(curr);
		double distance = Math.sqrt(Math.pow(curr.x + this.width / 2 - xp, 2) + Math.pow(curr.x + this.width / 2 - yp, 2));

		return (this.touchable && distance <= this.inputRadius) ? this : null;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2)
	{
		// Not handled
		return false;
	}

	@Override
	public void touchDragged(float arg0, float arg1, int arg2)
	{
		// Not handled
	}

	@Override
	public void touchUp(float arg0, float arg1, int arg2)
	{
		// Not handled
	}
}
