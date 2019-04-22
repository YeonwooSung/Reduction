
public class CircularTest {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: java CircularTest <input_file> <output1> <output2> <output3>");
			System.exit(2);
		}

		String inputFile = args[0];
		String outputFile1 = args[1];
		String outputFile2 = args[2];
		String outputFile3 = args[3];

		long startTime = System.currentTimeMillis();

		try {
			StraightforwardReduction reduction = new StraightforwardReduction(inputFile);

			KCol kcol = reduction.getKCol();
			SAT sat = kcol.convert_kcol_to_sat();
			sat.printCNF(outputFile1);

			reduction = new StraightforwardReduction(outputFile1);
			sat = reduction.getSAT();
			ThreeSAT sat3 = sat.convertSAT_to_3SAT();
			sat3.printCNF(outputFile2);

			KCol newKcol = sat3.convertToKCol();
			newKcol.printKCol(inputFile);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		long endTime = System.currentTimeMillis();

		long duration = endTime - startTime;
		duration /= 1000L;

		System.out.println(duration);

		//If the program success, it should exit with exit code 0.
		System.exit(0);
	}

}
