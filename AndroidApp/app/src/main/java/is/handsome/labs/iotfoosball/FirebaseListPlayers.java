package is.handsome.labs.iotfoosball;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class FirebaseListPlayers extends FirebaseList<Player> {

    private FirebaseImgSetter mFirebaseImgSetter;
    private RecyclerAdapter mRecyclerAdapter;
    private List<Game> mGames;

    public FirebaseListPlayers(DatabaseReference mRef, RecyclerAdapter recyclerAdapter, List<Game> games,
                               FirebaseImgSetter firebaseImgSetter) {
        super(mRef, Player.class);
        this.mGames = games;
        this.mRecyclerAdapter = recyclerAdapter;
        this.mFirebaseImgSetter = firebaseImgSetter;
    }

    @Override
    protected void afterAdded(int index) {
        Timber.d("# " + String.valueOf(index) + " " + mDataList.get(index).getNick() + " added");
        calcScore(index);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void afterChanged(int index) {
        calcScore(index);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void afterRemoved(int index) {
        mRecyclerAdapter.notifyDataSetChanged();
    }

    public void recalcScore(String key) {
        Log.d("score", "trying to reacalc " + key);
        int index = mKeyList.indexOf(key);
        Log.d("score", "index of key " + index);
        for (int i = 0; i < mKeyList.size(); i++) {
            Log.d("score", "mKeyList " + i + " " + mKeyList.get(i));
        }
        if (index != -1) {
            Log.d("score", "recalc in " + key);
            calcScore(index);
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public FirebaseImgSetter getFirebaseImgSetter() {
        return mFirebaseImgSetter;
    }

    public String calcScore(int index) {
        int winCount = 0;
        int loseCount = 0;
        Log.d("score", "calcScore game.size = " + String.valueOf(mGames.size()));
        for (int i = 0; i < mGames.size(); i++) {
            Log.d("score", "calc game # " + i);
            Log.d("score", "mKeyList.get(index) = " + mKeyList.get(index));
            Log.d("score", "id11 " + mGames.get(i).getIdPlayerA1()
                    + " id12 " + mGames.get(i).getIdPlayerA2()
                    + " id21 " + mGames.get(i).getIdPlayerB1()
                    + " id22 " + mGames.get(i).getIdPlayerB2());
            Log.d("score", "score 1 " + mGames.get(i).getScoreA());
            Log.d("score", "score 2 " + mGames.get(i).getScoreB());
            if (Objects.equals(mGames.get(i).getIdPlayerA1(), mKeyList.get(index)) ||
                    Objects.equals(mGames.get(i).getIdPlayerA2(), mKeyList.get(index))) {
                if (mGames.get(i).getScoreA() > mGames.get(i).getScoreB()) {
                    winCount++;
                }
                else {
                    loseCount++;
                }
            }
            if (Objects.equals(mGames.get(i).getIdPlayerB1(), mKeyList.get(index)) ||
                    Objects.equals(mGames.get(i).getIdPlayerB2(), mKeyList.get(index))) {
                if (mGames.get(i).getScoreA() < mGames.get(i).getScoreB()) {
                    winCount++;
                }
                else {
                    loseCount++;
                }
            }
        }
        mDataList.get(index).setWins(winCount);
        mDataList.get(index).setLoses(loseCount);
        Log.d("score", mDataList.get(index).getNick() + " W " + winCount + " L " + loseCount);
        return (winCount+":"+loseCount);
    }
}
