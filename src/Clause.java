import java.util.ArrayList;
import java.util.List;


public class Clause {

	List<Variable> variables;

	Clause() {
		variables = new ArrayList<Variable>();
	}

	/**
	 * Count the number of variables in the list.
	 * @return
	 */
	public int countVariables() {
		int count = variables.size();

		if (count > 0) {
			return count - 1;
		}

		return count; //-1 to exclude the null at the end of the list
	}

	/**
	 * Get the particular variable from the list.
	 * @param index The index of the variable.
	 * @return the variable instance
	 */
	public Variable getVariable(int index) {
		return variables.get(index);
	}

	/**
	 * Add a new variable instance to the list.
	 * @param v A variable that should be appended.
	 */
	public void appendVariable(Variable v) {
		variables.add(v);
	}

	/**
	 * Returns the list of variables
	 * @return variable array
	 */
	public Variable[] getListOfVariables() {
		return variables.stream().toArray(Variable[]::new);
	}

	/**
	 * Print out the variables.
	 */
	public void printVariables() {
		Variable[] vars = variables.stream().toArray(Variable[]::new);

		for (Variable var : vars) {
			if (var != null)
				var.printVar();
		}

		System.out.println();
	}
}
