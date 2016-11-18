package is.handsome.labs.iotfoosball.models;

public class TeamViewInfo {

    private String name;
    private String avatar1Url;
    private String avatar2Url;

    public TeamViewInfo() {
        this.name = "";
        this.avatar1Url = "";
        this.avatar2Url = "";
    }

    public TeamViewInfo (String name, String avatar1Url, String avatar2Url) {
        this.name = name;
        this.avatar1Url = avatar1Url;
        this.avatar2Url = avatar2Url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar1Url() {
        return avatar1Url;
    }

    public void setAvatar1Url(String avatar1Url) {
        this.avatar1Url = avatar1Url;
    }

    public String getAvatar2Url() {
        return avatar2Url;
    }

    public void setAvatar2Url(String avatar2Url) {
        this.avatar2Url = avatar2Url;
    }
}
