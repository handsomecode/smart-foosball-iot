package is.handsome.labs.iotfoosball.interactor;

import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.view.MainActivity;

public interface InterfaceInteractorFromPresenter {

    void initListeners();

    void onActivityResume();

    void onActivityPause();

    void onActivityStart();

    void onActivityStop();

    void onStartClick();

    void onEndClick();

    void onCntdwnClick();

    void onTimerClick();

    PlayerViewInfo getPlayerViewInfoByPosition(int position);

    int getPlayerCount();

    void notifyDraged(int position, int index, int positionFrom);

    void notifyListed(@MainActivity.Teams int team, int position);

    void notifyDragToBackground(int positionFrom);

    void requestLink(String link);
}
