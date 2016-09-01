package is.handsome.labs.iotfoosball;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends FeedbackFromPresenterActivity {

    public static final int A = 1;
    public static final int B = 2;

    @IntDef({A, B})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Teams {}

    //TODO use dagger for injecting all firebase object when it is necessary
    //TODO change transp color in textview to Theme transp color
    //TODO correct Layout (scorebars position is wrong)
    //TODO creat enum for modeselect

    //View
    private ArrayList<IncludePlayer> includePlayers;
    private PlayerRecyclerAdapter playerRecyclerAdapter;
    private Scorebar scorebarA;
    private Scorebar scorebarB;

    private Interactor interactor;

    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;

    @BindView(R.id.inc0)
    CardView inc0;
    @BindView(R.id.inc1)
    CardView inc1;
    @BindView(R.id.inc2)
    CardView inc2;
    @BindView(R.id.inc3)
    CardView inc3;

    @BindView(R.id.btnstart)
    Button btnstart;
    @BindView(R.id.btnend)
    Button btnend;
    @BindView(R.id.btncntdwn)
    Button btncntdwn;
    @BindView(R.id.playersRecyclerView)
    android.support.v7.widget.RecyclerView RecyclerView;
    @BindView(R.id.Timer)
    Button btntimer;

    @BindView(R.id.t1d)
    ScoreViewPager t1d;
    @BindView(R.id.t1u)
    ScoreViewPager t1u;
    @BindView(R.id.t2d)
    ScoreViewPager t2d;
    @BindView(R.id.t2u)
    ScoreViewPager t2u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timber.plant(new Timber.DebugTree());

        Log.d("myLog", "New start");

        interactor = new Interactor(this);

        includesInit();

        recyclerViewInit();

        scoreBarInit();

        RecyclerView.setAdapter(playerRecyclerAdapter);

        interactor.initListeners();

        for (int i = 0; i < 4; i++) {
            includePlayers.get(i)
                    .getInc()
                    .setOnDragListener(new DragListenerForIncludes(interactor, i));
        }

        main_layout.setOnDragListener(new DragListenerForBackground(interactor));

    }

    @Override
    protected void onResume() {
        super.onResume();
        interactor.onActivityResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        interactor.onActivityPause();
    }


    @Override
    public void onStart() {
        super.onStart();
        interactor.onActivityStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        interactor.onActivityStop();
    }

    @OnClick(R.id.btnstart)
    public void onStartClick() {
       interactor.onStartClick();
    }

    @OnClick(R.id.btnend)
    public void onEndClick() {
        interactor.onEndClick();
    }

    @OnClick(R.id.btncntdwn)
    public void onCntdwnClick() {
        interactor.onCntdwnClick();
    }

    @OnClick(R.id.Timer)
    public void onTimerClick() {
        interactor.onTimerClick();
    }

    @Override
    public void notifyDataSetRecyclerViewChanged() {
        playerRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    void setTime(String time) {
        btntimer.setText(time);
    }

    @Override
    void setPlayerInInclude(int position, int index) {
        PlayerViewInfo playerViewInfo = interactor.getPlayerViewInfoByPosition(index);
        includePlayers.get(position).nick.setText(playerViewInfo.getNick());
        includePlayers.get(position).score.setText(playerViewInfo.getScore());

       interactor.getImgSetterService().setAvatar(
                playerViewInfo.getNick(),
                includePlayers.get(position).avatar);

        includePlayers.get(position).avatar.setOnLongClickListener(
                new OnPlayerLongClickListener(index,
                        position,
                        includePlayers.get(position).getInc()));
    }

    @Override
    void clearPlayerInInclude(int position) {
        if (!(position >=4 || position < 0)) {
            includePlayers.get(position).nick.setText("player");
            includePlayers.get(position).score.setText("");
            interactor.getImgSetterService().setNullImg(includePlayers.get(position).avatar);
            includePlayers.get(position).avatar.setOnLongClickListener(null);
        }
    }

    @Override
    void setScorebarA(int ScoreA) {
        scorebarA.setScore(ScoreA);
    }

    @Override
    void setScorebarB(int ScoreB) {
        scorebarB.setScore(ScoreB);
    }

    private void includesInit() {
        includePlayers = new ArrayList<>(4);
        ArrayList<View> includes = new ArrayList<>(4);

        //adding butterknife for player's include
        includes.add(inc0);
        includes.add(inc1);
        includes.add(inc2);
        includes.add(inc3);

        Timber.d("inc added");

        for (int i = 0; i < 4; i++) {
            includePlayers.add(new IncludePlayer(includes.get(i)));
            ButterKnife.bind(includePlayers.get(i), includes.get(i));
            includePlayers.get(i).nick.setText("player");
            includePlayers.get(i).score.setText("");
        }
        Timber.d("components added");
    }

    private void recyclerViewInit() {
        RecyclerView.setHasFixedSize(true);
        android.support.v7.widget.RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.setLayoutManager(layoutManager);
        playerRecyclerAdapter = new PlayerRecyclerAdapter(interactor);
    }

    private void scoreBarInit() {
        ArrayList<ScoreViewPager> score1 = new ArrayList<>();
        score1.add(t1u);
        score1.add(t1d);
        scorebarA = new Scorebar(this, interactor, score1, A);

        ArrayList<ScoreViewPager> score2 = new ArrayList<>();
        score2.add(t2u);
        score2.add(t2d);
        scorebarB = new Scorebar(this, interactor, score2, B);
    }
}
