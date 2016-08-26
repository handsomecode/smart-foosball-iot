package is.handsome.labs.iotfoosball;

public class PlayerWithScore {
    static private Player playerDummy = new Player("","");
    private String mPlayerId;
    private Player mPlayer;
    private int mWins;
    private int mLosses;

    public PlayerWithScore(String playerId) {
        this.mPlayerId = playerId;
        this.mPlayer = playerDummy;
        this.mWins = 0;
        this.mLosses = 0;
    }

    public PlayerWithScore(String playerId, Player player) {
        this.mPlayerId = playerId;
        this.mPlayer = player;
        this.mWins = 0;
        this.mLosses = 0;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public void setPlayer(Player player) {
        this.mPlayer = player;
    }

    public int getWins() {
        return mWins;
    }

    public void setWins(int wins) {
        this.mWins = wins;
    }

    public int getLosses() {
        return mLosses;
    }

    public void setLosses(int losses) {
        this.mLosses = losses;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public void increaseWins() {
        this.mWins++;
    }

    public void increseLosses() {
        this.mLosses++;
    }

    public void increaseGame(boolean isWinner) {
        if (isWinner) {
            this.mWins++;
        } else {
            this.mLosses++;
        }
    }

    public void decreaseGame(boolean isWinner) {
        if (isWinner) {
            this.mWins++;
        } else {
            this.mLosses++;
        }
    }
}
