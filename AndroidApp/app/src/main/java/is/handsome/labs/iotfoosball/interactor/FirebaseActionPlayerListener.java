package is.handsome.labs.iotfoosball.interactor;

import java.util.List;

import is.handsome.labs.iotfoosball.models.FirebaseActionListener;
import is.handsome.labs.iotfoosball.presenter.InterfacePresentorFromInteractor;
import is.handsome.labs.iotfoosball.models.Player;
import is.handsome.labs.iotfoosball.models.PlayerWithScore;
import timber.log.Timber;

class FirebaseActionPlayerListener extends FirebaseActionListener<Player> {
    private final List<PlayerWithScore> playerWithScores;
    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;

    FirebaseActionPlayerListener(List<PlayerWithScore> playerWithScores,
            InterfacePresentorFromInteractor interfacePresentorFromInteractor) {
        this.playerWithScores = playerWithScores;
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;
    }

    @Override
    public void addingPerformed(String key, Player data, int index) {
        addPlayer(key, data, index);
        interfacePresentorFromInteractor.notifyDataPlayerSetRecyclerViewChanged();
    }

    @Override
    public void removingPerformed(String key, Player data, int index) {
        synchronized (playerWithScores) {
            for (int i = 0; i < playerWithScores.size(); i++) {
                if (playerWithScores.get(i).getPlayerId().equals(key)) {
                    playerWithScores.remove(i);
                    break;
                }
            }
        }
        interfacePresentorFromInteractor.notifyDataPlayerSetRecyclerViewChanged();
    }

    @Override
    public void initialisation(List<String> keyList, List<Player> dataList) {
        for (int i = 0; i < keyList.size(); i++) {
            addPlayer(keyList.get(i), dataList.get(i), i);
        }
        interfacePresentorFromInteractor.notifyDataPlayerSetRecyclerViewChanged();
    }

    private boolean addPlayer(String key, Player data, int index) {
        boolean isExist = false;
        synchronized (playerWithScores) {
            int i = 0;
            for (; i < playerWithScores.size(); i++) {
                if (playerWithScores.get(i).getPlayerId().equals(key)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Timber.d("adding new player " + key);
                playerWithScores.add(new PlayerWithScore(key, data));
            } else {
                playerWithScores.get(i).setPlayer(data);
                Timber.d("already existed " + key);
            }
        }
        return !isExist;
    }
}
