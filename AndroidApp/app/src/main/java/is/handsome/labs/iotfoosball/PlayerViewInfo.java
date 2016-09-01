package is.handsome.labs.iotfoosball;


//TODO AutoValue

class PlayerViewInfo {

    private String nick;
    private String avatar;
    private String score;

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public PlayerViewInfo(){

    }

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getScore() {
        return score;
    }
}
