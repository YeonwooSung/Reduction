
public class Variable {

	private int var;

	Variable(int var) {
		this.setVar(var);
	}

	/**
	 * Print out the value of var.
	 */
	public void printVar() {
		System.out.print(var + " ");
	}

	/**
	 * Getter for var.
	 * @return the var
	 */
	public int getVar() {
		return var;
	}

	/**
	 * Setter for var.
	 * @param var the var to set
	 */
	public void setVar(int var) {
		this.var = var;
	}

}
