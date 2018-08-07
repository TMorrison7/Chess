package management;

import java.util.HashSet;

import board.Chessboard;
import board.Location;
import pieces.*;

/**
 * Player stores information of a player, such as all the pieces it has, it
 * king, and if it is checked
 *
 * @author Xuefeng Zhu
 *
 */
public class Player {
	private String color;
	private King king;
	private Bishop bishop1;
	private Bishop bishop2;
	private Rook rook1;
	private Rook rook2;
	private HashSet<Piece> pieces; // pieces the player has
	private Piece checkPiece; // the opponent piece checks the king

	public Boolean isChecked; // if the king is currently checked
	public String name;
	public int score;

	/**
	 * Construct a Player with specific color
	 *
	 * @param color
	 */
	public Player(String color) {
		this.color = color;
		this.isChecked = false;
		this.pieces = new HashSet<Piece>();
		this.name = "";
		this.score = 0;
	}

	/**
	 * Helper function to be called when game restarts
	 */
	public void restart() {
		this.isChecked = false;
		this.pieces = new HashSet<Piece>();
	}

	/**
	 * @return color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return king
	 */
	public King getKing() {
		return king;
	}

	public int getKingColoumnLocation() { return king.getLocation().getCol(); }

	public int getBishop1ColoumnLocation() { return bishop1.getLocation().getCol(); }

	public int getBishop2ColoumnLocation() { return bishop2.getLocation().getCol(); }

	public int getRook1ColoumnLocation() { return rook1.getLocation().getCol(); }

	public int getRook2ColoumnLocation() { return rook2.getLocation().getCol(); }

	public void setKingColoumnLocation(King king) { this.king = king; }

	public void setBishop1ColoumnLocation(Bishop bishop1) { this.bishop1 = bishop1; }

	public void setBishop2ColoumnLocation(Bishop bishop2) { this.bishop2 = bishop2; }

	public void setRook1ColoumnLocation(Rook rook1) { this.rook1 = rook1; }

	public void setRook2ColoumnLocation(Rook rook2) { this.rook2 = rook2; }


	/**
	 * @return pieces
	 */
	public HashSet<Piece> getPieces() {
		return pieces;
	}

	/**
	 * Add the piece into player' pieces set
	 *
	 * @param piece
	 */
	public void addPiece(Piece piece) {
		if (piece instanceof King) {
			king = (King) piece;
		} else {
			pieces.add(piece);
		}
	}

	/**
	 * Remove the piece from players' pieces set
	 *
	 * @param piece
	 */
	public void removePiece(Piece piece) {
		pieces.remove(piece);
	}

	/**
	 * Update the pieces's available locations, and check if opponent is checked
	 */
	public void updateStat() {
		GameManager gameManager = GameManager.getInstance();
		Player opponent = gameManager.getOpponent(this);
		opponent.isChecked = false;

		for (Piece piece : pieces) {
			HashSet<Location> availableLocations = piece
					.updateAvaiableLocations();

			if (availableLocations.contains(opponent.king.getLocation())) {
				opponent.isChecked = true;
				opponent.checkPiece = piece;
			}
		}

		HashSet<Location> availableLocations = king.updateAvaiableLocations();
		if (availableLocations.contains(opponent.king.getLocation())) {
			opponent.isChecked = true;
			opponent.checkPiece = king;
		}
	}

	/**
	 * Check if the player is at checkmate stage
	 *
	 * @return true if checkmate
	 */
	public boolean isCheckmate() {
		if (!isChecked) {
			return false;
		}

		// check if the king has locations to move
		HashSet<Location> availableLocations = king.getAvailableLocations();
		if (availableLocations.size() > 0) {
			return false;
		}

		// check if there is piece to capture the piece checks the king
		for (Piece piece : pieces) {
			availableLocations = piece.getAvailableLocations();
			if (availableLocations.contains(checkPiece.getLocation())) {
				return false;
			}
		}

		// check if there piece to move to the location between king and
		// opponent's piece to prevent check
		Chessboard chessBoard = Chessboard.getInstance();
		HashSet<Location> pathLocations = chessBoard.getPath(
				checkPiece.getLocation(), king.getLocation());
		for (Location pathLocation : pathLocations) {
			for (Piece piece : pieces) {
				availableLocations = piece.getAvailableLocations();
				if (availableLocations.contains(pathLocation)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Check if the player is at the stalemate state
	 *
	 * @return true if it is
	 */
	public boolean isStalemate() {
		if (isChecked) {
			return false;
		}

		// check if the king has locations to move
		HashSet<Location> availableLocations = king.getAvailableLocations();
		if (availableLocations.size() > 0) {
			return false;
		}

		// check if there is piece to move
		for (Piece piece : pieces) {
			availableLocations = piece.getAvailableLocations();
			if (availableLocations.size() > 0) {
				return false;
			}
		}

		return true;
	}

}
