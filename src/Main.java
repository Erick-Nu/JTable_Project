import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /* VENTANA */
        JFrame loginFrame = new JFrame("Carros");
        loginFrame.setContentPane(new VentanaCarros().VentanaOne);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(800, 600);
        loginFrame.setPreferredSize(new Dimension(800, 600));
        loginFrame.pack();
        loginFrame.setVisible(true);
    }
}