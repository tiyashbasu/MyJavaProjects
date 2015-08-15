package annwithbackpropagation;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Tiyash Basu
 */
public class MainUI extends JFrame {
    private ANN ann;
    private JFileChooser fileChooser;
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
    
    MainUI() {
        this.frameInit();
        this.getContentPane().setPreferredSize(new Dimension(400, 210));
        InitUI();
    }
    
    private void InitUI() {
        this.setTitle("ANN Creator & Trainer");
        //Init Panel Switching Buttons
        panelMain = new JPanel();
        layoutMain = new SpringLayout();
        panelMain.setLayout(layoutMain);
        this.add(panelMain);
        buttonCreateANNPanel = new JButton("ANN Creation Panel");
        layoutMain.putConstraint(SpringLayout.WEST, buttonCreateANNPanel, 5, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, buttonCreateANNPanel, 5, SpringLayout.NORTH, panelMain);
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
        panelMain.add(buttonCreateANNPanel);
        buttonTrainANNPanel = new JButton("ANN Training Panel");
        layoutMain.putConstraint(SpringLayout.WEST, buttonTrainANNPanel, 2, SpringLayout.EAST, buttonCreateANNPanel);
        layoutMain.putConstraint(SpringLayout.NORTH, buttonTrainANNPanel, 5, SpringLayout.NORTH, panelMain);
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
        panelMain.add(buttonTrainANNPanel);
        buttonOperateANNPanel = new JButton("ANN Operation Panel");
        layoutMain.putConstraint(SpringLayout.WEST, buttonOperateANNPanel, 2, SpringLayout.EAST, buttonTrainANNPanel);
        layoutMain.putConstraint(SpringLayout.NORTH, buttonOperateANNPanel, 5, SpringLayout.NORTH, panelMain);
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
        panelMain.add(buttonOperateANNPanel);
        labelStatus = new JLabel("");
        layoutMain.putConstraint(SpringLayout.WEST, labelStatus, 10, SpringLayout.WEST, panelMain);
        layoutMain.putConstraint(SpringLayout.NORTH, labelStatus, -20, SpringLayout.SOUTH, panelMain);
        panelMain.add(labelStatus);
        //Init Create ANN Panel
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
        buttonCreateANN.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int len = Integer.parseInt(textFieldANNDesc1.getText());
                    int[] nodes = new int[len];
                    int pos = 0;
                    for (int i = 0; i <= len - 1; i++) {
                        nodes[i] = Integer.parseInt((textFieldANNDesc2.getText() + " ").substring(pos, (textFieldANNDesc2.getText() + " ").indexOf(" ", pos)));
                        pos = textFieldANNDesc2.getText().indexOf(" ", pos) + 1;
                        if (pos == 0)
                            break;
                    }
                    for (int i = 0; i <= len - 1; i++)
                        if (nodes[i] == 0) {
                            labelStatus.setText("Incorrect number of nodes. Please check input.");
                            return;
                        }
                    ann = new ANN(nodes);
                    labelStatus.setText("ANN created.");
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
        panelCreate.add(buttonCreateANN);
        buttonSaveANNFromCreate = new JButton("Save ANN");
        layoutCreatePanel.putConstraint(SpringLayout.WEST, buttonSaveANNFromCreate, 10, SpringLayout.EAST, buttonCreateANN);
        layoutCreatePanel.putConstraint(SpringLayout.NORTH, buttonSaveANNFromCreate, 0, SpringLayout.NORTH, buttonCreateANN);
        buttonSaveANNFromCreate.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ann == null) {
                    labelStatus.setText("Please create an ANN first.");
                    return;
                }
                fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(panelCreate) == JFileChooser.APPROVE_OPTION) {
                    try {
                        ann.Save(fileChooser.getSelectedFile().getPath());
                        labelStatus.setText("ANN saved.");
                    } catch (IOException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
        panelCreate.add(buttonSaveANNFromCreate);
        //Init Training Panel Controls
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
        buttonLoadANNTrain.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileChooser.showOpenDialog(panelTrain) == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileInputStream fIn = new FileInputStream(fileChooser.getSelectedFile().getPath());
                        ObjectInputStream oIn = new ObjectInputStream(fIn);
                        ann = (ANN) oIn.readObject();
                        labelStatus.setText("ANN loaded.");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
        panelTrain.add(buttonLoadANNTrain);
        labelSelectTrainingFile = new JLabel("Enter training file path:");
        layoutTrainPanel.putConstraint(SpringLayout.WEST, labelSelectTrainingFile, 10, SpringLayout.WEST, panelTrain);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, labelSelectTrainingFile, 10, SpringLayout.SOUTH, buttonLoadANNTrain);
        panelTrain.add(labelSelectTrainingFile);
        buttonSelectTrainingFile = new JButton("Select File");
        layoutTrainPanel.putConstraint(SpringLayout.EAST, buttonSelectTrainingFile, -10, SpringLayout.EAST, panelTrain);
        layoutTrainPanel.putConstraint(SpringLayout.NORTH, buttonSelectTrainingFile, -4, SpringLayout.NORTH, labelSelectTrainingFile);
        buttonSelectTrainingFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(panelTrain) == JFileChooser.APPROVE_OPTION)
                    textFieldSelectTrainingFile.setText(fileChooser.getSelectedFile().getPath());
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
        //Init Operation Panel Controls
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
        buttonLoadANNOperation.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileChooser.showOpenDialog(panelTrain) == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileInputStream fIn = new FileInputStream(fileChooser.getSelectedFile().getPath());
                        ObjectInputStream oIn = new ObjectInputStream(fIn);
                        ann = (ANN) oIn.readObject();
                        labelStatus.setText("ANN loaded.");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
        panelOperation.add(buttonLoadANNOperation);
        labelInputFile = new JLabel("Enter input file path:");
        layoutOperationPanel.putConstraint(SpringLayout.WEST, labelInputFile, 10, SpringLayout.WEST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, labelInputFile, 10, SpringLayout.SOUTH, buttonLoadANNOperation);
        panelOperation.add(labelInputFile);
        buttonSelectInputFile = new JButton("Select File");
        layoutOperationPanel.putConstraint(SpringLayout.EAST, buttonSelectInputFile, -10, SpringLayout.EAST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, buttonSelectInputFile, -4, SpringLayout.NORTH, labelInputFile);
        buttonSelectInputFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(panelOperation) == JFileChooser.APPROVE_OPTION)
                    textFieldInputFile.setText(fileChooser.getSelectedFile().getPath());
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
        buttonSelectOutputFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(panelOperation) == JFileChooser.APPROVE_OPTION)
                    textFieldOutputFile.setText(fileChooser.getSelectedFile().getPath());
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
        panelOperation.add(buttonSelectOutputFile);
        textFieldOutputFile = new JTextField();
        layoutOperationPanel.putConstraint(SpringLayout.WEST, textFieldOutputFile, 10, SpringLayout.EAST, labelOutputFile);
        layoutOperationPanel.putConstraint(SpringLayout.EAST, textFieldOutputFile, -10, SpringLayout.WEST, buttonSelectOutputFile);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, textFieldOutputFile, 2, SpringLayout.NORTH, buttonSelectOutputFile);
        panelOperation.add(textFieldOutputFile);
        buttonGenerateOutput = new JButton("Generate Output");
        layoutOperationPanel.putConstraint(SpringLayout.WEST, buttonGenerateOutput, 10, SpringLayout.WEST, panelOperation);
        layoutOperationPanel.putConstraint(SpringLayout.NORTH, buttonGenerateOutput, 20, SpringLayout.SOUTH, labelOutputFile);
        buttonGenerateOutput.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    double[][] inputs;
                    int[] outputs;
                    int i, j;
                    List<String> inputLines = Files.readAllLines(Paths.get(textFieldInputFile.getText()));
                    inputs = new double[inputLines.size()][];
                    for (i = inputLines.size() - 1; i >= 0; i--) {
                        try {
                            int len = Integer.parseInt(inputLines.get(i));
                            int[] nodes = new int[len];
                            int pos = 0;
                            for (int i = 0; i <= len - 1; i++) {
                                nodes[i] = Integer.parseInt((textFieldANNDesc2.getText() + " ").substring(pos, (textFieldANNDesc2.getText() + " ").indexOf(" ", pos)));
                                pos = textFieldANNDesc2.getText().indexOf(" ", pos) + 1;
                                if (pos == 0)
                                    break;
                            }
                            for (int i = 0; i <= len - 1; i++)
                                if (nodes[i] == 0) {
                                    labelStatus.setText("Incorrect number of nodes. Please check input.");
                                    return;
                                }
                            ann = new ANN(nodes);
                            labelStatus.setText("ANN created.");
                        }
                        catch (NumberFormatException ex) {
                            labelStatus.setText("Illegal number format found. Please check input.");
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
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
        panelOperation.add(buttonGenerateOutput);
        panelOperation.setVisible(false);
        this.pack();
    }
}
