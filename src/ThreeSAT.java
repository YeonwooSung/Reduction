import java.util.ArrayList;
import java.util.List;


public class ThreeSAT {

	private List<Clause> clauses;

	ThreeSAT() {
		clauses = new ArrayList<Clause>();
	}

	public void appendClause(Clause clause) {
		clauses.add(clause);
	}

}
