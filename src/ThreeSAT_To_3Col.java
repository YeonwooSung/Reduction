import java.io.IOException;


public class ThreeSAT_To_3Col {
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;

	static KCol convert_3sat_to_3col(ThreeSAT sat3) throws IOException {
		KCol col = new KCol();

		col.setNumOfCol(THREE);

		int limit = sat3.getNumOfVariables();
		int numOfNodes = ZERO;

		int trueNode = limit * TWO + ONE;
		int falseNode = limit * TWO + TWO;
		int baseNode = limit * TWO + THREE;

		numOfNodes += THREE;

		// make a triangle with true, false and base nodes
		Edge trueFalse = new Edge();
		trueFalse.setEndPoints(trueNode, falseNode);
		Edge falseBase = new Edge();
		falseBase.setEndPoints(falseNode, baseNode);
		Edge trueBase = new Edge();
		trueBase.setEndPoints(trueNode, baseNode);
		col.appendEdge(trueBase);
		col.appendEdge(falseBase);
		col.appendEdge(trueFalse);

		for (int i = ONE; i <= limit; i++) {
			//make a triangle with literal node, negate literal node and base node

			Edge e1 = new Edge();
			Edge e2 = new Edge();
			Edge e3 = new Edge();
			int negate = i + limit;

			e1.setEndPoints(i, negate);
			e2.setEndPoints(i, baseNode);
			e3.setEndPoints(negate, baseNode);

			numOfNodes += THREE; //increase the num of nodes
		}

		Clause[] clauses = sat3.getClauses();
		int numOfGadget = 0;

		// for each clause, add OR gadget graph.
		for (Clause c : clauses) {
			if (c.countVariables() != ZERO) {
				int ret = convertClauseToGadget(col, c, limit, sat3.getNumOfClauses(), numOfGadget);

				numOfGadget += ret;
			} else {
				if (sat3.checkIfHasEmptyClause()) {
					int gadgetNode = numOfGadget + ONE;
					numOfGadget += 1;

					Edge gadgetEdgeEmptyClause1 = new Edge();
					Edge gadgetEdgeEmptyClause2 = new Edge();
					Edge gadgetEdgeEmptyClause3 = new Edge();

					gadgetEdgeEmptyClause1.setEndPoints(gadgetNode, baseNode);
					gadgetEdgeEmptyClause2.setEndPoints(gadgetNode, falseNode);
					gadgetEdgeEmptyClause3.setEndPoints(gadgetNode, trueNode);

					col.appendEdge(gadgetEdgeEmptyClause1);
					col.appendEdge(gadgetEdgeEmptyClause2);
					col.appendEdge(gadgetEdgeEmptyClause3);
				}
			}
		}

		numOfNodes += numOfGadget;
		col.setNumOfNodes(numOfNodes);
		col.setNumOfEdges(col.countEdges());

		return col;
	}

	/**
	 * This method converts the clause to the OR gadget graph and append that graph to the KCol object.
	 *
	 * @param col
	 * @param clause
	 * @param numOfVariables
	 * @param numOfClauses
	 * @param numOfGadgets
	 * @return the number of generated nodes
	 */
	private static int convertClauseToGadget(KCol col, Clause clause, int numOfVariables, int numOfClauses, int numOfGadgets) {
		int value1 = clause.getVariable(ZERO).getVar();
		int value2 = ZERO;
		int value3 = ZERO;

		int gadgetStart = numOfVariables + numOfVariables + THREE + numOfGadgets;

		// check if the value1 is negative number
		if (value1 < ZERO) {
			value1 = numOfVariables - value1;
		}

		int count = clause.countVariables();

		// check if the clause has multiple variables
		if (count > ONE) {
			value2 = clause.getVariable(ONE).getVar();

			// check if the value2 is negative number
			if (value2 < ZERO) {
				value2 = numOfVariables - value2;
			}

			// check if the clause has more than two variables
			if (count > TWO) {
				value3 = clause.getVariable(TWO).getVar();

				// check if the value3 is negative number
				if (value3 < ZERO) {
					value3 = numOfVariables - value3;
				}

				//TODO
				generateGadget(gadgetStart, value1, value2, clause, col);
				gadgetStart += FIVE;
				generateGadget(gadgetStart, gadgetStart, value3, clause, col);
				gadgetStart += FIVE;

				// connect end point of gadget with false
				int falseNode = numOfVariables * TWO + TWO;

				Edge gadgetEdge = new Edge();
				gadgetEdge.setEndPoints(falseNode, gadgetStart);

				return 6;
			}
			
			generateGadget(gadgetStart, value1, value2, clause, col);
			return 3;
		}

		int gadgetNode1 = gadgetStart + ONE;
		Edge gadgetEdge1 = new Edge();
		gadgetEdge1.setEndPoints(gadgetNode1, value1);
		int baseNode = numOfVariables * 2 + THREE;
		Edge gadgetEdge2 = new Edge();
		gadgetEdge2.setEndPoints(baseNode, gadgetNode1);
		
		col.appendEdge(gadgetEdge1);
		col.appendEdge(gadgetEdge2);

		return 1;
	}

	/**
	 * This method generates a single OR gadget and append that gadget to KCol object.
	 *
	 * @param gadgetStart
	 * @param value1
	 * @param value2
	 * @param clause
	 * @param col
	 */
	static void generateGadget(int gadgetStart, int value1, int value2, Clause clause, KCol col) {
		int gadgetNode1 = gadgetStart + ONE;
		int gadgetNode2 = gadgetStart + TWO;
		int gadgetNode3 = gadgetStart + THREE;

		// make edges to connect gadget and literals
		Edge gadgetEdge1 = new Edge();
		Edge gadgetEdge2 = new Edge();

		// connect gadget and literals
		gadgetEdge1.setEndPoints(gadgetNode1, value1);
		gadgetEdge2.setEndPoints(gadgetNode2, value2);

		//append the edges to the KCol object
		col.appendEdge(gadgetEdge1);
		col.appendEdge(gadgetEdge2);

		// make edges so that the gadget could have clique in it.
		Edge cliqueEdge1 = new Edge();
		cliqueEdge1.setEndPoints(gadgetNode1, gadgetNode2);
		Edge cliqueEdge2 = new Edge();
		cliqueEdge2.setEndPoints(gadgetNode1, gadgetNode3);
		Edge cliqueEdge3 = new Edge();
		cliqueEdge3.setEndPoints(gadgetNode2, gadgetNode3);

		//append the edges to the KCol object
		col.appendEdge(cliqueEdge1);
		col.appendEdge(cliqueEdge2);
		col.appendEdge(cliqueEdge3);
	}

	public static void main(String[] args) {
		String inputFile = null;
		String outputFile = null;

		if (args.length > ZERO) {
			inputFile = args[ZERO];
			if (args.length > ONE) {
				outputFile = args[ONE];
			}
		}

		ThreeSAT_To_KCol converter = new ThreeSAT_To_KCol(inputFile);
		ThreeSAT sat3 = converter.getSatTHREE();

		try {
			KCol threeCol = convert_3sat_to_3col(sat3);
			threeCol.printKCol(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
