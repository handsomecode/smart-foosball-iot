package is.handsome.labs.iotfoosball.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.services.FirebaseStorageLinkService;
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

    void notifyListed(@MainActivity.Teams int team, int position);

    void notifyDragToBackground(int positionFrom);

    int getPlayerCount();

    void notifyPlayerDraged(int position, int index, int positionFrom);

    void notifyTeamDraged(int position, int teamIndex);

    android.view.View.OnDragListener getDragListenerForIncludes(int i);

    android.view.View.OnDragListener getDragListenerForBackground();

    RecyclerView.Adapter getPlayerRecyclerAdapter();

}
