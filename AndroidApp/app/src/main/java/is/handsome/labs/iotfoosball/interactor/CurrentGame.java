package is.handsome.labs.iotfoosball.interactor;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import is.handsome.labs.iotfoosball.models.Game;
import is.handsome.labs.iotfoosball.presenter.InterfacePresentorFromInteractor;
import is.handsome.labs.iotfoosball.models.PlayerWithScore;
import is.handsome.labs.iotfoosball.view.MainActivity;
import timber.log.Timber;

class CurrentGame {
    private InterfacePresentorFromInteractor interfacePresentorFromView;
    private Game game;
    private boolean isGameStarted;
    private DatabaseReference reference;
    private int threshold;
    private int scoreA;
    private int scoreB;
    private ArrayList<Integer> playerIndex;
    private List<PlayerWithScore> playerWithScoreList;

    public CurrentGame(InterfacePresentorFromInteractor interfacePresentorFromView,
            DatabaseReference databaseReference,
            List<PlayerWithScore> playerWithScoresList) {
        this.interfacePresentorFromView = interfacePresentorFromView;
        game = new Game();
        playerIndex = new ArrayList<>(4);
        isGameStarted = false;
        scoreA = 0;
        scoreB = 0;
        this.playerWithScoreList = playerWithScoresList;
        this.reference = databaseReference;
        threshold = 50;
        for (int i = 0; i < 4; i++) {
            playerIndex.add(i, -1);
        }
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setMode(String mode) {
        game.setMode(mode);
    }

    public Integer getPlayerIndex (int i) {
        return this.playerIndex.get(i);
    }

    public void startGame() {
        if (!isGameStarted) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
            String datestart = dateFormat.format(Calendar.getInstance().getTime());
            Log.d("gamesave", "set datestart to " + datestart);
            game.setDateStart(datestart);
            isGameStarted = true;
        }
    }

    public void endGame() {
        if (isGameStarted) {
            isGameStarted = false;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
            String dateend = dateFormat.format(Calendar.getInstance().getTime());
            Log.d("gamesave", "set dateend to " + dateend);
            game.setDateEnd(dateend);
            game.setIds(
                    (playerIndex.get(0) == -1) ? "" : playerWithScoreList.
                            get(playerIndex.get(0)).getPlayerId(),
                    (playerIndex.get(1) == -1) ? "" : playerWithScoreList.
                            get(playerIndex.get(1)).getPlayerId(),
                    (playerIndex.get(2) == -1) ? "" : playerWithScoreList.
                            get(playerIndex.get(2)).getPlayerId(),
                    (playerIndex.get(3) == -1) ? "" : playerWithScoreList.
                            get(playerIndex.get(3)).getPlayerId());
            game.setMode("2x2mb");
            game.setScore(scoreA, scoreB);
            reference.push().setValue(game);
            while ((scoreA != 0 || scoreB != 0)) {
                if (scoreA != 0) {
                    scoreA--;
                    interfacePresentorFromView.setScorebarA(scoreA);
                }
                if (scoreB != 0) {
                    scoreB--;
                    interfacePresentorFromView.setScorebarB(scoreB);
                }
            }
            for (int i = 0; i < 4; i++) {
                clearInclude(i);
            }
        }
    }

    public void notifyScored(@MainActivity.Teams int teamScored) {
        Timber.d("game notified");
        if (teamScored == MainActivity.A) {
            startGame();
            scoreA++;
            interfacePresentorFromView.setScorebarA(scoreA);
        }
        if (teamScored == MainActivity.B) {
            startGame();
            scoreB++;
            interfacePresentorFromView.setScorebarB(scoreB);
        }
    }

    public void notifyListed(@MainActivity.Teams int team, int position) {
        if (team == MainActivity.A) {
            scoreA = position;
        }
        if (team == MainActivity.B) {
            scoreB = position;
        }
    }

    public void notifyDraged(int position, int index, int positionFrom) {
        Timber.d("Dragged to " + position + " with index = " + index);
        if ((index < 0) || (index >= playerWithScoreList.size())) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (index == playerIndex.get(i)) {
                clearInclude(i);
            }
        }
        if (positionFrom != -1 && playerIndex.get(position) != -1) {
            setPlayer(positionFrom, playerIndex.get(position));
        }
        setPlayer(position, index);
    }

    public void clearInclude(int position) {
        playerIndex.set(position, -1);
        interfacePresentorFromView.clearPlayerInInclude(position);
    }

    public String getPlayerId(int position) {
        return playerWithScoreList.get(playerIndex.get(position)).getPlayerId();
    }

    private void setPlayer(int position, int index) {
        playerIndex.set(position, index);
        Timber.d("playerIndex = " + playerIndex.get(position));
        interfacePresentorFromView.setPlayerInInclude(position, index);
    }
}
