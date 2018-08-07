import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import management.*;

public class GameManagerTest {

	@Before
	public void setUp() {
		GameManager.initInstance();
	}

	@After
	public void tearDown() {
		GameManager.initInstance();
	}

	/**
	 * Test Chess960 for all pieces on the board
	 */
	@Test
	public void testChess960Pieces() {
		GameManager gameManager = GameManager.getInstance();
		gameManager.setMode(GameManager.CHESS960);
		gameManager.initPieces();

		Player currentPlayer = gameManager.getCurrentPlayer();
		assertEquals(19, currentPlayer.getPieces().size());
	}

	/**
	 * Test undoPreMovement
	 */
	@Test
	public void testBishop1ColumnPlacement() {
		GameManager gameManager = GameManager.getInstance();
		gameManager.setMode(GameManager.CHESS960);
		gameManager.initPieces();

		Player currentPlayer = gameManager.getCurrentPlayer();
		assertEquals(0, currentPlayer.getBishop1ColoumnLocation());
	}

	@Test
	public void testBishop2ColumnPlacement() {
		GameManager gameManager = GameManager.getInstance();
		gameManager.setMode(GameManager.CHESS960);
		gameManager.initPieces();

		Player currentPlayer = gameManager.getCurrentPlayer();
		assertEquals(7, currentPlayer.getBishop2ColoumnLocation());
	}
	@Test
	public void testRook1ColumnPlacement() {
		GameManager gameManager = GameManager.getInstance();
		gameManager.setMode(GameManager.CHESS960);
		gameManager.initPieces();

		Player currentPlayer = gameManager.getCurrentPlayer();

		assertTrue(currentPlayer.getRook1ColoumnLocation() < currentPlayer.getKingColoumnLocation() ||
		currentPlayer.getRook1ColoumnLocation() > currentPlayer.getKingColoumnLocation());
	}
	@Test
	public void testRook2ColumnPlacement() {
		GameManager gameManager = GameManager.getInstance();
		gameManager.setMode(GameManager.CHESS960);
		gameManager.initPieces();

		Player currentPlayer = gameManager.getCurrentPlayer();
		assertTrue(currentPlayer.getRook2ColoumnLocation() < currentPlayer.getKingColoumnLocation() ||
				currentPlayer.getRook2ColoumnLocation() > currentPlayer.getKingColoumnLocation());
	}
}