package is.handsome.labs.iotfoosball;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

class IncludePlayer {
    //TODO devide model and view
    @BindView(R.id.nick)
    TextView nick;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.score)
    TextView score;

    private View inc;
    private String playerid;

    public IncludePlayer(View view) {
        this.inc = view;
        playerid = "";
    }

    public View getInc() {
        return inc;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    //    @BindView(R.id.medal)
//    ImageView medal;
}
