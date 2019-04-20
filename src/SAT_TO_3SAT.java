
public class SAT_TO_3SAT {

	/*
	 * TODO comments
	 */
	public static void main(String[] args) {
		//TODO file name, command line arguments
		StraightforwardReduction reduction1 = new StraightforwardReduction("test_ok.cnf");

		SAT sat = reduction1.getSAT();
		sat.printClauses();

		ThreeSAT sat3 = sat.convertSAT_to_3SAT();
		sat3.printClauses();
	}

}
