package is.handsome.labs.iotfoosball;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class DragListenerForIncludes implements View.OnDragListener {

    private int mPosition;
    private CurentGame mCurentGame;

    public DragListenerForIncludes(int position, CurentGame curentGame) {
        this.mPosition = position;
        this.mCurentGame = curentGame;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        if (action == DragEvent.ACTION_DROP) {
            Log.d("drag", "drag " + event.getClipData().getItemAt(0).getText());
            int index = Integer.parseInt((String) event.getClipData().getItemAt(0).getText());
            mCurentGame.notifyDraged(mPosition, index);
        }
        return true;
    }
}
