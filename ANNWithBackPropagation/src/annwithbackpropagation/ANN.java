package annwithbackpropagation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Tiyash Basu
 */
public class ANN implements Serializable {
    private final double[][] nodes;
    private final double[][] bias;
    private final double[][][] weights;
    private final double ACTIVATION_PARAMETER = 0.75;
    private final double ERROR_THRESHOLD = 0.00001;
    private final double MOMENTUM_FACTOR = 0.5;

    ANN(int[] nodes) {
        int i, j;
        int l = nodes.length;
        this.nodes = new double[l][];
        bias = new double[l - 1][];
        weights = new double[l - 1][][];
        for (i = l - 1; i >= 0; i--)
            this.nodes[i] = new double[nodes[i]];
        for (i = l - 1; i >= 1; i--) {
            bias[i - 1] = new double[nodes[i]];
            weights[i - 1] = new double[nodes[i]][];
            for (j = nodes[i] - 1; j >= 0; j--)
                weights[i - 1][j] = new double[nodes[i - 1]];
        }
    }

    public boolean Save(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream fOut = new FileOutputStream(fileName);
        ObjectOutputStream oOut = new ObjectOutputStream(fOut);
        oOut.writeObject(this);
        return true;
    }
    
    public void FeedInput(double[] input) {
        int i, j, k;
        double sum;
        int size = nodes.length;
        for (i = input.length - 1; i >= 0; i--)
            nodes[0][i] = input[i];
        for (i = 1; i <= size - 1; i++) {
            for (j = 0; j < nodes[i].length; j++) {
                sum = 0;
                for (k = 0; k < nodes[i - 1].length; k++) {
                    sum += nodes[i - 1][k] * weights[i - 1][j][k];
                    nodes[i][j] = 1 / (1 + Math.exp((sum + bias[i][j]) * ACTIVATION_PARAMETER));
                }
            }
        }
    }

    public int[] GetOutput() {
        int outputLayerNumber = nodes.length - 1;
        int outputSize = nodes[outputLayerNumber].length;
        int[] results = new int[outputSize];
        for (int i = outputSize - 1; i >= 0; i--)
            results[i] = (nodes[outputLayerNumber][i] >= 0.5) ? 1 : 0;
        return results;
    }
}
