package com.handknittedapps.honeycombmatchthree.input;

public interface KeyboardInputHandler
{
	public void onKeyDown(int key);
	public void onKeyUp(int key);
	public boolean onKeyTyped(char key);
}
