
public class SAT_TO_3SAT {

	/*
	 * TODO comments
	 */
	public static void main(String[] args) {
		//TODO file name, command line arguments
		String inputFile = null;
		String outputFile = null;

		if (args.length > 0) {
			inputFile = args[1];

			if (args.length > 1) {
				outputFile = args[2];
			}
		}

		StraightforwardReduction reduction1 = new StraightforwardReduction(inputFile);
		SAT sat = reduction1.getSAT();

		ThreeSAT sat3 = sat.convertSAT_to_3SAT(); //convert SAT to 3-SAT

		sat3.printCNF(outputFile); //print out the output
	}

}
