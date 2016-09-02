package is.handsome.labs.iotfoosball.presenter;

import android.content.Context;

import is.handsome.labs.iotfoosball.services.ImgSetterService;
import is.handsome.labs.iotfoosball.interactor.InterfaceInteractorFromPresenter;
import is.handsome.labs.iotfoosball.view.InterfaceViewFromPresenter;
import is.handsome.labs.iotfoosball.view.MainActivity;
import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.interactor.Interactor;

public class Presenter implements InterfacePresentorFromInteractor, InterfacePresenterFromView {

    private InterfaceInteractorFromPresenter interfaceInteractorFromPresenter;
    private InterfaceViewFromPresenter interfaceViewFromPresenter;
    private Context context;

    public Presenter (Context context, InterfaceViewFromPresenter interfaceViewFromPresenter) {
        this.context = context;
        this.interfaceViewFromPresenter = interfaceViewFromPresenter;
        interfaceInteractorFromPresenter = new Interactor(context, this);
    }

    @Override
    public void notifyDataSetRecyclerViewChanged() {
        interfaceViewFromPresenter.notifyDataSetRecyclerViewChanged();
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
