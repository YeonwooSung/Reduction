import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ThreeSAT {
	private final int ZERO = 0;
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
	private final int FOUR = 4;
	private final String ZERO_STR = "0";

	private int numOfClauses;
	private int numOfVariables;
	private boolean hasEmptyClause; //to check if sat has an empty clause

	private List<Clause> clauses;

	ThreeSAT() {
		clauses = new ArrayList<Clause>();
	}

	/**
	 * Append the clause to the list of clauses.
	 *
	 * @param clause
	 */
	public void appendClause(Clause clause) {
		clauses.add(clause);
	}

	/**
	 * Print out clauses.
	 */
	public void printClauses() {
		Clause[] clauseArr = clauses.stream().toArray(Clause[]::new);

		for (Clause c : clauseArr) {
			c.printVariables();
		}
	}

	/**
	 * Count the number of clauses.
	 * @return The number of clauses.
	 */
	public int countNumOfClauses() {
		return clauses.size();
	}

	/**
	 * Getter for numOfClauses.
	 * @return the numOfClauses
	 */
	public int getNumOfClauses() {
		return numOfClauses;
	}

	/**
	 * Setter for numOfClauses.
	 * @param numOfClauses the numOfClauses to set
	 */
	public void setNumOfClauses(int numOfClauses) {
		this.numOfClauses = numOfClauses;
	}

	/**
	 * Getter for numOfVariables.
	 * @return the numOfVariables
	 */
	public int getNumOfVariables() {
		return numOfVariables;
	}

	/**
	 * Setter for numOfVariables.
	 * @param numOfVariables the numOfVariables to set
	 */
	public void setNumOfVariables(int numOfVariables) {
		this.numOfVariables = numOfVariables;
	}

	/**
	 * Getter for hasEmptyClause.
	 * @return the hasEmptyClause
	 */
	public boolean checkIfHasEmptyClause() {
		return hasEmptyClause;
	}

	/**
	 * Setter for hasEmptyClause
	 * @param hasEmptyClause the hasEmptyClause to set
	 */
	public void setHasEmptyClause(boolean hasEmptyClause) {
		this.hasEmptyClause = hasEmptyClause;
	}

	/**
	 * Print out the result of reduction with DIMACS format.
	 *
	 * @param fileName The name of the output file.
	 * 				   This should be null if we print out via standard output stream.
	 */
	public void printCNF(String fileName) {
		PrintWriter pw = null;

		// check if the fileName is null -> if null, use standard output stream
		if (fileName != null) {
			try {
				File file = new File(fileName);
				pw = new PrintWriter(file); // use the file stream
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			pw = new PrintWriter(System.out); //use standard output stream
		}

		// print out the preamble section
		pw.println("c sattothreesat");
		pw.println("p cnf " + this.numOfVariables + " " + this.numOfClauses);

		//convert array list to array.
		Clause[] clauseArr = clauses.stream().toArray(Clause[]::new);

		// use for-each loop to check all clauses
		for (Clause c : clauseArr) {
			Variable[] varArr = c.getListOfVariables();
			StringBuilder sb = new StringBuilder();

			// use for loop to check all variables in the clause
			for (int i = 0; i < varArr.length; i++) {
				Variable var = varArr[i];

				if (var != null) { //to avoid the null pointer exception
					int value = var.getVar();
					sb.append(value);
					sb.append(" ");

					if (i == varArr.length - ONE && value != ZERO)
						sb.append(ZERO_STR); // append "0" to the output string
					else if (value == ZERO) {
						this.setNumOfClauses(this.getNumOfClauses() + ONE);
					}
				} else {
					// check if this is not a first variable
					if (i != ZERO)
						sb.append(ZERO_STR); // append "0" to the output string
				}
			}

			pw.println(sb.toString()); //print out the output string
		}

		pw.flush(); //flush the output
		pw.close(); //close the print writer
	}

	/**
	 * This method converts the sat to kcol.
	 *
	 * @return
	 * @throws Exception 
	 */
	public KCol convertToKCol() throws Exception {
		KCol kcol = new KCol();

		// since the k-col have solution iff the number of variables is less than 4, we need to check the number of variables
		if (this.numOfVariables < FOUR) {
			// if the number of variables is less than 4, add additional variables
			this.setNumOfVariables(4);
		}

		/*
		 * Assume we have n variables.
		 * 1 to n for positive values of variables.
		 * n+1 to 2n for negative values of variables.
		 * 2n+1 to 3n for clique nodes.
		 * 3n+1 to 3n+k (where k is the number of clauses) for clause nodes.
		 */
		int numOfNodes = this.numOfVariables * THREE + this.numOfClauses;

		int numOfCol = this.numOfVariables + ONE;

		kcol.setNumOfNodes(numOfNodes);

		try {
			kcol.setNumOfCol(numOfCol); //set the number of colours
		} catch (IOException e) {
			e.printStackTrace();
		}

		int limit = this.numOfVariables * THREE;

		// use for loop to check all variables
		for (int i = ONE; i <= this.numOfVariables; i++) {

			// edge that connects v and not v
			Edge newEdge = new Edge();
			newEdge.setEndPoints(i, i + numOfVariables);
			kcol.appendEdge(newEdge);

			int targetClique = this.numOfVariables * TWO + i;

			// use for loop to connect vertex v and clique vertices.
			for (int j = numOfVariables * TWO + ONE; j <= limit; j++) {
				if (j != targetClique) {
					Edge cliqueEdge1 = new Edge();
					cliqueEdge1.setEndPoints(i, j);
					kcol.appendEdge(cliqueEdge1);
					Edge cliqueEdge2 = new Edge();
					cliqueEdge2.setEndPoints(i + this.numOfVariables, j);
					kcol.appendEdge(cliqueEdge2);
				}
			}
		}

		// connect vertices to make a clique
		for (int j = numOfVariables * TWO + ONE; j <= limit; j++) {
			for (int k = j + 1; k <= limit; k++) {
				Edge cliqueEdge = new Edge();
				cliqueEdge.setEndPoints(j, k);
				kcol.appendEdge(cliqueEdge);
			}
		}

		// connect clauses to literals that are not included in the clause

		HashMap<Integer, List<Integer>> hm = mapClausesToVariable();
		limit = this.numOfVariables * TWO;
		int minClauseNum = THREE * this.numOfVariables + ONE;
		int maxClauseNum = THREE * this.numOfVariables + this.numOfClauses;

		// use for loop to check all literals
		for (int i = ONE; i <= limit; i++) {

			// check if the current literal is included in any clauses
			if (hm.containsKey(i)) {
				List<Integer> list = hm.get(i);

				for (int j = minClauseNum; j <= maxClauseNum; j++) {
					// check if the list contains the current clause number.
					if (!list.contains(j)) {
						String endpointStr = i + " " + j;
						if (!kcol.hasEndpoints(endpointStr)) {
							//append endpoint i and j
							Edge clauseEdge = new Edge();
							clauseEdge.setEndPoints(i, j);
							kcol.appendEdge(clauseEdge);
						} else {
							throw new Exception();
						}
					}
				}
			} else {

				// if the current literal is not included in any clauses, add connect it with all clause nodes
				for (int j = minClauseNum; j <= maxClauseNum; j++) {
					String endpointStr = i + " " + j;
					if (!kcol.hasEndpoints(endpointStr)) {
						Edge clauseEdge = new Edge();
						clauseEdge.setEndPoints(i, j);
						kcol.appendEdge(clauseEdge);
					} else {
						throw new Exception();
					}
				}
			}
		}

		kcol.setNumOfEdges(kcol.countEdges()); //set the number of edges
		return kcol;
	}

	/**
	 * This method creates a hash map that maps the variables to the list of clauses that include the particular variable.
	 *
	 * @return hash map
	 */
	private HashMap<Integer, List<Integer>> mapClausesToVariable() {
		HashMap<Integer, List<Integer>> hm = new HashMap<Integer, List<Integer>>();

		Clause[] clauseArr = clauses.stream().toArray(Clause[]::new);

		int clauseNum = 3 * this.numOfVariables + ONE;

		// use for-each loop to check all clauses
		for (Clause clause : clauseArr) {
			Variable[] variables = clause.getListOfVariables();

			// use for-each loop to check all variables
			for (Variable variable : variables) {
				if (variable != null) {
					int value = variable.getVar();

					// check if the value is a negative number
					if (value < 0)
						value = this.numOfVariables - value;

					// check if the value is zero
					if (value != ZERO) {
						// check if the hash map already has this key
						if (hm.containsKey(value)) {
							List<Integer> list = hm.get(value);
							list.add(clauseNum);
							hm.replace(value, list); //replace the list with new list
						} else {
							List<Integer> list = new ArrayList<Integer>();
							list.add(clauseNum);
							hm.put(value, list); //add the list to the hash map
						}
					}
				}
			}

			clauseNum += 1; //increase the clauseNum as we will going to have next clause
		}

		return hm;
	}

}
