import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SAT {

	private int numOfClauses;
	private int numOfVariables;
	private boolean hasEmptyClause; //to check if sat has an empty clause

	private List<Clause> clauses;

	SAT() {
		clauses = new ArrayList<>();
		this.setHasEmptyClause(false);
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

	/**
	 * Convert SAT to 3-SAT.
	 * @return ThreeSAT (3-SAT) instance
	 */
	public ThreeSAT convertSAT_to_3SAT() {
		ThreeSAT sat3 = new ThreeSAT();

		int numOfClauses = this.countNumOfClauses();
		int totalNumOfClauses = 0;
		int offset = 1;

		for (int i = 0; i < numOfClauses; i++) {
			Clause clause = this.getClause(i);

			int numOfVariables = clause.countVariables();

			if (numOfVariables <= 3) {
				sat3.appendClause(clause);

				if (!this.hasEmptyClause) {
					// check if this clause if an empty clause
					if (clause.countVariables() > 0)
						if (clause.countVariables() > 1 || clause.getVariable(0) != null)
							totalNumOfClauses += 1;
				} else {
					totalNumOfClauses += 1;
				}

			} else {

				Clause newClause = new Clause();
				newClause.appendVariable(clause.getVariable(0));
				newClause.appendVariable(clause.getVariable(1));
				newClause.appendVariable(new Variable(this.getNumOfVariables() + offset));
				newClause.appendVariable(null);

				sat3.appendClause(newClause);
				totalNumOfClauses += 1;

				int limit = numOfVariables - 1;

				for (int j = 2; j < numOfVariables; j++) {
					newClause = new Clause();

					int sum = this.getNumOfVariables() + offset++;
					int newVal = - sum;
					newClause.appendVariable(new Variable(newVal));

					sat3.setNumOfVariables(sum);

					newClause.appendVariable(clause.getVariable(j));

					if (j < limit) {
						sum = this.getNumOfVariables() + offset;
						newClause.appendVariable(new Variable(sum));
						sat3.setNumOfVariables(sum);
					}

					newClause.appendVariable(null);

					totalNumOfClauses += 1;
					sat3.appendClause(newClause);
				}
			}
		}

		System.out.println(totalNumOfClauses);
		sat3.setNumOfClauses(totalNumOfClauses);

		return sat3;
	}

	/**
	 * Getter for hasEmptyClause.
	 * @return the hasEmptyClause
	 */
	public boolean checkIfHasEmptyClause() {
		return hasEmptyClause;
	}

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

	/**
	 * Setter for hasEmptyClause
	 * @param hasEmptyClause the hasEmptyClause to set
	 */
	public void setHasEmptyClause(boolean hasEmptyClause) {
		this.hasEmptyClause = hasEmptyClause;
	}

}
