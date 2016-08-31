package is.handsome.labs.iotfoosball;

import android.app.Activity;

public abstract class FeedbackFromPresenterActivity extends Activity {
    abstract void notifyDataSetRecyclerViewChanged();
    abstract void setTime(String time);
    abstract void setPlayerInInclude(int position, int index);
    abstract void clearPlayerInInclude(int position);
    abstract void setScorebarA(int ScoreA);
    abstract void setScorebarB(int ScoreB);
}
