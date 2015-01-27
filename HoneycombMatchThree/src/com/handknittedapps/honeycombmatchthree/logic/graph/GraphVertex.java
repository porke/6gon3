package com.handknittedapps.honeycombmatchthree.logic.graph;

import java.util.HashMap;

/** Direction assumption
// The direction 0 is up and the next ones are clockwise up to the final one
// for example:
//	  0
//	3 * 1
//	  2 	**/
public class GraphVertex<T>
{
	public GraphVertex(final T data)
	{
		this.data = data;
	}

	// Basic operations
	public boolean addEdge(int direction, final GraphVertex<T> endVertex)
	{
		//Check whether there are
		if (!this.neighbours.containsKey(direction))
		{
			this.neighbours.put(direction, endVertex);
			return true;
		}

		return false;
	}

	public void removeEdge(int direction)
	{
		this.neighbours.remove(direction);
	}


	public int numNeighbours() { return this.neighbours.size(); }

	// Get accessors
	public GraphVertex<T> getNeighbour(int direction)
	{
		return this.neighbours.containsKey(direction) ? this.neighbours.get(direction) : null;
	}
	public T getData() { return this.data; }

	// Set accessors
	public void setData(T data) { this.data = data; }

	//Internal data
	private  T data;
	private  HashMap<Integer, GraphVertex<T>> neighbours = new HashMap<Integer, GraphVertex<T>>();
}
