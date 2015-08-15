package annwithbackpropagation;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Tiyash Basu
 */
public class MainUI extends JFrame {
    private ANN ann;

    //<editor-fold defaultstate="collapsed" desc="UI controls declarations">
    //Panel Switching Controls
    JPanel panelMain;
    SpringLayout layoutMain;
    JButton buttonCreateANNPanel, buttonTrainANNPanel, buttonOperateANNPanel;
    JLabel labelStatus;
    //Creation Panel Controls
    JPanel panelCreate;
    SpringLayout layoutCreatePanel;
    JLabel labelANNDesc1, labelANNDesc2;
    JTextField textFieldANNDesc1, textFieldANNDesc2;
    JButton buttonCreateANN, buttonSaveANNFromCreate;
    //Training Panel Controls
    SpringLayout layoutTrainPanel;
    JPanel panelTrain;
    JLabel labelSelectTrainingFile;
    JTextField textFieldSelectTrainingFile;
    JButton buttonLoadANNTrain, buttonSelectTrainingFile, buttonTrainANN, buttonSaveANNFromTrain;
    //Operation Panel Controls
    SpringLayout layoutOperationPanel;
    JPanel panelOperation;
    JLabel labelInputFile,labelOutputFile;
    JTextField textFieldInputFile, textFieldOutputFile;
    JButton buttonLoadANNOperation, buttonSelectInputFile, buttonSelectOutputFile, buttonGenerateOutput;
    //</editor-fold>

    MainUI() {
        this.frameInit();
        this.getContentPane().setPreferredSize(new Dimension(400, 210));
        InitUI();
    }

    /** Converts a space separated list of numbers to an integer array and returns the array.
     * 
     * @param text The space separated string of numbers.
     * @param size The size of the array.
     * @return The integer array having numbers given in the "text" parameter.
     */
    private int[] StringToIntArray(String text, int size) {
        int[] values = new int[size];
        int pos1 = 0, pos2, i = 0;
        text = text.trim() + " ";
        do {
            pos2 = text.indexOf(" ", pos1);
            values[i++] = Integer.parseInt(text.substring(pos1, pos2));
            pos1 = pos2 + 1;
        } while (i < size && pos1 != 0);
        return values;
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

    /** This method saves the currently loaded ANN.
     * 
     */
    private void SaveANN() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
            ann.Save(fileChooser.getSelectedFile().getPath());
            labelStatus.setText("ANN Saved");
        }
    }

    /** This method loads an ANN from file.
     * 
     */
    private void LoadANN() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(panelTrain) == JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fIn = new FileInputStream(fileChooser.getSelectedFile().getPath());
                ObjectInputStream oIn = new ObjectInputStream(fIn);
                ann = (ANN) oIn.readObject();
                labelStatus.setText("ANN loaded.");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                labelStatus.setText("File not found.");
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                labelStatus.setText("Incorrect file format.");
            }
        }
    }

    /** This method encapsulates the select file operation using a file selection dialog box.
     * 
     * @return The name of the selected file, including its path.
     */
    private String SelectFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(panelTrain) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();
        return "";
    }

    /**This method initializes all the UI controls and the associated events.
     * 
     */
    private void InitUI() {
        this.setTitle("ANN Creator & Trainer");
        //<editor-fold defaultstate="collapsed" desc="Main panel controls initialization">
        //Main panel controls initialization
        panelMain = new JPanel();
        layoutMain = new SpringLayout();
        panelMain.setLayout(layoutMain);
        this.add(panelMain);

        buttonCreateANNPanel = new JButton("ANN Creation Panel");
        layoutMain.putConstraint(SpringLayout.WEST, buttonCreateANNPanel, 5, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, buttonCreateANNPanel, 5, SpringLayout.NORTH, panelMain);
        panelMain.add(buttonCreateANNPanel);

        buttonTrainANNPanel = new JButton("ANN Training Panel");
        layoutMain.putConstraint(SpringLayout.WEST, buttonTrainANNPanel, 2, SpringLayout.EAST, buttonCreateANNPanel);
        layoutMain.putConstraint(SpringLayout.NORTH, buttonTrainANNPanel, 5, SpringLayout.NORTH, panelMain);
        panelMain.add(buttonTrainANNPanel);

        buttonOperateANNPanel = new JButton("ANN Operation Panel");
        layoutMain.putConstraint(SpringLayout.WEST, buttonOperateANNPanel, 2, SpringLayout.EAST, buttonTrainANNPanel);
        layoutMain.putConstraint(SpringLayout.NORTH, buttonOperateANNPanel, 5, SpringLayout.NORTH, panelMain);
        panelMain.add(buttonOperateANNPanel);

        labelStatus = new JLabel("");
        layoutMain.putConstraint(SpringLayout.WEST, labelStatus, 10, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, labelStatus, -20, SpringLayout.SOUTH, panelMain);
        panelMain.add(labelStatus);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Event listeners for main panel controls">
        buttonCreateANNPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelTrain.setVisible(false);
                panelOperation.setVisible(false);
                panelCreate.setVisible(true);
                labelStatus.setText("");
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
        buttonTrainANNPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelOperation.setVisible(false);
                panelCreate.setVisible(false);
                panelTrain.setVisible(true);
                labelStatus.setText("");
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
        buttonOperateANNPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelCreate.setVisible(false);
                panelTrain.setVisible(false);
                panelOperation.setVisible(true);
                labelStatus.setText("");
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
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Creation panel controls initialization">
        //Creation panel controls initialization
        panelCreate = new JPanel();
        layoutCreatePanel = new SpringLayout();
        panelCreate.setLayout(layoutCreatePanel);
        layoutMain.putConstraint(SpringLayout.EAST, panelCreate, 0, SpringLayout.EAST, panelMain);
        layoutMain.putConstraint(SpringLayout.WEST, panelCreate, 0, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, panelCreate, 40, SpringLayout.NORTH, panelMain);
        layoutMain.putConstraint(SpringLayout.SOUTH, panelCreate, -10, SpringLayout.NORTH, labelStatus);
        panelMain.add(panelCreate);

        labelANNDesc1 = new JLabel("Enter number of layers:");
        layoutCreatePanel.putConstraint(SpringLayout.WEST, labelANNDesc1, 10, SpringLayout.WEST, panelCreate);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, labelANNDesc1, 10, SpringLayout.NORTH, panelCreate);
        panelCreate.add(labelANNDesc1);

        labelANNDesc2 = new JLabel("Enter number of nodes in layers:");
        layoutCreatePanel.putConstraint(SpringLayout.WEST, labelANNDesc2, 10, SpringLayout.WEST, panelCreate);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, labelANNDesc2, 20, SpringLayout.SOUTH, labelANNDesc1);
        panelCreate.add(labelANNDesc2);

        textFieldANNDesc1 = new JTextField();
        layoutCreatePanel.putConstraint(SpringLayout.EAST, textFieldANNDesc1, -10, SpringLayout.EAST, panelCreate);
        layoutCreatePanel.putConstraint(SpringLayout.WEST, textFieldANNDesc1, 10, SpringLayout.EAST, labelANNDesc1);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, textFieldANNDesc1, -2, SpringLayout.NORTH, labelANNDesc1);
        panelCreate.add(textFieldANNDesc1);

        textFieldANNDesc2 = new JTextField();
        layoutCreatePanel.putConstraint(SpringLayout.EAST, textFieldANNDesc2, -10, SpringLayout.EAST, panelCreate);
        layoutCreatePanel.putConstraint(SpringLayout.WEST, textFieldANNDesc2, 10, SpringLayout.EAST, labelANNDesc2);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, textFieldANNDesc2, -2, SpringLayout.NORTH, labelANNDesc2);
        panelCreate.add(textFieldANNDesc2);

        buttonCreateANN = new JButton("Create ANN");
        layoutCreatePanel.putConstraint(SpringLayout.WEST, buttonCreateANN, 10, SpringLayout.WEST, panelCreate);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, buttonCreateANN, 20, SpringLayout.SOUTH, labelANNDesc2);
        panelCreate.add(buttonCreateANN);

        buttonSaveANNFromCreate = new JButton("Save ANN");
        layoutCreatePanel.putConstraint(SpringLayout.WEST, buttonSaveANNFromCreate, 10, SpringLayout.EAST, buttonCreateANN);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, buttonSaveANNFromCreate, 0, SpringLayout.NORTH, buttonCreateANN);
        panelCreate.add(buttonSaveANNFromCreate);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Event listeners for creation panel controls">
        buttonCreateANN.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int len = Integer.parseInt(textFieldANNDesc1.getText());
                    int[] nodes = StringToIntArray(textFieldANNDesc2.getText(), len);
                    for (int i = 0; i <= len - 1; i++)
                        if (nodes[i] == 0) {
                            labelStatus.setText("Incorrect number of nodes. Please check input.");
                            return;
                        }
                    ann = new ANN(nodes);
                    labelStatus.setText("ANN created and loaded.");
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
        buttonSaveANNFromCreate.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ann == null) {
                    labelStatus.setText("Please create an ANN first.");
                    return;
                }
                SaveANN();
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
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Training panel controls initialization">
        //Training panel controls initialization
        panelTrain = new JPanel();
        layoutTrainPanel = new SpringLayout();
        panelTrain.setLayout(layoutTrainPanel);
        layoutMain.putConstraint(SpringLayout.EAST, panelTrain, 0, SpringLayout.EAST, panelMain);
        layoutMain.putConstraint(SpringLayout.WEST, panelTrain, 0, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, panelTrain, 40, SpringLayout.NORTH, panelMain);
        layoutMain.putConstraint(SpringLayout.SOUTH, panelTrain, -10, SpringLayout.SOUTH, labelStatus);
        panelMain.add(panelTrain);

        buttonLoadANNTrain = new JButton("Load ANN");
        layoutTrainPanel.putConstraint(SpringLayout.WEST, buttonLoadANNTrain, 10, SpringLayout.WEST, panelTrain);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, buttonLoadANNTrain, 10, SpringLayout.NORTH, panelTrain);
        panelTrain.add(buttonLoadANNTrain);

        labelSelectTrainingFile = new JLabel("Enter training file path:");
        layoutTrainPanel.putConstraint(SpringLayout.WEST, labelSelectTrainingFile, 10, SpringLayout.WEST, panelTrain);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, labelSelectTrainingFile, 10, SpringLayout.SOUTH, buttonLoadANNTrain);
        panelTrain.add(labelSelectTrainingFile);

        buttonSelectTrainingFile = new JButton("Select File");
        layoutTrainPanel.putConstraint(SpringLayout.EAST, buttonSelectTrainingFile, -10, SpringLayout.EAST, panelTrain);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, buttonSelectTrainingFile, -4, SpringLayout.NORTH, labelSelectTrainingFile);
        panelTrain.add(buttonSelectTrainingFile);

        textFieldSelectTrainingFile = new JTextField();
        layoutTrainPanel.putConstraint(SpringLayout.WEST, textFieldSelectTrainingFile, 10, SpringLayout.EAST, labelSelectTrainingFile);
        layoutTrainPanel.putConstraint(SpringLayout.EAST, textFieldSelectTrainingFile, -10, SpringLayout.WEST, buttonSelectTrainingFile);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, textFieldSelectTrainingFile, 2, SpringLayout.NORTH, buttonSelectTrainingFile);
        panelTrain.add(textFieldSelectTrainingFile);

        buttonTrainANN = new JButton("Train ANN");
        layoutTrainPanel.putConstraint(SpringLayout.WEST, buttonTrainANN, 10, SpringLayout.WEST, panelTrain);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, buttonTrainANN, 20, SpringLayout.SOUTH, labelSelectTrainingFile);
        panelTrain.add(buttonTrainANN);

        buttonSaveANNFromTrain = new JButton("Save ANN");
        layoutTrainPanel.putConstraint(SpringLayout.WEST, buttonSaveANNFromTrain, 10, SpringLayout.EAST, buttonTrainANN);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, buttonSaveANNFromTrain, 0, SpringLayout.NORTH, buttonTrainANN);
        panelTrain.add(buttonSaveANNFromTrain);

        panelTrain.setVisible(false);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Event listeners for training panel controls">
        buttonLoadANNTrain.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoadANN();
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
        buttonSelectTrainingFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textFieldSelectTrainingFile.setText(SelectFile());
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
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Operation panel controls initilization">
        //Operation panel controls initilization
        panelOperation = new JPanel();
        layoutOperationPanel = new SpringLayout();
        panelOperation.setLayout(layoutOperationPanel);
        layoutMain.putConstraint(SpringLayout.EAST, panelOperation, 0, SpringLayout.EAST, panelMain);
        layoutMain.putConstraint(SpringLayout.WEST, panelOperation, 0, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, panelOperation, 40, SpringLayout.NORTH, panelMain);
        layoutMain.putConstraint(SpringLayout.SOUTH, panelOperation, -10, SpringLayout.SOUTH, labelStatus);
        panelMain.add(panelOperation);

        buttonLoadANNOperation = new JButton("Load ANN");
        layoutOperationPanel.putConstraint(SpringLayout.WEST, buttonLoadANNOperation, 10, SpringLayout.WEST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, buttonLoadANNOperation, 10, SpringLayout.NORTH, panelOperation);
        panelOperation.add(buttonLoadANNOperation);

        labelInputFile = new JLabel("Enter input file path:");
        layoutOperationPanel.putConstraint(SpringLayout.WEST, labelInputFile, 10, SpringLayout.WEST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, labelInputFile, 10, SpringLayout.SOUTH, buttonLoadANNOperation);
        panelOperation.add(labelInputFile);

        buttonSelectInputFile = new JButton("Select File");
        layoutOperationPanel.putConstraint(SpringLayout.EAST, buttonSelectInputFile, -10, SpringLayout.EAST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, buttonSelectInputFile, -4, SpringLayout.NORTH, labelInputFile);
        panelOperation.add(buttonSelectInputFile);

        textFieldInputFile = new JTextField();
        layoutOperationPanel.putConstraint(SpringLayout.WEST, textFieldInputFile, 10, SpringLayout.EAST, labelInputFile);
        layoutOperationPanel.putConstraint(SpringLayout.EAST, textFieldInputFile, -10, SpringLayout.WEST, buttonSelectInputFile);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, textFieldInputFile, 2, SpringLayout.NORTH, buttonSelectInputFile);
        panelOperation.add(textFieldInputFile);

        labelOutputFile = new JLabel("Enter output file path:");
        layoutOperationPanel.putConstraint(SpringLayout.WEST, labelOutputFile, 10, SpringLayout.WEST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, labelOutputFile, 20, SpringLayout.SOUTH, labelInputFile);
        panelOperation.add(labelOutputFile);

        buttonSelectOutputFile = new JButton("Select File");
        layoutOperationPanel.putConstraint(SpringLayout.EAST, buttonSelectOutputFile, -10, SpringLayout.EAST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, buttonSelectOutputFile, -4, SpringLayout.NORTH, labelOutputFile);
        panelOperation.add(buttonSelectOutputFile);

        textFieldOutputFile = new JTextField();
        layoutOperationPanel.putConstraint(SpringLayout.WEST, textFieldOutputFile, 10, SpringLayout.EAST, labelOutputFile);
        layoutOperationPanel.putConstraint(SpringLayout.EAST, textFieldOutputFile, -10, SpringLayout.WEST, buttonSelectOutputFile);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, textFieldOutputFile, 2, SpringLayout.NORTH, buttonSelectOutputFile);
        panelOperation.add(textFieldOutputFile);

        buttonGenerateOutput = new JButton("Generate Output");
        layoutOperationPanel.putConstraint(SpringLayout.WEST, buttonGenerateOutput, 10, SpringLayout.WEST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, buttonGenerateOutput, 20, SpringLayout.SOUTH, labelOutputFile);
        panelOperation.add(buttonGenerateOutput);

        panelOperation.setVisible(false);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Event listeners for operation panel controls">
        buttonLoadANNOperation.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoadANN();
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
        buttonSelectInputFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textFieldInputFile.setText(SelectFile());
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
        buttonSelectOutputFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textFieldOutputFile.setText(SelectFile());
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
        buttonGenerateOutput.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int len = ann.InputSize();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(textFieldOutputFile.getText()))));
                    for (String line : Files.readAllLines(Paths.get(textFieldInputFile.getText()))) {
                        ann.FeedInput(StringToDoubleArray(line, len));
                        bw.write(Arrays.toString(ann.GetOutput()));
                        bw.newLine();
                    }
                    bw.close();
                    labelStatus.setText("Output generated.");
                } catch (IOException ex) {
                    Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
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
        //</editor-fold>

        this.pack();
    }
}
