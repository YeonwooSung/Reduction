import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class KCol {

	private int numOfNodes;
	private int numOfEdges;
	private int numOfCol;

	private List<Node> nodes;
	private List<Edge> edges;

	private List<String> endpoints;
	private List<Integer> ids;

	KCol() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();

		endpoints = new ArrayList<String>();
		ids = new ArrayList<Integer>();
	}

	/**
	 * Add a new tuple string that contains the end points.
	 * @param tuple
	 */
	public void addEndpointTuple(String tuple) {
		endpoints.add(tuple);
	}

	/**
	 * Check if the list of end point strings has the given end point string.
	 * If the given end point string is not in the list, then add it to the list.
	 * @param endpoint The end point string.
	 * @return Returns true if the list has a given string. Otherwise, returns false.
	 */
	public boolean hasEndpoints(String endpoint) {
		for (int i = 0; i < endpoints.size(); i++) {
			if (endpoints.get(i).equals(endpoint)) {
				return false;
			}
		}

		endpoints.add(endpoint);

		return true;
	}

	/**
	 * Check if the KCol instance contains the given node id.
	 * @param id The node id.
	 * @return Returns true if the id already exists. Otherwise, returns false.
	 */
	public boolean hasID(int id) {
		if (ids.contains(id)) {
			return true;
		}

		return false;
	}

	/**
	 * Add new node id.
	 * @param id The node id.
	 */
	public void addNewID(int id) {
		ids.add(id);
	}

	/**
	 * Append node to list of nodes.
	 * @param node
	 */
	public void appendNode(Node node) {
		nodes.add(node);

		this.addNewID(node.getId());
	}

	/**
	 * Append edge to list of edges.
	 * @param edge
	 */
	public void appendEdge(Edge edge) {
		edges.add(edge);
	}

	/**
	 * Counts the number of existing nodes.
	 * @return size of ndoes.
	 */
	public int countNodes() {
		return nodes.size();
	}

	/**
	 * Counts the number of existing edges.
	 * @return size of edges.
	 */
	public int countEdges() {
		return edges.size();
	}

	/**
	 * A getter for numOfNodes.
	 * @return the numOfNodes
	 */
	public int getNumOfNodes() {
		return numOfNodes;
	}

	/**
	 * A setter for numOfNodes.
	 * @param numOfNodes the numOfNodes to set
	 */
	public void setNumOfNodes(int numOfNodes) {
		this.numOfNodes = numOfNodes;
	}

	/**
	 * A getter for numOfEdges.
	 * @return the numOfEdges
	 */
	public int getNumOfEdges() {
		return numOfEdges;
	}

	/**
	 * A setter for numOfEdges
	 * @param numOfEdges the numOfEdges to set
	 */
	public void setNumOfEdges(int numOfEdges) {
		this.numOfEdges = numOfEdges;
	}

	/**
	 * Getter for numOfCol.
	 * @return the number of colours
	 */
	public int getNumOfCol() {
		return numOfCol;
	}

	/**
	 * Setter for numOfCol.
	 * @param numOfCol the number of colours to set
	 * @throws IOException to check the invalid input
	 */
	public void setNumOfCol(int numOfCol) throws IOException {
		if (numOfCol < 0) {
			throw new IOException();
		}

		this.numOfCol = numOfCol;
	}

	/**
	 * Check the number of nodes.
	 * @return True or false.
	 */
	public boolean checkNumOfNodes() {
		if (numOfNodes != this.countNodes()) {
			return false;
		}

		return true;
	}

	/**
	 * Check the number of edges.
	 * @return True or false.
	 */
	public boolean checkNumOfEdges() {
		if (numOfEdges != this.countEdges()) {
			return false;
		}

		return true;
	}

	/**
	 * Node without a descriptor will take on a default value 1.
	 * So, this method will check if there is any missing node, and add all missing nodes with default value.
	 */
	public void validateNodes() {

		// check if all nodes are generated
		if (!checkNumOfNodes()) {
			//sort the list of IDs to check which one is missing
			Collections.sort(ids);

			int index = 1;
			int i = 0;

			while (i < ids.size()) {
				int cur = ids.get(i);

				if (cur != index) {
					nodes.add(new Node(index, 1));
				} else {
					i++;
				}

				index++;
			}

			//if ids.size is less than numOfNodes
			for (; index <= numOfNodes; index++) {
				nodes.add(new Node(index, 1));
			}
		}
	}

}
