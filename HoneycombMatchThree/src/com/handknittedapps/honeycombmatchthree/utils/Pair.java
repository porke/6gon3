package com.handknittedapps.honeycombmatchthree.utils;

public class Pair<T1, T2>
{
	public Pair(T1 f, T2 s)
	{
		this.first = f;
		this.second = s;
	}

	public T1 getFirst() { return this.first; }
	public T2 getSecond() { return this.second; }
	public void setFirst(T1 value) { this.first = value; }
	public void setSecond(T2 value) { this.second = value; }
	
	private T1 first;
	private T2 second;
}
