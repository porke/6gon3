package com.handknittedapps.honeycombmatchthree.utils;

public final class AtomicReference<T> 
{
	public AtomicReference()
	{
		this(null);
	}
	
	public AtomicReference(T obj)
	{
		this.value = obj;
	}
	
	private T value = null;
	
	public T get() {return this.value; }
	public void set(T value) {this.value = value;}
}
