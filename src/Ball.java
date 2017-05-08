import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
    private double radius;
    private Color color;

    public Ball()
    {
        px = 395.0;
        py = 350.0;
        color = Color.white;
        radius = 10.0;
        //deltaX = Math.random() > 0.5 ? 1.5 : -1.5;
        deltaX = 0.0;
        deltaY = 1.0;
        //deltaY = 0;
    }

    public void draw(Graphics2D g) {
        Rectangle2D r = this.getR2D();
        g.setPaint(this.color);
        g.fill(r);
    }

    public void update(int width, int height, int speed) {
        px += speed * deltaX;
        py += speed * deltaY;
        if (px + radius >= width) {
            px = width - radius;
            deltaX *= -1;
        } else if (px < 0) {
            px = 0;
            deltaX *= -1;
        }
        if (py + radius >= height) {
            py = height - radius;
            deltaY *= -1;
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
        return radius;
    }

    public boolean checkCollision(Paddle paddle,int speed) {

        Rectangle2D rc = new Rectangle2D.Double(px,py,radius,radius);

        if(rc.intersects(paddle.getPx(),paddle.getPy(),paddle.getW(),paddle.getH()))
        {
            if(this.py+this.radius > paddle.getPy() + speed) {
                this.deltaX *= -1;
                return true;
            } else {
                double p = 0.0;
                if((this.px + radius/2) > paddle.getPx() && (this.px + radius/2) < (paddle.getPx()+paddle.getW())) {
                    p = this.px + radius/2 - paddle.getPx(); //stedny bod dotyku lopty
                } else if(this.px < (double) paddle.getPx()) {
                    p = this.px + this.radius - paddle.getPx();
                } else if(this.px + this.radius > (double) (paddle.getPx() + paddle.getW())) {
                    p = this.px - paddle.getPx();
                }
                double w = paddle.getW();                       //šírka padla
                double perc = (p*100)/(w/2);                    //percento zo šírky padla
                int n = (deltaX < 0.0) ? -1 : 1;                // nasobic z ktorej strany ide lopta aby pokracovala v smere
                if((this.px + radius/2) > (paddle.getPx() + paddle.getW()/2))
                {
                    double y = 100.0 - (perc - 100.0);
                    deltaY = y * -0.01;
                    double x = 100.0 - y;
                    deltaX = x * 0.015 * n;
                    //System.out.println("y " + y*-0.01 + " x " + x * 0.015);
                    return true;
                } else {
                    deltaY = perc * -0.01;
                    double x = 100.0 - perc;
                    n = (deltaX == 0.0) ? -1 : n;       //ak lopta prichadza kolmo, aby sa odrazila do lavej strany
                    deltaX = x * 0.015 * n;
                    return true;

                }
            }
        }
        return false;
    }

    public void checkCollision(ArrayList<Brick> bricks,int speed) {
        Rectangle2D rec = this.getR2D();
        for(int i = 0; i < bricks.size();i++){
            if(!bricks.get(i).getVisibility())return;
            Rectangle2D r2 = new Rectangle2D.Double(bricks.get(i).getPx(),bricks.get(i).getPy(),bricks.get(i).getW(),bricks.get(i).getH());
            if(rec.intersects(r2))
            {
                if(r2.intersectsLine(rec.getX(),rec.getY()+speed,rec.getX(),rec.getY()+rec.getWidth()-speed)) {
                    deltaX *= -1;
                    bricks.get(i).setHidden();
                    System.out.println("lavy");
                    return;
                }
                if(r2.intersectsLine(rec.getX()+speed,rec.getY(),rec.getX()+rec.getWidth()-speed,rec.getY())) {
                    deltaY *= -1;
                    bricks.get(i).setHidden();
                    System.out.println("horny");
                    return;
                }
                if(r2.intersectsLine(rec.getX()+rec.getWidth(),rec.getY()+speed,rec.getX()+rec.getWidth(),rec.getY()+rec.getWidth()-speed)) {
                    deltaX *= -1;
                    bricks.get(i).setHidden();
                    System.out.println("pravy");
                    return;
                }
                if(r2.intersectsLine(rec.getX()+speed,rec.getY()+rec.getWidth(),rec.getX()+rec.getWidth()-speed,rec.getY()+rec.getWidth())) {
                    deltaY *= -1;
                    bricks.get(i).setHidden();
                    System.out.println("dolny");
                    return;
                }
            }
        }
    }

    public Rectangle2D getR2D()
    {
        return new Rectangle2D.Double(this.px,this.py,this.radius,this.radius);
    }
}
