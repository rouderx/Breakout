import java.awt.*;

/**
 * Created by lukas on 04.05.2017.
 */
public class Brick {

    private int px;
    private int py;
    private int x;
    private int y;
    private int w;
    private int h;

    private Color color;

    private boolean visible = true;

    //private final int top = 100;


    public Brick(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.w =100;
        this.h =30;
        this.px = x*this.w;
        this.py = y*this.h;
    }

    public void paint(Graphics2D g2d) {
        if(!visible)return;
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

    public boolean getVisibility() {
        return visible;
    }

    public void setHidden() {
        visible = false;
    }
}
