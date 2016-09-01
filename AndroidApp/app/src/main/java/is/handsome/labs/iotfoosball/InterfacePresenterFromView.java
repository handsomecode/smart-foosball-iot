package is.handsome.labs.iotfoosball;

public interface InterfacePresenterFromView {

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

    ImgSetterService getImgSetterService();

    void notifyListed(@MainActivity.Teams int team, int position);

    void notifyDragToBackground(int positionFrom);

    int getPlayerCount();

    void notifyDraged(int position, int index, int positionFrom);
}
