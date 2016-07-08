package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import java.util.Locale;

public class DragListenerForIncludes implements View.OnDragListener {
    private int index;
    private IncludePlayer includePlayer;
    private FirebaseListPlayers firebaseListPlayers;
    private Context context;

    public DragListenerForIncludes(int index, IncludePlayer includePlayer,
                                   FirebaseListPlayers firebaseListPlayers,
                                   Context context) {
        this.index = index;
        this.includePlayer = includePlayer;
        this.firebaseListPlayers = firebaseListPlayers;
        this.context = context;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        if (action == DragEvent.ACTION_DROP) {
            int index = Integer.parseInt((String) event.getClipData().getItemAt(0).getText());
            Log.d("drag", "drag " + event.getClipData().getItemAt(0).getText());
            includePlayer.setPlayerid(firebaseListPlayers.getKeyList().get(index));
            Log.d("drag", "player id = " + String.valueOf(includePlayer.getPlayerid()));

            includePlayer.nick.setText(firebaseListPlayers.getDataList().get(index).getNick());
            String score = String.format(Locale.US, "%d:%d",
                    firebaseListPlayers.getDataList().get(index).getWin(),
                    firebaseListPlayers.getDataList().get(index).getLose());
            includePlayer.score.setText(score);

            firebaseListPlayers.getFirebaseImgSetter().
                    setAvatar(firebaseListPlayers.getDataList().get(index).getNick(),
                            includePlayer.avatar);
        }
        return true;
    }
}