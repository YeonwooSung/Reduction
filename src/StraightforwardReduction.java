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
	private List<String> comments; //list to store comments
	private KCol kcol;
	private SAT sat;

	StraightforwardReduction(String name) {
		try {
			if (name == null) {
				br = new BufferedReader(new InputStreamReader(System.in));
			} else {
				br = new BufferedReader(new FileReader(new File(name)));
			}

			comments = new ArrayList<String>();

			//read the input file
			this.readAndParse();

			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// error handling -> file not found
			System.exit(1);
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			// error handling -> invalid input
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
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
		int numOfReadLine = 0;
		boolean isCNF = false;
		boolean shouldBeEmpty = false;

		String line;

		while ((line = br.readLine()) != null) {
			//String line = br.readLine();

			// check whether the read line is supposed to be a preamble or not
			if (preamble) {

				// Check the first character of the line
				if (line.startsWith("c")) {

					comments.add(line); //add comment line to the list of comments

				} else if (line.startsWith("p")) {
					String[] words = line.split("\\s+"); //split the line by whitespace characters

					if (words.length != 4) 
						throw new IOException();
					if (words[1].contains("edge")) {
						/* words[1] = FORMAT, words[2] = NODES, words[3] = EDGES */

						//NumberFormatException might be occurred if the word is not an integer
						int nodes = Integer.parseInt(words[2]);
						int edges = Integer.parseInt(words[3]);

						numOfReadLine = edges;
						kcol.setNumOfEdges(edges);
						kcol.setNumOfNodes(nodes);

						// read line to know the number of colours
						String colourLine = br.readLine();

						if (colourLine.startsWith("colours")) {
							String[] colourArray = colourLine.split("\\s+");
							int numOfColour = Integer.parseInt(colourArray[1]);

							kcol.setNumOfCol(numOfColour);
						} else {
							throw new IOException();
						}

					} else if (words[1].equals("cnf")) {
						/* words[1] = FORMAT, words[2] = VARIABLES, words[3] = CLAUSES */

						isCNF = true;
						int variables = Integer.parseInt(words[2]);
						int clauses = Integer.parseInt(words[3]);
						numOfReadLine = clauses - 1;

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
					if (line.equals("")) {
						continue;
					}

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

					// use for loop to iterate all literals
					for (int i = 0; i < literals.length; i++) {
						String literal = literals[i];

						if (literal.equals("0")) {

							//check if the clause is empty
							if (clause.countVariables() != 0) {
								clause.appendVariable(null); //TODO NULL
								sat.appendClause(clause);
								clause = new Clause();
							}

							if (i != literals.length - 1)
								numOfReadLine -= 1;
						} else {
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

					sat.appendClause(clause);

				} else { //input file is an edge type.

					String[] words = line.split("\\s+");

					if (words.length != 3) 
						throw new IOException();

					//check if this line is an edge descriptor
					if (words[0].equals("e")) {
						// edge descriptor -> "e W V"  (W and V specify the end points of the edge)

						numOfReadLine -= 1;

						int startpoint = Integer.parseInt(words[1]);
						int endpoint = Integer.parseInt(words[2]);

						String tuple;

						if (Math.max(startpoint, endpoint) != startpoint) {
							//startpoint < endpoint
							tuple = startpoint + "," + endpoint;
						} else {
							//endpoint < startpoint
							tuple = endpoint + "," + startpoint;
						}

						if (!kcol.hasEndpoints(tuple)) 
							throw new IOException();

						kcol.addEndpointTuple(tuple);

						Edge edge = new Edge();
						edge.setEndPoints(startpoint, endpoint);

						kcol.appendEdge(edge);

					} else if (words[0].equals("n")) { //check if this line is a node descriptor
						//node descriptor -> "n ID VALUE"

						Node node = new Node();
						int id = Integer.parseInt(words[1]);

						if (!kcol.hasID(id)) {
							throw new IOException();
						}

						node.setId(id);
						node.setValue(Integer.parseInt(words[2]));

						kcol.appendNode(node);

					} else {
						throw new IOException();
					}
				}
			}

		} //while loop ends

		if (numOfReadLine != 0 && !shouldBeEmpty) {
			System.out.println(numOfReadLine); //TODO need to remove this later
			System.exit(3);
		} else if (shouldBeEmpty) {
			//TODO solution1.cnf -> ok because there are no clauses at all
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


	/*
	 * TODO comment!
	 */
	public static void main(String[] args) {
//		StraightforwardReduction reduction1 = new StraightforwardReduction("test_ok.cnf");
//
//		System.out.println("yes");
//
//		SAT sat = reduction1.getSAT();
//		sat.printClauses();
//
//		System.out.println("yes");
//
//		ThreeSAT sat3 = reduction1.convertSAT_to_3SAT();
//		System.out.println("haha");
//
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
