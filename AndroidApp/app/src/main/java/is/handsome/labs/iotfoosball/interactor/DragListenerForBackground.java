package is.handsome.labs.iotfoosball.interactor;

import android.view.DragEvent;
import android.view.View;

import is.handsome.labs.iotfoosball.presenter.InterfacePresenterFromView;

class DragListenerForBackground implements View.OnDragListener  {

    private InterfacePresenterFromView interfacePresenterFromView;

    public DragListenerForBackground(InterfacePresenterFromView interfacePresenterFromView) {
        this.interfacePresenterFromView = interfacePresenterFromView;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        int action = dragEvent.getAction();
        if (action == DragEvent.ACTION_DROP) {
            if (dragEvent.getClipData().getItemCount() > 1) {
                int positionFrom = Integer.parseInt(
                        (String) dragEvent.getClipData().getItemAt(1).getText());
                interfacePresenterFromView.notifyDragToBackground(positionFrom);
            }
        }
        return true;
    }
}
