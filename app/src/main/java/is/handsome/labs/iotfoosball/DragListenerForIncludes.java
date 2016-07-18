package is.handsome.labs.iotfoosball;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import java.util.Locale;

public class DragListenerForIncludes implements View.OnDragListener {

    private IncludePlayer mIncludePlayer;
    private FirebaseListPlayers mFirebaseListPlayers;

    public DragListenerForIncludes(IncludePlayer includePlayer,
                                   FirebaseListPlayers firebaseListPlayers) {

        this.mIncludePlayer = includePlayer;
        this.mFirebaseListPlayers = firebaseListPlayers;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        if (action == DragEvent.ACTION_DROP) {
            int index = Integer.parseInt((String) event.getClipData().getItemAt(0).getText());
            Log.d("drag", "drag " + event.getClipData().getItemAt(0).getText());
            mIncludePlayer.setPlayerId(mFirebaseListPlayers.getKeyList().get(index));
            Log.d("drag", "player id = " + String.valueOf(mIncludePlayer.getPlayerId()));

            mIncludePlayer.nick.setText(mFirebaseListPlayers.getDataList().get(index).getNick());
            String score = String.format(Locale.US, "%d:%d",
                    mFirebaseListPlayers.getDataList().get(index).getWins(),
                    mFirebaseListPlayers.getDataList().get(index).getLoses());
            mIncludePlayer.score.setText(score);

            mFirebaseListPlayers.getFirebaseImgSetter().
                    setAvatar(mFirebaseListPlayers.
                            getDataList().
                            get(index).
                            getNick(),
                            mIncludePlayer.avatar);
        }
        return true;
    }
}
