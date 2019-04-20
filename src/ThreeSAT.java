import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class ThreeSAT {
	private int numOfClauses;
	private int numOfVariables;
	private boolean hasEmptyClause; //to check if sat has an empty clause

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
				e.printStackTrace(); //TODO
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
					
					if (i == varArr.length - 1)
						sb.append("0");
				} else {
					if (i != 0)
						sb.append("0");
				}
			}

			pw.println(sb.toString());
		}

		pw.flush(); //flush the output
		pw.close(); //close the print writer
	}

}
