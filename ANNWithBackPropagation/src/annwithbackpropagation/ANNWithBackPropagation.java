package annwithbackpropagation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Tiyash Basu
 */
public class ANNWithBackPropagation {
    private static Object mainUI;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            MainUI mainUI = new MainUI();
            mainUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainUI.setVisible(true);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ANNWithBackPropagation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
