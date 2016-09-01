package is.handsome.labs.iotfoosball;

import java.util.Collections;
import java.util.List;

public class ActionGameListener extends ActionListener<Game> {
    private final List<PlayerWithScore> playerWithScores;
    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;

    ActionGameListener(List<PlayerWithScore> playerWithScores,
            InterfacePresentorFromInteractor interfacePresentorFromInteractor) {
        this.playerWithScores = playerWithScores;
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;
    }

    @Override
    public void addingPerformed(String key, Game data, int index) {
        addScore(key, data, index);
        interfacePresentorFromInteractor.notifyDataSetRecyclerViewChanged();
    }

    @Override
    public void removingPerformed(String key, Game data, int index) {
        if ((data.getScoreA() < data.getScoreB())) {
            return;
        }
        boolean isAWinner;
        isAWinner = data.getScoreA() < data.getScoreB();
        synchronized (playerWithScores) {
            for (int i = 0; i < playerWithScores.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    if (data.getPlayer(j).equals(playerWithScores.get(i).getPlayerId())){
                        playerWithScores.get(i).decreaseGame(j < 2 ? isAWinner : !isAWinner);
                    }
                }
            }
        }
        interfacePresentorFromInteractor.notifyDataSetRecyclerViewChanged();
    }

    @Override
    public void initialisation(List<String> keyList, List<Game> dataList) {
        for (int i = 0; i < keyList.size(); i++) {
            addScore(keyList.get(i), dataList.get(i), i);
        }
        interfacePresentorFromInteractor.notifyDataSetRecyclerViewChanged();
    }

    private void addScore(String playerId, Game data, int index) {
        if ((data.getScoreA() == data.getScoreB())) {
            return;
        }
        boolean isAWinner;
        isAWinner = data.getScoreA() > data.getScoreB();
        boolean isPlayerFound[] = {false, false, false, false};
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (data.getPlayer(j).equals(data.getPlayer(i))) {
                    isPlayerFound[j] = true;
                }
            }
        }
        synchronized (playerWithScores) {
            for (int i = 0; i < playerWithScores.size(); i++) {
                for (int j = 0; j < 4; j++)
                    if (data.getPlayer(j).equals(playerWithScores.get(i).getPlayerId())) {
                        playerWithScores.get(i).increaseGame(j < 2 ? isAWinner : !isAWinner);
                        isPlayerFound[j] = true;
                        placePlayerByScore(i);
                    }
            }
            for (int j = 0; j < 4; j++) {
                if (!isPlayerFound[j] && !data.getPlayer(j).equals("")) {
                    playerWithScores.add(new PlayerWithScore(data.getPlayer(j)));
                    playerWithScores.get(playerWithScores.size()-1)
                            .increaseGame(j < 2 ? isAWinner : !isAWinner);
                    placePlayerByScore(playerWithScores.size()-1);
                }
            }
        }
    }

    private boolean placePlayerByScore(int index) {
        if (index == 0) {
            return false;
        }
        int i = index;
        if (index == 1) {
            if (playerWithScores.get(index).getWins() > playerWithScores.get(i-1).getWins()) {
                Collections.swap(playerWithScores, index, 0);
            }
            return true;
        }
        while (playerWithScores.get(index).getWins() > playerWithScores.get(i-1).getWins()) {
            i--;
        }
        Collections.swap(playerWithScores, index, i);
        return true;
    }
}
