package ai;

public class Location {
	public final int row, col;
	private final String LOC_STRING;
	
	public Location(String locString) {
		LOC_STRING = locString;
		row = Board.SIZE - (locString.charAt(1) - '0');
		col = locString.charAt(0) - 'a';
	}
	
	public Location(int row, int col) {
		this.row = row;
		this.col = col;
		LOC_STRING = (char)('a' + col) + "" + (char)('0' + (Board.SIZE - row));
	}
	
	@Override
	public String toString() {
		return LOC_STRING;
	}
	
	@Override
	public int hashCode() {
		return row * 73 + col * 71;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean equals = false;
		if(o instanceof Location) {
			Location other = (Location) o;
			equals = row == other.row && col == other.col;
		}
		return equals;
	}
}
