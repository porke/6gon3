package com.handknittedapps.honeycombmatchthree.control;


public class StateMachine
{
	public void changeState(IGameState gameState)
	{
		if (this.activeState != null)
		{
			this.activeState.onExitBase();
		}

		this.activeState = gameState;
		if (gameState != null)
		{
			this.activeState.onEnterBase();
		}
	}

	public void onExitState()
	{
		this.changeState(null);
	}

	public boolean onFrame()
	{
		if (this.activeState != null
			&& !this.activeState.onFrame())
		{
			this.changeState(this.activeState.getNextState());
		}

		return (this.activeState != null);
	}

	public void onPause()
	{
		if (this.activeState != null)
		{
			this.activeState.onPause();
		}
	}

	public void onResume()
	{
		if (this.activeState != null)
		{
			this.activeState.onResume();
		}
	}

	public void onBackPressed()
	{
		if (this.activeState != null)
		{
			this.activeState.onBackPressed();
		}
	}

	private IGameState activeState;
}
