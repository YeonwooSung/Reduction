import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class ThreeSAT {
	private final int ZERO = 0;
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
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

		if (fileName != null) {
			try {
				File file = new File(fileName);
				pw = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			pw = new PrintWriter(System.out);
		}

		// print out the preamble section
		pw.println("c kcoltosat");
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
						sb.append(ZERO_STR);
					else if (value == ZERO) {
						this.setNumOfClauses(this.getNumOfClauses() + ONE);
					}
				} else {
					if (i != ZERO)
						sb.append(ZERO_STR);
				}
			}

			pw.println(sb.toString());
		}

		pw.flush(); //flush the output
		pw.close(); //close the print writer
	}

	/**
	 * This method converts the sat to kcol.
	 *
	 * @return
	 */
	public KCol convertToKCol() {
		KCol kcol = new KCol();

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

			//TODO what if numOfVariables < 4 ??
		} catch (IOException e) {
			e.printStackTrace();
		}

		int limit = this.numOfVariables * 3;

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
					Edge cliqueEdge = new Edge();
					cliqueEdge.setEndPoints(i, j);
					kcol.appendEdge(cliqueEdge);
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

		//TODO connect clause and literals that are not included in the clause

		kcol.validateNodes(); //validate the nodes of the kcol object

		return kcol;
	}

}
