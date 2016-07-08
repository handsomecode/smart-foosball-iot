package is.handsome.labs.iotfoosball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

class Player {

    private String nick;
    private String avatar;
    private int ind;
    private int win;
    private int lose;

    private Player() {}

    public Player(String nick, String avatar) {
        this.nick = nick;
        this.avatar = avatar;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }
}

class Game {
    private String datestart;
    private String dateend;
    private String idplayer11;
    private String idplayer12;
    private String idplayer21;
    private String idplayer22;
    private String mode;
    private int score1;
    private int score2;

    public Game() {}

    public Game(String datestart, String dateend,
                String idplayer11, String idplayer12, String idplayer21, String idplayer22,
                 String mode, int score1, int score2) {
        this.datestart = datestart;
        this.dateend = dateend;
        this.idplayer11 = idplayer11;
        this.idplayer12 = idplayer12;
        this.idplayer21 = idplayer21;
        this.idplayer22 = idplayer22;
        this.mode = mode;
        this.score1 = score1;
        this.score2 = score2;
    }

    public String getDatestart() {
        return datestart;
    }

    public void setDatestart(String datestart) {
        this.datestart = datestart;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getIdplayer11() {
        return idplayer11;
    }

    public String getIdplayer12() {
        return idplayer12;
    }

    public String getIdplayer21() {
        return idplayer21;
    }

    public String getIdplayer22() {
        return idplayer22;
    }

    public void setIds(String id11, String id12, String id21, String id22) {
        this.idplayer11 = id11;
        this.idplayer12 = id12;
        this.idplayer21 = id21;
        this.idplayer22 = id22;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore(int score1, int score2) {
        this.score1 = score1;
        this.score2 = score2;
    }
}