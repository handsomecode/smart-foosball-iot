package is.handsome.labs.iotfoosball.models;

public class Team {

    private String id;
    private String name;
    private String player1id;
    private String player2id;

    public Team(String id,
            String name,
            String player1id,
            String player2id) {
        this.id = id;
        this.name = name;
        this.player1id = player1id;
        this.player2id = player2id;
    }

    private Team() { //Constructor required by Firebase API

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1id() {
        return player1id;
    }

    public void setPlayer1id(String player1id) {
        this.player1id = player1id;
    }

    public String getPlayer2id() {
        return player2id;
    }

    public void setPlayer2id(String player2id) {
        this.player2id = player2id;
    }
}
