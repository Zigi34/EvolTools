package org.zigi.jcuda;

public class Node {
	byte type; // 0 - terminal, 1 - neterminal
	byte model; // 0 - plus, 1 - minus, 2 - krat
	double value; // 0 - hodnota
	byte child; // pocet potomku

	public static final byte TERMINAL = 0;
	public static final byte NETERMINAL = 1;
	public static final byte NONE = -1;

	public static final byte PLUS = 0;
	public static final byte MINUS = 1;
	public static final byte KRAT = 2;
	public static final byte DELENO = 3;
	public static final byte SIN = 4;
	public static final byte COS = 5;
	public static final byte POWER = 8;
	public static final byte VALUE = 10;

	public Node(byte typ, byte model, byte child, double value) {
		this.type = typ;
		this.model = model;
		this.child = child;
		this.value = value;
	}
}
