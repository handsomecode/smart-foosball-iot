package is.handsome.labs.iotfoosball.interactor;

import android.app.Activity;
import android.content.Context;

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
import is.handsome.labs.iotfoosball.services.DataReaderService;
import is.handsome.labs.iotfoosball.services.FirebaseAuthService;
import is.handsome.labs.iotfoosball.services.FirebaseDatabaseListService;
import is.handsome.labs.iotfoosball.services.ImgSetterService;
import is.handsome.labs.iotfoosball.services.SoundService;
import is.handsome.labs.iotfoosball.services.UsbService;
import is.handsome.labs.iotfoosball.view.MainActivity;

public class Interactor implements InterfaceInteractorFromPresenter {

    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;
    private ImgSetterService imgSetterService;
    private SoundService soundPlayer;
    private UsbService usbService;
    private FirebaseAuthService fbAuthService;
    private PhraseSpotterForCountStart phraseSpotter;
    private TimerForClock timerClock;
    private CurrentGame currentGame;
    private Activity activity;

    private List<PlayerWithScore> playerWithScoreList;
    private DataReaderService dataReaderService;
    private FirebaseDatabaseListService<Player> fbDatabasePlayersService;
    private FirebaseDatabaseListService<Game> fbDatabaseGamesService;

    public Interactor(Activity activity,
            InterfacePresentorFromInteractor interfacePresentorFromInteractor) {

        this.activity = activity;
                
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;

        imgSetterService = new ImgSetterService(activity.getApplicationContext(),
                "gs://handsomefoosball.appspot.com");

        soundPlayer = new SoundService(activity.getApplicationContext(), R.raw.countdown);

        dataReaderService = new DataReaderService(activity.getApplicationContext(), R.raw.data);

        fbAuthService = new FirebaseAuthService(activity,
                dataReaderService.getFbLogin(),
                dataReaderService.getFbPassword());

        playerWithScoreList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        fbDatabaseGamesService =
                new FirebaseDatabaseListService<>(database.getRef().child("/games/"), Game.class);

        fbDatabasePlayersService =
                new FirebaseDatabaseListService<>(database.getRef().child("/players/"),
                        Player.class);

        timerClock = new TimerForClock(interfacePresentorFromInteractor, 1000, false);

        phraseSpotter = new PhraseSpotterForCountStart(soundPlayer);

        phraseSpotter.init(activity.getApplicationContext(),
                dataReaderService.getYandexApi());

        currentGame = new CurrentGame(interfacePresentorFromInteractor,
                database.child("/games/"),
                playerWithScoreList);

        SerialHandler serialHandler = SerialHandler.getInstance(currentGame);

        usbService = new UsbService(activity.getApplicationContext().getApplicationContext(),
                serialHandler);
    }

    @Override
    public ImgSetterService getImgSetterService() {
        return imgSetterService;
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
    public void onActivityResume() {
        usbService.onResume();
    }

    @Override
    public void onActivityPause() {
        usbService.onPause();
    }

    @Override
    public void onActivityStart() {
        fbAuthService.onStart();
        phraseSpotter.onStart(activity.getApplicationContext());
    }

    @Override
    public void onActivityStop() {
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
    public void onCntdwnClick() {
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
        playerViewInfo.setAvatar("avatars/" +
                playerWithScoreList.get(position).getPlayer().getNick().toLowerCase() + ".jpg");
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
}
