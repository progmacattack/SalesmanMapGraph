package roadgraph;

import java.util.ArrayList;
import java.util.List;

import geography.GeographicPoint;

/**
 * This class describes a single node/vertex on the MapGraph. The primary purpose of this class
 * is to relate a given location to a given set of neighbors.
 * @author Adam Sickmiller
 *
 */
public class MapNode implements Comparable<MapNode> {
	private GeographicPoint location; //the geographic location of this node
	private List<MapNode> outgoingNeighbors; //all neighors accessible from this node
	private Double distanceTraveled = Double.POSITIVE_INFINITY; //in some search algorithms, we need to keep track of distance
									 //traveled to get to this node
	private Double predictedDistanceToDestination = 0d;
	
	public Double getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(Double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	/**
	 * Create a single node by providing a GeographicPoint location
	 * @param location the longitude/latitude of this node
	 */
	public MapNode(GeographicPoint location) {
		this.location = location;
		outgoingNeighbors = new ArrayList<MapNode>();
	}
	
	/**
	 * Add a single neighbor that is accessible from this node
	 * @param neighbor to be added
	 */
	public void addOutoingNeighbor(MapNode neighbor) {
		outgoingNeighbors.add(neighbor);
	}
	
	//everything below are getters and setters for variables
	
	/** Get the latitude/longitude of this node
	 * 
	 * @return GeographicPoint with lat/lon information
	 */
	public GeographicPoint getLocation() {
		return location;
	}
	
	/** Set the latitude/longitude of this node
	 * 
	 * @param GeographicPoint with lat/lon information
	 */
	public void setLocation(GeographicPoint location) {
		this.location = location;
	}
	
	/** Get a complete list of all neighbors accessible from this node
	 * 
	 * @return list of MapNodes of all neighbors accessible from this node
	 */
	public List<MapNode> getOutgoingNeighbors() {
		return outgoingNeighbors;
	}
	
	/** Set a complete list of all neighbors accessible from this node
	 * 
	 * @param list of MapNodes of all neighbors accessible from this node
	 */
	public void setOutgoingNeighbors(List<MapNode> outgoingNeighbors) {
		this.outgoingNeighbors = outgoingNeighbors;
	}

	@Override
	public int compareTo(MapNode other) {
		if(this.getDistanceTraveled() + this.getPredictedDistanceToDestination()
			< other.getDistanceTraveled() + other.getPredictedDistanceToDestination()) {
			return -1;
		}
		
		if(this.getDistanceTraveled() + this.getPredictedDistanceToDestination()
			> other.getDistanceTraveled() + other.getPredictedDistanceToDestination()) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapNode other = (MapNode) obj;
		if (distanceTraveled == null) {
			if (other.distanceTraveled != null)
				return false;
		} else if (!distanceTraveled.equals(other.distanceTraveled))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (outgoingNeighbors == null) {
			if (other.outgoingNeighbors != null)
				return false;
		} else if (!outgoingNeighbors.equals(other.outgoingNeighbors))
			return false;
		if (predictedDistanceToDestination == null) {
			if (other.predictedDistanceToDestination != null)
				return false;
		} else if (!predictedDistanceToDestination.equals(other.predictedDistanceToDestination))
			return false;
		return true;
	}

	public Double getPredictedDistanceToDestination() {
		return predictedDistanceToDestination;
	}

	public void setPredictedDistanceToDestination(Double predictedDistanceToDestination) {
		this.predictedDistanceToDestination = predictedDistanceToDestination;
	}
	
	public void clearDistances() {
		this.predictedDistanceToDestination = 0d;
		this.distanceTraveled = Double.POSITIVE_INFINITY;
	}
}
