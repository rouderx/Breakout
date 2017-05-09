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
import java.util.Collections;
import java.util.Comparator;
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
    private Player player;
    private ArrayList<Player> topPlayers;

    private int level = 1;
    private int speed = 5;
    private boolean start;  //lopta caka na start
    private boolean gameOver = false;
    public static boolean game = false;
    private Timer timer;
    private boolean paddleChecker = true;

    public Game(String name)
    {
        player = new Player(name);
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(new GridBagLayout());
        //topPlayers = new ArrayList<Player>();
        loadStats();
        showMenu();
    }

    public void initGame() {
        ball = new Ball();
        paddle = new Paddle();
        bricks = new ArrayList<Brick>();
        loadBricks();
        game = true;
        start = true;
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
                    case KeyEvent.VK_P:
                        if(timer.isRunning()) {
                            timer.stop();
                            repaint();
                        } else {
                            timer.start();
                        }
                        break;
                    default:
                        System.out.println(e.getKeyCode());
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                paddle.setDeltaX(0);
                if(start)ball.setDeltaX(0);
            }
        });
        timer = new Timer(13, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ball.update(getWidth(),getHeight(),speed);
                paddle.update(getWidth(),ball.getPx(),ball.getPy(),ball.getRadius(),speed);
                if(ball.getPy() > 350 && paddleChecker){
                    paddleChecker = !ball.checkCollision(paddle,speed);
                    if(ball.getPy()+speed > 450)paddleChecker = true;
                }

                if(ball.getPy() < 350){
                    ball.checkCollision(bricks,speed,player);
                    paddleChecker = true;
                }

                if(!game){
                    gameOver = true;
                    repaint();
                    timer.stop();
                    gameOver();
                    //showMenu();
                }

                if(bricks.size() == 0){
                    level++;
                    //speed++;
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
        if(gameOver) {
            ball.draw(g2d);
            paddle.draw(g2d);
            for (Brick brick : bricks) {
                brick.draw(g2d);
            }
            float alpha = 0.5f;
            g2d.setColor(new Color(0,0,0,alpha));
            g2d.fillRect(0,0,800,500);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("Arial",Font.BOLD,20));
            g2d.drawString("GAME OVER",300,200);
        }
        if(game) {
            ball.draw(g2d);
            paddle.draw(g2d);
            for (Brick brick : bricks) {
                brick.draw(g2d);
            }
            g2d.setColor(Color.white);
            g2d.setFont(new Font("Arial",Font.BOLD,20));
            g2d.drawString(Integer.toString(player.getScore()),10,20);
            if(!timer.isRunning()) {
                g2d.setColor(Color.white);
                g2d.setFont(new Font("Arial",Font.BOLD,50));
                g2d.drawString("Pause",400,250);
            }
        } else if(!gameOver){
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

    private void gameOver() {
        if(topPlayers.size() > 4) {
            if(player.getScore() >= topPlayers.get(topPlayers.size()-1).getScore()) {
                topPlayers.remove(topPlayers.size()-1);
                topPlayers.add(player);
                sortPlayers();
            }
        } else {
            topPlayers.add(player);
            if(topPlayers.size()>1)sortPlayers();
        }
        JButton st = getStatButton();
        JButton home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                gameOver = false;
                showMenu();
                repaint();
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,0,10,0);
        add(st,gbc);
        gbc.insets = new Insets(10,0,10,0);
        gbc.gridy = 1;
        add(home,gbc);
        revalidate();
    }

    private JButton getStatButton() {
        JButton stat = new JButton("Top players");
        stat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame stats = new JFrame("Top 5");
                stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                stats.setResizable(false);
                JPanel jp = new JPanel();
                jp.setBackground(Color.black);
                JLabel lab = new JLabel("Player   |    Score");
                lab.setForeground(Color.white);
                jp.add(lab);
                String[] columnNames = {"Name","Score"};
                ArrayList<Object[]> dat = new ArrayList<>();
                Object[][] data = new Object[5][2];
                for(int i = 0; i < topPlayers.size();i++) {
                     data[i] = new Object[]{topPlayers.get(i).getName(),topPlayers.get(i).getScore()};
                }
                JTable table = new JTable(data,columnNames);
                table.setRowHeight(20);
                jp.add(table);
                jp.setFocusable(true);
                jp.setPreferredSize(new Dimension(200,150));
                stats.add(jp);
                stats.pack();
                stats.setLocationRelativeTo(null);
                stats.setVisible(true);
            }
        });
        return stat;
    }

    private void showMenu() {
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
        JButton stat = getStatButton();
        JButton c = new JButton("Close");
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStats();
                System.exit(0);
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(100,0,10,0);
        add(b,gbc);
        gbc.insets = new Insets(10,0,10,0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(stat,gbc);
        gbc.gridy = 2;
        add(c,gbc);
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

    public void saveStats() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("src/Resources/top5.txt");
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(topPlayers);
            oout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStats() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/Resources/top5.txt"));
            topPlayers = (ArrayList<Player>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(topPlayers.size()>1)sortPlayers();
    }

    private void sortPlayers() {
        Collections.sort(topPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return (o2.getScore() >= o1.getScore())?1:-1;
            }
        });
    }
}