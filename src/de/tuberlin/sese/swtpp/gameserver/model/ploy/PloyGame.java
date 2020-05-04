package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

/**
 * Class Cannon extends the abstract class Game as a concrete game instance that
 * allows to play Cannon.
 *
 */
public class PloyGame extends Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424778147226994452L;

	/************************
	 * member
	 ***********************/

	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state
	// TODO: insert additional game data here
	private String board;

	/************************
	 * constructors
	 ***********************/

	public PloyGame() {
		super();

		// TODO: init internal representation
	}

	public String getType() {
		return "ploy";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.blackPlayer = players.get(0);
				this.whitePlayer = players.get(1);
				nextPlayer = blackPlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (whiteGaveUp())
				gameInfo = "white gave up";
			else if (didWhiteDraw() && !didBlackDraw())
				gameInfo = "white called draw";
			else if (!didWhiteDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "white won";
		}

		return gameInfo;
	}

	@Override
	public String nextPlayerString() {
		return isWhiteNext() ? "w" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(p -> p.requestedDraw())) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) {
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/*******************************************
	 * Helpful stuff
	 ******************************************/

	/**
	 * 
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 * 
	 * @param player
	 * @return
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	@Override
	public void setBoard(String state) {

		this.board = state;

		// Note: This method is for automatic testing. A regular game would not start at
		// some artificial state.
		// It can be assumed that the state supplied is a regular board that can be
		// reached during a game.
	}

	@Override
	public String getBoard() {

		if (this.board == null || "".equals(this.board)) {
			return ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,";
		}

		return this.board;
	}
	
	@Override
	public boolean tryMove(String moveString, Player player) {
		
		boolean result = false;

		if (checkFormat(moveString)) {

			String[][] gameBoard = getGameBoardArray();
			
			int x = moveString.charAt(0) - 97;
			int y = moveString.charAt(1) - 49;
			
			if(gameBoard[y][x] != null && !"".equals(gameBoard[y][x])) {
			
				
				char color = gameBoard[y][x].charAt(0);
				Figure figure = new Figure(x, y, color);
				
				if(rightPlayer(player, figure)) {//turn von player checken
					result = conditionFulfilled(moveString, gameBoard, figure, player);
				}
			}
		}

		return result;
	}

	/**
	 * checks format of moveString
	 * 
	 * @param moveString
	 * @return
	 */
	private boolean checkFormat(String moveString) {
		
		if(moveString != null && moveString.matches("^[a-i][1-9]-[a-i][1-9]-[0-7]$")) {
			
			String[] arr = moveString.split("-");
			
			return !(arr[0].equals(arr[1]) && "0".equals(arr[2]));
		}
		
		return false;
	}

	/**
	 * converts String Board into a two-dimensional array 
	 * 
	 * @return
	 */
	private String[][] getGameBoardArray() {

		String[][] gameBoard = new String[9][9];
		String[] row = getBoard().split("/");
		int index = 0;

		for (int i = row.length - 1; i >= 0; i--) {
			gameBoard[index++] = row[i].split(",", -9);
		}

		return gameBoard;
	}

	private int getXCompare(String moveString) {

		int xCompare = Character.compare(moveString.charAt(3), moveString.charAt(0));

		if (xCompare != 0) {
			xCompare = xCompare > 0 ? 1 : -1;
		}

		return xCompare;
	}

	private int getYCompare(String moveString) {

		int yCompare = Character.compare(moveString.charAt(4), moveString.charAt(1));

		if (yCompare != 0) {
			yCompare = yCompare > 0 ? 1 : -1;
		}

		return yCompare;
	}

	/**
	 * finds directions of figures
	 * Directions are described as every value 
	 * that refer to (xCompare, yCompare)
	 *
	 * (-1_1)  (0_1)  (1_1)
	 * (-1_0)  (0_0)  (1_0) 
	 * (-1,-1) (0_-1) (1_-1)
	 * 
	 * @param moveString
	 * @param fiName 
	 * @return
	 */
	private int findDirection(String moveString, String fiName, int xCompare, int yCompare) {

		/*
		 * the array "directions" does not include (0,0), the current position of figure,
		 * because it already has been checked in function tryMove
		 */
		String[] directions = new String[] { "0_1", "1_1", "1_0", "1_-1", "0_-1", "-1_-1", "-1_0", "-1_1" };
		String bin = getBinaryCode(fiName);
		
		if(bin.contains("1")) {
		
			for (int i = 0; i < directions.length; i++) {
				if (directions[i].equals(xCompare + "_" + yCompare)) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * checks the validity of the rotation made by the figure 
	 * 
	 * @param moveString
	 * @param bin
	 * @return
	 */
	private boolean checkRotation(String moveString, String bin) {

		int rotationNumber = bin.replace("0", "").length();
		String[] moves = moveString.split("-");
		int rotation = Integer.parseInt(moves[2]);

		// figure not of type shield
		if (rotationNumber != 1) {
			if(rotation != 0) {
				if(!moves[0].equals(moves[1]))
				return false;
			}
		}

		return true;
	}

	private boolean rightPlayer(Player player, Figure fi) {

		return (isWhiteNext() && Character.compare(fi.getColor(), 'w') == 0) 
				|| (!isWhiteNext() && Character.compare(fi.getColor(), 'b') == 0);
	}

	private void updateBoardState(String[][] gameBoardArray, String moveString, Player player) {

		int x = moveString.charAt(0) - 97;
		int y = moveString.charAt(1) - 49;
		int xZiel = moveString.charAt(3) - 97;
		int yZiel = moveString.charAt(4) - 49;

		if ((x != xZiel || y != yZiel) && isCommander(gameBoardArray[yZiel][xZiel])) {

			regularGameEnd(player);
		}
		
		String m = gameBoardArray[y][x];
		gameBoardArray[y][x] = "";
		gameBoardArray[yZiel][xZiel] = m;
		StringBuffer strBuff = new StringBuffer();
		
		for (int i = gameBoardArray.length - 1; i >= 0; i--) {		
			strBuff.append(String.join(",", gameBoardArray[i]));
			strBuff.append("/");
		}
		setBoard(strBuff.deleteCharAt(strBuff.length() - 1).toString());
	}

	private String getBinaryCode(String figur) {
		
		return String.format("%8s", Integer.toBinaryString(Integer.valueOf(figur.substring(1))))
				.replace(" ", "0");
	}

	private boolean isCommander(String figur) {

		if (figur == null || "".equals(figur)) {
			return false;
		}

		return getBinaryCode(figur).replace("0", "").length() == 4;
	}

	private boolean isGameFinished(Player player) {

		if (!finished) {

			int boardLength = getBoard().length();
			int db = boardLength - getBoard().replace("b", "").length();
			int dw = boardLength - getBoard().replace("w", "").length();

			if (db == 1 || dw == 1) {
				regularGameEnd(player);
			}

		}
		
		return finished;
	}

	private String rotation(String figur, int rotation) {

		String bin = getBinaryCode(figur);
		bin = bin.substring(rotation).concat(bin.substring(0, rotation));
		int decimal = Integer.parseInt(bin, 2);

		return figur.substring(0, 1) + decimal;
	}

	private void caseResult(String[][] gameBoard, String moveString, Player player) {
		
		updateBoardState(gameBoard, moveString, player);
		
		if (!isGameFinished(player)) {
			nextPlayer = isWhiteNext() ? blackPlayer : whitePlayer;
		}
	}
	
	private int getStepFigur(String bin) {
		
		int stepNumber = bin.replace("0", "").length(); 
		
		if(stepNumber == 4) {
			stepNumber = 1;
		}
		
		return stepNumber;
	}

	private boolean conditionFulfilled(String moveString, String[][] gameBoard, Figure fi, Player player) {

		boolean result = false;
		String[] moves = moveString.split("-");

		if (moves[0].equals(moves[1])) {
			
			gameBoard[fi.getY()][fi.getX()] = rotation(gameBoard[fi.getY()][fi.getX()], Integer.parseInt(moves[2]));
			result = true;
		} else {
			
			int direction = findDirection(moveString, gameBoard[fi.getY()][fi.getX()] , getXCompare(moveString), getYCompare(moveString));
			
			if(direction == -1)
				return false;
			
			result = checkWay(gameBoard, moveString, fi, direction);
		}
		
		if(result) {
			caseResult(gameBoard, moveString, player);
		}
		
		return result;
	}
	
	private boolean checkWay(String[][] gameBoard, String moveString, Figure fi, int direction) {
		
		String bin = getBinaryCode(gameBoard[fi.getY()][fi.getX()]);
		
		if (Character.compare(bin.charAt(7 - direction), '1') != 0 || !checkRotation(moveString, bin)) {

			return false;
		} 
		
		gameBoard[fi.getY()][fi.getX()] = rotation(gameBoard[fi.getY()][fi.getX()], Integer.parseInt(moveString.split("-")[2])); // rotate
		
		int stepNumber = getStepFigur(bin); // steps made by the figure
		
		return forLoop(gameBoard, moveString, stepNumber, fi);
	}
	
	private boolean forLoop(String[][] gameBoard, String moveString, int stepNumber, Figure fi) {
		
		int xNew = fi.getX();
		int yNew = fi.getY();
		
		for (int i = 0; i < stepNumber; i++) {
			xNew += getXCompare(moveString);
			yNew += getYCompare(moveString);
			
			if ((yNew == moveString.charAt(4) - 49 && xNew == moveString.charAt(3) - 97)) {
				
				if("".equals(gameBoard[yNew][xNew]) || Character.compare(gameBoard[yNew][xNew].charAt(0), fi.getColor()) != 0) {
					return true;	
				}
			} else if (!"".equals(gameBoard[yNew][xNew])) {
				break;
			}
		}
		
		return false;
	}
}
