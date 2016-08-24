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
                for (int j = 0; j < 4; j++) {
                    if (data.getPlayer(j).equals(mPlayerWithScores.get(i).getPlayerId())){
                        mPlayerWithScores.get(i).decreaseGame(j < 2 ? isAWinner : !isAWinner);
                    }
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
        boolean isPlayerFound[] = {false, false, false, false};
        synchronized (mPlayerWithScores) {
            for (int i = 0; i < mPlayerWithScores.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    if (data.getPlayer(j).equals(mPlayerWithScores.get(i).getPlayerId())){
                        mPlayerWithScores.get(i).decreaseGame(j < 2 ? isAWinner : !isAWinner);
                        isPlayerFound[j] = true;
                    }
                }
            }
            for (int j = 0; j < 4; j++) {
                if (!isPlayerFound[j] && !data.getPlayer(j).equals("")){
                    mPlayerWithScores.add(new PlayerWithScore(data.getPlayer(j)));
                    mPlayerWithScores.get(mPlayerWithScores.size()-1).increaseGame(isAWinner);
                }
            }
        }
    }
}
