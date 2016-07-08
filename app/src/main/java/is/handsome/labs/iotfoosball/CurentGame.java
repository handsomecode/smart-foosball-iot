package is.handsome.labs.iotfoosball;

import android.media.SoundPool;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Opengamer on 05.07.2016.
 */
public class CurentGame {
    //TODO extends from game ?
    private Game game;
    private boolean isgamestarted;
    private ArrayList<IncludePlayer> includePlayers;
    private DatabaseReference mReference;
    private ScoreViewPager t1;
    private ScoreViewPager t2;
    private int threshold;

    public CurentGame(ArrayList<IncludePlayer> includePlayers, DatabaseReference databaseReference,
                      ScoreViewPager t1, ScoreViewPager t2) {
        isgamestarted = false;
        this.t1 = t1;
        this.t2 = t2;
        game = new Game();
        this.includePlayers = includePlayers;
        this.mReference = databaseReference;
        threshold = 50;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setMode(String mode) {
        game.setMode(mode);
    }

    public void startGame() {
        if (!isgamestarted) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ROOT);
            String datestart = dateFormat.format(Calendar.getInstance().getTime());
            Log.d("gamesave", "set datestart to " + datestart);
            game.setDatestart(datestart);
            isgamestarted = true;
        }
    }

    public void endGame() {
        if (isgamestarted) {
            isgamestarted = false;
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ROOT);
            String dateend = dateFormat.format(Calendar.getInstance().getTime());
            Log.d("gamesave", "set dateend to " + dateend);
            game.setDateend(dateend);
            game.setIds(includePlayers.get(0).getPlayerid(),
                    includePlayers.get(1).getPlayerid(),
                    includePlayers.get(2).getPlayerid(),
                    includePlayers.get(3).getPlayerid());
            game.setMode("2x2mb");
            //game.setScore(t1.getCurrentItem(), t2.getCurrentItem());
            mReference.push().setValue(game);
        }
    }

    public void notify_game() {
        Log.d("score2", "game notified");
        game.setScore(t1.getCurrentItem(), t2.getCurrentItem());
        if ((game.getScore1() >= threshold)|| (game.getScore2() >= threshold)) endGame();
    }
}
