import Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
                String name= JOptionPane.showInputDialog("Zadaj svoje meno: ");

                Game g = new Game(name);

                JFrame frame = new JFrame("Breakout");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.add(g);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        g.saveStats();
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        new Breakout();
    }


}
