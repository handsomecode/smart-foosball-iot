package is.handsome.labs.iotfoosball;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Presenter {

    private ActivityWithPresenter activityWithPresenter;
    private ImgSetterService imgSetterService;
    private SoundService soundPlayer;
    private UsbService usbService;
    private FirebaseAuthService fbAuthService;
    private PhraseSpotterForCountStart phraseSpotter;
    private TimerForClock timerClock;
    private CurrentGame currentGame;

    //Model
    private List<PlayerWithScore> playerWithScoreList;
    private DataReaderService dataReaderService;
    private FirebaseDatabaseListService<Player> fbDatabasePlayersService;
    private FirebaseDatabaseListService<Game> fbDatabaseGamesService;



    public Presenter(ActivityWithPresenter activityWithPresenter) {

        this.activityWithPresenter = activityWithPresenter;

        imgSetterService = new ImgSetterService(activityWithPresenter,
                "gs://handsomefoosball.appspot.com");

        soundPlayer = new SoundService(activityWithPresenter, R.raw.countdown);

        dataReaderService = new DataReaderService(activityWithPresenter, R.raw.data);

        fbAuthService = new FirebaseAuthService(activityWithPresenter,
                dataReaderService.getFbLogin(),
                dataReaderService.getFbPassword());

        playerWithScoreList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        fbDatabaseGamesService =
                new FirebaseDatabaseListService<>(database.getRef().child("/games/"), Game.class);

        fbDatabasePlayersService =
                new FirebaseDatabaseListService<>(database.getRef().child("/players/"),
                        Player.class);

        timerClock = new TimerForClock(activityWithPresenter, 1000, false);

        phraseSpotter = new PhraseSpotterForCountStart(soundPlayer);

        phraseSpotter.init(activityWithPresenter.getApplicationContext(),
                dataReaderService.getYandexApi());

        currentGame = new CurrentGame(activityWithPresenter,
                database.child("/games/"),
                playerWithScoreList);

        usbService = new UsbService(activityWithPresenter, currentGame);
    }

    public ImgSetterService getImgSetterService() {
        return imgSetterService;
    }

    public void initListeners() {
        ActionGameListener actionGameListener =
                new ActionGameListener(playerWithScoreList, activityWithPresenter);
        fbDatabaseGamesService.addListener(actionGameListener);
        ActionPlayerListener actionPlayerListener =
                new ActionPlayerListener(playerWithScoreList, activityWithPresenter);
        fbDatabasePlayersService.addListener(actionPlayerListener);

    }

    public void onActivityResume() {
        usbService.onResume();
    }

    public void onActivityPause() {
        usbService.onPause();
    }

    public void onActivityStart() {
        fbAuthService.onStart();
        phraseSpotter.onStart(activityWithPresenter.getApplicationContext());
    }

    public void onActivityStop() {
        phraseSpotter.onStop();
        fbAuthService.onStop();
    }

    public void onStartClick() {
        currentGame.startGame();
    }

    public void onEndClick() {
        currentGame.endGame();
        timerClock.reset();
    }

    public void onCntdwnClick() {
        soundPlayer.soundPlay();
    }

    public void onTimerClick() {
        if (timerClock.isPaused()) {
            timerClock.resume();
        } else {
            timerClock.pause();
        }
    }

    public PlayerViewInfo getPlayerViewInfoByPosition (int position) {
        PlayerViewInfo playerViewInfo = new PlayerViewInfo();
        playerViewInfo.setNick(playerWithScoreList.get(position).getPlayer().getNick());
        playerViewInfo.setScore(String.format(Locale.US, "%d:%d",
                playerWithScoreList.get(position).getWins(),
                playerWithScoreList.get(position).getLosses()));
        playerViewInfo.setAvatar("avatars/" +
                playerWithScoreList.get(position).getPlayer().getNick().toLowerCase() + ".jpg");
        return playerViewInfo;
    }

    public int getPlayerCount() {
        return playerWithScoreList.size();
    }

    public void notifyDraged(int position, int index, int positionFrom) {
        currentGame.notifyDraged(position, index, positionFrom);
    }

    public void notifyListed(@MainActivity.Teams int team, int position) {
        currentGame.notifyListed(team, position);
    }

    public void notifyDragToBackground(int positionFrom) {
        currentGame.clearInclude(positionFrom);
    }
}
