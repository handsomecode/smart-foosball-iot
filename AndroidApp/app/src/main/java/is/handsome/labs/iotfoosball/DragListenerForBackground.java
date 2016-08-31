package is.handsome.labs.iotfoosball;

import android.view.DragEvent;
import android.view.View;

public class DragListenerForBackground implements View.OnDragListener  {

    private Presenter presenter;

    public DragListenerForBackground(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        int action = dragEvent.getAction();
        if (action == DragEvent.ACTION_DROP) {
            if (dragEvent.getClipData().getItemCount() > 1) {
                int positionFrom = Integer.parseInt(
                        (String) dragEvent.getClipData().getItemAt(1).getText());
                presenter.notifyDragToBackground(positionFrom);
            }
        }
        return true;
    }
}
