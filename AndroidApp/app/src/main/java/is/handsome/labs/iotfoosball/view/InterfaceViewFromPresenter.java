package is.handsome.labs.iotfoosball.view;

import android.widget.ImageView;
import android.widget.TextView;

public interface InterfaceViewFromPresenter {

    void setTime(String time);

    void setScorebarA(int ScoreA);

    void setScorebarB(int ScoreB);

    TextView getIncludeNick(int position);

    TextView getIncludeScore(int position);

    ImageView getIncludeAvatar(int position);
}
