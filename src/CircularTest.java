
public class CircularTest {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java CircularTest <input_file> <output_file>");
			System.exit(2);
		}

		String inputFile = args[0];
		String outputFile = args[1];

		try {
			StraightforwardReduction reduction = new StraightforwardReduction(inputFile);

			KCol kcol = reduction.getKCol();
			SAT sat = kcol.convert_kcol_to_sat();
			sat.printCNF(outputFile);

			reduction = new StraightforwardReduction(outputFile);
			sat = reduction.getSAT();
			ThreeSAT sat3 = sat.convertSAT_to_3SAT();
			sat3.printCNF(outputFile);

			KCol newKcol = sat3.convertToKCol();
			newKcol.printKCol(outputFile);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//If the program success, it should exit with exit code 0.
		System.exit(0);
	}

}
