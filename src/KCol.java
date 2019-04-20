import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

	public SAT convert_kcol_to_sat() {
		SAT sat = new SAT();

		int numOfVariables = numOfCol * numOfNodes; //num of variables = (num of nodes) * (num of colours)

		sat.setNumOfVariables(numOfVariables); // set number of variables

		// combination of n_C_2, where n is the number of colours.
		ArrayList<ArrayList<Integer>> combination = Combination.makeCombinition(numOfCol);


		for (int i = 1; i <= numOfNodes; i++) { //to check all nodes
			int val = i;
			Clause clause = new Clause();

			/*
			 * Use for loop to generate and append 'At-least-once' clauses to the SAT object.
			 * A single clause for each node i, which says that each node has to have
			 * at least one colour.
			 */
			for (int j = 0; j++ < numOfCol; val += numOfNodes) { //to check all colors
				clause.appendVariable(new Variable(val));
			}

			sat.appendClause(clause); //append a generated clause to sat instance

			val = i;

			/*
			 * Use for loop to generate and append 'At-most-once' clauses to the SAT object.
			 * A clause for every node and pair j1 j2 of colours.
			 * The clause should say that node i can't be both colour j1 and colour j2.
			 */
			for (int j = 0; j < combination.size(); j++) {
				/*
				 * Use combination to check all posible combination of colours.
				 */
				ArrayList<Integer> al = combination.get(j);
				clause = new Clause();

				for (int k = 0; k < al.size(); k++) {
					int not_val = - ((al.get(k) - 1) * numOfNodes + i);
					clause.appendVariable(new Variable(not_val));
				}
				sat.appendClause(clause);
			}
		}

		/*
		 * Use for loop to generate and append 'edge' clauses.
		 */
		for (int j = 0; j < numOfEdges; j++) {
			Edge edge = edges.get(j);
			Clause clause = new Clause();

			int start = - edge.getStartPoint();
			int end = - edge.getEndPoint();

			for (int k = 0; k < numOfCol; k++) {
				clause.appendVariable(new Variable(start));
				clause.appendVariable(new Variable(end));

				sat.appendClause(clause); //append the clause to the list
				clause = new Clause();

				// change the value so that each node could have different values for different colours.
				start -= numOfNodes;
				end -= numOfNodes;
			}
		}
		sat.setNumOfClauses(sat.countNumOfClauses()); //set number of clauses

		return sat;
	}

	/**
	 * Add a new tuple string that contains the end points.
	 * @param tuple
	 */
	public void addEndpointTuple(String tuple) {
		endpoints.add(tuple);
	}

	/**
	 * Get the specific node from list.
	 * @param index The index of the node.
	 * @return the node
	 */
	public Node getNode(int index) {
		return nodes.get(index);
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

			//if ids.size() is less than numOfNodes
			for (; index <= numOfNodes; index++) {
				nodes.add(new Node(index, 1));
			}
		}
	}

	public void printKCol(String fileName) {
		PrintWriter pw = null;

		if (fileName != null) {
			try {
				File file = new File(fileName);
				pw = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace(); //TODO
			}
		} else {
			pw = new PrintWriter(System.out);
		}

		// print out the preamble section
		pw.println("c kcoltosat");
		pw.println("p edge " + this.numOfNodes + " " + this.numOfEdges);
		pw.println("colours " + this.numOfCol);

		//convert array list to array.
		Edge[] edgeArr = edges.stream().toArray(Edge[]::new);

		// use for-each loop to check all clauses
		for (Edge e : edgeArr) {
			StringBuilder sb = new StringBuilder();

			int startPoint = e.getStartPoint();
			int endPoint = e.getEndPoint();

			sb.append("e ");
			sb.append(startPoint);
			sb.append(" ");
			sb.append(endPoint);

			pw.println(sb.toString());
		}

		pw.flush(); //flush the output
		pw.close(); //close the print writer
	}

	/**
	 * Print out nodes in the list.
	 */
	public void printNodes() {
		Node[] nodeArr = nodes.stream().toArray(Node[]::new);
		for (Node n : nodeArr) {
			System.out.print(n.getId() + " ");
		}
		System.out.println();
	}

	/**
	 * Print out edges in the list.
	 */
	public void printEdges() {
		Edge[] edgeArr = edges.stream().toArray(Edge[]::new);
		for (Edge e : edgeArr) {
			System.out.print("<" + e.getStartPoint() + "," + e.getEndPoint() + ">");
		}
		System.out.println();
	}
}
