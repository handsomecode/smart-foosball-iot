package is.handsome.labs.iotfoosball;

import java.util.ArrayList;
import java.util.List;

public class ActionGameListener extends ActionListener<Game> {
    private final List<PlayerWithScore> mPlayerWithScores;
    private PlayerRecyclerAdapter mPlayerRecyclerAdapter;

    ActionGameListener(List<PlayerWithScore> playerWithScores,
            PlayerRecyclerAdapter playerRecyclerAdapter) {
        this.mPlayerWithScores = playerWithScores;
        this.mPlayerRecyclerAdapter = playerRecyclerAdapter;
    }

    @Override
    public void addingPerformed(String key, Game data, int index) {
        addScore(key, data, index);
        mPlayerRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void removingPerformed(String key, Game data, int index) {
        if ((data.getScoreA() < data.getScoreB())) {
            return;
        }
        boolean isAWinner;
        isAWinner = data.getScoreA() < data.getScoreB();
        synchronized (mPlayerWithScores) {
            for (int i = 0; i < mPlayerWithScores.size(); i++) {
                if (data.getIdPlayerA1().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).decreaseGame(isAWinner);
                }
                if (data.getIdPlayerA2().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).decreaseGame(isAWinner);
                }
                if (data.getIdPlayerB1().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).decreaseGame(!isAWinner);
                }
                if (data.getIdPlayerB1().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).decreaseGame(!isAWinner);
                }
            }
        }
        mPlayerRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void initialisation(List<String> keyList, List<Game> dataList) {
        for (int i = 0; i < keyList.size(); i++) {
            addScore(keyList.get(i), dataList.get(i), i);
        }
        mPlayerRecyclerAdapter.notifyDataSetChanged();
    }

    private void addScore(String playerId, Game data, int index) {
        if ((data.getScoreA() == data.getScoreB())) {
            return;
        }
        boolean isAWinner;
        isAWinner = data.getScoreA() > data.getScoreB();
        boolean isPlayerA1Found = false;
        boolean isPlayerA2Found = false;
        boolean isPlayerB1Found = false;
        boolean isPlayerB2Found = false;
        synchronized (mPlayerWithScores) {
            for (int i = 0; i < mPlayerWithScores.size(); i++) {
                if (data.getIdPlayerA1().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).increaseGame(isAWinner);
                    isPlayerA1Found = true;
                }
                if (data.getIdPlayerA2().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).increaseGame(isAWinner);
                    isPlayerA2Found = true;
                }
                if (data.getIdPlayerB1().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).increaseGame(!isAWinner);
                    isPlayerB1Found = true;
                }
                if (data.getIdPlayerB2().equals(mPlayerWithScores.get(i).getPlayerId())) {
                    mPlayerWithScores.get(i).increaseGame(!isAWinner);
                    isPlayerB2Found = true;
                }
            }
            if (!isPlayerA1Found && !data.getIdPlayerA1().equals("")) {
                mPlayerWithScores.add(new PlayerWithScore(data.getIdPlayerA1()));
                mPlayerWithScores.get(mPlayerWithScores.size()-1).increaseGame(isAWinner);
            }
            if (!isPlayerA2Found && !data.getIdPlayerA2().equals("")) {
                mPlayerWithScores.add(new PlayerWithScore(data.getIdPlayerA2()));
                mPlayerWithScores.get(mPlayerWithScores.size()-1).increaseGame(isAWinner);
            }
            if (!isPlayerB1Found && !data.getIdPlayerB1().equals("")) {
                mPlayerWithScores.add(new PlayerWithScore(data.getIdPlayerB1()));
                mPlayerWithScores.get(mPlayerWithScores.size()-1).increaseGame(isAWinner);
            }
            if (!isPlayerB2Found && !data.getIdPlayerB2().equals("")) {
                mPlayerWithScores.add(new PlayerWithScore(data.getIdPlayerB2()));
                mPlayerWithScores.get(mPlayerWithScores.size()-1).increaseGame(isAWinner);
            }
        }
    }
}
