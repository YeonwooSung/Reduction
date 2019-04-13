
public class Node {

	private int id;
	private int value;

	Node() {
		this.setValue(1);
	}

	Node(int id, int value) {
		this.setId(id);
		this.setValue(value);
	}

	/**
	 * Getter for id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter for id.
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter for value.
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Setter for value.
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
