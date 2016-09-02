package is.handsome.labs.iotfoosball.view;

public interface InterfaceViewFromPresenter {
    void notifyDataSetRecyclerViewChanged();

    void setTime(String time);

    void setPlayerInInclude(int position, int index);

    void clearPlayerInInclude(int position);

    void setScorebarA(int ScoreA);

    void setScorebarB(int ScoreB);
}
