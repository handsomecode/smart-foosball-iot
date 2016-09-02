package is.handsome.labs.iotfoosball.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import is.handsome.labs.iotfoosball.services.ImgSetterService;
import is.handsome.labs.iotfoosball.interactor.InterfaceInteractorFromPresenter;
import is.handsome.labs.iotfoosball.view.InterfaceViewFromPresenter;
import is.handsome.labs.iotfoosball.view.MainActivity;
import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.interactor.Interactor;

public class Presenter implements InterfacePresentorFromInteractor, InterfacePresenterFromView {

    private InterfaceInteractorFromPresenter interfaceInteractorFromPresenter;
    private InterfaceViewFromPresenter interfaceViewFromPresenter;
    private Activity activity;
    private android.support.v7.widget.RecyclerView.Adapter playerRecyclerAdapter;

    public Presenter (Activity activity) {
        this.activity = activity;
        this.interfaceViewFromPresenter = (InterfaceViewFromPresenter) activity;
        interfaceInteractorFromPresenter = new Interactor(activity, this);
        playerRecyclerAdapter = new PlayerRecyclerAdapter(this);
    }


    @Override
    public View.OnDragListener getDragListenerForIncludes(int i) {
        return new DragListenerForIncludes(this, i);
    }

    @Override
    public View.OnDragListener getDragListenerForBackground() {
        return new DragListenerForBackground(this);
    }

    @Override
    public OnPlayerLongClickListener getOnPlayerLongClickListener(int index, int position, View view) {
        return new OnPlayerLongClickListener(index, position, view);
    }

    @Override
    public RecyclerView.Adapter getPlayerRecyclerAdapter() {
        return playerRecyclerAdapter;
    }

    @Override
    public void notifyDataSetRecyclerViewChanged() {
        playerRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTime(String time) {
        interfaceViewFromPresenter.setTime(time);
    }

    @Override
    public void setPlayerInInclude(int position, int index) {
        interfaceViewFromPresenter.setPlayerInInclude(position, index);
    }

    @Override
    public void clearPlayerInInclude(int position) {
        interfaceViewFromPresenter.clearPlayerInInclude(position);
    }

    @Override
    public void setScorebarA(int scoreA) {
        interfaceViewFromPresenter.setScorebarA(scoreA);
    }

    @Override
    public void setScorebarB(int scoreB) {
        interfaceViewFromPresenter.setScorebarB(scoreB);
    }

    @Override
    public void initListeners() {
        interfaceInteractorFromPresenter.initListeners();
    }

    @Override
    public void onActivityResume() {
        interfaceInteractorFromPresenter.onActivityResume();
    }

    @Override
    public void onActivityPause() {
        interfaceInteractorFromPresenter.onActivityPause();
    }

    @Override
    public void onActivityStart() {
        interfaceInteractorFromPresenter.onActivityStart();
    }

    @Override
    public void onActivityStop() {
        interfaceInteractorFromPresenter.onActivityStop();
    }

    @Override
    public void onStartClick() {
        interfaceInteractorFromPresenter.onStartClick();
    }

    @Override
    public void onEndClick() {
        interfaceInteractorFromPresenter.onEndClick();
    }

    @Override
    public void onCntdwnClick() {
        interfaceInteractorFromPresenter.onCntdwnClick();
    }

    @Override
    public void onTimerClick() {
        interfaceInteractorFromPresenter.onTimerClick();
    }

    @Override
    public PlayerViewInfo getPlayerViewInfoByPosition(int position) {
        return interfaceInteractorFromPresenter.getPlayerViewInfoByPosition(position);
    }

    @Override
    public ImgSetterService getImgSetterService() {
        return interfaceInteractorFromPresenter.getImgSetterService();
    }

    @Override
    public void notifyListed(@MainActivity.Teams int team, int position) {
        interfaceInteractorFromPresenter.notifyListed(team, position);
    }

    @Override
    public void notifyDragToBackground(int positionFrom) {
        interfaceInteractorFromPresenter.notifyDragToBackground(positionFrom);
    }

    @Override
    public int getPlayerCount() {
        return interfaceInteractorFromPresenter.getPlayerCount();
    }

    @Override
    public void notifyDraged(int position, int index, int positionFrom) {
        interfaceInteractorFromPresenter.notifyDraged(position, index, positionFrom);
    }
}