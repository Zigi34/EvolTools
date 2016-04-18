package org.zigi.jcuda;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.instrument.Instrumentation;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.CosFunction;
import org.zigi.evolution.solution.value.DivideFunction;
import org.zigi.evolution.solution.value.MultiplyFunction;
import org.zigi.evolution.solution.value.Node;
import org.zigi.evolution.solution.value.NumericValue;
import org.zigi.evolution.solution.value.Power2Function;
import org.zigi.evolution.solution.value.SinFunction;
import org.zigi.evolution.solution.value.SubtractionFunction;
import org.zigi.evolution.solution.value.SumFunction;

public class NodeStruct implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8136467481890448452L;

    public static Instrumentation INSTRUMENTATION;
    public static final Logger LOG = Logger.getLogger(NodeStruct.class);

    byte type; // 0 - terminal, 1 - neterminal
    byte model; // 0 - plus, 1 - minus, 2 - krat
    double value; // 0 - hodnota
    byte child; // pocet potomku

    public static final byte TERMINAL = 0x0;
    public static final byte NETERMINAL = 0x1;
    public static final byte NONE = 0xf;

    public static final byte PLUS = 0x0;
    public static final byte MINUS = 0x1;
    public static final byte KRAT = 0x2;
    public static final byte DELENO = 0x3;
    public static final byte SIN = 0x4;
    public static final byte COS = 0x5;
    public static final byte POWER = 0x8;
    public static final byte VALUE = 0x9;
    public static final byte NULL_MODEL = 0xf;

    public static final byte NULL_CHILD = 0xf;

    public static final double NULL_VALUE = Double.MIN_VALUE;

    public NodeStruct() {
    }

    public NodeStruct(byte typ, byte model, byte child, double value) {
	this.type = typ;
	this.model = model;
	this.child = child;
	this.value = value;
    }

    public static byte[] serialize(List<Solution> solutions, int maxDeep) {
	int nodes = Double.valueOf(Math.pow(2, maxDeep - 1) - 1.0).intValue();
	int objectSize = 11;
	try {
	    ByteBuffer buffer = ByteBuffer.allocate(solutions.size() * nodes * objectSize);

	    for (int node = 0; node < nodes; node++)
		for (int i = 0; i < solutions.size(); i++) {
		    TreeSolution tree = (TreeSolution) solutions.get(i);
		    Node n = null;
		    try {
			n = tree.getNode(node);
		    } catch (Exception e) {
			// LOG.warn("Uzel s indexem " + node + " neexistuje");
		    }

		    NodeStruct struct = new NodeStruct();
		    if (n != null) {
			int child = n.getMaxChild();
			if (child == 0) {
			    struct.child = 0;
			    struct.type = NodeStruct.TERMINAL;
			} else {
			    struct.child = (byte) child;
			    struct.type = NodeStruct.NETERMINAL;
			}

			if (n.getValue() instanceof SumFunction)
			    struct.model = NodeStruct.PLUS;
			else if (n.getValue() instanceof SubtractionFunction)
			    struct.model = NodeStruct.MINUS;
			else if (n.getValue() instanceof MultiplyFunction)
			    struct.model = NodeStruct.KRAT;
			else if (n.getValue() instanceof DivideFunction)
			    struct.model = NodeStruct.DELENO;
			else if (n.getValue() instanceof SinFunction)
			    struct.model = NodeStruct.SIN;
			else if (n.getValue() instanceof CosFunction)
			    struct.model = NodeStruct.COS;
			else if (n.getValue() instanceof Power2Function)
			    struct.model = NodeStruct.POWER;
			else if (n.getValue() instanceof NumericValue) {
			    NumericValue value = (NumericValue) n.getValue();
			    struct.model = NodeStruct.VALUE;
			    struct.value = (double) value.getValue();
			}
		    } else {
			struct.type = NodeStruct.NONE;
			struct.child = NodeStruct.NULL_CHILD;
			struct.model = NodeStruct.NULL_MODEL;
			struct.value = NodeStruct.NULL_VALUE;
		    }

		    // zapiseme objekt do pole
		    buffer.put(struct.type);
		    buffer.put(struct.model);
		    buffer.put(struct.child);
		    buffer.putDouble(struct.value);
		}
	    return buffer.array();
	} catch (Exception e) {
	    LOG.error(e);
	}
	return null;
    }

    public static byte[] serializeObject(List<Solution> solutions, int maxDeep) {
	int nodes = Double.valueOf(Math.pow(2, maxDeep - 1) - 1.0).intValue();

	try {
	    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(bytesOut);

	    for (int node = 0; node < nodes; node++)
		for (int i = 0; i < solutions.size(); i++) {
		    TreeSolution tree = (TreeSolution) solutions.get(i);
		    Node n = null;
		    try {
			n = tree.getNode(node);
		    } catch (Exception e) {
			LOG.warn("Uzel s indexem " + node + " neexistuje");
		    }

		    NodeStruct struct = new NodeStruct();
		    if (n != null) {
			int child = n.getMaxChild();
			if (child == 0) {
			    struct.child = 0;
			    struct.type = NodeStruct.TERMINAL;
			} else {
			    struct.child = (byte) child;
			    struct.type = NodeStruct.NETERMINAL;
			}

			if (n.getValue() instanceof SumFunction)
			    struct.model = NodeStruct.PLUS;
			else if (n.getValue() instanceof SubtractionFunction)
			    struct.model = NodeStruct.MINUS;
			else if (n.getValue() instanceof MultiplyFunction)
			    struct.model = NodeStruct.KRAT;
			else if (n.getValue() instanceof DivideFunction)
			    struct.model = NodeStruct.DELENO;
			else if (n.getValue() instanceof SinFunction)
			    struct.model = NodeStruct.SIN;
			else if (n.getValue() instanceof CosFunction)
			    struct.model = NodeStruct.COS;
			else if (n.getValue() instanceof Power2Function)
			    struct.model = NodeStruct.POWER;
			else if (n.getValue() instanceof NumericValue) {
			    NumericValue value = (NumericValue) n.getValue();
			    struct.model = NodeStruct.VALUE;
			    struct.value = (double) value.getValue();
			}
		    } else {
			struct.type = NodeStruct.NONE;
			struct.child = NodeStruct.NULL_CHILD;
			struct.model = NodeStruct.NULL_MODEL;
			struct.value = NodeStruct.NULL_VALUE;
		    }

		    // zapiseme objekt do pole
		    oos.writeObject(struct);
		}
	    return bytesOut.toByteArray();
	} catch (Exception e) {
	    LOG.error(e);
	}
	return null;
    }
}
