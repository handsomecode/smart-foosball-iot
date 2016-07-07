package ru.opengamer.foosball;

import android.app.Activity;
import android.media.SoundPool;
import android.os.Bundle;
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
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    ArrayList<IncludePlayer> includeplayers;

    //TODO use dagger for injecting all firebase object when it is necessary
    //TODO change transp color in textview to Theme transp color
    //TODO correct Layout (scorebars position is wrong)
    //TODO creat enum for modeselect

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseListPlayers firebasePlayers;
    private FirebaseListGames firebaseGames;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseImgStorage firebaseImgStorage;
    private SoundPool soundPool;
    private int soundId;

    private ArrayList<View> includes;

    private android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;

    private Scorebar scorebar1;
    private Scorebar scorebar2;

    private CurentGame curentGame;

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

        includeplayers = new ArrayList<>(4);
        includes = new ArrayList<>(4);

        //adding butterknife for player's include
        includes.add(inc0);
        includes.add(inc1);
        includes.add(inc2);
        includes.add(inc3);

        Log.d("myLog", "inc added");

        for (int i = 0; i < 4; i++) {
            includeplayers.add(new IncludePlayer(includes.get(i)));
            ButterKnife.bind(includeplayers.get(i), includes.get(i));
            includeplayers.get(i).nick.setText("player");
            includeplayers.get(i).score.setText("");
        }

        Log.d("myLog", "components added");

        soundPool = new SoundPool.Builder().build();
        soundId = soundPool.load(this, R.raw.countdown, 1);

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
            e.printStackTrace();
        }
        finally {
            try {
                bData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                data.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //TODO it's wrong to save password like this
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

        RecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new RecyclerAdapter();
        RecyclerView.setAdapter(mRecyclerAdapter);

        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://handsomefoosball.appspot.com");
        firebaseImgStorage = new FirebaseImgStorage(storageRef, this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseGames = new FirebaseListGames(mDatabase.child("games"), includeplayers);

        firebasePlayers = new FirebaseListPlayers(mDatabase.child("players"), mRecyclerAdapter, firebaseGames.getDataList(), firebaseImgStorage);

        firebaseGames.setPlayer(firebasePlayers);

        mRecyclerAdapter.setFirebase(firebasePlayers, firebaseImgStorage);

        for (int i = 0; i < 4; i++) {
            includeplayers.get(i).getInc().setOnDragListener(new DragListenerForIncludes(i, includeplayers.get(i), firebasePlayers, this));
        }

        ArrayList<ScoreViewPager> score1 = new ArrayList<>();
        score1.add(t1u);
        score1.add(t1d);
        scorebar1 = new Scorebar(score1, this);

        ArrayList<ScoreViewPager> score2 = new ArrayList<>();
        score2.add(t2u);
        score2.add(t2d);
        scorebar2 = new Scorebar(score2, this);

        curentGame = new CurentGame(includeplayers, mDatabase.child("games"), t1u, t2u);

        scorebar1.setCurentGame(curentGame);
        scorebar2.setCurentGame(curentGame);
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

    @OnClick({R.id.btnstart, R.id.btnend, R.id.btncntdwn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnstart:
                curentGame.startGame();
                break;
            case R.id.btnend:
                curentGame.endGame();
                break;
            case R.id.btncntdwn:
                soundPool.play(soundId, 1, 1, 1, 0, 1);
                break;
        }
    }
}
