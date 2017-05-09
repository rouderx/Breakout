package Game;

import Sprites.Ball;
import Sprites.Brick;
import Sprites.Paddle;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private int speed = 5;
    private boolean start;  //lopta caka na start
    public static boolean game = false;
    private Timer timer;
    private boolean paddleChecker = true;

    public Game()
    {
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(new GridBagLayout());

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        paddle.setDeltaX(-1);
                        if(start)ball.setDeltaX(-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        paddle.setDeltaX(1);
                        if(start)ball.setDeltaX(+1);
                        break;
                    case KeyEvent.VK_SPACE:
                        if(start) {
                            ball.setDeltaY(-1);
                            start = false;
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                paddle.setDeltaX(0);
                if(start)ball.setDeltaX(0);
            }
        });
        showMenu();
    }

    public void initGame() {
        ball = new Ball();
        paddle = new Paddle();
        bricks = new ArrayList<Brick>();
        loadBricks();
        game = true;
        start = true;

        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ball.update(getWidth(),getHeight(),speed);
                paddle.update(getWidth(),ball.getPx(),ball.getPy(),ball.getRadius(),speed);
                if(ball.getPy() > 350 && paddleChecker){
                    paddleChecker = !ball.checkCollision(paddle,speed);
                    if(ball.getPy()+speed > 450)paddleChecker = true;
                }

                if(ball.getPy() < 350){
                    ball.checkCollision(bricks,speed);
                    paddleChecker = true;
                }

                if(!game){
                    showMenu();
                    timer.stop();
                }
                System.out.println(bricks.size());
                if(bricks.size() == 0){
                    level++;
                    speed++;
                    start = true;
                    resetPositions();
                    loadBricks();
                }
                repaint();
            }
        });
        timer.start();
    }

    private void resetPositions() {
        ball.resetPosition();
        paddle.resetPosition();
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
        if(game) {
            ball.draw(g2d);
            paddle.draw(g2d);
            for (Brick brick : bricks) {
                brick.draw(g2d);
            }
        } else {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("src/Resources/Breakout.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int w = img.getWidth(null);
            int x = (this.getWidth()-w)/2;
            g2d.drawImage(img, x, 0, null);
        }
        g2d.dispose();
    }

    private void gameOver(Graphics2D g) {
        g.setColor(Color.magenta);
        g.drawString("Game Over",400,250);
    }

    private void showMenu() {
        Game g = this;
        JButton b = new JButton("Start");
        b.setAlignmentY(200);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                revalidate();
                repaint();
                initGame();
            }
        });
        JButton c = new JButton("Close");
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,0,10,0);
        g.add(b,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        g.add(c,gbc);
        revalidate();
        repaint();
    }

    private void loadBricks() {
        bricks.clear();
        String line = null;
        try {
            BufferedReader br=new BufferedReader(new FileReader("src/Resources/levels.txt"));
            while((line=br.readLine())!=null){
                Pattern p = Pattern.compile(level + "\\[.+\\]");
                Matcher m = p.matcher(line);
                if(m.find()) {
                    break;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        line = line.replace(level+"[","");
        line = line.replace("]","");
        String[] levels = line.split(";");
        for(int i = 0; i < levels.length; i++) {
            levels[i] = levels[i].replace("{","");
            levels[i] = levels[i].replace("}","");
            String[] brick = levels[i].split(",");
            bricks.add(new Brick(Integer.parseInt(brick[0]),Integer.parseInt(brick[1]),new Color(Integer.parseInt(brick[2]),Integer.parseInt(brick[3]),Integer.parseInt(brick[4]))));
        }
    }
}