import Game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lukas on 04.05.2017.
 */
public class Breakout extends JFrame {
    
    public Breakout()
    {
        initFrame();
    }

    private void initFrame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Breakout");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                //frame.setUndecorated(true);
                String name= JOptionPane.showInputDialog("Zadaj svoje meno: ");
                frame.add(new Game(name));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new Breakout();
    }


}
