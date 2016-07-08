package is.handsome.labs.iotfoosball;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Objects;

public class FirebaseListPlayers extends FirebaseList<Player> {

    FirebaseImgStorage firebaseImgStorage;

    private RecyclerAdapter recyclerAdapter;
    private List<Game> games;

    public FirebaseListPlayers(DatabaseReference mRef, RecyclerAdapter recyclerAdapter, List<Game> games,
                               FirebaseImgStorage firebaseImgStorage) {
        super(mRef, Player.class);
        this.games = games;
        this.recyclerAdapter = recyclerAdapter;
        this.firebaseImgStorage = firebaseImgStorage;
    }

    public FirebaseImgStorage getFirebaseImgStorage() {
        return firebaseImgStorage;
    }

    @Override
    protected void afterAdded(int index) {
        Log.d("score", dataList.get(index).getNick() + " added");
        calcScore(index);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void afterChanged(int index) {
        calcScore(index);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void afterRemoved(int index) {
        recyclerAdapter.notifyDataSetChanged();
    }

    public void recalcScore(String key) {
        Log.d("score", "trying to reacalc " + key);
        int index = keyList.indexOf(key);
        Log.d("score", "index of key " + index);
        for (int i = 0; i < keyList.size(); i++) {
            Log.d("score", "keyList " + i + " " + keyList.get(i));
        }
        if (index != -1) {
            Log.d("score", "recalc in " + key);
            calcScore(index);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    public String calcScore(int index) {
        int winCount = 0;
        int loseCount = 0;
        Log.d("score", "calcScore game.size = " + String.valueOf(games.size()));
        for (int i = 0; i < games.size(); i++) {
            Log.d("score", "calc game # " + i);
            Log.d("score", "keyList.get(index) = " + keyList.get(index));
            Log.d("score", "id11 " + games.get(i).getIdplayer11()
                    + " id12 " + games.get(i).getIdplayer12()
                    + " id21 " + games.get(i).getIdplayer21()
                    + " id22 " + games.get(i).getIdplayer22());
            Log.d("score", "score 1 " + games.get(i).getScore1());
            Log.d("score", "score 2 " + games.get(i).getScore2());
            if (Objects.equals(games.get(i).getIdplayer11(), keyList.get(index)) ||
                    Objects.equals(games.get(i).getIdplayer12(), keyList.get(index))) {
                if (games.get(i).getScore1() > games.get(i).getScore2()) {
                    winCount++;
                }
                else {
                    loseCount++;
                }
            }
            if (Objects.equals(games.get(i).getIdplayer21(), keyList.get(index)) ||
                    Objects.equals(games.get(i).getIdplayer22(), keyList.get(index))) {
                if (games.get(i).getScore1() < games.get(i).getScore2()) {
                    winCount++;
                }
                else {
                    loseCount++;
                }
            }
        }
        dataList.get(index).setWin(winCount);
        dataList.get(index).setLose(loseCount);
        Log.d("score", dataList.get(index).getNick() + " W " + winCount + " L " + loseCount);
        return (winCount+":"+loseCount);
    }
}
