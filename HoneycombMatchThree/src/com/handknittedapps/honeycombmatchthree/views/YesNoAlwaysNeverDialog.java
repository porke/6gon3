package com.handknittedapps.honeycombmatchthree.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class YesNoAlwaysNeverDialog extends YesNoDialog
{
	public YesNoAlwaysNeverDialog(String prompt, ClickListener onClosed)
	{
		super(prompt, onClosed);

		Skin skin = Resources.getSkin();
		Button always = new TextButton("Always", skin.getStyle(TextButtonStyle.class));
		always.setClickListener(this.alwaysClicked);
		this.add(always)
			.center()
			.maxHeight(BaseView.MainButtonHeight);

		Button never = new TextButton("Never", skin.getStyle(TextButtonStyle.class));
		never.setClickListener(this.neverClicked);
		this.add(never)
			.center()
			.maxHeight(BaseView.MainButtonHeight);
	}

	private ClickListener alwaysClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			YesNoAlwaysNeverDialog.this.returnValue = DialogReturnValue.Always;
			YesNoAlwaysNeverDialog.this.closeCallback.click(YesNoAlwaysNeverDialog.this, 0, 0);
		}
	};

	private ClickListener neverClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			YesNoAlwaysNeverDialog.this.returnValue = DialogReturnValue.Never;
			YesNoAlwaysNeverDialog.this.closeCallback.click(YesNoAlwaysNeverDialog.this, 0, 0);
		}
	};
}
