package ai;

public class Move implements Comparable<Move> {
	public final Location from, to;
	private int value = 0;
	
	public Move(Location from, Location to) {
		this.from = from;
		this.to = to;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return from + " " + to;
	}

	@Override
	public int compareTo(Move m2) {
		return value - m2.value;
	}
}
