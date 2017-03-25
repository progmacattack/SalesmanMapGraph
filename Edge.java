package roadgraph;

/**
 * This class describes the edge between two vertices. We assume this edge to be a road
 * @author Adam Sickmiller
 *
 */
public class Edge {
	private String roadType;
	private double length;
	private MapNode begin;
	private MapNode end;
	
	//below are getters and setters for variables 
	
	/**
	 * Get the type of road represented by this edge
	 * @return String describing the road type
	 */
	public String getRoadType() {
		return roadType;
	}
	
	/**
	 * Set the type of road represented by this edge
	 * @param roadType a String describing the type of road
	 */
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	
	/**
	 * Retrieve the length of this edge, in kilometers
	 * @return the length of this edge, in kilometers
	 */
	public double getLength() {
		return length;
	}
	
	/**
	 * Set the length of this edge, in kilometers
	 * @param the length of this edge, in kilometers
	 */
	public void setLength(double length) {
		this.length = length;
	}
	
	/**
	 * Retrieve the origin of this edge
	 * @return MapNode representing the origin of this edge
	 */
	public MapNode getBegin() {
		return begin;
	}
	
	/**
	 * Set the origin of this edge
	 * @param begin MapNode representing the origin of this edge
	 */
	public void setBegin(MapNode begin) {
		this.begin = begin;
	}
	
	/**
	 * Retrieve the destination/end of this edge
	 * @return MapNode representing the destination/end of this edge
	 */
	public MapNode getEnd() {
		return end;
	}
	
	/**
	 * Set the destination/end of this edge
	 * @param MapNode representing the destination/end of this edge
	 */
	public void setEnd(MapNode end) {
		this.end = end;
	}
}
