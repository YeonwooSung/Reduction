
public class KCol_To_SAT {

	/*
	 * TODO comment!
	 */
	public static void main(String[] args) {
		//TODO file name, command line arguments
		String inputFile = null;
		String outputFile = null;

		if (args.length > 0) {
			inputFile = args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}
		}

		StraightforwardReduction reduction2 = new StraightforwardReduction(inputFile);

		KCol kcol = reduction2.getKCol();
		SAT newsat = kcol.convert_kcol_to_sat();
		newsat.printCNF(outputFile);

		//If the program success, it should exit with exit code 0.
		System.exit(0);
	}

}
