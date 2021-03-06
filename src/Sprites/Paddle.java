package Sprites;

import java.awt.*;

/**
 * Created by lukas on 04.05.2017.
 */
public class Paddle{
    private int px;
    private int py;
    private int w;
    private int h;
    private int deltaX;

    private Color color;

    public Paddle()
    {
        px = 360;
        py = 450;
        w = 80;
        h = 10;
        deltaX = 0;
        color = Color.magenta;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(px,py,w,h);
    }

    public void update(int width, double bx, double by, double r, int speed) {
        if(by + r >= py && by <= py + h) {
            if(bx + r + speed < px || bx - speed > px+w) {
                px += speed * deltaX;
            }
        } else {
            px += speed * deltaX;
        }

        if (px + w >= width) {
            px = width - w;
        } else if (px < 0) {
            px = 0;
        }
    }

    public int getPx() {return px;}
    public int getPy() {return py;}
    public int getW() {return w;}
    public int getH() {return h;}

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void resetPosition() {
        px = 360;
        py = 450;
    }
}
