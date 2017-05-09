package Sprites;

import java.awt.*;

/**
 * Created by lukas on 04.05.2017.
 */
public class Brick {

    private int px;
    private int py;
    private int w;
    private int h;

    private Color color;


    public Brick(int x, int y, Color color) {
        this.color = color;
        this.w =100;
        this.h =30;
        this.px = x*this.w;
        this.py = y*this.h;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(px,py,w,h);
    }

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
