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

    private View mInc;
    private String mPlayerId;

    public IncludePlayer(View view) {
        this.mInc = view;
        mPlayerId = "";
    }

    public View getInc() {
        return mInc;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(String mPlayerId) {
        this.mPlayerId = mPlayerId;
    }

    //    @BindView(R.id.medal)
//    ImageView medal;
}
