package management;

import board.Chessboard;
import board.Location;
import com.sun.deploy.util.ArrayUtil;
import pieces.*;
import java.lang.Object;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
/*
Test Bishop Positions
Test if King is between Rooks

 */

/**
 * GameManger initiate and manage all information related the game
 *
 * @author Xuefeng Zhu
 *
 */
public class GameManager {
	public static final String CLASSIC = "Classic";
	public static final String CUSTOM = "Custom";
	public static final String CHESS960 = "Chess 960";

	public static final String CHECKMATE = "checkmate";
	public static final String STALEMATE = "stalemate";
	public static final String INPROGRESS = "in progress";

	private static GameManager gameManger = null;

	private Player playerW;
	private Player playerB;
	private Player currentPlayer;
	private Movement preMovement = null; // store previous player's movement
	private String mode;
	private String status;

	/**
	 * Construct a new GameManager and initialize players and chessboard
	 */
	protected GameManager() {
		playerW = new Player(Piece.WHITE);
		playerB = new Player(Piece.BLACK);
		currentPlayer = playerW;
		mode = CLASSIC;
		status = INPROGRESS;

		Chessboard.initInstance(8, 8);
	}

	/**
	 * Initialize the GameManager
	 *
	 * @return
	 */
	public static GameManager initInstance() {
		gameManger = new GameManager();
		return gameManger;
	}

	/**
	 * Get the static instance of Gamemanager
	 *
	 * @return
	 */
	public static GameManager getInstance() {
		if (gameManger == null) {
			gameManger = new GameManager();
		}
		return gameManger;
	}

	/**
	 * Helper function to be called when game restarts
	 */
	public void restart() {
		playerW.restart();
		playerB.restart();
		currentPlayer = playerW;
		status = INPROGRESS;

		Chessboard.initInstance(8, 8);
		initPieces();
	}

	/**
	 * Initilize all pieces and place them on the board
	 */
	public void initPieces() {

		if( mode == CLASSIC) {
			initPiecesHelper(playerW, 7, 6);
			initPiecesHelper(playerB, 0, 1);
		}
		else if (mode == CUSTOM) {
			initCustomPiecesHelper(playerW, 4);
			initCustomPiecesHelper(playerB, 3);
		}
		else if ( mode == CHESS960) {
			Chess960PiecesHelper(playerW, 7, 6);
			Chess960PiecesHelper(playerB, 0, 1);
		}
		updateGameStat();
	}

	/**
	 * Helper function for initPeces
	 *
	 * @param player
	 * @param rearRow
	 *            the index of rear row of the player
	 * @param frontRow
	 *            the index of front row of the player
	 */
	private void initPiecesHelper(Player player, int rearRow, int frontRow) {
		Chessboard chessBoard = Chessboard.getInstance();

		Location location = chessBoard.getLocatoin(rearRow, 0);
		new Rook(location, player);

		location = chessBoard.getLocatoin(rearRow, 7);
		new Rook(location, player);


		location = chessBoard.getLocatoin(rearRow, 1);
		new Knight(location, player);

		location = chessBoard.getLocatoin(rearRow, 6);
		new Knight(location, player);

		location = chessBoard.getLocatoin(rearRow, 2);
		new Bishop(location, player);

		location = chessBoard.getLocatoin(rearRow, 5);
		new Bishop(location, player);

		location = chessBoard.getLocatoin(rearRow, 3);
		new Queen(location, player);

		location = chessBoard.getLocatoin(rearRow, 4);
		new King(location, player);

		for (int col = 0; col < chessBoard.getWidth(); col++) {
			location = chessBoard.getLocatoin(frontRow, col);
			new Pawn(location, player);
		}

	}


	/**
	 * Helper function for initializing custom pieces
	 * @param player
	 * @param row
	 */
	private void initCustomPiecesHelper(Player player, int row) {
		Chessboard chessBoard = Chessboard.getInstance();

		Location location = chessBoard.getLocatoin(row, 0);
		new Ferz(location, player);

		location = chessBoard.getLocatoin(row, 7);
		new Ferz(location, player);

		location = chessBoard.getLocatoin(row, 3);
		new Nightrider(location, player);

		location = chessBoard.getLocatoin(row, 4);
		new Nightrider(location, player);
	}

	private void Chess960PiecesHelper(Player player, int rearRow, int frontRow) {

		Location location = null;
		Chessboard chessBoard = Chessboard.getInstance();
		Random rand = new Random();
		List<Integer> list = new ArrayList<Integer>();

		int[] ar = {1, 2, 3, 4, 5, 6};
		boolean keepGoing = true;
		for (int i = ar.length - 1; i > -1; i--) {
			int index = rand.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
		for (int i = 0; i < ar.length; i++) {
			list.add(ar[i]);
		}
		int king = 0;
		int ro1 = 0;
		int ro2 = 0;
		int que = 0;
		int kni1 = 0;
		int kni2 = 0;
		int randomNum = ThreadLocalRandom.current().nextInt(2, 5);
		for (int i = 0; i < ar.length; i++) {
			if (randomNum == ar[i]) {
				king = randomNum;
				location = chessBoard.getLocatoin(rearRow, king);
				new King(location, player);
				King king1 = new King(location, player);
				player.setKingColoumnLocation(king1);
			}
		}
		while (keepGoing) {
			int num1 = ThreadLocalRandom.current().nextInt(1, 6);
			int num2 = ThreadLocalRandom.current().nextInt(1, 6);
			if (num1 != num2 && num1 > king && num2 < king) {
				ro1 = num1;
				ro2 = num2;
				location = chessBoard.getLocatoin(rearRow, ro1);
				new Rook(location, player);
				Rook rook1 = new Rook(location, player);
				player.setRook1ColoumnLocation(rook1);

				location = chessBoard.getLocatoin(rearRow, ro2);
				new Rook(location, player);
				Rook rook2 = new Rook(location, player);
				player.setRook2ColoumnLocation(rook2);

				keepGoing = false;
			}

		}
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != ro1 && ar[i] != ro2 && ar[i] != king) {
				que = ar[i];
				location = chessBoard.getLocatoin(rearRow, que);
				new Queen(location, player);
				i = ar.length;
			}
		}
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != ro1 && ar[i] != ro2 && ar[i] != king && ar[i]!= que) {
				kni1 = ar[i];
				location = chessBoard.getLocatoin(rearRow, kni1);
				new Knight(location, player);
				i = ar.length;
			}
		}
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != ro1 && ar[i] != ro2 && ar[i] != king && ar[i] != kni1 && ar[i] != que) {
				kni2 = ar[i];
				location = chessBoard.getLocatoin(rearRow, kni2);
				new Knight(location, player);
				i = ar.length;
			}
		}

		location = chessBoard.getLocatoin(rearRow, 0);
		new Bishop(location, player);
		Bishop bishop1 = new Bishop(location, player);
		player.setBishop1ColoumnLocation(bishop1);

		location = chessBoard.getLocatoin(rearRow, 7);
		new Bishop(location, player);
		Bishop bishop2 = new Bishop(location, player);
		player.setBishop2ColoumnLocation(bishop2);



		for (int col = 0; col < chessBoard.getWidth(); col++) {
			location = chessBoard.getLocatoin(frontRow, col);
			new Pawn(location, player);
		}

	}

	/**
	 * @return preMovement
	 */
	public Movement getPreMovement() {
		return preMovement;
	}

	/**
	 * Set the preMovement to movement
	 *
	 * @param movement
	 */
	public void setPreMovement(Movement movement) {
		preMovement = movement;
	}

	/**
	 * @return mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * Set the mode to the specific mode
	 *
	 * @param mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Switch to another player
	 *
	 * @return next player
	 */
	public Player switchPlayer() {
		if (currentPlayer == playerW) {
			currentPlayer = playerB;
		} else {
			currentPlayer = playerW;
		}
		return currentPlayer;
	}

	/**
	 * Get the opponent of the player
	 *
	 * @param player
	 * @return opponent Player
	 */
	public Player getOpponent(Player player) {
		if (player == playerW) {
			return playerB;
		} else {
			return playerW;
		}
	}

	/**
	 * Check if current player's movement is valid (not causing king check)
	 * Update opponent statistics to see if opponent is checkmate
	 *
	 * @return true if everything goes well, false if the previous movement if
	 *         not valid
	 */
	public boolean updateGameStat() {
		Player opponent = getOpponent(currentPlayer);
		opponent.updateStat();
		if (currentPlayer.isChecked) {
			return false;
		}

		currentPlayer.updateStat();
		if (opponent.isCheckmate()) {
			status = CHECKMATE;
		}
		if (opponent.isStalemate()) {
			status = STALEMATE;
		}

		return true;
	}

	/**
	 * Undo the previous movement
	 */
	public void undoPreMovement() {
		preMovement.piece.undoMove(preMovement.preLocation);

		// check if the previous movement captured enemy piece
		if (preMovement.capturedPiece != null) {
			Location location = preMovement.capturedPiece.getLocation();
			Chessboard chessboard = Chessboard.getInstance();
			chessboard.addPiece(preMovement.capturedPiece, location);
			Player player = preMovement.capturedPiece.getPlayer();
			player.addPiece(preMovement.capturedPiece);
		}

		preMovement = null;
	}
}
