package Game;

import java.io.Serializable;

/**
 * Created by lukas on 09.05.2017.
 */
public class Player implements Serializable{
    private int score;
    private String name;

    public Player(String name) {
        if(name.isEmpty())name="Fantom";
        this.name = name;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
