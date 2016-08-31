package is.handsome.labs.iotfoosball;

public class PlayerWithScore {
    static private Player playerDummy = new Player("","");
    private String playerId;
    private Player player;
    private int wins;
    private int losses;

    public PlayerWithScore(String playerId) {
        this.playerId = playerId;
        this.player = playerDummy;
        this.wins = 0;
        this.losses = 0;
    }

    public PlayerWithScore(String playerId, Player player) {
        this.playerId = playerId;
        this.player = player;
        this.wins = 0;
        this.losses = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void increaseWins() {
        this.wins++;
    }

    public void increseLosses() {
        this.losses++;
    }

    public void increaseGame(boolean isWinner) {
        if (isWinner) {
            this.wins++;
        } else {
            this.losses++;
        }
    }

    public void decreaseGame(boolean isWinner) {
        if (isWinner) {
            this.wins++;
        } else {
            this.losses++;
        }
    }
}
