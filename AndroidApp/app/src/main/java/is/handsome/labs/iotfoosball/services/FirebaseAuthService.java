package is.handsome.labs.iotfoosball.services;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthService {
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener fbAuthListener;

    public FirebaseAuthService(Context context, String fbLogin, String fbPassword) {

        fbAuth = FirebaseAuth.getInstance();

        fbAuthListener = new FirebaseAuth.AuthStateListener() {
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

        fbAuth.signInWithEmailAndPassword(fbLogin, fbPassword)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("firebase auth", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("firebase auth", "signInWithEmail", task.getException());
                        }
                    }
                });
    }

    public void onStart() {
        fbAuth.addAuthStateListener(fbAuthListener);
    }

    public void onStop() {
        if (fbAuthListener != null) {
            fbAuth.removeAuthStateListener(fbAuthListener);
        }
    }
}
