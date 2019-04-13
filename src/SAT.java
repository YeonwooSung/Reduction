import java.util.ArrayList;
import java.util.List;


public class SAT {

	private int numOfClauses;
	private int numOfVariables;

	private List<Clause> clauses;

	SAT() {
		clauses = new ArrayList<>();
	}

	/**
	 * Get specific clause from the list.
	 * @param index The index of the clause.
	 * @return the clause
	 */
	public Clause getClause(int index) {
		return clauses.get(index);
	}

	/**
	 * Add clause object to the list of clauses.
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
	 * Count the number of clauses in the list of clauses.
	 * @return clauses.size()
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

}
