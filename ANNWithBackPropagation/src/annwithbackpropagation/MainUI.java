package annwithbackpropagation;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author Tiyash Basu
 */
public class MainUI extends JFrame {
    private ANN ann;
    //<editor-fold defaultstate="collapsed" desc="UI controls declarations">
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;
    //Default panel controls
    private JPanel defaultPanel;
    private SpringLayout defaultLayout;
    private JLabel labelStatus, labelCurrentlyActiveANN;
    //ANN creation panel controls
    private JPanel creationPanel;
    private SpringLayout creationPanelLayout;
    private JLabel labelCPANNDimensions, labelCPActivationParameter;
    private JTextField textFieldCPANNDimensions, textFieldCPActivationParameter;
    private JButton buttonCPCreate;
    //ANN operation panel controls
    private JPanel operationPanel;
    private SpringLayout operationPanelLayout;
    private JLabel labelOPInputFile, labelOPOutputFile;
    private JTextField textFieldOPInputFile, textFieldOPOutputFile;
    private JButton buttonOPSelectInputFile, buttonOPSelectOutputFile, buttonOPGenerateOutput;
    //ANN training panel controls
    private JPanel trainingPanel;
    private SpringLayout trainingPanelLayout;
    private JLabel labelTPTrainingFile, labelTPLearningRate, labelTPMomentumFactor, labelTPErrorThreshold;
    private JTextField textFieldTPTrainingFile, textFieldTPLearningRate, textFieldTPMomentumFactor, textFieldTPErrorThreshold;
    private JButton buttonTPSelectTrainingFile, buttonTPTrain;
    //</editor-fold>
    
    public MainUI() {
        this.frameInit();
        this.getContentPane().setPreferredSize(new Dimension(400, 160));
        this.setTitle("ANN Primer");
        initDefaultPanel();
        initMenuBar();
        initDefaultPanel();
        initCreationPanel();
        initOperationPanel();
        initTrainingPanel();
        this.pack();
        creationPanel.setVisible(false);
        trainingPanel.setVisible(false);
        operationPanel.setVisible(true);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuItem = new JMenuItem("New");
        menuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                trainingPanel.setVisible(false);
                operationPanel.setVisible(false);
                creationPanel.setVisible(true);
                labelStatus.setText("");
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Save");
        menuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if (ann == null) {
                    labelStatus.setText("No ANN currently active.");
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(menuBar) == JFileChooser.APPROVE_OPTION) {
                    try {
                        ann.Save(fileChooser.getSelectedFile().getPath());
                        labelStatus.setText("ANN Saved.");
                        labelCurrentlyActiveANN.setText("Currently active ANN: " + fileChooser.getSelectedFile().getName());
                    } catch (IOException ex) {
                        labelStatus.setText("Could not save ANN. Please try again.");
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Load");
        menuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(menuBar) == JFileChooser.APPROVE_OPTION) {
                    try {
                        if (ann == null)
                            ann = new ANN();
                        ann.Load(fileChooser.getSelectedFile().getPath());
                        labelStatus.setText("ANN loaded.");
                        labelCurrentlyActiveANN.setText("Currently active ANN: " + fileChooser.getSelectedFile().getName());
                    } catch (IOException | ClassNotFoundException ex) {
                        labelStatus.setText("Could not load ANN. Please try again.");
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("Controls");
        menuItem = new JMenuItem("Train ANN");
        menuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                creationPanel.setVisible(false);
                operationPanel.setVisible(false);
                trainingPanel.setVisible(true);
                labelStatus.setText("");
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Operate ANN");
        menuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                creationPanel.setVisible(false);
                trainingPanel.setVisible(false);
                operationPanel.setVisible(true);
                labelStatus.setText("");
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("About");
        menuItem = new JMenuItem("About");
        menu.add(menuItem);
        menuItem = new JMenuItem("Help");
        menu.add(menuItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void initDefaultPanel() {
        defaultPanel = new JPanel();
        defaultLayout = new SpringLayout();
        defaultPanel.setLayout(defaultLayout);

        labelCurrentlyActiveANN = new JLabel("Currently Active ANN: None");
        defaultLayout.putConstraint(SpringLayout.WEST, labelCurrentlyActiveANN, 10, SpringLayout.WEST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.SOUTH, labelCurrentlyActiveANN, -10, SpringLayout.SOUTH, defaultPanel);
        defaultPanel.add(labelCurrentlyActiveANN);

        labelStatus = new JLabel("");
        defaultLayout.putConstraint(SpringLayout.WEST, labelStatus, 10, SpringLayout.WEST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.SOUTH, labelStatus, -15, SpringLayout.SOUTH, labelCurrentlyActiveANN);
        defaultPanel.add(labelStatus);

        this.add(defaultPanel);
    }

    private void initCreationPanel() {
        creationPanel = new JPanel();
        defaultLayout.putConstraint(SpringLayout.WEST, creationPanel, 0, SpringLayout.WEST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.EAST, creationPanel, 0, SpringLayout.EAST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.NORTH, creationPanel, 0, SpringLayout.NORTH, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.SOUTH, creationPanel, 30, SpringLayout.SOUTH, defaultPanel);
        creationPanelLayout = new SpringLayout();
        creationPanel.setLayout(creationPanelLayout);

        labelCPANNDimensions = new JLabel("Enter ANN Dimensions:");
        creationPanelLayout.putConstraint(SpringLayout.WEST, labelCPANNDimensions, 10, SpringLayout.WEST, creationPanel);
        creationPanelLayout.putConstraint(SpringLayout.NORTH, labelCPANNDimensions, 20, SpringLayout.NORTH, creationPanel);
        creationPanel.add(labelCPANNDimensions);

        textFieldCPANNDimensions = new JTextField();
        creationPanelLayout.putConstraint(SpringLayout.WEST, textFieldCPANNDimensions, 30, SpringLayout.EAST, labelCPANNDimensions);
        creationPanelLayout.putConstraint(SpringLayout.EAST, textFieldCPANNDimensions, -10, SpringLayout.EAST, creationPanel);
        creationPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldCPANNDimensions, 0, SpringLayout.VERTICAL_CENTER, labelCPANNDimensions);
        creationPanel.add(textFieldCPANNDimensions);

        labelCPActivationParameter = new JLabel("Enter Activation Parameter:");
        creationPanelLayout.putConstraint(SpringLayout.WEST, labelCPActivationParameter, 10, SpringLayout.WEST, creationPanel);
        creationPanelLayout.putConstraint(SpringLayout.NORTH, labelCPActivationParameter, 10, SpringLayout.SOUTH, labelCPANNDimensions);
        creationPanel.add(labelCPActivationParameter);

        textFieldCPActivationParameter = new JTextField();
        creationPanelLayout.putConstraint(SpringLayout.WEST, textFieldCPActivationParameter, 0, SpringLayout.WEST, textFieldCPANNDimensions);
        creationPanelLayout.putConstraint(SpringLayout.EAST, textFieldCPActivationParameter, 0, SpringLayout.EAST, textFieldCPANNDimensions);
        creationPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldCPActivationParameter, 0, SpringLayout.VERTICAL_CENTER, labelCPActivationParameter);
        creationPanel.add(textFieldCPActivationParameter);

        buttonCPCreate = new JButton("Create ANN");
        creationPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, buttonCPCreate, 0, SpringLayout.HORIZONTAL_CENTER, creationPanel);
        creationPanelLayout.putConstraint(SpringLayout.NORTH, buttonCPCreate, 15, SpringLayout.SOUTH, labelCPActivationParameter);
        creationPanel.add(buttonCPCreate);
        buttonCPCreate.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int[] nodes = StringToIntArray(textFieldCPANNDimensions.getText());
                    for (int i = nodes.length - 1; i >= 0; i--)
                        if (nodes[i] == 0) {
                            labelStatus.setText("Incorrect number of nodes. Please check input.");
                            return;
                        }
                    ann = new ANN(nodes, Double.parseDouble(textFieldCPActivationParameter.getText()));
                    labelStatus.setText("ANN created.");
                    labelCurrentlyActiveANN.setText("Currently Active ANN: Current unsaved");
                }
                catch (NumberFormatException ex) {
                    labelStatus.setText("Illegal number format found. Please check input.");
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        defaultPanel.add(creationPanel);
    }

    private void initOperationPanel() {
        operationPanel = new JPanel();
        defaultLayout.putConstraint(SpringLayout.WEST, operationPanel, 0, SpringLayout.WEST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.EAST, operationPanel, 0, SpringLayout.EAST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.NORTH, operationPanel, 0, SpringLayout.NORTH, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.SOUTH, operationPanel, 30, SpringLayout.SOUTH, defaultPanel);
        operationPanelLayout = new SpringLayout();
        operationPanel.setLayout(operationPanelLayout);

        labelOPInputFile = new JLabel("Enter Input File:");
        operationPanelLayout.putConstraint(SpringLayout.WEST, labelOPInputFile, 10, SpringLayout.WEST, operationPanel);
        operationPanelLayout.putConstraint(SpringLayout.NORTH, labelOPInputFile, 20, SpringLayout.NORTH, operationPanel);
        operationPanel.add(labelOPInputFile);
        
        buttonOPSelectInputFile = new JButton("Select");
        operationPanelLayout.putConstraint(SpringLayout.EAST, buttonOPSelectInputFile, -10, SpringLayout.EAST, operationPanel);
        operationPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonOPSelectInputFile, 0, SpringLayout.VERTICAL_CENTER, labelOPInputFile);
        operationPanel.add(buttonOPSelectInputFile);
        buttonOPSelectInputFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textFieldOPInputFile.setText(SelectFile());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        textFieldOPInputFile = new JTextField();
        operationPanelLayout.putConstraint(SpringLayout.WEST, textFieldOPInputFile, 15, SpringLayout.EAST, labelOPInputFile);
        operationPanelLayout.putConstraint(SpringLayout.EAST, textFieldOPInputFile, -10, SpringLayout.WEST, buttonOPSelectInputFile);
        operationPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldOPInputFile, 0, SpringLayout.VERTICAL_CENTER, labelOPInputFile);
        operationPanel.add(textFieldOPInputFile);

        labelOPOutputFile = new JLabel("Enter Output File:");
        operationPanelLayout.putConstraint(SpringLayout.WEST, labelOPOutputFile, 10, SpringLayout.WEST, operationPanel);
        operationPanelLayout.putConstraint(SpringLayout.NORTH, labelOPOutputFile, 10, SpringLayout.SOUTH, labelOPInputFile);
        operationPanel.add(labelOPOutputFile);
        
        buttonOPSelectOutputFile = new JButton("Select");
        operationPanelLayout.putConstraint(SpringLayout.EAST, buttonOPSelectOutputFile, -10, SpringLayout.EAST, operationPanel);
        operationPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonOPSelectOutputFile, 0, SpringLayout.VERTICAL_CENTER, labelOPOutputFile);
        operationPanel.add(buttonOPSelectOutputFile);
        buttonOPSelectOutputFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textFieldOPOutputFile.setText(SelectFile());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        textFieldOPOutputFile = new JTextField();
        operationPanelLayout.putConstraint(SpringLayout.EAST, textFieldOPOutputFile, 0, SpringLayout.EAST, textFieldOPInputFile);
        operationPanelLayout.putConstraint(SpringLayout.WEST, textFieldOPOutputFile, 0, SpringLayout.WEST, textFieldOPInputFile);
        operationPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldOPOutputFile, 0, SpringLayout.VERTICAL_CENTER, labelOPOutputFile);
        operationPanel.add(textFieldOPOutputFile);

        buttonOPGenerateOutput = new JButton("Generate Output");
        operationPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, buttonOPGenerateOutput, 0, SpringLayout.HORIZONTAL_CENTER, operationPanel);
        operationPanelLayout.putConstraint(SpringLayout.NORTH, buttonOPGenerateOutput, 15, SpringLayout.SOUTH, labelOPOutputFile);
        operationPanel.add(buttonOPGenerateOutput);
        buttonOPGenerateOutput.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (ann == null) {
                        labelStatus.setText("No ANN currently active.");
                        return;
                    }
                    if (textFieldOPInputFile.getText().equals("")) {
                        labelStatus.setText("No input file selected.");
                        return;
                    }
                    if (textFieldOPOutputFile.getText().equals("")) {
                        labelStatus.setText("No input file selected.");
                        return;
                    }
                    int len = ann.InputSize();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(textFieldOPOutputFile.getText()))));
                    for (String line : Files.readAllLines(Paths.get(textFieldOPInputFile.getText()))) {
                        ann.FeedInput(StringToDoubleArray(line, len));
                        bw.write(Arrays.toString(ann.GetOutput()));
                        bw.newLine();
                    }
                    bw.close();
                    labelStatus.setText("Output generated.");
                } catch (IOException ex) {
                    labelStatus.setText("Invalid input or output file.");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    labelStatus.setText("Invalid input file.");
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        defaultPanel.add(operationPanel);
    }

    private void initTrainingPanel() {
        trainingPanel = new JPanel();
        defaultLayout.putConstraint(SpringLayout.WEST, trainingPanel, 0, SpringLayout.WEST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.EAST, trainingPanel, 0, SpringLayout.EAST, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.NORTH, trainingPanel, 0, SpringLayout.NORTH, defaultPanel);
        defaultLayout.putConstraint(SpringLayout.SOUTH, trainingPanel, 30, SpringLayout.SOUTH, defaultPanel);
        trainingPanelLayout = new SpringLayout();
        trainingPanel.setLayout(trainingPanelLayout);

        labelTPTrainingFile = new JLabel("Enter Training File:");
        trainingPanelLayout.putConstraint(SpringLayout.WEST, labelTPTrainingFile, 10, SpringLayout.WEST, trainingPanel);
        trainingPanelLayout.putConstraint(SpringLayout.NORTH, labelTPTrainingFile, 20, SpringLayout.NORTH, trainingPanel);
        trainingPanel.add(labelTPTrainingFile);
        
        buttonTPSelectTrainingFile = new JButton("Select");
        trainingPanelLayout.putConstraint(SpringLayout.EAST, buttonTPSelectTrainingFile, -10, SpringLayout.EAST, trainingPanel);
        trainingPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonTPSelectTrainingFile, 0, SpringLayout.VERTICAL_CENTER, labelTPTrainingFile);
        trainingPanel.add(buttonTPSelectTrainingFile);
        buttonTPSelectTrainingFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textFieldTPTrainingFile.setText(SelectFile());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        textFieldTPTrainingFile = new JTextField();
        trainingPanelLayout.putConstraint(SpringLayout.WEST, textFieldTPTrainingFile, 20, SpringLayout.EAST, labelTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.EAST, textFieldTPTrainingFile, -10, SpringLayout.WEST, buttonTPSelectTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldTPTrainingFile, 0, SpringLayout.VERTICAL_CENTER, labelTPTrainingFile);
        trainingPanel.add(textFieldTPTrainingFile);
        
        buttonTPTrain = new JButton("Start Training");

        labelTPLearningRate = new JLabel("Learning Rate:");
        trainingPanelLayout.putConstraint(SpringLayout.WEST, labelTPLearningRate, 0, SpringLayout.WEST, labelTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.NORTH, labelTPLearningRate, 10, SpringLayout.SOUTH, labelTPTrainingFile);
        trainingPanel.add(labelTPLearningRate);

        textFieldTPLearningRate = new JTextField();
        trainingPanelLayout.putConstraint(SpringLayout.WEST, textFieldTPLearningRate, 0, SpringLayout.WEST, textFieldTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.EAST, textFieldTPLearningRate, -10, SpringLayout.WEST, buttonTPTrain);
        trainingPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldTPLearningRate, 0, SpringLayout.VERTICAL_CENTER, labelTPLearningRate);
        trainingPanel.add(textFieldTPLearningRate);
        
        labelTPMomentumFactor = new JLabel("Momentum Factor:");
        trainingPanelLayout.putConstraint(SpringLayout.WEST, labelTPMomentumFactor, 0, SpringLayout.WEST, labelTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.NORTH, labelTPMomentumFactor, 10, SpringLayout.SOUTH, labelTPLearningRate);
        trainingPanel.add(labelTPMomentumFactor);

        textFieldTPMomentumFactor = new JTextField();
        trainingPanelLayout.putConstraint(SpringLayout.WEST, textFieldTPMomentumFactor, 0, SpringLayout.WEST, textFieldTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.EAST, textFieldTPMomentumFactor, -10, SpringLayout.WEST, buttonTPTrain);
        trainingPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldTPMomentumFactor, 0, SpringLayout.VERTICAL_CENTER, labelTPMomentumFactor);
        trainingPanel.add(textFieldTPMomentumFactor);
        
        labelTPErrorThreshold = new JLabel("Error Threshold:");
        trainingPanelLayout.putConstraint(SpringLayout.WEST, labelTPErrorThreshold, 0, SpringLayout.WEST, labelTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.NORTH, labelTPErrorThreshold, 10, SpringLayout.SOUTH, labelTPMomentumFactor);
        trainingPanel.add(labelTPErrorThreshold);

        textFieldTPErrorThreshold = new JTextField();
        trainingPanelLayout.putConstraint(SpringLayout.WEST, textFieldTPErrorThreshold, 0, SpringLayout.WEST, textFieldTPTrainingFile);
        trainingPanelLayout.putConstraint(SpringLayout.EAST, textFieldTPErrorThreshold, -10, SpringLayout.WEST, buttonTPTrain);
        trainingPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textFieldTPErrorThreshold, 0, SpringLayout.VERTICAL_CENTER, labelTPErrorThreshold);
        trainingPanel.add(textFieldTPErrorThreshold);

        trainingPanelLayout.putConstraint(SpringLayout.EAST, buttonTPTrain, -10, SpringLayout.EAST, trainingPanel);
        trainingPanelLayout.putConstraint(SpringLayout.NORTH, buttonTPTrain, -10, SpringLayout.SOUTH, labelTPLearningRate);
        trainingPanelLayout.putConstraint(SpringLayout.SOUTH, buttonTPTrain, 10, SpringLayout.NORTH, labelTPErrorThreshold);
        trainingPanel.add(buttonTPTrain);
        buttonTPTrain.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (ann == null) {
                        labelStatus.setText("No ANN currently active.");
                        return;
                    }
                    if (textFieldTPTrainingFile.getText().equals("")) {
                        labelStatus.setText("No training file selected.");
                        return;
                    }
                    if (textFieldTPLearningRate.getText().equals("")) {
                        labelStatus.setText("Learning rate is missing.");
                        return;
                    }
                    if (textFieldTPMomentumFactor.getText().equals("")) {
                        labelStatus.setText("Momentum factor is missing.");
                        return;
                    }
                    if (textFieldTPErrorThreshold.getText().equals("")) {
                        labelStatus.setText("Error threshold is missing.");
                        return;
                    }
                    int i, j;
                    int len = ann.InputSize() + ann.OutputSize();
                    int ipLen = ann.InputSize();
                    int opLen = len - ipLen;
                    double[][] input;
                    double[] temp;
                    int[][] output;
                    ArrayList list = new ArrayList();
                    for (String line : Files.readAllLines(Paths.get(textFieldTPTrainingFile.getText()))) {
                        list.add(StringToDoubleArray(line, len));
                    }
                    input = new double[list.size()][];
                    output = new int[list.size()][];
                    for (i = 0; i < list.size(); i++) {
                        input[i] = new double[ipLen];
                        output[i] = new int[opLen];
                        temp = (double[]) list.get(i);
                        for (j = 0; j < ipLen; j++)
                            input[i][j] = temp[j];
                        for (j = 0; j < opLen; j++)
                            output[i][j] = (int) temp[ipLen + j];
                    }
                    labelStatus.setText("Training now...");
                    ann.train(input, output, Double.parseDouble(textFieldTPLearningRate.getText()), Double.parseDouble(textFieldTPMomentumFactor.getText()), Double.parseDouble(textFieldTPErrorThreshold.getText()));
                    labelStatus.setText("Training successful.");
                } catch (ArrayIndexOutOfBoundsException | IOException ex) {
                    labelStatus.setText("Invalid training file.");
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        defaultPanel.add(trainingPanel);
    }

    /** Converts a space separated list of numbers to an integer array and returns the array.
     * 
     * @param text The space separated string of numbers.
     * @param size The size of the array.
     * @return The integer array having numbers given in the "text" parameter.
     */
    private int[] StringToIntArray(String text) {
        ArrayList values = new ArrayList();
        int pos1 = 0, pos2;
        text = text.trim() + " ";
        pos2 = text.indexOf(" ", pos1);
        while (pos2 != -1) {
            values.add(Integer.parseInt(text.substring(pos1, pos2)));
            pos1 = pos2 + 1;
            pos2 = text.indexOf(" ", pos1);
        }
        int[] arr = new int[values.size()];
        for (int i = arr.length - 1; i >= 0; i--)
            arr[i] = (int) values.get(i);
        return arr;
    }

    /** Converts a space separated list of numbers to a double array and returns the array.
     * 
     * @param text The space separated string of numbers.
     * @param size The size of the array.
     * @return The double array having numbers given in the "text" parameter.
     */
    private double[] StringToDoubleArray(String text, int size) {
        try {
            double[] values = new double[size];
            int pos1 = 0, pos2, i = 0;
            text = text.trim() + " ";
            do {
                pos2 = text.indexOf(" ", pos1);
                values[i++] = Double.parseDouble(text.substring(pos1, pos2));
                pos1 = pos2 + 1;
            } while (i < size && pos1 != 0);
            return values;
        } catch (StringIndexOutOfBoundsException ex) {
            labelStatus.setText("Invalid input file.");
        }
        return null;
    }

    /** This method encapsulates the select file operation using a file selection dialog box.
     * 
     * @return The name of the selected file, including its path.
     */
    private String SelectFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();
        return "";
    }
}
