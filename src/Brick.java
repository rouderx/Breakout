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

    private boolean visible = true;

    private final int top = 100;


    public Brick(int px, int py, Color color) {
        this.px = px;
        this.py = py;
        this.color = color;
        this.w =100;
        this.h =20;
    }

    public void paint(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(px*w,(py*h)+top,w,h);
    }

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py*+top;
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
