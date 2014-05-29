package ai;
import java.util.List;
import java.util.Scanner;
import ai.Board.Color;

public class RandomAI {
	private static final Scanner SCAN = new Scanner(System.in);
	private Board board;
	private Color color;
	
	public RandomAI(Board board, Color color) {
		this.board = board;
		this.color = color;
	}
	
	public void makeMove() {
		List<Move> validMoves = board.getAllValidMoves(color);
		Move randomMove = validMoves.get((int)(Math.random() * validMoves.size()));
		board.makeMove(randomMove);
		System.out.println(randomMove);
		
	}
	
	public void nextMove() {
		String line = SCAN.nextLine();
		line = line.trim();
		line = line.toLowerCase();
		Move move = new Move( new Location(line.substring(0, 2)), new Location(line.substring(3)));
		board.makeMove(move);
	}

	public static void main(String[] args) {
		String colorString = args[0];
	
		Color color = colorString.equals("white") ? Color.white : Color.black;
		Board board = new Board();
		RandomAI randomAI = new RandomAI(board, color);
	
		if(color == Color.black)
			randomAI.nextMove();
		while (true) {
			randomAI.makeMove();
			randomAI.nextMove();
//			for(Move move : validMoves) { 
//				System.out.println(move);
//			}
		}
	}
}
