package annwithbackpropagation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tiyash Basu
 */
public class ANN implements Serializable {
    private double[][] node;
    private double[][] bias;
    private double[][][] weight;
    private double activationParameter;

    /**This is the default constructor and if used, it is imperative to use a load command to initialize the created ANN from a save file.
     * 
     */
    public ANN() {
        
    }

    /**This creates a multilayer ANN, which can be trained using back propagation algorithm.
     *
     * @param nodes An Int32 array having number of nodes in each layer, assuming data flows from the first layer to the last layer.
     * @param activationParameter The activation parameter for the ANN's sigmoid function.
     */
    public ANN(int[] nodes, double activationParameter) {
        int i, j, k;
        this.activationParameter = activationParameter;
        int noOfLayers = nodes.length;
        this.node = new double[noOfLayers][];
        bias = new double[noOfLayers - 1][];
        weight = new double[noOfLayers - 1][][];
        for (i = noOfLayers - 1; i >= 0; i--)
            this.node[i] = new double[nodes[i]];
        for (i = noOfLayers - 1; i >= 1; i--) {
            bias[i - 1] = new double[nodes[i]];
            for (j = bias [i - 1].length - 1; j >= 0; j--)
                bias[i - 1][j] = Math.random() / 100000000000000.0;
            weight[i - 1] = new double[nodes[i]][];
            for (j = nodes[i] - 1; j >= 0; j--) {
                weight[i - 1][j] = new double[nodes[i - 1]];
                for (k = weight[i - 1][j].length - 1; k >= 0; k--)
                    weight[i - 1][j][k] = Math.random() / 100000000000000.0;
            }
        }
    }

    /**Trains the ANN using Back Propagation algorithm.
     * 
     * @param trainingInput The training inputs.
     * @param targetOutput The corresponding expected outputs.
     * @param learningRate The learning rate of the training.
     * @param momentumFactor The momentum factor of the training.
     * @param errorThreshold The maximum accepted error expected at the end of training.
     */
    public void train(double[][] trainingInput, int[][] targetOutput, double learningRate, double momentumFactor, double errorThreshold) {
        int i, j, k, l, j2;
        int numberOfNodes, lastWeightLayerNum;
        int outputLayerNum = node.length - 1;
        double sum, previousDeltaWeight;
        double delta[][] = new double[node.length - 1][];
        double deltaWeight[][][] = new double[node.length - 1][][];
        for (i = 0; i < outputLayerNum; i++) {
            delta[i] = new double[node[i + 1].length];
            deltaWeight[i] = new double[node[i + 1].length][];
            for (j = node[i + 1].length - 1; j >= 0; j--)
                deltaWeight[i][j] = new double[node[i].length];
        }
        double[] error = new double[trainingInput.length];
        while (true) {
            for (i = 0; i < trainingInput.length; i++) {
                FeedInput(trainingInput[i]);
                error[i] = GetError(targetOutput[i]);
            }
            if (GetMax(error) <= errorThreshold)
                break;
            for (i = 0; i < trainingInput.length; i++) {
                FeedInput(trainingInput[i]);
                //Updating weights of the output layer.
                numberOfNodes = node[outputLayerNum - 1].length - 1;
                lastWeightLayerNum = outputLayerNum - 1;
                for (j = node[outputLayerNum].length - 1; j >= 0; j--) {
                    delta[lastWeightLayerNum][j] = (node[outputLayerNum][j] * (1 - node[outputLayerNum][j]) * (node[outputLayerNum][j] - targetOutput[i][j]));
                    bias[lastWeightLayerNum][j] += (activationParameter * learningRate * delta[lastWeightLayerNum][j]);
                    for (k = numberOfNodes; k >= 0; k--) {
                        previousDeltaWeight = deltaWeight[lastWeightLayerNum][j][k];
                        deltaWeight[lastWeightLayerNum][j][k] = (activationParameter * learningRate * delta[lastWeightLayerNum][j] * node[lastWeightLayerNum][k]);
                        weight[lastWeightLayerNum][j][k] -= (deltaWeight[lastWeightLayerNum][j][k] + momentumFactor * previousDeltaWeight);
                    }
                }
                //updating weights of the hidden layer
                for (j = outputLayerNum - 1; j >= 1; j--) {
                    j2 = j - 1;
                    for (k = node[j].length - 1; k >= 0; k--) {
                        sum = 0;
                        for (l = node[j + 1].length - 1; l >= 0; l--)
                            sum += delta[j][l] * weight[j][l][k];
                        delta[j2][k] = node[j][k] * (1 - node[j][k]) * sum;
                        bias[j2][k] += (activationParameter * learningRate * delta[j2][k]);
                        for (l = node[j2].length - 1; l >= 0; l--) {
                            previousDeltaWeight = deltaWeight[j2][k][l];
                            deltaWeight[j2][k][l] = (activationParameter * learningRate * delta[j2][k] * node[j2][l]);
                            weight[j2][k][l] -= (deltaWeight[j2][k][l] + momentumFactor * previousDeltaWeight);
                        }
                    }
                }
            }
        }
    }

    /** Calculates the error between the output and the target.
     * 
     * @param target The expected value of the output.
     * @return The error between the expected output and the actual output.
     */
    private double GetError(int[] target) {
        double sum = 0, temp;
        int lastLayerNum = node.length - 1;
        for (int i = target.length - 1; i >= 0; i--) {
            temp = node[lastLayerNum][i] - target[i];
            sum += temp * temp;
        }
        return sum / 2;
    }

    /**Returns maximum value from a double array;
     * 
     * @param array The array which will be searched.
     * @return The maximum value.
     */
    private double GetMax(double[] array) {
        double max = array[0];
        for (int i = array.length - 1; i >= 1; i--)
            if (max < array[i])
                max = array[i];
        return max;
    }

    /**This method is used to push inputs to the first layer of the ANN, which is then forward propagated towards the successive layers.
     * 
     * @param input A double array which contains the inputs to the ANN.
     * @throws ArrayIndexOutOfBoundsException If the size of the input array is smaller than the size of the input layer of the ANN.
     */
    public void FeedInput(double[] input) throws ArrayIndexOutOfBoundsException {
        int i, j, k, size;
        double sum;
        size = node.length;
        for (i = node[0].length - 1; i >= 0; i--)
            node[0][i] = input[i];
        for (i = 1; i < size; i++) {
            for (j = 0; j < node[i].length; j++) {
                sum = 0;
                for (k = 0; k < node[i - 1].length; k++)
                    sum += node[i - 1][k] * weight[i - 1][j][k];
                node[i][j] = 1 / (1 + Math.exp(-1 * (sum + bias[i - 1][j]) * activationParameter));
                //node[i][j] = 1 / (1 + Math.exp(-1 * sum * activationParameter));
            }
        }
    }

    /**This method is used to return the output of the ANN generated from the last execution of the FeedInput method. 
     * 
     * @return The output the the ANN as an Int32 array of 0s and 1s;
     */
    public int[] GetOutput() {
        int outputLayerNumber = node.length - 1;
        int outputSize = node[outputLayerNumber].length;
        int[] results = new int[outputSize];
        for (int i = outputSize - 1; i >= 0; i--)
            results[i] = (node[outputLayerNumber][i] >= 0.5) ? 1 : 0;
        return results;
    }

    /**This method returns the size of the input layer of the ANN.
     * 
     * @return The number of nodes in the input layer.
     */
    public int InputSize() {
        return node[0].length;
    }

    /**This method returns the size of the output layer of the ANN.
     * 
     * @return The number of nodes in the output layer.
     */
    public int OutputSize() {
        return node[node.length - 1].length;
    }

    /**This method is used to save the ANN.
     * 
     * @param fileName The file name to which the ANN should be saved. It should contain the entire path of the file.
     * @throws FileNotFoundException If the file at the specified path is not found.
     * @throws IOException If the file at the specified path is inaccessible.*
     */
    public void Save(String fileName) throws FileNotFoundException, IOException{
        ArrayList saveList = new ArrayList();
        saveList.add(node);
        saveList.add(bias);
        saveList.add(weight);
        saveList.add(activationParameter);
        FileOutputStream fOut = new FileOutputStream(fileName);
        ObjectOutputStream oOut = new ObjectOutputStream(fOut);
        oOut.writeObject(saveList);
        oOut.close();
        fOut.close();
    }

    /**This method is used to load an ANN from a saved file.
     * 
     * @param fileName The file name from which the ANN should be loaded from. It should contain the entire path of the file.
     * @throws FileNotFoundException If the file at the specified path is not found.
     * @throws IOException If the file at the specified path is inaccessible.
     * @throws ClassNotFoundException May throw this exception when the ArrayList java class definition is corrupt in system.
     */
    public void Load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fIn = new FileInputStream(fileName);
        ObjectInputStream oIn = new ObjectInputStream(fIn);
        ArrayList loadList = (ArrayList) oIn.readObject();
        node = (double[][]) loadList.get(0);
        bias = (double[][]) loadList.get(1);
        weight = (double[][][]) loadList.get(2);
        activationParameter = (double) loadList.get(3);
    }
}
