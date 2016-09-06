package is.handsome.labs.iotfoosball.presenter;

import android.net.Uri;

public interface InterfacePresentorFromInteractor {

    void notifyDataSetRecyclerViewChanged();

    void setTime(String time);

    void setPlayerInInclude(int position, int index);

    void clearPlayerInInclude(int position);

    void setScorebarA(int ScoreA);

    void setScorebarB(int ScoreB);

    void reciveImgLink(String avatar, Uri uri);
}
