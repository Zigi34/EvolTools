package org.zigi.evolution.main;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuCtxSynchronize;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.cuLaunchKernel;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyDtoH;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;
import static jcuda.driver.JCudaDriver.cuModuleLoad;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.algorithm.terminate.FitnessTerminate;
import org.zigi.evolution.problem.ArtificialAnt;
import org.zigi.evolution.problem.RegressionProblem;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.CosFunction;
import org.zigi.evolution.solution.value.DivideFunction;
import org.zigi.evolution.solution.value.MultiplyFunction;
import org.zigi.evolution.solution.value.NumericConstant;
import org.zigi.evolution.solution.value.PowerFunction;
import org.zigi.evolution.solution.value.SinFunction;
import org.zigi.evolution.solution.value.SubtractionFunction;
import org.zigi.evolution.solution.value.SumFunction;
import org.zigi.evolution.util.Population;

public class Main {

    private static final Random RAND = new Random();
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * Provadi krizeni mezi dvema stromy
     * 
     * @param sol1
     *            prvni reseni
     * @param sol2
     *            druhe reseni
     */
    public void crossTreeSolutions(TreeSolution sol1, TreeSolution sol2) {
	int index1 = RAND.nextInt(sol1.size());
	int index2 = RAND.nextInt(sol2.size());

	// samotné křížení
	TreeSolution.changeSubTree(sol1, index1, sol2, index2);
    }

    public void artificialAnt() {
	try {
	    ArtificialAnt problem = new ArtificialAnt();
	    // problem.setMaxHeight(6);
	    // problem.setMaxMoves(420);
	    // problem.setYard(new File("resources/artificial_ant"));
	    // LOG.info("Drobečků: " + problem.getCrumbs());

	    GeneticProgramming alg = new GeneticProgramming();
	    alg.setProblem(problem);
	    alg.addChangeListener(new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
		    LOG.info(evt.getNewValue());
		}
	    });
	    alg.start();

	    Solution best = alg.getBestSolution();
	    System.out.println("BEST: " + best.toString());
	    // int[][] path = problem.getPath(best);

	    // print best path
	    // ArtificialAnt.printPath(path);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void regression() {
	try {
	    RegressionProblem problem = new RegressionProblem();
	    problem.loadDataset(new File("resources/regression"), ";");
	    problem.addFenotype(new SinFunction());
	    problem.addFenotype(new CosFunction());
	    problem.addFenotype(new SumFunction());
	    problem.addFenotype(new SubtractionFunction());
	    problem.addFenotype(new MultiplyFunction());
	    problem.addFenotype(new PowerFunction());
	    problem.addFenotype(new DivideFunction());
	    // problem.addFenotype(new NumericValue());
	    NumericConstant const1 = new NumericConstant("X", -2.0, 10.0);
	    problem.addFenotype(const1);
	    // NumericConstant const2 = new NumericConstant("Y", 1.0, 3.0);
	    // problem.addFenotype(const2);
	    // NumericConstant const3 = new NumericConstant("Z", 3.0, 25.0);
	    // problem.addFenotype(const3);

	    int index = 1;
	    for (; index < 20; index++) {
		GeneticProgramming gp = new GeneticProgramming();
		gp.addChangeListener(new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getNewValue().equals(GeneticProgramming.ALGORITHM_TERMINATED)) {
			    LOG.info(String.format("(%s) BEST: %s", gp.getActualGeneration(), gp.getBestSolution()));
			}
		    }
		});
		gp.addTerminateFunction(new FitnessTerminate(20.0));
		Population pop = new Population(200);
		gp.setPopulation(pop);
		gp.setProblem(problem);
		gp.setGeneration(1000);
		gp.start();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Náhodné generování stromu požadované hloubky
     * 
     * @return
     */
    public static TreeSolution randomTree(ArtificialAnt problem) {
	return (TreeSolution) problem.randomSolution();
    }

    public static void main(String[] args) throws IOException {
	Main test = new Main();
	// test.artificialAnt();
	// test.regression();

	// Enable exceptions and omit all subsequent error checks
	JCudaDriver.setExceptionsEnabled(true);

	// Create the PTX file by calling the NVCC
	String ptxFileName = preparePtxFile("resources/JCudaVectorAddKernel.cu");

	LOG.info("CUDA start");
	// Initialize the driver and create a context for the first device.
	cuInit(0);
	CUdevice device = new CUdevice();
	cuDeviceGet(device, 0);
	CUcontext context = new CUcontext();
	cuCtxCreate(context, 0, device);

	// Load the ptx file.
	CUmodule module = new CUmodule();
	cuModuleLoad(module, ptxFileName);

	// Obtain a function pointer to the "add" function.
	CUfunction function = new CUfunction();
	cuModuleGetFunction(function, module, "add");

	int numElements = 100000;

	// Allocate and fill the host input data
	float hostInputA[] = new float[numElements];
	float hostInputB[] = new float[numElements];
	for (int i = 0; i < numElements; i++) {
	    hostInputA[i] = (float) i;
	    hostInputB[i] = (float) i;
	}

	// Allocate the device input data, and copy the
	// host input data to the device
	CUdeviceptr deviceInputA = new CUdeviceptr();
	cuMemAlloc(deviceInputA, numElements * Sizeof.FLOAT);
	cuMemcpyHtoD(deviceInputA, Pointer.to(hostInputA), numElements * Sizeof.FLOAT);
	CUdeviceptr deviceInputB = new CUdeviceptr();
	cuMemAlloc(deviceInputB, numElements * Sizeof.FLOAT);
	cuMemcpyHtoD(deviceInputB, Pointer.to(hostInputB), numElements * Sizeof.FLOAT);

	// Allocate device output memory
	CUdeviceptr deviceOutput = new CUdeviceptr();
	cuMemAlloc(deviceOutput, numElements * Sizeof.FLOAT);

	// Set up the kernel parameters: A pointer to an array
	// of pointers which point to the actual values.
	Pointer kernelParameters = Pointer.to(Pointer.to(new int[] { numElements }), Pointer.to(deviceInputA), Pointer.to(deviceInputB), Pointer.to(deviceOutput));

	// Call the kernel function.
	int blockSizeX = 256;
	int gridSizeX = (int) Math.ceil((double) numElements / blockSizeX);
	cuLaunchKernel(function, gridSizeX, 1, 1, // Grid dimension
		blockSizeX, 1, 1, // Block dimension
		0, null, // Shared memory size and stream
		kernelParameters, null // Kernel- and extra parameters
	);
	cuCtxSynchronize();

	// Allocate host output memory and copy the device output
	// to the host.
	float hostOutput[] = new float[numElements];
	cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, numElements * Sizeof.FLOAT);

	// Verify the result
	boolean passed = true;
	for (int i = 0; i < numElements; i++) {
	    float expected = i + i;
	    if (Math.abs(hostOutput[i] - expected) > 1e-5) {
		System.out.println("At index " + i + " found " + hostOutput[i] + " but expected " + expected);
		passed = false;
		break;
	    }
	}
	System.out.println("Test " + (passed ? "PASSED" : "FAILED"));

	// Clean up.
	cuMemFree(deviceInputA);
	cuMemFree(deviceInputB);
	cuMemFree(deviceOutput);
	LOG.info("CUDA stop");
    }

    /**
     * The extension of the given file name is replaced with "ptx". If the file with the resulting name does not exist, it is compiled from the given file using NVCC. The name of the PTX file is
     * returned.
     *
     * @param cuFileName
     *            The name of the .CU file
     * @return The name of the PTX file
     * @throws IOException
     *             If an I/O error occurs
     */
    private static String preparePtxFile(String cuFileName) throws IOException {
	int endIndex = cuFileName.lastIndexOf('.');
	if (endIndex == -1) {
	    endIndex = cuFileName.length() - 1;
	}
	String ptxFileName = cuFileName.substring(0, endIndex + 1) + "ptx";
	File ptxFile = new File(ptxFileName);
	if (ptxFile.exists()) {
	    return ptxFileName;
	}

	File cuFile = new File(cuFileName);
	if (!cuFile.exists()) {
	    throw new IOException("Input file not found: " + cuFileName);
	}
	String modelString = "-m" + System.getProperty("sun.arch.data.model");
	String command = "nvcc " + modelString + " -ptx " + cuFile.getPath() + " -o " + ptxFileName;

	System.out.println("Executing\n" + command);
	Process process = Runtime.getRuntime().exec(command);

	String errorMessage = new String(toByteArray(process.getErrorStream()));
	String outputMessage = new String(toByteArray(process.getInputStream()));
	int exitValue = 0;
	try {
	    exitValue = process.waitFor();
	} catch (InterruptedException e) {
	    Thread.currentThread().interrupt();
	    throw new IOException("Interrupted while waiting for nvcc output", e);
	}

	if (exitValue != 0) {
	    System.out.println("nvcc process exitValue " + exitValue);
	    System.out.println("errorMessage:\n" + errorMessage);
	    System.out.println("outputMessage:\n" + outputMessage);
	    throw new IOException("Could not create .ptx file: " + errorMessage);
	}

	System.out.println("Finished creating PTX file");
	return ptxFileName;
    }

    /**
     * Fully reads the given InputStream and returns it as a byte array
     *
     * @param inputStream
     *            The input stream to read
     * @return The byte array containing the data from the input stream
     * @throws IOException
     *             If an I/O error occurs
     */
    private static byte[] toByteArray(InputStream inputStream) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	byte buffer[] = new byte[8192];
	while (true) {
	    int read = inputStream.read(buffer);
	    if (read == -1) {
		break;
	    }
	    baos.write(buffer, 0, read);
	}
	return baos.toByteArray();
    }
}
