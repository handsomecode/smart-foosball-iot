package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class FirebaseImgSetter {
    private HashMap<String,Uri> mLinks;
    private StorageReference mStorageRef;
    private Picasso mPicasso;

    public FirebaseImgSetter(Context context, StorageReference storageReference) {
        this.mStorageRef = storageReference;
        mPicasso = Picasso.with(context);
        mLinks = new HashMap<>();
    }

    public void setImg(final String link, ImageView imageView) {
        final WeakReference<ImageView> ImageViewWeakRef = new WeakReference<ImageView>(imageView);
        imageView.setTag(link); //protection from reusing unactual img
        imageView.setImageResource(0);
        if (!mLinks.containsKey(link)) {
            StorageReference pathRef = mStorageRef.child(link);
            pathRef.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageView imageView = ImageViewWeakRef.get();
                            Log.d("myLog", "save Uri link to " + link + " is " + uri.toString());
                            mLinks.put(link, uri);
                            if ((imageView != null) && (imageView.getTag() == link))
                                mPicasso.load(uri).into(imageView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ImageView imageView = ImageViewWeakRef.get();
                            Log.d("exception", e.toString());
                            if ((imageView != null) && (imageView.getTag() == link))
                                mPicasso.load(R.mipmap.opengamer).into(imageView);
                        }
                    });
        } else {
            mPicasso.load(mLinks.get(link)).into(imageView);
        }
    }

    public void setAvatar (String nick, ImageView imageView) {
        setImg("avatars/" + nick.toLowerCase() + ".jpg", imageView);
    }

    public void delImg(String link) {
        if (mLinks.containsKey(link)) {
            mLinks.remove(link);
        }
    }

    public void setNullImg(ImageView imageView) {
        mPicasso.cancelRequest(imageView);
        imageView.setImageDrawable(null);
    }
}
