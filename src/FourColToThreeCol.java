import java.io.IOException;


public class FourColToThreeCol {
	private final int ZERO = 0;
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
	private final int SEVEN = 7;

	private KCol fourcol;
	private KCol threecol;
	private boolean isConverted;

	FourColToThreeCol(KCol fourcol) {
		this.fourcol = fourcol;
		this.threecol = new KCol();
		isConverted = false;
		try {
			this.threecol.setNumOfCol(THREE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public KCol getThreeCol() {
		return threecol;
	}

	public void convert4ColTo3Col() {
		if (isConverted) {
			return;
		}

		int nodeNum = fourcol.countNodes();
		int numOfNodes = ZERO;

		int groundNode = nodeNum + nodeNum + ONE; //ground vertex of 3-col graph

		// use for loop to make vertex gadgets for all vertices
		for (int i = ONE; i <= nodeNum; i++) {
			connectByEdges(groundNode, i);
			connectByEdges(groundNode, i + nodeNum);
			numOfNodes += TWO;
		}

		numOfNodes += ONE;

		Edge[] edges = fourcol.getEdges();

		// iterate array of edges to make edge gadgets for all edges
		for (Edge edge : edges) {
			generateEdgeGadget(edge, numOfNodes, groundNode, nodeNum);
			numOfNodes += SEVEN;
		}

		threecol.setNumOfEdges(threecol.countEdges());
		threecol.setNumOfNodes(numOfNodes);

		isConverted = true;
	}

	private void generateEdgeGadget(Edge e, int numOfNodes, int groundNode, int nodeNum) {
		int startpoint = e.getStartPoint();
		int endpoint = e.getEndPoint();
		int startpoint2 = startpoint + nodeNum;
		int endpoint2 = endpoint + nodeNum;

		int a1 = numOfNodes + ONE;
		int a2 = a1 + ONE;
		int b1 = a2 + ONE;
		int b2 = b1 + ONE;
		int c1 = b2 + ONE;
		int c2 = c1 + ONE;
		int d = c2 + ONE;

		connectByEdges(groundNode, d);
		connectByEdges(startpoint, a1);
		connectByEdges(startpoint2, a2);
		connectByEdges(endpoint, a1);
		connectByEdges(endpoint2, a2);
		connectByEdges(a1, b1);
		connectByEdges(a1, c1);
		connectByEdges(a2, b2);
		connectByEdges(a2, c2);
		connectByEdges(c1, d);
		connectByEdges(c1, c2);
		connectByEdges(c2, d);
		connectByEdges(startpoint, b1);
		connectByEdges(startpoint2, b2);
		connectByEdges(endpoint, b1);
		connectByEdges(endpoint2, b2);
	}

	private void connectByEdges(int vertex1, int vertex2) {
		Edge edge = new Edge();
		edge.setEndPoints(vertex1, vertex2);
		threecol.appendEdge(edge);
	}

	public static void main(String[] args) {
		String inputFile = null;
		String outputFile = null;

		if (args.length > 0) {
			inputFile = args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}
		}

		StraightforwardReduction reduction = new StraightforwardReduction(inputFile);

		KCol col4 = reduction.getKCol(); //four colourable graph
		FourColToThreeCol converter = new FourColToThreeCol(col4);

		converter.convert4ColTo3Col();

		KCol col3 = converter.getThreeCol();
		col3.printKCol(outputFile);
	}
}
