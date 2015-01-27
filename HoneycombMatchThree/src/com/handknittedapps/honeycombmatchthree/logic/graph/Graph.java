package com.handknittedapps.honeycombmatchthree.logic.graph;

import java.util.ArrayList;

import com.handknittedapps.honeycombmatchthree.utils.AtomicReference;


public class Graph <T>
{
	public GraphVertex<T> addVertex(T value)
	{
		GraphVertex<T> gv = new GraphVertex<T>(value);
		this.vertices.add(gv);
		return gv;
	}

	public void deleteVertex(GraphVertex<T> value)
	{
		this.vertices.remove(value);
	}

	public GraphVertex<T> getVertex(int index)
	{
		return this.vertices.get(index);
	}

	public static <T> int moveToTheEnd(GraphVertex<T> from, int direction, AtomicReference<GraphVertex<T>> outIterator)
	{
		// The path will always be one shorter than the number of loop checks
		// because the last vertex will be null
		GraphVertex<T> next = from;
		outIterator.set(next);
		int pathLen = 0;
		while (next != null)
		{
			outIterator.set(next);
			++pathLen;
			next = next.getNeighbour(direction);
		}

		if (next == null)
		{
			pathLen--;
		}

		return pathLen;
	}

	public static <T> int moveToTheEndOfNonEmptyVertices(GraphVertex<T> from, int direction, AtomicReference<GraphVertex<T>> outIterator)
	{
		// The path will always be one shorter than the number of loop checks
		// because the last vertex will be null
		GraphVertex<T> next = from;
		outIterator.set(next);
		int pathLen = 0;
		while (next != null
			&& next.getData() != null)
		{
			outIterator.set(next);
			++pathLen;
			next = next.getNeighbour(direction);
		}

		if (next == null
				|| next.getData() == null)
		{
			pathLen--;
		}

		return pathLen;
	}

	public int numVerts()
	{
		return this.vertices.size();
	}

	public ArrayList<GraphVertex<T>> getVertices()
	{
		return this.vertices;
	}

	ArrayList<GraphVertex<T>> vertices = new ArrayList<GraphVertex<T>>();
}
