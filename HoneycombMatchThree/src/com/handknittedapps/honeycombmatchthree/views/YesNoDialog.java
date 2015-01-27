package com.handknittedapps.honeycombmatchthree.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class YesNoDialog extends Window
{
	public YesNoDialog(String prompt, ClickListener onClosed)
	{
		super(Resources.getSkin());
		this.setModal(true);
		this.closeCallback = onClosed;

		Skin skin = Resources.getSkin();
		Label lblPrompt = new Label(prompt, skin.getStyle(LabelStyle.class));
		lblPrompt.setAlignment(Align.CENTER, Align.CENTER);
		lblPrompt.setWrap(true);
		this.add(lblPrompt)
			.center()
			.expandX()
			.fillX()
			.colspan(2);
		this.row();

		Button yes = new TextButton("Yes", skin.getStyle(TextButtonStyle.class));
		yes.setClickListener(this.yesClicked);
		this.add(yes)
			.center()
			.maxHeight(BaseView.MainButtonHeight);

		Button no = new TextButton("No", skin.getStyle(TextButtonStyle.class));
		no.setClickListener(this.noClicked);
		this.add(no)
			.center()
			.maxHeight(BaseView.MainButtonHeight);

		this.setFillParent(true);
		this.row();
	}

	private ClickListener yesClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			YesNoDialog.this.returnValue = DialogReturnValue.Yes;
			YesNoDialog.this.closeCallback.click(YesNoDialog.this, 0, 0);
		}
	};

	private ClickListener noClicked = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			YesNoDialog.this.returnValue = DialogReturnValue.No;
			YesNoDialog.this.closeCallback.click(YesNoDialog.this, 0, 0);
		}
	};

	public boolean wasYesClicked() { return this.returnValue == DialogReturnValue.Yes; }
	public DialogReturnValue getReturnValue() {return this.returnValue;}

	protected DialogReturnValue returnValue;
	protected ClickListener closeCallback;
}
