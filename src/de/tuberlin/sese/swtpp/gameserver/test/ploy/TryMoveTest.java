package de.tuberlin.sese.swtpp.gameserver.test.ploy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.ploy.PloyGame;

public class TryMoveTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	PloyGame game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "", "ploy");
		
		game = (PloyGame) controller.getGame(gameID);
		blackPlayer = game.getPlayer(user1);

	}
	
	public void startGame(String initialBoard, boolean whiteNext) {
		controller.joinGame(user2, "ploy");		
		whitePlayer = game.getPlayer(user2);
		
		game.setBoard(initialBoard);
		game.setNextPlayer(whiteNext? whitePlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean white, boolean expectedResult) {
		if (white)
			assertEquals(expectedResult, game.tryMove(move, whitePlayer));
		else 
			assertEquals(expectedResult,game.tryMove(move, blackPlayer));
	}
	
	public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
		String board = game.getBoard().replaceAll("e", "");
		
		assertEquals(expectedBoard,board);
		assertEquals(finished, game.isFinished());

		if (!game.isFinished()) {
			assertEquals(whiteNext, game.isWhiteNext());
		} else {
			assertEquals(whiteWon, whitePlayer.isWinner());
			assertEquals(!whiteWon, blackPlayer.isWinner());
		}
	}
	

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
	@Test
	public void exampleTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e3-e4-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}

	//TODO: implement test cases of same kind as example here
	
	// checks format - should not work
	@Test
	public void formatTestNr1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d3d4-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// checks format - should not work
	@Test
	public void formatTestNr2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("0-d3-d4",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	@Test
	public void notMyTurnTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("e3-e4-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	@Test
	public void notMyTurnTest2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e7-e6-0",true,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	
	@Test
	public void notMyFigureTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("e3-e4-0",true,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// checks Shield - should work
	@Test
	public void shieldTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("e7-e6-0",true,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,,w16,,,/,,,,w16,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// checks Shield - should not work
	@Test
	public void shieldTest2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,,w16,,,/,,,,w6,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d3-c3-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,,w16,,,/,,,,w6,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// checks Probe - should work
	@Test
	public void probeTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d2-c3-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,b130,b1,b1,b1,,,/,,b3,,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// checks Probe - should not work
	@Test
	public void probeTest2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d2-d3-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// checks Lance - should work
	@Test
	public void lanceTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("b1-b4-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,b69,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,,b146,b131,b170,b131,b146,b69,",true,false,false);
	}

	// checks Lance - should not work
	@Test
	public void lanceTest2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("b1-c2-0",true,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// checks Commander - should work
	@Test
	public void comTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e1-e1-1",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b85,b131,b146,b69,",true,false,false);
	}
	
	// checks Commander - should not work
	@Test
	public void comTest2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e1-e2-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// checks if black player wins
	@Test
	public void blackPlayerWinsTest() {
		startGame(",w84,w41,w56,,w56,w41,w84,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,w170,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e3-e4-0",false,true);
		assertGameState(",w84,w41,w56,,w56,w41,w84,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,true,false);
	}
	
	// checks if white player wins
	@Test
	public void whitePlayerWinsTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,w16,,,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("e2-e1-0",true,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,b69,b146,b131,w16,b131,b146,b69,",false,true,true);
	}
	
	// rotation number too high 
	@Test
	public void tooHighRotationValueTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d3-d4-8",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// rotation number too low
	@Test
	public void tooLowRotationValueTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("d3-d4--1",true,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// no moving and no rotation - should not work
	@Test
	public void frozenFigureTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d3-d3-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// horizontal-left-movement
	@Test
	public void horizontalLeftTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("b1-a1-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/b69,,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// horizontal-right-movement
	@Test
	public void horizontalRightTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("h9-i9-0",true,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,,w84/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// vertical-up-movement
	@Test
	public void verticalUpTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("c2-c4-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,b3,,,,,,/,,,b1,b1,b1,,,/,,,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// vertical-down-movement
	@Test
	public void verticalDownTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("d7-d6-0",true,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,,w16,w16,,,/,,,w16,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// diagonal-left-movement
	@Test
	public void diagLeftTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d2-b4-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,b130,,,,,,,/,,,b1,b1,b1,,,/,,b3,,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// diagonal-right-movement
	@Test
	public void diagRightTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true);
		assertMove("c9-a7-0",true,true);
		assertGameState(",w84,,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/w41,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// only-Commander-movement
	@Test
	public void onlyOneCommanderTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,w16,,,,,/,,,b131,b170,,,,",true);
		assertMove("d2-d1-0",true,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,w16,b170,,,,",false,true,true);
	}
	
	// only-Commander-movement
	@Test
	public void onlyOneCommanderTest2() {
		startGame(",,,,,,,,/,,,,,,,,/,,,w170,w16,,,,/,,,,b1,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b131,b170,,,,",false);
		assertMove("e6-e7-0",false,true);
		assertGameState(",,,,,,,,/,,,,,,,,/,,,w170,b1,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b131,b170,,,,",true,true,false);
	}
	
	// moving and eating-movement
	@Test
	public void movingAndEatingTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,b1,,,,,/,,,,,,,,/,,,,,,,,/,,,,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("d6-d7-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,b1,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
		
	
	// pick false-movement
	@Test
	public void pickEmptyFigureTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("a1-a2-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// moving and rotation-movement - should not work
	@Test
	public void rotationNotShieldTest1() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("b1-b3-2",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	// rotation-movement - should work
	@Test
	public void rotationNotShieldTest2() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("b1-b1-2",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b21,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	// moving and rotation(Shield)-movement - should work
	@Test
	public void rotationShieldTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e3-e4-1",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,b2,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	@Test
	public void notFoundDirectionTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b0,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("e3-e4-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b0,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	@Test
	public void commanderMovingTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,w16,,,,,/,,,b131,b170,,,,",false);
		assertMove("e1-f2-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,w16,,b170,,,/,,,b131,,,,,",true,false,false);
	}
	
	@Test
	public void canNotMovingTest() {
		startGame(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b0,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false);
		assertMove("f1-h3-0",false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b0,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
	
	@Test
	public void setBoardTest1() {
		startGame("",false);
		assertMove("e3-e4-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	@Test
	public void setBoardTest2() {
		startGame(null,false);
		assertMove("e3-e4-0",false,true);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,b1,,,,/,,,b1,,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",true,false,false);
	}
	
	@Test
	public void setMoveTest() {
		startGame(null,false);
		assertMove(null,false,false);
		assertGameState(",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,",false,false,false);
	}
}
