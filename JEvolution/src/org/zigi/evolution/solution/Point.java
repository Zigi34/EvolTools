package org.zigi.evolution.solution;

public class Point extends CloneableValue<Point> {
	private Double x;
	private Double y;

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	@Override
	public Point clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return String.format("Point(x: %.3f, y:%.3f)", getX(), getY());
	}
}
