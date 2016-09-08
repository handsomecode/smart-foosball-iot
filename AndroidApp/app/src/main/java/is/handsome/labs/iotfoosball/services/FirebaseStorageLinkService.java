package is.handsome.labs.iotfoosball.services;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Timer;

import is.handsome.labs.iotfoosball.R;
import timber.log.Timber;

public class FirebaseStorageLinkService {
    private HashMap<String,Uri> links;
    private StorageReference storageRef;
    private InterfaceFirebaseStorageLinkReciver interfaceFirebaseStorageLinkReciver;

    public FirebaseStorageLinkService(Context context, String link,
            InterfaceFirebaseStorageLinkReciver interfaceFirebaseStorageLinkReciver) {
        StorageReference storageRef = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(link);
        this.storageRef = storageRef;
        this.links = new HashMap<>();
        this.interfaceFirebaseStorageLinkReciver = interfaceFirebaseStorageLinkReciver;
    }

    public void requestAvatar(final String avatar) {
        final String link = "avatars/" + avatar + ".jpg";
        Timber.d("LINK = %s", link);
        if (!avatar.equals("")) {
            if (!links.containsKey(link)) {
                StorageReference pathRef = storageRef.child(link);
                pathRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Timber.d("Success on %s = %s", avatar, uri.toString());
                                interfaceFirebaseStorageLinkReciver.reciveLink(avatar, uri);
                                links.put(link, uri);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Timber.d("Fail on %s", avatar);
                                interfaceFirebaseStorageLinkReciver.reciveLink(avatar, null);
                            }
                        });
            } else {
                interfaceFirebaseStorageLinkReciver.reciveLink(avatar, links.get(link));
            }
        } else {
            interfaceFirebaseStorageLinkReciver.reciveLink(avatar, null);
        }
    }
}
