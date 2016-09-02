package is.handsome.labs.iotfoosball.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.services.ImgSetterService;
import is.handsome.labs.iotfoosball.view.MainActivity;

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

    android.view.View.OnDragListener getDragListenerForIncludes(int i);

    android.view.View.OnDragListener getDragListenerForBackground();

    OnPlayerLongClickListener getOnPlayerLongClickListener(int index, int position, View view);

    RecyclerView.Adapter getPlayerRecyclerAdapter();

}
