package ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board{
	public static final int SIZE = 8;
	public static final char EMPTY = '-';
	private static final char[] row0 = {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};
	private static final char[] row1 = {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'};
	private static final char[] row6 = {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'};
	private static final char[] row7 = {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};
	public enum Color {white, black};
	private char[][] board = new char[SIZE][SIZE];	
	
	
	public Board() {
//		for(int row = 0; row < SIZE; row++) {
//			for(int col = 0; col < SIZE; col++) {
//				board[row][col] = EMPTY;
//			}
//		}
		board[0] = row0;
		board[1] = row1;
		board[6] = row6;
		board[7] = row7;
//		board[0][6] = 'r';
//		board[3][6] = 'K';
//		board[3][7] = 'p';
		for(int row = 2; row < 6; row++) {
			for(int col = 0; col < SIZE; col++) {
				board[row][col] = EMPTY;
			}
		}
	}
	
	public Board(Board boardToCopy) {
		for(int i = 0; i < SIZE; i++) {
			System.arraycopy(boardToCopy.board[i], 0, board[i], 0, boardToCopy.board[i].length);
		}
	}
	
	public Board(Board boardToCopy, Move move) {
		this(boardToCopy);
		makeMove(move);
	}
	
	public int getPieceValue(char piece) {
		char uPiece = Character.toUpperCase(piece);
		switch(uPiece) {
		case 'K':
			return 100;
		case 'Q':
			return 9;
		case 'R':
			return 5;
		case 'B':
		case 'N':
			return 3;
		case 'P':
			return 1;
		default:
			return 0;
		}
	}

	public List<Move> getAllValidMoves(Color color) {
		List<Move> validMoves = new ArrayList<Move>();
		List<Location> pieces = getColorLocations(color);
		for(Location from : pieces) {
			if(pieceAt(from) != EMPTY) {
				for(int row = 0; row < SIZE; row++) {
					for(int col = 0; col < SIZE; col++) {
						Location to = new Location(row, col);
						Move move = new Move(from, to);
						if(isValidMove(move)){
							validMoves.add(move);
						}
					}
				}
			}
		}
		removeCheckMoves(validMoves, color);
		return validMoves;
	}
	
	public void removeCheckMoves(List<Move> validMoves, Color color) {
		Iterator<Move> itr = validMoves.iterator();
		while(itr.hasNext()) {
			Move next = itr.next();
			Board testBoard = new Board(this, next);
			if(testBoard.isInCheck(color)) {
				itr.remove();
			}
		}
	}
	
	public Location getKing(Color color) {
		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE; col++) {
				Location loc = new Location(row, col);
				char piece = pieceAt(loc);
				if((piece == 'k' || piece == 'K') && getColor(piece) == color) { 
					return loc; 
				}
			}
		}
		return null;
	}

//	public boolean isInCheck(Color color) {
//		Location kingLoc = getKing(color);
//		boolean check = false;
//		for(int row = 0; row < SIZE && !check; row++) {
//			for(int col = 0; col < SIZE && !check; col++) {
//				Location testLoc = new Location(row, col);
//				Move testMove = new Move(testLoc, kingLoc);
//				if(isValidMove(testMove)) {
//					check = true;
//				}
//			}
//		}
//		return check;
//	}
	
	public boolean isInDanger(Color color, Location pieceLoc) {
		boolean check = false;
		List<Location> enemy = getColorLocations(
				color == Color.white ? Color.black : Color.white);
		for(Location loc : enemy) {
			Move testMove = new Move(loc, pieceLoc);
			if(isValidMove(testMove)) {
				check = true;
			}
		}
		return check;
	}
	
	public boolean isInCheck(Color color) {
		Location kingLoc = getKing(color);
		return isInDanger(color, kingLoc);
	}
	
	public void makeMove(Move move) {
		char piece = pieceAt(move.from);
		board[move.to.row][move.to.col] = piece;
		board[move.from.row][move.from.col] = EMPTY; 
	}
	
	public List<Location> getColorLocations(Color color) {
		List<Location> pieces = new ArrayList<Location>();
		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE; col++) {
				Location loc = new Location(row, col);
				char piece = pieceAt(loc);
				if(piece != EMPTY && getColor(piece) == color) {
					pieces.add(loc);
				}
			}
		}
		return pieces;
	}
	
	public char pieceAt(Location loc) {
		return board[loc.row][loc.col];
	}
	
	public boolean isValidMove(Move move) {
		boolean valid = false;
		char toMove = pieceAt(move.from);
		char toCapture = pieceAt(move.to);
		int rowDiff = move.to.row - move.from.row;
		int colDiff = move.to.col - move.from.col;
		Color color = getColor(toMove);
		char upperPiece = Character.toUpperCase(toMove);
		switch (upperPiece) {
		case 'Q':
			valid = validQueenMove(move, toCapture, rowDiff, colDiff, color);
			break;
		case 'K':
			valid = validKingMove(move, toCapture, rowDiff, colDiff, color);
			break;
		case 'R':
			valid = validRookMove(move, toCapture, rowDiff, colDiff, color);
			break;
		case 'N':
			valid = validKnightMove(move, toCapture, rowDiff, colDiff, color);
			break;
		case 'P':
			valid = validPawnMove(move, toCapture, rowDiff, colDiff, color);
			break;
		case 'B':
			valid = validBishopMove(move, toCapture, rowDiff, colDiff, color);
		}
		return valid;
	}
	
	public static Color getColor(char piece) {
		return Character.isUpperCase(piece) ? Color.white : Color.black;
	}
	
	private boolean isPathClear(Location from, Location to, int rowDiff, int colDiff) {
		boolean clear = true;
		int rowInc = (rowDiff == 0) ? 0 : rowDiff / Math.abs(rowDiff);
		int colInc = (colDiff == 0) ? 0 : colDiff / Math.abs(colDiff);
		Location currentLoc = new Location(from.row + rowInc, from.col + colInc);
		while(!currentLoc.equals(to) && clear) {
			if(pieceAt(currentLoc) != EMPTY) {
				clear = false;
			}
			currentLoc = new Location(currentLoc.row + rowInc, currentLoc.col + colInc);
		}
		return clear;
	}
	
	public boolean validPawnMove(Move move, char toCapture, int rowDiff, int colDiff, Color color) {
		if(Math.abs(rowDiff) == 1 || Math.abs(rowDiff) == 2) {
			//System.out.println("Debug");
		}
		int rowMovement = color == Color.white ? -1 : 1;
		boolean firstMove = move.from.row == (color == Color.white ? 6 : 1);
		boolean validRow = rowDiff == rowMovement || (rowDiff == (firstMove ? rowMovement * 2 : rowMovement)); 
		boolean validCapture = rowDiff == rowMovement && (colDiff == 1 || colDiff == -1) && toCapture != EMPTY && getColor(toCapture) != color;
		boolean validMove = colDiff == 0 && validRow && toCapture == EMPTY && (firstMove ? isPathClear(move.from, move.to, rowDiff, colDiff) : true); 
		return validCapture || validMove;
	}
	
	public boolean validKnightMove(Move move, char toCapture, int rowDiff, int colDiff, Color color) {
		rowDiff = Math.abs(rowDiff);
		colDiff = Math.abs(colDiff);
		return (rowDiff == 1 || rowDiff == 2) && 
				(colDiff == 1 || colDiff == 2) && 
				(rowDiff != colDiff) &&
				(toCapture == EMPTY || getColor(toCapture) != color);
	}
	
	public boolean validRookMove(Move move, char toCapture, int rowDiff, int colDiff, Color color) {
		return (rowDiff == 0 || colDiff == 0) && 
				!(rowDiff == 0 && colDiff == 0) && 
				isPathClear(move.from, move.to, rowDiff, colDiff) && 
				(toCapture == EMPTY || getColor(toCapture) != color);
	}

	public boolean validQueenMove(Move move, char toCapture, int rowDiff, int colDiff, Color color) {
		return ((rowDiff == 0 || colDiff == 0) || Math.abs(rowDiff) == Math.abs(colDiff)) &&  
				!(rowDiff == 0 && colDiff == 0) && 
				isPathClear(move.from, move.to, rowDiff, colDiff) && 
				(toCapture == EMPTY || getColor(toCapture) != color);
	}
	
	public boolean validBishopMove(Move move, char toCapture, int rowDiff,  int colDiff, Color color) {
		return (Math.abs(rowDiff) == Math.abs(colDiff)) &&  
				!(rowDiff == 0 && colDiff == 0) && 
				isPathClear(move.from, move.to, rowDiff, colDiff) && 
				(toCapture == EMPTY || getColor(toCapture) != color);
	}
	
	public boolean validKingMove(Move move, char toCapture, int rowDiff, int colDiff, Color color) {
		return (Math.abs(rowDiff) == 1 || rowDiff == 0) &&
				(Math.abs(colDiff) == 1 || colDiff == 0) &&
				!(colDiff == 0 && rowDiff == 0) &&
				(toCapture == EMPTY || getColor(toCapture) != color);
	}
}