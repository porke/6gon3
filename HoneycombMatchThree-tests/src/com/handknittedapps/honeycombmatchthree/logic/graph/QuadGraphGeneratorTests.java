package com.handknittedapps.honeycombmatchthree.logic.graph;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.graph.Graph;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.logic.graph.QuadGraphGenerator;
import com.handknittedapps.honeycombmatchthree.utils.AtomicReference;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class QuadGraphGeneratorTests 
{
	private static Graph<Block> graph = new Graph<Block>();
	private static int SideSize = 4;
	
	@BeforeClass
	public static void initialize()
	{
		final int numVertices = QuadGraphGeneratorTests.SideSize * QuadGraphGeneratorTests.SideSize;
		for (int i = 0; i < numVertices; ++i)		
		{
			Point position = new Point(i % QuadGraphGeneratorTests.SideSize, i / QuadGraphGeneratorTests.SideSize);
			QuadGraphGeneratorTests.graph.addVertex(new Block(position, BlockColour.Blue, BlockType.Normal, false));
		}
		
		new QuadGraphGenerator().generateGraph(QuadGraphGeneratorTests.graph, QuadGraphGeneratorTests.SideSize);	
	}
	
	@Test
	public void graphHorizontalSizeTest_ShouldReturnCorrectSideSize()
	{
		GraphVertex<Block> from = QuadGraphGeneratorTests.graph.getVertex(0);
		AtomicReference<GraphVertex<Block>> outIterator = new AtomicReference<GraphVertex<Block>>();
		int pathLength = Graph.moveToTheEnd(from, 1, outIterator);
		Assert.assertEquals(QuadGraphGeneratorTests.SideSize - 1, pathLength);
	}
	
	@Test
	public void graphVerticalSizeTest_ShouldReturnCorrectSideSize()
	{
		GraphVertex<Block> from = QuadGraphGeneratorTests.graph.getVertex(0);
		AtomicReference<GraphVertex<Block>> outIterator = new AtomicReference<GraphVertex<Block>>();
		int pathLength = Graph.moveToTheEnd(from, 2, outIterator);
		Assert.assertEquals(QuadGraphGeneratorTests.SideSize - 1, pathLength);
	}
}
