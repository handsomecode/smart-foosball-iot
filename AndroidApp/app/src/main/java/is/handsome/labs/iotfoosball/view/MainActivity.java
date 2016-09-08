package is.handsome.labs.iotfoosball.view;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import is.handsome.labs.iotfoosball.R;
import is.handsome.labs.iotfoosball.presenter.InterfacePresenterFromView;
import is.handsome.labs.iotfoosball.presenter.Presenter;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements InterfaceViewFromPresenter {

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
    private ArrayList<PlayerViewGroup> playerViewGroups;

    private Scorebar scorebarA;
    private Scorebar scorebarB;

    private InterfacePresenterFromView interfacePresenterFromView;

    @BindView(R.id.main_constrainlayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.inc0_cv)
    CardView incCv0;
    @BindView(R.id.inc1_cv)
    CardView incCv1;
    @BindView(R.id.inc2_cv)
    CardView incCv2;
    @BindView(R.id.inc3_cv)
    CardView incCv3;

    @BindView(R.id.btnstart)
    Button btnstart;
    @BindView(R.id.btnend)
    Button btnend;
    @BindView(R.id.btncntdwn)
    Button btncntdwn;
    @BindView(R.id.playersRecyclerView)
    RecyclerView RecyclerView;
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

        interfacePresenterFromView = new Presenter(this);

        includesInit();

        recyclerViewInit();

        scoreBarInit();

        RecyclerView.setAdapter(interfacePresenterFromView.getPlayerRecyclerAdapter());

        interfacePresenterFromView.initListeners();

        for (int i = 0; i < 4; i++) {
            playerViewGroups.get(i)
                    .getInc()
                    .setOnDragListener(interfacePresenterFromView.getDragListenerForIncludes(i));
        }

        constraintLayout.setOnDragListener(interfacePresenterFromView.getDragListenerForBackground());

    }

    @Override
    protected void onResume() {
        super.onResume();
        interfacePresenterFromView.onActivityResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        interfacePresenterFromView.onActivityPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        interfacePresenterFromView.onActivityStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        interfacePresenterFromView.onActivityStop();
    }

    @OnClick(R.id.btnstart)
    public void onStartClick() {
       interfacePresenterFromView.onStartClick();
    }

    @OnClick(R.id.btnend)
    public void onEndClick() {
        interfacePresenterFromView.onEndClick();
    }

    @OnClick(R.id.btncntdwn)
    public void onCountdownClick() {
        interfacePresenterFromView.onCntdwnClick();
    }

    @OnClick(R.id.Timer)
    public void onTimerClick() {
        interfacePresenterFromView.onTimerClick();
    }

    @Override
    public void setTime(String time) {
        btntimer.setText(time);
    }

    @Override
    public void setScorebarA(int ScoreA) {
        scorebarA.setScore(ScoreA);
    }

    @Override
    public void setScorebarB(int ScoreB) {
        scorebarB.setScore(ScoreB);
    }

    @Override
    public TextView getIncludeNick(int position) {
        return playerViewGroups.get(position).nick;
    }

    @Override
    public TextView getIncludeScore(int position) {
        return playerViewGroups.get(position).score;
    }

    @Override
    public ImageView getIncludeAvatar(int position) {
        return playerViewGroups.get(position).avatar;
    }

    private void includesInit() {
        playerViewGroups = new ArrayList<>(4);
        ArrayList<View> includes = new ArrayList<>(4);

        //adding butterknife for player's include
        includes.add(incCv0);
        includes.add(incCv1);
        includes.add(incCv2);
        includes.add(incCv3);

        Timber.d("inc added");

        for (int i = 0; i < 4; i++) {
            playerViewGroups.add(new PlayerViewGroup(includes.get(i)));
            ButterKnife.bind(playerViewGroups.get(i), includes.get(i));
            playerViewGroups.get(i).nick.setText("player"); //TODO move this to presentor
            playerViewGroups.get(i).score.setText("");
        }
        Timber.d("components added");
    }

    private void recyclerViewInit() {
        RecyclerView.setHasFixedSize(true);
        android.support.v7.widget.RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.setLayoutManager(layoutManager);
    }

    private void scoreBarInit() {
        ArrayList<ScoreViewPager> score1 = new ArrayList<>(2);
        score1.add(t1u);
        score1.add(t1d);
        scorebarA = new Scorebar(this, interfacePresenterFromView, score1, A);

        ArrayList<ScoreViewPager> score2 = new ArrayList<>(2);
        score2.add(t2u);
        score2.add(t2d);
        scorebarB = new Scorebar(this, interfacePresenterFromView, score2, B);
    }
}
