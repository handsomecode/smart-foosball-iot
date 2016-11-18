package is.handsome.labs.iotfoosball.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Slot {
    private String dateStart;
    private String dateEnd;
    private String teamAid;
    private String teamBid;
    private int scoreA;
    private int scoreB;
    private String id;
    private DateFormat dateFormat;

    public Slot() {

    }

    public Slot(String dateStart,
            String dateEnd,
            String teamAid,
            String teamBid,
            int scoreA,
            int scoreB,
            String id) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.teamAid = teamAid;
        this.teamBid = teamBid;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.id = id;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
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

    public String getTeamAid() {
        return teamAid;
    }

    public void setTeamAid(String teamAid) {
        this.teamAid = teamAid;
    }

    public String getTeamBid() {
        return teamBid;
    }

    public void setTeamBid(String teamBid) {
        this.teamBid = teamBid;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFromDate() {
        try {
            return dateFormat.parse(dateStart);
        } catch (ParseException p) {
            return new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24));
        }

    }

    public Date getToDate() {
        try {
            return dateFormat.parse(dateEnd);
        } catch (ParseException p) {
            return new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24));
        }
    }
}
