package is.handsome.labs.iotfoosball.models;

public class Player {

    private String avatarGoogleURL;
    private String username;
    private String uid;
    private String type;

    public Player(String avatarGoogleURL,
            String username,
            String uid,
            String type  ) {
        this.avatarGoogleURL = avatarGoogleURL;
        this.username = username;
        this.uid = uid;
        this.type = type;
    }

    private Player() { //Constructor required by Firebase API

    }

    public String getAvatarGoogleUrl() {
        return avatarGoogleURL;
    }

    public void setAvatarGoogleUrl(String avatarGoogleUrl) {
        this.avatarGoogleURL = avatarGoogleUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNick() {
        return username;
    }

}