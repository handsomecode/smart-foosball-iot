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

    public IncludePlayer(View view) {
        this.mInc = view;
    }

    public View getInc() {
        return mInc;
    }

    //    @BindView(R.id.medal)
//    ImageView medal;
}
