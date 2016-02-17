package org.zigi.evolution.solution.value;

public class Configuration {
	private Integer posX = 0;
	private Integer posY = 0;
	private Direction direction = Direction.RIGHT;
	private Integer moves = 0;
	private Integer foundCrumbs = 0;

	public Integer getPosX() {
		return posX;
	}

	public void setPosX(Integer posX) {
		this.posX = posX;
	}

	public Integer getPosY() {
		return posY;
	}

	public void setPosY(Integer posY) {
		this.posY = posY;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Integer getMoves() {
		return moves;
	}

	public void setMoves(Integer moves) {
		this.moves = moves;
	}

	public Integer getFoundCrumbs() {
		return foundCrumbs;
	}

	public void setFoundCrumbs(Integer foundCrumbs) {
		this.foundCrumbs = foundCrumbs;
	}

	public void foundCrumbsIncrement() {
		this.foundCrumbs++;
	}

	public void moveIncrement() {
		moves++;
	}

}
