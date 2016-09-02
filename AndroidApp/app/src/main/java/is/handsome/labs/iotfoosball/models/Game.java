package is.handsome.labs.iotfoosball.models;

public class Game {
    private String dateStart;
    private String dateEnd;
    private String idPlayerA1;
    private String idPlayerA2;
    private String idPlayerB1;
    private String idPlayerB2;
    private String mode;
    private int scoreA;
    private int scoreB;

    public Game(String dateStart,
            String dateEnd,
            String idPlayerA1,
            String idPlayerA2,
            String idPlayerB1,
            String idPlayerB2, String mode,
            int scoreA,
            int scoreB) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.idPlayerA1 = idPlayerA1;
        this.idPlayerA2 = idPlayerA2;
        this.idPlayerB1 = idPlayerB1;
        this.idPlayerB2 = idPlayerB2;
        this.mode = mode;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

    public Game() { //Constructor required by Firebase API

    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getIdPlayerA1() {
        return idPlayerA1;
    }

    public String getIdPlayerA2() {
        return idPlayerA2;
    }

    public String getIdPlayerB1() {
        return idPlayerB1;
    }

    public String getIdPlayerB2() {
        return idPlayerB2;
    }

    public void setIds(String idA1, String idA2, String idB1, String idB2) {
        this.idPlayerA1 = idA1;
        this.idPlayerA2 = idA2;
        this.idPlayerB1 = idB1;
        this.idPlayerB2 = idB2;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getScoreA() {
        return scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScore(int scoreA, int scoreB) {
        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

    public String getPlayer(int i) {
        if (i == 0) {
            return idPlayerA1;
        }
        if (i == 1) {
            return idPlayerA2;
        }
        if (i == 2) {
            return idPlayerB1;
        }
        if (i == 3) {
            return idPlayerB2;
        }
        return null;
    }
}
