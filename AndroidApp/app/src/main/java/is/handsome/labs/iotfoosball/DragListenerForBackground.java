package is.handsome.labs.iotfoosball;

import android.view.DragEvent;
import android.view.View;

public class DragListenerForBackground implements View.OnDragListener  {

    private Interactor interactor;

    public DragListenerForBackground(Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        int action = dragEvent.getAction();
        if (action == DragEvent.ACTION_DROP) {
            if (dragEvent.getClipData().getItemCount() > 1) {
                int positionFrom = Integer.parseInt(
                        (String) dragEvent.getClipData().getItemAt(1).getText());
                interactor.notifyDragToBackground(positionFrom);
            }
        }
        return true;
    }
}
