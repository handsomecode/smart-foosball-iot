package is.handsome.labs.iotfoosball.models;

public class Player {

    private String nick; //TODO Firebase API forgiben to change name of field to mNick
    private String avatar;

    public Player(String nick, String avatar) {
        this.nick = nick;
        this.avatar = avatar;
    }

    private Player() { //Constructor required by Firebase API

    }

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

}