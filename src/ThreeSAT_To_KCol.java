import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class ThreeSAT_To_KCol {
	private ThreeSAT satTHREE;
	private BufferedReader br;

	private final int ZERO = 0;
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
	private final int FOUR = 4;

	private static final String[] listOfFiles = {"aim-50-1_6-yes1-4_3sat.cnf", "aim-100-1_6-no-1_3sat.cnf"};
	private static final String[] listOfOutput = {"aim-50-1_6-yes1-4.col", "aim-100-1_6-no-1.col"};

	ThreeSAT_To_KCol(String fileName) {
		try {
			if (fileName != null) {
				br = new BufferedReader(new FileReader(new File(fileName)));
			} else {
				br = new BufferedReader(new InputStreamReader(System.in));
			}

			// read the input file
			this.parseInput();

			br.close();

		} catch (FileNotFoundException e) {
			System.exit(ONE);
		} catch (IOException | NumberFormatException e) {
			System.exit(ONE);
		} catch (Exception e) {
			System.exit(ONE);
		}
	}

	/**
	 * This method reads and parses the input file.
	 *
	 * @throws IOException could be occurred while reading the input.
	 * @throws NumberFormatException could be occurred when it tries to convert string to integer.
	 * @throws Exception could be occurred when some unexpected error occurs
	 */
	private void parseInput() throws IOException, NumberFormatException, Exception {
		satTHREE = new ThreeSAT();

		String line;

		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) { //check if the read line is empty
				continue;
			}

			String[] words = line.split("\\s+"); //split the line by whitespace characters

			if (words[ZERO].equals("c")) 
				continue; //it is okay to skip the comment line
			else if (words[ZERO].equals("p")) {
				//check if the input format is valid
				if (words.length != FOUR) 
					throw new IOException();

				if (words[ONE].equals("cnf")) {
					/* words[1] = FORMAT, words[2] = VARIABLES, words[3] = CLAUSES */

					int variables = Integer.parseInt(words[TWO]);
					int clauses = Integer.parseInt(words[THREE]);

					satTHREE.setNumOfClauses(clauses);
					satTHREE.setNumOfVariables(variables);
				} else {
					throw new IOException(); //throw IOException if the input format is invalid
				}

				break; //break the loop as the preamble is finished
			} else {
				throw new IOException(); //throw the IOException for invalid input
			}

		}

		StringBuilder sb = new StringBuilder(); //string builder to store all input lines

		// read lines until it gets the end of the file
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}

		String str = sb.toString(); //convert the string builder to the string

		if (!str.isEmpty()) { //check whether the string is empty or not
			String[] literals = str.split("\\s+");

			Clause clause = new Clause();
			int counter = ZERO; //to check if a clause has more than THREE literals
			boolean hasEmptyClause = false; //to check if the THREE-SAT has an empty clause.

			for (int i = ZERO; i < literals.length; i++) {
				if (literals[i].equals(""))
					continue;

				//while convert a string to integer, NumberFormatException will be occurred if the current literal is not a number
				int literal = Integer.parseInt(literals[i]);

				// check if the literal is in a valid range
				if (Math.abs(literal) > satTHREE.getNumOfVariables())
					throw new IOException();

				if (literal != ZERO) { //check if the current literal is ZERO
					hasEmptyClause = false;

					counter += ONE;

					if (counter > THREE)
						throw new Exception(); //throw an exception if the clause of THREE-SAT has more than THREE literals

					Variable var = new Variable(literal);
					clause.appendVariable(var);
				} else {
					if (hasEmptyClause) {
						satTHREE.setHasEmptyClause(true);
					} else {
						hasEmptyClause = true;
					}

					counter = ZERO; //reset the counter value as the clause is terminated
					satTHREE.appendClause(clause); //append the clause to the list of clauses
					clause = new Clause();
				}
			}

			if (hasEmptyClause && satTHREE.getNumOfClauses() == ONE) {
				satTHREE.setHasEmptyClause(true);
			}

			satTHREE.appendClause(clause); //append the clause to the list of clauses
		}
	}

	/**
	 * Getter for satTHREE.
	 * @return the satTHREE
	 */
	public ThreeSAT getSatTHREE() {
		return satTHREE;
	}

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

		// if the first command line argument is just "test", then this program will execute the test mode
		if (inputFile.equals("test")) {

			/*
			 * With the test mode, this program will do the reduction multiple times to convert all
			 * given test input files.
			 *
			 * Everytime it finishes a single reduction, this program will write a new line to the
			 * csv file
			 */

			try {
				//clears file every time
				Writer output = new BufferedWriter(new FileWriter("threesattokcol.csv", true));

				for (int i = 0; i < listOfFiles.length; i++) {
					long startTime = System.currentTimeMillis(); //to count the run time

					ThreeSAT_To_KCol converter = new ThreeSAT_To_KCol(listOfFiles[i]);
					ThreeSAT sat3 = converter.getSatTHREE();

					int clauses = sat3.getNumOfClauses();
					int variables = sat3.getNumOfVariables();

					try {
						KCol kcol = sat3.convertToKCol();
						kcol.printKCol(listOfOutput[i]);
					} catch(Exception e) {
						e.printStackTrace();
						System.exit(1);
					}

					long endTime = System.currentTimeMillis(); //to count the run time
					long totalTime = endTime - startTime;

					output.append("\n" + clauses + "," + variables + "," + totalTime);
				}
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			ThreeSAT_To_KCol converter = new ThreeSAT_To_KCol(inputFile);
			ThreeSAT sat3 = converter.getSatTHREE();

			try {
				KCol kcol = sat3.convertToKCol();
				kcol.printKCol(outputFile);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		System.exit(0);
	}

}
