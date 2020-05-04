package de.tuberlin.sese.swtpp.gameserver.model.ploy;

public class Figure {
	
	private int x;
	private int y;
	private char color;
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public char getColor() {
		return color;
	}

	public Figure(int x, int y, char color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
}
