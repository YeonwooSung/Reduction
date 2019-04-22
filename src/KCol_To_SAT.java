
public class KCol_To_SAT {

	/*
	 * Reduction from K-colourable to SAT.
	 */
	public static void main(String[] args) {
		String inputFile = null;
		String outputFile = null;

		if (args.length > 0) {
			inputFile = args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}
		}

		StraightforwardReduction reduction = new StraightforwardReduction(inputFile);

		KCol kcol = reduction.getKCol();
		SAT newsat = kcol.convert_kcol_to_sat();
		newsat.printCNF(outputFile);

		//If the program success, it should exit with exit code 0.
		System.exit(0);
	}

}
