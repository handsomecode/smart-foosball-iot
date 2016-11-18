package is.handsome.labs.iotfoosball.models;

//TODO AutoValue

public class PlayerViewInfo {

    private String nick;
    private String avatarUrl;
//    private String score;

    public void setNick(String nick) {
        String shortNick = nick.substring(0,nick.indexOf(' '));
        String firstLetterOfSurname = nick.substring(nick.indexOf(' ')+1, nick.indexOf(' ')+2);
        shortNick += firstLetterOfSurname;
        this.nick = shortNick;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

//    public void setScore(String score) {
//        this.score = score;
//    }

    public PlayerViewInfo(){

    }

    public String getNick() {
        return nick;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

//    public String getScore() {
//        return score;
//    }
}
