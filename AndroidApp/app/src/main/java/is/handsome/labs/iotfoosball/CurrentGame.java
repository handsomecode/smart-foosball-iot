package is.handsome.labs.iotfoosball;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class CurrentGame {
    private Game mGame;
    private boolean mIsGameStarted;
    private ArrayList<IncludePlayer> mIncludePlayers;
    private DatabaseReference mReference;
    private int mThreshold;
    private int mScoreA;
    private Scorebar mScorebarA;
    private int mScoreB;
    private Scorebar mScorebarB;
    private ArrayList<Integer> mPlayerIndex;
    private List<PlayerWithScore> mPlayerWithScoreList;
    private FirebaseImgSetter mFirebaseImgSetter;

    public CurrentGame(ArrayList<IncludePlayer> includePlayers,
            DatabaseReference databaseReference,
            Scorebar teamA,
            Scorebar teamB,
            FirebaseImgSetter firebaseImgSetter,
            List<PlayerWithScore> playerWithScoresList) {
        mGame = new Game();
        mPlayerIndex = new ArrayList<>(4);
        mIsGameStarted = false;
        this.mScorebarA = teamA;
        mScoreA = 0;
        this.mScorebarB = teamB;
        mScoreB = 0;
        this.mPlayerWithScoreList = playerWithScoresList;
        this.mIncludePlayers = includePlayers;
        this.mReference = databaseReference;
        mThreshold = 50;
        for (int i = 0; i < 4; i++) {
            mPlayerIndex.add(i, -1);
        }
        this.mFirebaseImgSetter = firebaseImgSetter;
    }

    public void setThreshold(int threshold) {
        this.mThreshold = threshold;
    }

    public void setMode(String mode) {
        mGame.setMode(mode);
    }

    public Integer getPlayerIndex (int i) {
        return this.mPlayerIndex.get(i);
    }

    public void startGame() {
        if (!mIsGameStarted) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ROOT);
            String datestart = dateFormat.format(Calendar.getInstance().getTime());
            Log.d("gamesave", "set datestart to " + datestart);
            mGame.setDateStart(datestart);
            mIsGameStarted = true;
        }
    }

    public void endGame() {
        if (mIsGameStarted) {
            mIsGameStarted = false;
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ROOT);
            String dateend = dateFormat.format(Calendar.getInstance().getTime());
            Log.d("gamesave", "set dateend to " + dateend);
            mGame.setDateEnd(dateend);
            mGame.setIds(
                    (mPlayerIndex.get(0) == -1) ? "" : mPlayerWithScoreList.
                            get(mPlayerIndex.get(0)).getPlayerId(),
                    (mPlayerIndex.get(1) == -1) ? "" : mPlayerWithScoreList.
                            get(mPlayerIndex.get(1)).getPlayerId(),
                    (mPlayerIndex.get(2) == -1) ? "" : mPlayerWithScoreList.
                            get(mPlayerIndex.get(2)).getPlayerId(),
                    (mPlayerIndex.get(3) == -1) ? "" : mPlayerWithScoreList.
                            get(mPlayerIndex.get(3)).getPlayerId());
            mGame.setMode("2x2mb");
            mGame.setScore(mScoreA, mScoreB);
            mReference.push().setValue(mGame);
            while ((mScoreA != 0 || mScoreB != 0)) {
                if (mScoreA != 0) {
                    mScoreA--;
                    mScorebarA.setScore(mScoreA);
                }
                if (mScoreB != 0) {
                    mScoreB--;
                    mScorebarB.setScore(mScoreB);
                }
            }
            for (int i = 0; i < 4; i++) {
                clearInclude(i);
            }
        }
    }

    public void notifyScored(@MainActivity.GOALS int teamScored) {
        Timber.d("game notified");
        if (teamScored == MainActivity.GOAL_A) {
            startGame();
            mScoreA++;
            mScorebarA.setScore(mScoreA);
        }
        if (teamScored == MainActivity.GOAL_B) {
            startGame();
            mScoreB++;
            mScorebarB.setScore(mScoreB);
        }
        //if ((mScoreA >= mThreshold)|| (mScoreB >= mThreshold)) endGame();
    }

    public void notifyListed(Scorebar scorebar, int position) {
        if(scorebar == mScorebarA) {
            mScoreA = position;
        }
        if (scorebar == mScorebarB) {
            mScoreB = position;
        }
    }

    public void notifyDraged(int position, int index, int positionFrom) {
        Timber.d("Dragged to " + position + " with index = " + index);
        if ((index < 0) || (index >= mPlayerWithScoreList.size())) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (index == mPlayerIndex.get(i)) {
                clearInclude(i);
            }
        }

        if (positionFrom != -1) {
            setPlayer(positionFrom, mPlayerIndex.get(position));
        }

        setPlayer(position, index);


    }

    public void clearInclude(int position) {
        if (position >=4 || position < 0) {
            return;
        }
        mIncludePlayers.get(position).nick.setText("player");
        mIncludePlayers.get(position).score.setText("");
        mFirebaseImgSetter.setNullImg(mIncludePlayers.get(position).avatar);
        mPlayerIndex.set(position, -1);
        mIncludePlayers.get(position).avatar.setOnLongClickListener(null);
    }

    public String getPlayerId(int position) {
        return mPlayerWithScoreList.get(mPlayerIndex.get(position)).getPlayerId();
    }

    private void setPlayer(int position, int index) {
        mPlayerIndex.set(position, index);
        Timber.d("mPlayerIndex = " + mPlayerIndex.get(position));
        PlayerWithScore player = mPlayerWithScoreList.get(mPlayerIndex.get(position));
        mIncludePlayers.get(position).nick.setText(player.getPlayer().getNick());
        String score = String.format(Locale.US, "%d:%d",
                player.getWins(),
                player.getLosses());
        mIncludePlayers.get(position).score.setText(score);

        mFirebaseImgSetter.setAvatar(
                player.getPlayer().getNick(),
                mIncludePlayers.get(position).avatar);

        mIncludePlayers.get(position).avatar.setOnLongClickListener(
                new OnPlayerLongClickListener(index,
                        position,
                        mIncludePlayers.get(position).getInc()));
    }
}
