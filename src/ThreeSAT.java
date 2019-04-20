import java.util.ArrayList;
import java.util.List;


public class ThreeSAT {
	private int numOfClauses;
	private int numOfVariables;

	private List<Clause> clauses;

	ThreeSAT() {
		clauses = new ArrayList<Clause>();
	}

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

}
