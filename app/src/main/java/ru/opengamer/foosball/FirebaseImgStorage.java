package ru.opengamer.foosball;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Opengamer on 03.07.2016.
 */
public class FirebaseImgStorage {
    private HashMap<String,Uri> links;
    private StorageReference storageRef;
    private Context context;

    public FirebaseImgStorage(StorageReference storageReference, Context context) {
        this.storageRef = storageReference;
        this.context = context;
        links = new HashMap<>();
    }

    public void setImg(final String link, final ImageView imageView) {
        if (!links.containsKey(link)) {
            StorageReference pathRef = storageRef.child(link);
            pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("myLog", "save Uri link to " + link + " is " + uri.toString());
                    links.put(link, uri);
                    Picasso.with(context).load(uri).into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("exception", e.toString());
                    Picasso.with(context).load(R.mipmap.opengamer).into(imageView);
                }
            });
        }
        else {
            Picasso.with(context).load(links.get(link)).into(imageView);
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

    public void delImg(final String link) {
        if (links.containsKey(link)) {
            links.remove(link);
        }
    }
}
