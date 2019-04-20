import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class StraightforwardReduction {
	private BufferedReader br;
	private KCol kcol;
	private SAT sat;

	private final int ZERO = 0;
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
	private final int FOUR = 4;

	StraightforwardReduction(String name) {
		try {
			if (name == null) {
				br = new BufferedReader(new InputStreamReader(System.in));
			} else {
				br = new BufferedReader(new FileReader(new File(name)));
			}

			//read the input file
			this.readAndParse();

			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace(); //TODO
			// error handling -> file not found
			System.exit(ONE);
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			// error handling -> invalid input
			System.exit(ONE);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ONE);
		}
	}

	/**
	 * This method reads and parses the file to generate k-col instance.
	 * @throws IOException could be occurred while reading the input file.
	 */
	private void readAndParse() throws NumberFormatException, IOException {
		kcol = new KCol();
		sat = new SAT();

		boolean preamble = true;
		int numOfLineToRead = 0;
		boolean isCNF = false;
		boolean shouldBeEmpty = false;

		String line;

		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) 
				continue;

			// check whether the read line is supposed to be a preamble or not
			if (preamble) {

				// Check the first character of the line
				if (line.startsWith("c")) {
					continue; //if the first character is c, continue the loop as we don't need to care about comment line
				} else if (line.startsWith("p")) {
					String[] words = line.split("\\s+"); //split the line by whitespace characters

					if (words.length != FOUR) 
						throw new IOException();
					if (words[ONE].contains("edge")) {
						/* words[1] = FORMAT, words[2] = NODES, words[3] = EDGES */

						//NumberFormatException might be occurred if the word is not an integer
						int nodes = Integer.parseInt(words[TWO]);
						int edges = Integer.parseInt(words[THREE]);

						numOfLineToRead = edges;
						kcol.setNumOfEdges(edges);
						kcol.setNumOfNodes(nodes);

						// read line to know the number of colours
						String colourLine = br.readLine();

						if (colourLine.startsWith("colours")) {
							String[] colourArray = colourLine.split("\\s+");
							int numOfColour = Integer.parseInt(colourArray[ONE]);

							kcol.setNumOfCol(numOfColour);
						} else {
							throw new IOException();
						}

					} else if (words[ONE].equals("cnf")) {
						/* words[1] = FORMAT, words[2] = VARIABLES, words[3] = CLAUSES */

						isCNF = true;
						int variables = Integer.parseInt(words[TWO]);
						int clauses = Integer.parseInt(words[THREE]);
						numOfLineToRead = clauses;

						//check if this is an empty clause
						if (clauses == 0)
							shouldBeEmpty = true;

						sat.setNumOfClauses(clauses);
						sat.setNumOfVariables(variables);
					} else {
						throw new IOException(); //invalid input format
					}

					preamble = false; //preamble ends

				} else {

					String[] splitted = line.split("\\s+");

					// use for loop to check all words in the line
					for (int i = 0; i < splitted.length; i++) {
						if (!splitted[i].isEmpty()) { //check whether this line is empty or not
							throw new IOException(); //invalid file format
						}
					}

				}

			} else {

				// check if the input file is a cnf type
				if (isCNF) {

					//parse the line to build clause instances.
					StringBuilder sb = new StringBuilder();
					sb.append(line);

					// use while loop to read until it gets the end of the file
					while ((line = br.readLine()) != null) {
						sb.append("\n");
						sb.append(line);
					}

					String[] literals = sb.toString().split("\\s+"); //split by whitespace characters
					Clause clause = new Clause();
					boolean hasEmptyClause = false; //to check if the SAT has an empty clause.

					// use for loop to iterate all literals
					for (int i = 0; i < literals.length; i++) {
						String literal = literals[i];

						if (literal.equals("0")) {
							if (hasEmptyClause) {
								sat.setHasEmptyClause(true);
							} else {
								hasEmptyClause = true;
							}

							//check if the clause is empty
							if (clause.countVariables() != 0) {
								clause.appendVariable(null); //TODO NULL
								sat.appendClause(clause);
								clause = new Clause();
							}

							if (i != literals.length - ONE)
								numOfLineToRead -= ONE;
						} else {
							hasEmptyClause = false;
							int l = Integer.parseInt(literal);

							// check if the literal is in the valid range
							if (Math.abs(l) > sat.getNumOfVariables())
								throw new IOException();
							// check if there is any clause in the file should have 0 clauses.
							if (shouldBeEmpty)
								throw new IOException();

							Variable var = new Variable(l);
							clause.appendVariable(var);
						}
					}

					if (hasEmptyClause && sat.getNumOfClauses() == ONE) {
						sat.setHasEmptyClause(true);
					}

					sat.appendClause(clause);

				} else { //input file is an edge type.

					//check if the read line is a comment line
					if (line.startsWith("c ")) 
						continue;

					String[] words = line.split("\\s+");

					// if the read line is a valid input, then it should have THREE tokens: 'e for edge and TWO end points of the edge'
					if (words.length != THREE) 
						throw new IOException();

					//check if this line is an edge descriptor
					if (words[0].equals("e")) {
						// edge descriptor -> "e W V"  (W and V specify the end points of the edge)

						numOfLineToRead -= ONE;

						int startpoint = Integer.parseInt(words[ONE]);
						int endpoint = Integer.parseInt(words[TWO]);

						// we cannot have negative numbers in edge
						if (startpoint < ONE || endpoint < ONE)
							throw new IOException();

						String tuple;

						if (Math.max(startpoint, endpoint) != startpoint) {
							//startpoint < endpoint
							tuple = startpoint + "," + endpoint;
						} else {
							//endpoint < startpoint
							tuple = endpoint + "," + startpoint;
						}

						if (!kcol.hasEndpoints(tuple)) //check if the kcol instance already has this edge
							throw new IOException();

						kcol.addEndpointTuple(tuple);

						Edge edge = new Edge();
						edge.setEndPoints(startpoint, endpoint);

						kcol.appendEdge(edge);

					} else if (words[0].equals("n")) { //check if this line is a node descriptor
						//node descriptor -> "n ID VALUE"

						Node node = new Node();
						int id = Integer.parseInt(words[ONE]);

						if (!kcol.hasID(id)) { //check if this node is duplicated
							throw new IOException();
						}

						node.setId(id);
						node.setValue(Integer.parseInt(words[TWO]));

						kcol.appendNode(node);

					} else {
						throw new IOException();
					}
				}
			}

		} //while loop ends

		if (isCNF) {
			//TODO
		} else {
			if (numOfLineToRead != 0 && !shouldBeEmpty) {
				System.exit(ONE);
			}
		}

		if (!isCNF) {
			if (!kcol.checkNumOfEdges()) {
				throw new IOException();
			}

			kcol.validateNodes(); //add nodes that are not appended to the KCol instance yet.
		}

	}

	/**
	 * Getter for SAT instance.
	 * @return sat
	 */
	public SAT getSAT() {
		return sat;
	}

	/**
	 * Getter for KCol instance.
	 * @return kcol
	 */
	public KCol getKCol() {
		return kcol;
	}

}
