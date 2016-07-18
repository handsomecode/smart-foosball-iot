package is.handsome.labs.iotfoosball;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CurentGame {
    //TODO extends from mGame ?
    private Game mGame;
    private boolean mIsGameStarted;
    private ArrayList<IncludePlayer> mIncludePlayers;
    private DatabaseReference mReference;
    private ScoreViewPager mTeamA;
    private ScoreViewPager mTeamB;
    private int mThreshold;

    public CurentGame(ArrayList<IncludePlayer> includePlayers,
                      DatabaseReference databaseReference,
                      ScoreViewPager teamA,
                      ScoreViewPager teamB) {
        mIsGameStarted = false;
        this.mTeamA = teamA;
        this.mTeamB = teamB;
        mGame = new Game();
        this.mIncludePlayers = includePlayers;
        this.mReference = databaseReference;
        mThreshold = 50;
    }

    public void setThreshold(int threshold) {
        this.mThreshold = threshold;
    }

    public void setMode(String mode) {
        mGame.setMode(mode);
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
            mGame.setIds(mIncludePlayers.get(0).getPlayerId(),
                    mIncludePlayers.get(1).getPlayerId(),
                    mIncludePlayers.get(2).getPlayerId(),
                    mIncludePlayers.get(3).getPlayerId());
            mGame.setMode("2x2mb");
            //mGame.setScore(mTeamA.getCurrentItem(), mTeamB.getCurrentItem());
            mReference.push().setValue(mGame);
        }
    }

    public void notify_game() {
        Log.d("score2", "mGame notified");
        mGame.setScore(mTeamA.getCurrentItem(), mTeamB.getCurrentItem());
        if ((mGame.getScoreA() >= mThreshold)|| (mGame.getScoreB() >= mThreshold)) endGame();
    }
}
