package is.handsome.labs.iotfoosball.interactor;

import android.app.Activity;
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import is.handsome.labs.iotfoosball.R;
import is.handsome.labs.iotfoosball.models.Game;
import is.handsome.labs.iotfoosball.presenter.InterfacePresentorFromInteractor;
import is.handsome.labs.iotfoosball.models.Player;
import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.models.PlayerWithScore;
import is.handsome.labs.iotfoosball.services.ActivityProvider;
import is.handsome.labs.iotfoosball.services.AuthDataReaderService;
import is.handsome.labs.iotfoosball.services.FirebaseAuthService;
import is.handsome.labs.iotfoosball.services.FirebaseDatabaseListService;
import is.handsome.labs.iotfoosball.services.FirebaseStorageLinkService;
import is.handsome.labs.iotfoosball.services.InterfaceFirebaseStorageLinkReciver;
import is.handsome.labs.iotfoosball.services.SoundService;
import is.handsome.labs.iotfoosball.services.UsbService;
import is.handsome.labs.iotfoosball.view.MainActivity;

public class Interactor implements InterfaceInteractorFromPresenter, InterfaceFirebaseStorageLinkReciver {

    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;
    private FirebaseStorageLinkService firebaseStorageLinkService;
    private SoundService soundPlayer;
    private UsbService usbService;
    private FirebaseAuthService fbAuthService;
    private PhraseSpotterForCountStart phraseSpotter;
    private TimerForClock timerClock;
    private CurrentGame currentGame;
    private ActivityProvider activityProvider;

    private List<PlayerWithScore> playerWithScoreList;
    private AuthDataReaderService authDataReaderService;
    private FirebaseDatabaseListService<Player> fbDatabasePlayersService;
    private FirebaseDatabaseListService<Game> fbDatabaseGamesService;

    public Interactor(ActivityProvider activityProvider,
            InterfacePresentorFromInteractor interfacePresentorFromInteractor) {

        this.activityProvider = activityProvider;
                
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;

        firebaseStorageLinkService =
                new FirebaseStorageLinkService(activityProvider
                        .getActivity()
                        .getApplicationContext(),
                "gs://handsomefoosball.appspot.com", this);

        soundPlayer =
                new SoundService(activityProvider.getActivity().getApplicationContext(),
                        R.raw.countdown);

        authDataReaderService =
                new AuthDataReaderService(activityProvider.getActivity().getApplicationContext(),
                        R.raw.data);

        fbAuthService = new FirebaseAuthService(activityProvider.getActivity(),
                authDataReaderService.getFbLogin(),
                authDataReaderService.getFbPassword());

        playerWithScoreList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        fbDatabaseGamesService =
                new FirebaseDatabaseListService<>(database.getRef().child("/games/"), Game.class);

        fbDatabasePlayersService =
                new FirebaseDatabaseListService<>(database.getRef().child("/players/"),
                        Player.class);

        timerClock = new TimerForClock(interfacePresentorFromInteractor, 1000, false);

        phraseSpotter = new PhraseSpotterForCountStart(soundPlayer);

        phraseSpotter.init(activityProvider.getActivity().getApplicationContext(),
                authDataReaderService.getYandexApi());

        currentGame = new CurrentGame(interfacePresentorFromInteractor,
                database.child("/games/"),
                playerWithScoreList);

        SerialHandler serialHandler = SerialHandler.getInstance(currentGame);

        usbService =
                new UsbService(activityProvider
                        .getActivity()
                        .getApplicationContext()
                        .getApplicationContext(),
                serialHandler);
    }

    @Override
    public void initListeners() {
        FirebaseActionGameListener actionGameListener =
                new FirebaseActionGameListener(playerWithScoreList,
                        interfacePresentorFromInteractor);
        fbDatabaseGamesService.addListener(actionGameListener);
        FirebaseActionPlayerListener actionPlayerListener =
                new FirebaseActionPlayerListener(playerWithScoreList,
                        interfacePresentorFromInteractor);
        fbDatabasePlayersService.addListener(actionPlayerListener);

    }

    //TODO Activity to just resume
    //TODO
    @Override
    public void resumeServices() {
        usbService.onResume();
    }

    @Override
    public void pauseServices() {
        usbService.onPause();
        timerClock.cancel();
        timerClock.reset();
    }

    @Override
    public void startServices() {
        fbAuthService.onStart();
        phraseSpotter.onStart(activityProvider.getActivity().getApplicationContext());
    }

    @Override
    public void stopServices() {
        phraseSpotter.onStop();
        fbAuthService.onStop();
    }

    @Override
    public void onStartClick() {
        currentGame.startGame();
    }

    @Override
    public void onEndClick() {
        currentGame.endGame();
        timerClock.reset();
    }

    @Override
    public void onCountdownClick() {
        soundPlayer.soundPlay();
    }

    @Override
    public void onTimerClick() {
        if (timerClock.isPaused()) {
            timerClock.resume();
        } else {
            timerClock.pause();
        }
    }

    @Override
    public PlayerViewInfo getPlayerViewInfoByPosition(int position) {
        PlayerViewInfo playerViewInfo = new PlayerViewInfo();
        playerViewInfo.setNick(playerWithScoreList.get(position).getPlayer().getNick());
        playerViewInfo.setScore(String.format(Locale.US, "%d:%d",
                playerWithScoreList.get(position).getWins(),
                playerWithScoreList.get(position).getLosses()));
        playerViewInfo.setAvatar(
                playerWithScoreList.get(position).getPlayer().getNick().toLowerCase());
        return playerViewInfo;
    }

    @Override
    public int getPlayerCount() {
        return playerWithScoreList.size();
    }

    @Override
    public void notifyDraged(int position, int index, int positionFrom) {
        currentGame.notifyDraged(position, index, positionFrom);
    }

    @Override
    public void notifyListed(@MainActivity.Teams int team, int position) {
        currentGame.notifyListed(team, position);
    }

    @Override
    public void notifyDragToBackground(int positionFrom) {
        currentGame.clearInclude(positionFrom);
    }

    @Override
    public void requestLink(String avatar) {
        firebaseStorageLinkService.requestAvatar(avatar);
    }

    @Override
    public void reciveLink(String request, Uri uri) {
        interfacePresentorFromInteractor.reciveImgLink(request, uri);
    }
}
