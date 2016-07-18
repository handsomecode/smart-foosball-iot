package is.handsome.labs.iotfoosball;

import android.app.Activity;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends Activity {

    //TODO use dagger for injecting all firebase object when it is necessary
    //TODO change transp color in textview to Theme transp color
    //TODO correct Layout (scorebars position is wrong)
    //TODO creat enum for modeselect

    private ArrayList<IncludePlayer> mIncludeplayers;
    private static SerialHandler sSerialHandler;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseListPlayers mFirebasePlayers;
    private FirebaseListGames mFirebaseGames;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseImgSetter mFirebaseImgSetter;
    private SoundPool mSoundPool;
    private int mSoundId;
    private SerialUsb mSerialUsb;
    private ArrayList<View> mIncludes;
    private android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private Scorebar mScorebarA;
    private Scorebar mScorebarB;
    private CurentGame mCurentGame;

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
    @BindView(R.id.RecyclerView)
    android.support.v7.widget.RecyclerView RecyclerView;

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
        Log.d("myLog", "New start");

        includesInit();

        Timber.plant(new Timber.DebugTree());

        mSoundPool = new SoundPool.Builder().build();
        mSoundId = mSoundPool.load(this, R.raw.countdown, 1);

        firebaseAuth();

        recyclerViewInit();

        mStorageRef = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl("gs://handsomefoosball.appspot.com");
        mFirebaseImgSetter = new FirebaseImgSetter(this, mStorageRef);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseGames = new FirebaseListGames(mDatabase.child("games"), mIncludeplayers);

        mFirebasePlayers = new FirebaseListPlayers(mDatabase.child("players"),
                mRecyclerAdapter,
                mFirebaseGames.getDataList(),
                mFirebaseImgSetter);

        mFirebaseGames.setPlayer(mFirebasePlayers);

        mRecyclerAdapter.setFirebase(mFirebasePlayers, mFirebaseImgSetter);

        RecyclerView.setAdapter(mRecyclerAdapter);

        for (int i = 0; i < 4; i++) {
            mIncludeplayers.get(i)
                    .getInc()
                    .setOnDragListener(new DragListenerForIncludes(mIncludeplayers.get(i),
                            mFirebasePlayers));
        }

        curentGameInit();

        sSerialHandler = new SerialHandler(this, mScorebarA, mScorebarB);

        mSerialUsb = new SerialUsb(getApplicationContext(), sSerialHandler);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSerialUsb.close();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.btnstart)
    public void onStartClick() {
        mCurentGame.startGame();
    }

    @OnClick(R.id.btnend)
    public void onEndClick() {
        mCurentGame.endGame();
    }

    @OnClick(R.id.btncntdwn)
    public void onCntdwnClick() {
        mSoundPool.play(mSoundId, 1, 1, 1, 0, 1);
    }

    private void curentGameInit() {
        ArrayList<ScoreViewPager> score1 = new ArrayList<>();
        score1.add(t1u);
        score1.add(t1d);
        mScorebarA = new Scorebar(this, score1);

        ArrayList<ScoreViewPager> score2 = new ArrayList<>();
        score2.add(t2u);
        score2.add(t2d);
        mScorebarB = new Scorebar(this, score2);

        mCurentGame = new CurentGame(mIncludeplayers, mDatabase.child("games"), t1u, t2u);

        mScorebarA.setCurentGame(mCurentGame);
        mScorebarB.setCurentGame(mCurentGame);
    }

    private void recyclerViewInit() {
        RecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new RecyclerAdapter();
    }

    private void firebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("firebaseauth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("firebaseauth", "onAuthStateChanged:signed_out");
                }
            }
        };

        InputStream data = this.getResources().openRawResource(R.raw.data);
        BufferedReader bData = new BufferedReader(new InputStreamReader(data));
        String login = "login";
        String password = "password";
        try {
            login = bData.readLine();
            Log.d("myLogs", "Login " + login);
            password = bData.readLine();
            Log.d("myLogs", "Password " + password);
        } catch (IOException e) {
            Timber.d(e, "File with login pass error");
        }

        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("firebase auth", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("firebase auth", "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void includesInit() {
        mIncludeplayers = new ArrayList<>(4);
        mIncludes = new ArrayList<>(4);

        //adding butterknife for player's include
        mIncludes.add(inc0);
        mIncludes.add(inc1);
        mIncludes.add(inc2);
        mIncludes.add(inc3);

        Log.d("myLog", "inc added");

        for (int i = 0; i < 4; i++) {
            mIncludeplayers.add(new IncludePlayer(mIncludes.get(i)));
            ButterKnife.bind(mIncludeplayers.get(i), mIncludes.get(i));
            mIncludeplayers.get(i).nick.setText("player");
            mIncludeplayers.get(i).score.setText("");
        }
        Log.d("myLog", "components added");
    }

    static class SerialHandler extends Handler{
        private WeakReference<Activity> mActivity;
        private WeakReference<Scorebar> mScorebarA;
        private WeakReference<Scorebar> mScorebarB;

        public SerialHandler(Activity activity, Scorebar scorebarA, Scorebar scorebarB) {
            mActivity = new WeakReference<Activity>(activity);
            mScorebarA = new WeakReference<Scorebar>(scorebarA);
            mScorebarB = new WeakReference<Scorebar>(scorebarB);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 48) mScorebarA.get().goal();
            if (msg.what == 49) mScorebarB.get().goal();
        }

    }
}
