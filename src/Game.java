import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by lukas on 04.05.2017.
 */
public class Game extends JPanel{
    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 500;

    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;

    private int level = 1;
    private int speed = 3;
    private boolean game = false;
    private Timer timer;

    private boolean paddleChecker = true;

    public Game()
    {
        setBackground(Color.BLACK);
        setFocusable(true);
        ball = new Ball();
        paddle = new Paddle();
        bricks = new ArrayList<Brick>();
        loadBricks();
        game = true;
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                paddle.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                paddle.keyReleased(e);
            }
        });
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ball.update(getWidth(),getHeight(),speed);
                paddle.update(getWidth(),ball.getPx(),ball.getPy(),ball.getRadius(),speed);
                if(ball.getPy() > 350 && paddleChecker){
                    paddleChecker = !ball.checkCollision(paddle,speed);
                }
                if(ball.getPy() < 350){
                    ball.checkCollision(bricks,speed);
                    paddleChecker = true;
                }
                repaint();
                if(!game){timer.stop();}
            }
        });
        timer.start();
        gameOver();
    }

    private void loadBricks() {
        bricks.clear();
        bricks.add(new Brick(5,0,Color.cyan));
    }

    private void gameOver() {
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(B_WIDTH, B_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        ball.draw(g2d);
        paddle.draw(g2d);
        for (Brick brick : bricks) {
            brick.paint(g2d);
        }
        g2d.dispose();
    }

    public void increaseLevel() {
        level++;
        loadBricks();
    }

}
