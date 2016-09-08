package is.handsome.labs.iotfoosball.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import is.handsome.labs.iotfoosball.R;

class PlayerViewGroup {
    @BindView(R.id.nick)
    TextView nick;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.score)
    TextView score;

    private View inc;

    public PlayerViewGroup(View view) {
        this.inc = view;
    }

    public View getInc() {
        return inc;
    }
}
