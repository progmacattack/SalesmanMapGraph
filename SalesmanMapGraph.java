package roadgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import geography.GeographicPoint;

/***
 * This class extends MapGraph to specify a solution to the so-called 'Traveling Salesman' problem. Given an origin
 * and list of destinations, what is a reasonable path from the origin to each destination and then back to the origin?
 * 
 * Set up the class by adding vertices and then adding destionations and an origin from among all vertices
 * 
 * @author Adam Sickmiller for Advanced Data Structures in Java, offered by University
 * of California San Diego / Coursera
 *
 */
public class SalesmanMapGraph extends MapGraph {
	private List<GeographicPoint> destinations;
	private List<GeographicPoint> suggestedRoute;
	private GeographicPoint origin;
	
	public SalesmanMapGraph() {
		destinations = new ArrayList<>();
		suggestedRoute = new ArrayList<>();
	}
	
	/** Add the origin of the 'Traveling Salesman' path. The path will begin
	 * and end at the origin.
	 * 
	 * @param origin - a GeographicPoint known by this graph that is the origin of the
	 * 'traveling salesman's' path. The path will visit all added destinations and
	 * return to the origin
	 * @return boolean - will return true if the origin is successfully added
	 * @throws IllegalArgumentException if the origin is not in the graph
	 * 
	 */
	public boolean addOrigin(GeographicPoint origin) { 
		if(this.getVerticesMap().containsKey(origin)) {
			this.origin = origin;
			return true;
		} else {
			throw new IllegalArgumentException("Origin must be a known destination in the vertices map");
		}
	}
	
	/** Add a destination that must be reached by the 'traveling salesman.' One or more destinations should be
	 * added so that other methods can return something meaningful for the class instance
	 * 
	 * @param destination - one of the destinations for the salesman 
	 * @return - true if the destination has been successfully added to the list of destinations
	 */
	public boolean addDestination(GeographicPoint destination) {
		if(this.getVerticesMap().containsKey(destination)) {
			return destinations.add(destination);
		} else {
			throw new IllegalArgumentException("Destination must be a known destination in the vertices map");
		}
	}
	
	/** Using the list of destinations added to this class instance, return a reasonable path from the specified
	 * origin to all destinations and then back to the origin. The path returned will be based on a "greedy"
	 * approach. This means that 
	 * @param origin the GeographicPoint where we start and end. Must exist in vertices map
	 * @return List<GeographicPoint> showing the path of destinations, in the order that they should be visited
	 **/
	public List<GeographicPoint> bestPathGreedy() {
		if(destinations.contains(origin)) {
			destinations.remove(origin);
		}		
		suggestedRoute.add(origin);
		
		//each destination must visit all non-visited destinations
		GeographicPoint curr = origin;
		GeographicPoint closest = null; //this is where we store the destination closest to curr
		while(destinations.size() > 0) {
			Double minDistance = Double.POSITIVE_INFINITY;
			for(GeographicPoint dest: destinations) { //go through all possible destinations from current point
				//determine distance to each destination from curr					
				List<GeographicPoint> route = findRouteBetween(curr, dest);
				Double distance = sumEdges(route);
				if(distance < minDistance) {
					closest = dest; //this is the closest to curr so far
					minDistance = distance;
				}
			}
			suggestedRoute.add(closest);  //add closest location to suggest route
			destinations.remove(closest); //remove it from possible destinations
			curr = closest;	//begin searching from the closest in next iteration
		}		
		suggestedRoute.add(origin); //return home
		return suggestedRoute;
	}
	
	/** Find the route between two geographic points using the astar search algorithm
	 * 
	 * @param start The GeographicPoint from which to start
	 * @param end The GeographiPoint destination
	 * @return a list of GeographicPoints representing ordered hops along the route
	 */
	private List<GeographicPoint> findRouteBetween(GeographicPoint start, GeographicPoint end) {
		return aStarSearch(start, end);
	}
	
	/** Given a route, return the total distance of that route
	 * 
	 * @param route - an ordered list of geographic points representing a route.  an edge must exist
	 * between each point and points adjacent to it.
	 * @returns Double - sum of the distance between all edges on the route 
	 * @throws IllegalArgumentException if there is not a valid path between adjacent points on the list
	 */
	private Double sumEdges(List<GeographicPoint> route) {
		Map<GeographicPoint, MapNode> verticesMap = getVerticesMap();		
		Map<Integer, Edge> edgeMap = getEdgeMap();
		Double sum = 0d;
		for(int i = 0; i < route.size() - 1; i++) {
			MapNode cur = verticesMap.get(route.get(i));
			MapNode nex = verticesMap.get(route.get(i + 1));
			Edge e = edgeMap.get(GraphUtility.generateHashMapKeyForEdge(cur, nex));
			if(e == null) {
				throw new IllegalArgumentException("Route passed to this method must be an *ordered* list of"
						+ " points, where each point has an edge between it and its neighbors");
			} else {
				Double edgeLength = cur.getLocation().distance(nex.getLocation());
				sum = sum + edgeLength;
			}
		}
		return sum;
	}
}
