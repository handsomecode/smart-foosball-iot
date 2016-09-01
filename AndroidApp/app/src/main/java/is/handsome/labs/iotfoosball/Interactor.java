package is.handsome.labs.iotfoosball;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Interactor implements InterfaceInteractorFromPresenter {

    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;
    private ImgSetterService imgSetterService;
    private SoundService soundPlayer;
    private UsbService usbService;
    private FirebaseAuthService fbAuthService;
    private PhraseSpotterForCountStart phraseSpotter;
    private TimerForClock timerClock;
    private CurrentGame currentGame;
    private Context context;

    private List<PlayerWithScore> playerWithScoreList;
    private DataReaderService dataReaderService;
    private FirebaseDatabaseListService<Player> fbDatabasePlayersService;
    private FirebaseDatabaseListService<Game> fbDatabaseGamesService;

    public Interactor(Context context,
            InterfacePresentorFromInteractor interfacePresentorFromInteractor) {

        this.context = context;
                
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;

        imgSetterService = new ImgSetterService(context,
                "gs://handsomefoosball.appspot.com");

        soundPlayer = new SoundService(context, R.raw.countdown);

        dataReaderService = new DataReaderService(context, R.raw.data);

        fbAuthService = new FirebaseAuthService(context,
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

        phraseSpotter.init(context,
                dataReaderService.getYandexApi());

        currentGame = new CurrentGame(interfacePresentorFromInteractor,
                database.child("/games/"),
                playerWithScoreList);

        usbService = new UsbService(context, currentGame);
    }

    @Override
    public ImgSetterService getImgSetterService() {
        return imgSetterService;
    }

    @Override
    public void initListeners() {
        ActionGameListener actionGameListener =
                new ActionGameListener(playerWithScoreList, interfacePresentorFromInteractor);
        fbDatabaseGamesService.addListener(actionGameListener);
        ActionPlayerListener actionPlayerListener =
                new ActionPlayerListener(playerWithScoreList, interfacePresentorFromInteractor);
        fbDatabasePlayersService.addListener(actionPlayerListener);

    }

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
        phraseSpotter.onStart(context);
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
