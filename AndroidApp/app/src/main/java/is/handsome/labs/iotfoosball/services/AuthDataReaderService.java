package is.handsome.labs.iotfoosball.services;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

public class AuthDataReaderService {
    private String fbLogin;
    private String fbPassword;
    private String yandexApi;
    private String firebaseStorageLink;

    public AuthDataReaderService(Context context, int dataId) {
        InputStream data = context.getResources().openRawResource(dataId);
        BufferedReader bData = new BufferedReader(new InputStreamReader(data));
        fbLogin = "login";
        fbPassword = "password";
        yandexApi = "yandexAPI";
        firebaseStorageLink = "firebaseStorageLink";

        try {
            fbLogin = bData.readLine();
            Timber.d("Login " + fbLogin);
            fbPassword = bData.readLine();
            Timber.d("Password " + fbPassword);
            firebaseStorageLink = bData.readLine();
            Timber.d("Firebase storage link " + firebaseStorageLink);
            yandexApi = bData.readLine();
            Timber.d("Yandex API " + yandexApi);
        } catch (IOException e) {
            Timber.d(e, "File with login pass error");
        }
    }


    public String getFbLogin() {
        return fbLogin;
    }

    public String getFbPassword() {
        return fbPassword;
    }

    public String getYandexApi() {
        return yandexApi;
    }

    public String getFirebaseStorageLink() {
        return firebaseStorageLink;
    }
}
