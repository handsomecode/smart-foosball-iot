package is.handsome.labs.iotfoosball;

public interface InterfaceInteractorFromPresenter {

    ImgSetterService getImgSetterService();

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
}
