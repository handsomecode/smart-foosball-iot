package is.handsome.labs.iotfoosball.interactor;

import java.util.List;

import is.handsome.labs.iotfoosball.models.FirebaseActionListener;
import is.handsome.labs.iotfoosball.models.Team;
import is.handsome.labs.iotfoosball.presenter.InterfacePresentorFromInteractor;
import timber.log.Timber;


public class FirebaseActionTeamListener extends FirebaseActionListener<Team> {

    private List<Team> teamList;
    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;

    FirebaseActionTeamListener(List<Team> teamList,
            InterfacePresentorFromInteractor interfacePresentorFromInteractor) {
        this.teamList = teamList;
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;
    }

    @Override
    public void addingPerformed(String key, Team data, int index) {
        Team team = (Team) data;
        team.setId(key);
        addTeam(key, team, index);
        interfacePresentorFromInteractor.notifyDataTeamsSetRecyclerViewChanged();
    }

    @Override
    public void removingPerformed(String key, Team data, int index) {
        boolean isExist = false;
        int i = 0;
        for (; i < teamList.size(); i++) {
            if (teamList.get(i).getId().equals(key)) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            teamList.remove(i);
            Timber.d("Remove team " + i + " " + key);
        } else {
            Timber.d("team doesn't exist");
        }
        interfacePresentorFromInteractor.notifyDataTeamsSetRecyclerViewChanged();
    }

    @Override
    public void changingPerformed(String key, Team data, int index) {
        Team team = (Team) data;
        team.setId(key);
        addTeam(key, team, index);
        interfacePresentorFromInteractor.notifyDataTeamsSetRecyclerViewChanged();
    }

    @Override
    public void initialisation(List keyList, List dataList) {
        super.initialisation(keyList, dataList);
        interfacePresentorFromInteractor.notifyDataTeamsSetRecyclerViewChanged();
    }

    @Override
    public void listenerRemovingPerformed() {
        super.listenerRemovingPerformed();
        interfacePresentorFromInteractor.notifyDataTeamsSetRecyclerViewChanged();
    }

    private boolean addTeam(String key, Team data, int index) {
        boolean isExist = false;
        synchronized (teamList) {
            int i = 0;
            for (; i < teamList.size(); i++) {
                if (teamList.get(i).getId().equals(key)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Timber.d("adding new team " + key);
                teamList.add(data);
            } else {
                teamList.set(i, data);
                Timber.d("team already existed " + key);
            }
        }
        return !isExist;
    }
}
