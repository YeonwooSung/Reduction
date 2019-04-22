import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class SAT_TO_3SAT {

	private static final String[] listOfFiles = {"hole6.cnf", "quinn.cnf", "aim-50-1_6-yes1-4.cnf", 
		"aim-100-1_6-no-1.cnf", "dubois20.cnf", "dubois21.cnf", 
		"dubois22.cnf", "par8-1-c.cnf", "zebra_v155_c1135.cnf", "hole6.cnf", 
		"bf0432-007.cnf", "bf0432-007_double.cnf", "bf0432-007_triple.cnf",
		"bf0432-007_4.cnf", "bf0432-007_5.cnf", "bf0432-007_6.cnf"};

	private static final String[] listOfOutput = {"hole6_3sat.cnf", "quinn_3sat.cnf", 
		"aim-50-1_6-yes1-4_3sat.cnf", "aim-100-1_6-no-1_3sat.cnf", 
		"dubois20_3sat.cnf", "dubois21_3sat.cnf", "dubois22_3sat.cnf",
		"par8-1-c_3sat.cnf", "zebra_v155_c1135_3sat.cnf", "hole6_3sat.cnf", 
		"bf0432-007_3sat.cnf", "bf0432-007_double_3sat.cnf", "bf0432-007_triple_3sat.cnf",
		"bf0432-007_4_3sat.cnf", "bf0432-007_5_3sat.cnf", "bf0432-007_6_3sat.cnf"};

	/*
	 * TODO comments
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

		StraightforwardReduction reduction1 = new StraightforwardReduction(inputFile);
		SAT sat = reduction1.getSAT();
		ThreeSAT sat3 = sat.convertSAT_to_3SAT(); //convert SAT to 3-SAT

		sat3.printCNF(outputFile); //print out the output

	}

}
