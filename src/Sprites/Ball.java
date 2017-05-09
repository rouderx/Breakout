package Sprites;
import Game.Game;
import Game.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by lukas on 04.05.2017.
 */
public class Ball {
    private double px;
    private double py;
    private double deltaX;
    private double deltaY;
    private double w;
    private Color color;

    public Ball()
    {
        px = 395.0;
        py = 439.0;
        color = Color.white;
        w = 10.0;
        deltaX = 0.0;
        deltaY = 0.0;
    }

    public void draw(Graphics2D g) {
        Rectangle2D r = this.getR2D();
        g.setPaint(this.color);
        g.fill(r);
    }

    public void update(int width, int height, int speed) {
        px += speed * deltaX;
        py += speed * deltaY;
        if (px + w >= width) {
            px = width - w;
            deltaX *= -1;
        } else if (px < 0) {
            px = 0;
            deltaX *= -1;
        }
        if (py + w >= height) {
            py = height - w;
            Game.game = false;
        } else if (py < 0) {
            py = 0;
            deltaY *= -1;
        }
    }

    public double getPy() {
        return py;
    }

    public double getPx() {
        return px;
    }

    public double getRadius() {
        return w;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public boolean checkCollision(Paddle paddle, int speed) {

        Rectangle2D rc = new Rectangle2D.Double(px,py,w,w);

        if(rc.intersects(paddle.getPx(),paddle.getPy(),paddle.getW(),paddle.getH()))
        {
            if(this.py+this.w > paddle.getPy() + speed) {
                this.deltaX *= -1;
                return true;
            } else {
                double p = 0.0;
                if((this.px + w/2) > paddle.getPx() && (this.px + w/2) < (paddle.getPx()+paddle.getW())) {
                    p = this.px + w/2 - paddle.getPx(); //stredny bod dotyku lopty
                } else if(this.px < (double) paddle.getPx()) {
                    p = this.px + this.w - paddle.getPx();
                } else if(this.px + this.w > (double) (paddle.getPx() + paddle.getW())) {
                    p = this.px - paddle.getPx();
                }
                double w = paddle.getW();                       //šírka padla
                double perc = (p*100)/(w/2);                    //percento zo šírky padla
                int n = 1;                                      //do ktorej strany pojde lopta
                if((this.px + this.w/2) > (paddle.getPx() + paddle.getW()/2)) {
                    perc = 100.0 - (perc - 100.0);
                } else {
                    n = -1;
                }
                deltaY = perc * -0.01;
                if(Math.abs(deltaY) < 0.2)deltaY=-0.2;
                double x = 100.0 - perc;
                deltaX = x * 0.01 * n;
                return true;
            }
        }
        return false;
    }

    public void checkCollision(ArrayList<Brick> bricks, int speed, Player player) {
        Rectangle2D rec = this.getR2D();
        for(int i = 0; i < bricks.size();i++){
            Rectangle2D r2 = new Rectangle2D.Double(bricks.get(i).getPx(),bricks.get(i).getPy(),bricks.get(i).getW(),bricks.get(i).getH());
            if(rec.intersects(r2))
            {
                if(r2.intersectsLine(rec.getX(),rec.getY()+speed,rec.getX(),rec.getY()+rec.getWidth()-speed)) {
                    deltaX *= -1;
                    bricks.remove(i);
                    player.setScore(player.getScore()+1);
                    //System.out.println("lavy");
                    break;
                }
                if(r2.intersectsLine(rec.getX()+speed,rec.getY(),rec.getX()+rec.getWidth()-speed,rec.getY())) {
                    deltaY *= -1;
                    bricks.remove(i);
                    player.setScore(player.getScore()+1);
                    //System.out.println("horny");
                    break;
                }
                if(r2.intersectsLine(rec.getX()+rec.getWidth(),rec.getY()+speed,rec.getX()+rec.getWidth(),rec.getY()+rec.getWidth()-speed)) {
                    deltaX *= -1;
                    bricks.remove(i);
                    player.setScore(player.getScore()+1);
                    //System.out.println("pravy");
                    break;
                }
                if(r2.intersectsLine(rec.getX()+speed,rec.getY()+rec.getWidth(),rec.getX()+rec.getWidth()-speed,rec.getY()+rec.getWidth())) {
                    deltaY *= -1;
                    bricks.remove(i);
                    player.setScore(player.getScore()+1);
                    //System.out.println("dolny");
                    break;
                }
            }
        }
    }
    public Rectangle2D getR2D()
    {
        return new Rectangle2D.Double(this.px,this.py,this.w,this.w);
    }

    public void resetPosition() {
        px = 395.0;
        py = 439.0;
        deltaX = 0.0;
        deltaY = 0.0;
    }
}
