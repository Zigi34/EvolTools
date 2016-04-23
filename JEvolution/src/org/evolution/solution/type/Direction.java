package org.evolution.solution.type;

public class Direction {
	private int direction = 0;

	public static Direction LEFT = new Direction(0);
	public static Direction TOP = new Direction(1);
	public static Direction RIGHT = new Direction(2);
	public static Direction DOWN = new Direction(3);

	private Direction() {
		this.direction = 0;
	}

	public Direction(int direction) {
		this();
		this.direction = (direction % 3);
	}

	public int getDirection() {
		return direction;
	}

}
