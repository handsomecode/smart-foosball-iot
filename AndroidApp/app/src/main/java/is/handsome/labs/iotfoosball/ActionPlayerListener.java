package is.handsome.labs.iotfoosball;

import java.util.List;

import timber.log.Timber;

public class ActionPlayerListener extends ActionListener<Player> {
    private final List<PlayerWithScore> mPlayerWithScores;
    private ActivityWithPresenter activityWithPresenter;

    ActionPlayerListener(List<PlayerWithScore> playerWithScores,
            ActivityWithPresenter activityWithPresenter) {
        this.mPlayerWithScores = playerWithScores;
        this.activityWithPresenter = activityWithPresenter;
    }

    @Override
    public void addingPerformed(String key, Player data, int index) {
        addPlayer(key, data, index);
        activityWithPresenter.notifyDataSetRecyclerViewChanged();
    }

    @Override
    public void removingPerformed(String key, Player data, int index) {
        synchronized (mPlayerWithScores) {
            for (int i = 0; i < mPlayerWithScores.size(); i++) {
                if (mPlayerWithScores.get(i).getPlayerId().equals(key)) {
                    mPlayerWithScores.remove(i);
                    break;
                }
            }
        }
        activityWithPresenter.notifyDataSetRecyclerViewChanged();
    }

    @Override
    public void initialisation(List<String> keyList, List<Player> dataList) {
        for (int i = 0; i < keyList.size(); i++) {
            addPlayer(keyList.get(i), dataList.get(i), i);
        }
        activityWithPresenter.notifyDataSetRecyclerViewChanged();
    }

    private boolean addPlayer(String key, Player data, int index) {
        boolean isExist = false;
        synchronized (mPlayerWithScores) {
            int i = 0;
            for (; i < mPlayerWithScores.size(); i++) {
                if (mPlayerWithScores.get(i).getPlayerId().equals(key)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Timber.d("adding new player " + key);
                mPlayerWithScores.add(new PlayerWithScore(key, data));
            } else {
                mPlayerWithScores.get(i).setPlayer(data);
                Timber.d("already existed " + key);
            }
        }
        return !isExist;
    }
}
