
public class Edge {

	private int startPoint;
	private int endPoint;

	/**
	 * Set both start point and end point.
	 * @param startPoint The start point.
	 * @param endPoint The end point.
	 */
	public void setEndPoints(int startPoint, int endPoint) {
		this.setStartPoint(startPoint);
		this.setEndPoint(endPoint);
	}

	/**
	 * Getter for start point.
	 * @return the startPoint
	 */
	public int getStartPoint() {
		return startPoint;
	}

	/**
	 * Setter for start point.
	 * @param startPoint the startPoint to set
	 */
	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Getter for end point.
	 * @return the endPoint
	 */
	public int getEndPoint() {
		return endPoint;
	}

	/**
	 * Setter for end point.
	 * @param endPoint the endPoint to set
	 */
	public void setEndPoint(int endPoint) {
		this.endPoint = endPoint;
	}
}
