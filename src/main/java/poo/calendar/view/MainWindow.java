package poo.calendar.view;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class MainWindow extends JFrame {
    /** Eclipse force this variable to exist. */
	private static final long serialVersionUID = 9110319843067855971L;

	public MainWindow(){
    	initUI();
    }
	
	private void initUI() {
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainWindow ex = new MainWindow();
            ex.setVisible(true);
        });
    }
}
