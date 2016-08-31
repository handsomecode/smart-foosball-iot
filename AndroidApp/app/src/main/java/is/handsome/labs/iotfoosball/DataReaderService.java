package is.handsome.labs.iotfoosball;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

public class DataReaderService {
    private String fbLogin;
    private String fbPassword;
    private String yandexApi;

    public DataReaderService(Context context, int dataId) {
        InputStream data = context.getResources().openRawResource(dataId);
        BufferedReader bData = new BufferedReader(new InputStreamReader(data));
        fbLogin = "login";
        fbPassword = "password";
        yandexApi = "yandexAPI";
        try {
            fbLogin = bData.readLine();
            Timber.d("Login " + fbLogin);
            fbPassword = bData.readLine();
            Timber.d("Password " + fbPassword);
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
}
