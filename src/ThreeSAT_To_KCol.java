import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ThreeSAT_To_KCol {
	private ThreeSAT sat3;
	private BufferedReader br;
	private List comments;

	ThreeSAT_To_KCol(String fileName) {
		try {
			if (fileName != null) {
				br = new BufferedReader(new FileReader(new File(fileName)));
			} else {
				br = new BufferedReader(new InputStreamReader(System.in));
			}

			comments = new ArrayList<String>();

			// read the input file
			this.parseInput();

			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace(); //TODO
			System.exit(1);
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace(); //TODO
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace(); //TODO
			System.exit(1);
		}
	}

	private void parseInput() throws IOException, NumberFormatException, Exception {
		sat3 = new ThreeSAT();

		String line;

		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) { //check if the read line is empty
				continue;
			}

			String[] words = line.split("\\s+"); //split the line by whitespace characters

			if (words[0].equals("c")) 
				continue; //it is okay to skip the comment line
			else if (words[0].equals("p")) {
				//TODO
				if (words.length != 4) 
					throw new IOException();

				if (words[1].equals("cnf")) {
					/* words[1] = FORMAT, words[2] = VARIABLES, words[3] = CLAUSES */

					int variables = Integer.parseInt(words[2]);
					int clauses = Integer.parseInt(words[3]);

					sat3.setNumOfClauses(clauses);
					sat3.setNumOfVariables(variables);
				} else {
					throw new IOException();
				}

				break;
			} else {
				throw new IOException(); //throw the IOException for invalid input
			}

		}

		StringBuilder sb = new StringBuilder(); //string builder to store all input lines

		// read lines until it gets the end of the file
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String str = sb.toString();

		if (!str.isEmpty()) { //check whether the string is empty or not
			String[] literals = str.split("\\s+");

			Clause clause = new Clause();
			int counter = 0; //to check if a clause has more than 3 literals

			for (int i = 0; i < literals.length; i++) {
				//while convert a string to integer, NumberFormatException will be occurred if the current literal is not a number
				int literal = Integer.parseInt(literals[i]);

				if (literal != 0) {
					counter += 1;
					if (counter > 3)
						throw new Exception(); //throw an exception if the clause of 3-SAT has more than 3 literals

					Variable var = new Variable(literal);
					clause.appendVariable(var);
				} else {
					counter = 0; //reset the counter value as the clause is terminated
					sat3.appendClause(clause); //append the clause to the list of clauses
					clause = new Clause();
				}
			}

			sat3.appendClause(clause); //append the clause to the list of clauses
		}
	}

	/**
	 * Getter for sat3.
	 * @return the sat3
	 */
	public ThreeSAT getSat3() {
		return sat3;
	}

	/*
	 * TODO comments
	 */
	public static void main(String[] args) {
		ThreeSAT_To_KCol converter = new ThreeSAT_To_KCol("syntax_yes.cnf");
		ThreeSAT sat3 = converter.getSat3();
		sat3.printClauses();
		System.out.println("test");

		System.exit(0);
	}

}
