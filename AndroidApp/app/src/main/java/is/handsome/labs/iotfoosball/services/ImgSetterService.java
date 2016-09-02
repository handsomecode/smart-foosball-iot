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

import is.handsome.labs.iotfoosball.R;

public class ImgSetterService {
    private HashMap<String,Uri> links;
    private StorageReference storageRef;
    private Picasso picasso;

    public ImgSetterService(Context context, String link) {
        StorageReference storageRef = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(link);
        this.storageRef = storageRef;
        this.picasso = Picasso.with(context);
        this.links = new HashMap<>();
    }

    public void setImg(final String link, ImageView imageView) {
        final WeakReference<ImageView> ImageViewWeakRef = new WeakReference<ImageView>(imageView);
        imageView.setTag(link); //protection from reusing unactual img
        imageView.setImageResource(0);
        if (!links.containsKey(link)) {
            StorageReference pathRef = storageRef.child(link);
            pathRef.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageView imageView = ImageViewWeakRef.get();
                            Log.d("myLog", "save Uri link to " + link + " is " + uri.toString());
                            links.put(link, uri);
                            if ((imageView != null) && (imageView.getTag() == link))
                                picasso.load(uri).into(imageView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ImageView imageView = ImageViewWeakRef.get();
                            Log.d("exception", e.toString());
                            if ((imageView != null) && (imageView.getTag() == link))
                                picasso.load(R.mipmap.opengamer).into(imageView);
                        }
                    });
        } else {
            picasso.load(links.get(link)).into(imageView);
        }
    }

    public void setAvatar (String nick, ImageView imageView) {
        setImg("avatars/" + nick.toLowerCase() + ".jpg", imageView);
    }

    public void setNullImg(ImageView imageView) {
        picasso.cancelRequest(imageView);
        imageView.setImageDrawable(null);
    }
}
