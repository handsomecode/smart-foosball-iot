package is.handsome.labs.iotfoosball.interactor;

import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.view.MainActivity;

public interface InterfaceInteractorFromPresenter {

    void initListeners();

    void resumeServices();

    void pauseServices();

    void startServices();

    void stopServices();

    void onStartClick();

    void onEndClick();

    void onCountdownClick();

    void onTimerClick();

    PlayerViewInfo getPlayerViewInfoByPosition(int position);

    int getPlayerCount();

    void notifyDraged(int position, int index, int positionFrom);

    void notifyListed(@MainActivity.Teams int team, int position);

    void notifyDragToBackground(int positionFrom);

    void requestLink(String link);
}
