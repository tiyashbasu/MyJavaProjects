package annwithbackpropagation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiyash Basu
 */
public class ANN implements Serializable {
    private final double[][] node;
    private final double[][] bias;
    private final double[][][] weight;
    private final double ACTIVATION_PARAMETER = 0.75;
    private final double ERROR_THRESHOLD = 0.00001;
    private final double MOMENTUM_FACTOR = 0.5;

    /**This creates a multilayer ANN, which can be trained using back propagation algorithm.
     *
     * @param nodes An Int32 array having number of nodes in each layer, assuming data flows from the first layer to the last layer.
     */
    ANN(int[] nodes) {
        int i, j, k;
        int noOfLayers = nodes.length;
        this.node = new double[noOfLayers][];
        bias = new double[noOfLayers - 1][];
        weight = new double[noOfLayers - 1][][];
        for (i = noOfLayers - 1; i >= 0; i--)
            this.node[i] = new double[nodes[i]];
        for (i = noOfLayers - 1; i >= 1; i--) {
            bias[i - 1] = new double[nodes[i]];
            for (j = bias [i - 1].length - 1; j >= 0; j--)
                bias[i - 1][j] = Math.random() / 1000000000000.0;
            weight[i - 1] = new double[nodes[i]][];
            for (j = nodes[i] - 1; j >= 0; j--) {
                weight[i - 1][j] = new double[nodes[i - 1]];
                for (k = weight[i - 1][j].length - 1; k >= 0; k--)
                    weight[i - 1][j][k] = Math.random() / 1000000000000.0;
            }
        }
    }

    /**This method is used to save the ANN.
     * 
     * @param fileName The file name to which the ANN should be saved. It should contain the entire path of the file.
     * @return True if the save is successful. Otherwise false.
     */
    public boolean Save(String fileName){
        try {
            FileOutputStream fOut = new FileOutputStream(fileName);
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            oOut.close();
            fOut.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ANN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ANN.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
                node[i][j] = 1 / (1 + Math.exp((sum + bias[i - 1][j]) * ACTIVATION_PARAMETER));
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
}
