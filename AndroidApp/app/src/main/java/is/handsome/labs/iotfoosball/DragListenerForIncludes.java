package is.handsome.labs.iotfoosball;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class DragListenerForIncludes implements View.OnDragListener {

    private int position;
    private InterfacePresenterFromView InterfacePresenterFromView;

    public DragListenerForIncludes(InterfacePresenterFromView InterfacePresenterFromView, int position) {
        this.position = position;
        this.InterfacePresenterFromView = InterfacePresenterFromView;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        if (action == DragEvent.ACTION_DROP) {
            Log.d("drag", "drag " + event.getClipData().getItemAt(0).getText());
            int index = Integer.parseInt((String) event.getClipData().getItemAt(0).getText());
            int positionFrom =
                    event.getClipData().getItemCount() > 1 ?
                            Integer.parseInt((String) event.getClipData().getItemAt(1).getText())
                            : -1;
            InterfacePresenterFromView.notifyDraged(position, index, positionFrom);
        }
        return true;
    }
}
