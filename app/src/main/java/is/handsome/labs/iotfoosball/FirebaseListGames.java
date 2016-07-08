package is.handsome.labs.iotfoosball;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirebaseListGames extends FirebaseList<Game> {
    private FirebaseListPlayers players;
    private ArrayList<IncludePlayer> includeplayers;

    public FirebaseListGames(DatabaseReference mRef, ArrayList<IncludePlayer> includeplayers) {
        super(mRef, Game.class);
        this.includeplayers = includeplayers;
    }

    public void setPlayer(FirebaseListPlayers players) {
        this.players = players;
    }

    @Override
    protected void afterAdded(int index) {
        Log.d("score", "game add " + index + " size of data " + dataList.size());
        if (players != null) {
            Log.d("score", "player = " + players.toString());
            players.recalcScore(dataList.get(index).getIdplayer11());
            players.recalcScore(dataList.get(index).getIdplayer12());
            players.recalcScore(dataList.get(index).getIdplayer21());
            players.recalcScore(dataList.get(index).getIdplayer22());
            reCalcIncludes();
       }
    }

    @Override
    protected void afterChanged(int index) {
        Log.d("score", "game change " + index + " size of data " + dataList.size());
        if (players != null) {
            players.recalcScore(dataList.get(index).getIdplayer11());
            players.recalcScore(dataList.get(index).getIdplayer12());
            players.recalcScore(dataList.get(index).getIdplayer21());
            players.recalcScore(dataList.get(index).getIdplayer22());
            reCalcIncludes();
        }
    }

    @Override
    protected void afterRemoved(int index) {
    }

    protected void reCalcIncludes() {
        for (int i = 0; i < 4; i++) {
            if (includeplayers.get(i).getPlayerid() != "") {
            int index = players.getKeyList().indexOf(includeplayers.get(i).getPlayerid());
                String score = String.format(Locale.US, "%d:%d",
                        players.getDataList().get(index).getWin(),
                        players.getDataList().get(index).getLose());
                includeplayers.get(i).score.setText(score);
            }
        }
    }
}
