package roadgraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import geography.GeographicPoint;
import util.GraphLoader;

public class SalesmanMapGraphTester {
	SalesmanMapGraph smMapGraph;
	List<GeographicPoint> destinations;
	@Before
	public void setup() {
		smMapGraph = new SalesmanMapGraph();
	}
	
	@Test
	public void addOrigin() {
		addVertex();
		GeographicPoint origin = new GeographicPoint(1.5, 1.2);
		assertTrue(smMapGraph.addOrigin(origin));
		
		GeographicPoint unknownGp = new GeographicPoint(7.7, 7.7);
		try {
			smMapGraph.addOrigin(unknownGp);
		} catch(IllegalArgumentException iae) {
			assertTrue("Should have exception because this origin is not in graph",
					iae instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void addDestination() {
		addVertex();
		GeographicPoint destination1 = new GeographicPoint(1.5, 1.2);
		GeographicPoint destination2 = new GeographicPoint(1.1, 3.1);
		assertTrue(smMapGraph.addDestination(destination1));
		assertTrue(smMapGraph.addDestination(destination2));
	}
	
	@Test
	public void addFalseDestination() {
		addVertex();
		GeographicPoint destination1 = new GeographicPoint(3.3, 3.2);
		GeographicPoint destination2 = new GeographicPoint(1.7, 5.1);
		try {
			smMapGraph.addDestination(destination1);
			assert false; //should have thrown exception
		} catch(Exception e) {
			assertTrue("Should throw illegal argument exception because destination is"
					+ "not in the graph", e instanceof IllegalArgumentException);
		}
		
		try {
			smMapGraph.addDestination(destination2);
			assert false; //should have thrown exception
		} catch(Exception e) {
			assertTrue("Should throw illegal argument exception because destination is"
					+ "not in the graph", e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void addVertex() {
		GeographicPoint location1 = new GeographicPoint(1.5, 1.2);
		GeographicPoint location2 = new GeographicPoint(1.1, 3.1);
		assertTrue(smMapGraph.addVertex(location1));
		assertTrue(smMapGraph.addVertex(location2));
		assertEquals("Should have two vertices", 2, smMapGraph.getNumVertices());
		
		assertFalse("Null location returns false on add attempt", smMapGraph.addVertex(null));
		GeographicPoint location1Duplicate = new GeographicPoint(1.5, 1.2);
		assertFalse("Existing location returns false on add attempt", smMapGraph.addVertex(location1Duplicate));
	}
	
	@Test
	public void testSalesmanGreedyPath() {
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", smMapGraph);
		System.out.println("Done loading. Map info: ");
		
		GeographicPoint start = new GeographicPoint(1.0, 1.0);
		GeographicPoint destination1 = new GeographicPoint(4.0, 1.0);
		GeographicPoint destination2 = new GeographicPoint(6.5, 0.0);
		GeographicPoint destination3 = new GeographicPoint(8.0, -1.0);
		smMapGraph.addOrigin(start);
		smMapGraph.addDestination(destination1);
		smMapGraph.addDestination(destination2);
		smMapGraph.addDestination(destination3);
		List<GeographicPoint> bestRoute = smMapGraph.bestPathGreedy();
		System.out.println("Proposed route: " + bestRoute.toString());
		
		Assert.assertEquals("First stop should be origin", start, bestRoute.get(0));
		Assert.assertEquals("Last stop should be origin", start, bestRoute.get(bestRoute.size() - 1));
		Assert.assertEquals("Second stop on route should be to 4.0, 1.0 because that is the closest to the point before it.",
				destination1, bestRoute.get(1));
		
	}
}
