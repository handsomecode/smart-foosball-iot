package is.handsome.labs.iotfoosball.presenter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import is.handsome.labs.iotfoosball.R;
import is.handsome.labs.iotfoosball.services.FirebaseStorageLinkService;
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
    private Picasso picasso;
    private HashMap<ImageView, String> viewAvatar;
    private HashMap<ImageView, Boolean> isLinkReqested;

    public Presenter (Activity activity) {
        this.activity = activity;
        this.picasso = Picasso.with(activity);
        this.interfaceViewFromPresenter = (InterfaceViewFromPresenter) activity;
        interfaceInteractorFromPresenter = new Interactor(activity, this);
        playerRecyclerAdapter = new PlayerRecyclerAdapter(this);
        viewAvatar = new HashMap<>();
        isLinkReqested = new HashMap<>();
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
        PlayerViewInfo playerViewInfo =
                interfaceInteractorFromPresenter.getPlayerViewInfoByPosition(index);
        interfaceViewFromPresenter.getIncludeNick(position).setText(playerViewInfo.getNick());
        interfaceViewFromPresenter.getIncludeScore(position).setText(playerViewInfo.getScore());

        setAvatar(interfaceViewFromPresenter.getIncludeAvatar(position), playerViewInfo.getAvatar());

        interfaceViewFromPresenter.getIncludeAvatar(position).setOnLongClickListener(
                new OnPlayerLongClickListener(index,
                        position,
                        interfaceViewFromPresenter.getIncludeAvatar(position)));
    }

    @Override
    public void clearPlayerInInclude(int position) {
        interfaceViewFromPresenter.getIncludeNick(position).setText("player");
        interfaceViewFromPresenter.getIncludeScore(position).setText("");

        interfaceViewFromPresenter.getIncludeAvatar(position).setImageBitmap(null);

        interfaceViewFromPresenter.getIncludeAvatar(position).setOnLongClickListener(null);
    }

    @Override
    public void reciveImgLink(String avatar, Uri uri) {
        synchronized (viewAvatar.getClass()) {
            for (Map.Entry<ImageView, String> entry : viewAvatar.entrySet()) {
                ImageView imageView = entry.getKey();
                String savedAvatar = entry.getValue();
                if (isLinkReqested.get(imageView) && savedAvatar.equals(avatar)) {
                    if (uri == null) {
                        picasso.load(R.mipmap.opengamer).into(imageView);
                    }
                    picasso.load(uri).into(imageView);
                    isLinkReqested.put(imageView, false);
                }
            }
        }
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

    public void setAvatar(ImageView imageView, String avatar) {
        synchronized (viewAvatar.getClass()) {
            viewAvatar.put(imageView, avatar);
            if (avatar != null) {
                isLinkReqested.put(imageView, true);
                interfaceInteractorFromPresenter.requestLink(avatar);
            } else {
                isLinkReqested.put(imageView, false);
                picasso.load(R.mipmap.opengamer).into(imageView);
            }
        }

    }
}