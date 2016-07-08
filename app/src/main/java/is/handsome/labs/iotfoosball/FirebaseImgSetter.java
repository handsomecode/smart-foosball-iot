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

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseImgSetter {
    private HashMap<String,Uri> links;
    private StorageReference storageRef;
    private Picasso picasso;

    public FirebaseImgSetter(StorageReference storageReference, Context context) {
        this.storageRef = storageReference;
        picasso = Picasso.with(context);
        links = new HashMap<>();
    }

    public void setImg(final String link, ImageView imageView) {
        final WeakReference<ImageView> sImageView = new WeakReference<ImageView>(imageView);
        imageView.setTag(link); //protection from reusing unactual img
        imageView.setImageResource(R.mipmap.opengamer);
        if (!links.containsKey(link)) {
            StorageReference pathRef = storageRef.child(link);
            pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ImageView imageView = sImageView.get();
                    Log.d("myLog", "save Uri link to " + link + " is " + uri.toString());
                    links.put(link, uri);
                    if ((imageView.getTag() == link) && (imageView != null))
                        picasso.load(uri).into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ImageView imageView = sImageView.get();
                    Log.d("exception", e.toString());
                    if ((imageView.getTag() == link) && (imageView != null))
                        picasso.load(R.mipmap.opengamer).into(imageView);
                }
            });
        }
        else {
            picasso.load(links.get(link)).into(imageView);
        }

        //  Same but w\o picasso

//        File localFile = null;
//        try {
//            localFile = File.createTempFile("avatars", "jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final File finalLocalFile = localFile;
//        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//
//                Bitmap myBitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
//                v.setImageBitmap(myBitmap);
//                Log.d("myLog", "firebase end");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Log.d("myLog", "file creating failed");
//                exception.printStackTrace();
//            }
//        });
    }

    public void setAvatar (String nick, ImageView imageView) {
        setImg("avatars/" + nick.toLowerCase() + ".jpg", imageView);
    }

    public void delImg(final String link) {
        if (links.containsKey(link)) {
            links.remove(link);
        }
    }
}
