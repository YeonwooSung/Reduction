
public class KCol_To_SAT {

	/*
	 * TODO comment!
	 */
	public static void main(String[] args) {
		//TODO file name, command line arguments
		StraightforwardReduction reduction2 = new StraightforwardReduction("test.col");
		System.out.println("col test start");

		KCol kcol = reduction2.getKCol();
		kcol.printNodes();
		kcol.printEdges();
		SAT newsat = kcol.convert_kcol_to_sat();
		newsat.printClauses();

		//If the program success, it should exit with exit code 0.
		System.exit(0);
	}

}
