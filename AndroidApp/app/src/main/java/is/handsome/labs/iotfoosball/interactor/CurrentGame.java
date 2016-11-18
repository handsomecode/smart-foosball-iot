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
import is.handsome.labs.iotfoosball.models.Team;
import is.handsome.labs.iotfoosball.presenter.InterfacePresentorFromInteractor;
import is.handsome.labs.iotfoosball.models.PlayerWithScore;
import is.handsome.labs.iotfoosball.view.MainActivity;
import timber.log.Timber;

class CurrentGame {
    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;
    private Game game;
    private boolean isGameStarted;
    private DatabaseReference reference;
    private DatabaseReference currentGameRef;
    private int threshold;
    private int scoreA;
    private int scoreB;
    private ArrayList<Integer> playerIndex;
    private List<PlayerWithScore> playerWithScoreList;
    private DateFormat dateFormat;
    private List<String> playerStringFbNames;
    private List<Team> teamArrayList;

    public CurrentGame(InterfacePresentorFromInteractor interfacePresentorFromInteractor,
            DatabaseReference databaseReference,
            DatabaseReference currentGameRef,
            List<PlayerWithScore> playerWithScoresList,
            List<Team> teamArrayList) {
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;
        this.teamArrayList = teamArrayList;
        game = new Game();
        playerIndex = new ArrayList<>(4);
        isGameStarted = false;
        scoreA = 0;
        scoreB = 0;
        this.playerWithScoreList = playerWithScoresList;
        this.reference = databaseReference;
        this.currentGameRef = currentGameRef;
        threshold = 50;
        for (int i = 0; i < 4; i++) {
            playerIndex.add(i, -1);
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        playerStringFbNames = new ArrayList<>();
        playerStringFbNames.add(0, "idPlayerA1");
        playerStringFbNames.add(1, "idPlayerA2");
        playerStringFbNames.add(2, "idPlayerB1");
        playerStringFbNames.add(3, "idPlayerB2");
        for (int i = 0; i < 4; i++) {
            currentGameRef.child(playerStringFbNames.get(i)).setValue("");
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
                    interfacePresentorFromInteractor.setScorebarA(scoreA);
                }
                if (scoreB != 0) {
                    scoreB--;
                    interfacePresentorFromInteractor.setScorebarB(scoreB);
                }
            }
            for (int i = 0; i < 4; i++) {
                clearInclude(i);
            }
        }
        for (int i = 0; i < 4; i++) {
            currentGameRef.child(playerStringFbNames.get(i)).setValue("");
        }
    }

    public void notifyScored(@MainActivity.Teams int teamScored) {
        Timber.d("game notified");
        if (teamScored == MainActivity.A) {
            startGame();
            scoreA++;
            currentGameRef.child("scoreA").setValue(scoreA);
            interfacePresentorFromInteractor.setScorebarA(scoreA);
        }
        if (teamScored == MainActivity.B) {
            startGame();
            scoreB++;
            currentGameRef.child("scoreB").setValue(scoreB);
            interfacePresentorFromInteractor.setScorebarB(scoreB);
        }
    }

    public void notifyListed(@MainActivity.Teams int team, int position) {
        if (team == MainActivity.A) {
            scoreA = position;
            currentGameRef.child("scoreA").setValue(scoreA);
        }
        if (team == MainActivity.B) {
            scoreB = position;
            currentGameRef.child("scoreB").setValue(scoreB);
        }
    }

    public void notifyPlayerDraged(int position, int index, int positionFrom) {
        Timber.d("Dragged player to " + position + " with index = " + index);
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

    public void notifyTeamDraged(int position, int teamIndex) {
        Timber.d("Dragged player to " + position + " with index = " + teamIndex);
        if ((teamIndex < 0) || (teamIndex >= teamArrayList.size())) {
            return;
        }
        String player1id = teamArrayList.get(teamIndex).getPlayer1id();
        int player1index = -1;
        if (player1id != null) {
            if (!player1id.equals("")) {
                for (int i = 0; i < playerWithScoreList.size(); i++) {
                    if (playerWithScoreList.get(i).getPlayerId().equals(player1id)) {
                        player1index = i;
                        break;
                    }
                }
            }
        }

        String player2id = teamArrayList.get(teamIndex).getPlayer2id();
        int player2index = -1;
        if (player2id != null) {
            if (!player2id.equals("")) {
                for (int i = 0; i < playerWithScoreList.size(); i++) {
                    if (playerWithScoreList.get(i).getPlayerId().equals(player2id)) {
                        player2index = i;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (player1index == playerIndex.get(i) ||
                    player2index == playerIndex.get(i)) {
                clearInclude(i);
            }
        }

        if (position<2) {
            if (player1index != -1) {
                setPlayer(0, player1index);
            } else {
                clearInclude(0);
            }
            if (player2index != -1) {
                setPlayer(1, player2index);
            } else {
                clearInclude(1);
            }
        } else {
            if (player1index != -1) {
                setPlayer(2, player1index);
            } else {
                clearInclude(2);
            }
            if (player2index != -1) {
                setPlayer(3, player2index);
            } else {
                clearInclude(3);
            }
        }
    }

    public void clearInclude(int position) {
        playerIndex.set(position, -1);
        interfacePresentorFromInteractor.clearPlayerInInclude(position);
        currentGameRef.child(playerStringFbNames.get(position)).setValue("");
        recalcTeams();
    }

    public String getPlayerId(int position) {
        if (playerIndex.get(position) < playerWithScoreList.size() && playerIndex.get(position) > 0) {
            return playerWithScoreList.get(playerIndex.get(position)).getPlayerId();
        }
        return "-321";
    }

    public int findPlayerIndexFromId(String id) {
        int i = 0;
        for (; i < playerWithScoreList.size(); i++) {
            if (playerWithScoreList.get(i).getPlayerId().equals(id)) {
                break;
            }
        }
        return playerIndex.get(i);
    }

    private void recalcTeams() {
        recalcTeamA();
        recalcTeamB();
    }

    private void recalcTeamA() {
        //check teamA1
        for (int i = 0; i < teamArrayList.size(); i++) {
            if (teamArrayList.get(i).getPlayer1id() != null) {
                if (teamArrayList.get(i).getPlayer1id().equals(getPlayerId(0))) {
                    for (int j = 0; j < teamArrayList.size(); j++) {
                        if (teamArrayList.get(i).getPlayer2id() != null) {
                            if (teamArrayList.get(i).getPlayer2id().equals(getPlayerId(1))) {
                                currentGameRef.child("teamAid").setValue(teamArrayList.get(j).getId());
                                return;
                            }
                        }
                    }
                }
            }
        }
        //check teamA2
        for (int i = 0; i < teamArrayList.size(); i++) {
            if (teamArrayList.get(i).getPlayer1id() != null) {
                if (teamArrayList.get(i).getPlayer1id().equals(getPlayerId(1))) {
                    for (int j = 0; j < teamArrayList.size(); j++) {
                        if (teamArrayList.get(i).getPlayer2id() != null) {
                            if (teamArrayList.get(i).getPlayer2id().equals(getPlayerId(0))) {
                                currentGameRef.child("teamAid").setValue(teamArrayList.get(j).getId());
                                return;
                            }
                        }
                    }
                }
            }
        }
        currentGameRef.child("teamAid").setValue("");
    }

    private void recalcTeamB() {
        //check teamB1
        for (int i = 0; i < teamArrayList.size(); i++) {
            if (teamArrayList.get(i).getPlayer1id() != null) {
                if (teamArrayList.get(i).getPlayer1id().equals(getPlayerId(2))) {
                    for (int j = 0; j < teamArrayList.size(); j++) {
                        if (teamArrayList.get(i).getPlayer2id() != null) {
                            if (teamArrayList.get(i).getPlayer2id().equals(getPlayerId(3))) {
                                currentGameRef.child("teamBid").setValue(teamArrayList.get(j).getId());
                                return;
                            }
                        }
                    }
                }
            }
        }
        //check teamB2
        for (int i = 0; i < teamArrayList.size(); i++) {
            if (teamArrayList.get(i).getPlayer1id() != null) {
                if (teamArrayList.get(i).getPlayer1id().equals(getPlayerId(3))) {
                    for (int j = 0; j < teamArrayList.size(); j++) {
                        if (teamArrayList.get(i).getPlayer2id() != null) {
                            if (teamArrayList.get(i).getPlayer2id().equals(getPlayerId(2))) {
                                currentGameRef.child("teamBid").setValue(teamArrayList.get(j).getId());
                                return;
                            }
                        }
                    }
                }
            }
        }
        currentGameRef.child("teamBid").setValue("");
    }

    private void setPlayer(int position, int index) {
        playerIndex.set(position, index);
        currentGameRef.child(playerStringFbNames.get(position)).setValue(getPlayerId(position));
        Timber.d("playerIndex = " + playerIndex.get(position));
        interfacePresentorFromInteractor.setPlayerInInclude(position, index);
        recalcTeams();
    }
}
