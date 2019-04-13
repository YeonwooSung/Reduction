import java.util.ArrayList;


public class Combination {

	/**
	 * The aim of this method is to make the combinations for the Reed Muller code.
	 * @param n for n_C_2
	 * @return the array list that contains the all combinations.
	 */
	static ArrayList<ArrayList<Integer>> makeCombinition(int n) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

		if (n < 0 || n < 2) { //check if the n is in the correct range
			return result; //if not, return the empty list
		}

		ArrayList<Integer> item = new ArrayList<Integer>();
		findCombinations(n, 2, 1, item, result);

		return result;
	}

	/**
	 * Find all combinations and add them in the array list.
	 * @param n for n_C_k
	 * @param k for n_C_k
	 * @param start the starting number
	 * @param item the array list for the combination
	 * @param res the array list that contains all combinations (the result of this method)
	 */
	static void findCombinations(int n, int k, int start, ArrayList<Integer> item, ArrayList<ArrayList<Integer>> res) {
		if (item.size() == k) {
			res.add(new ArrayList<Integer>(item));
			return;
		}

		for (int i = start; i <= n; i++) {
			item.add(i);
			findCombinations(n, k, i + 1, item, res);
			item.remove(item.size() - 1);
		}
	}
}
