package is.handsome.labs.iotfoosball.presenter;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

class DragListenerForIncludes implements View.OnDragListener {

    private int position;
    private is.handsome.labs.iotfoosball.presenter.InterfacePresenterFromView interfacePresenterFromView;

    public DragListenerForIncludes(InterfacePresenterFromView InterfacePresenterFromView, int position) {
        this.position = position;
        this.interfacePresenterFromView = InterfacePresenterFromView;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        if (action == DragEvent.ACTION_DROP) {
            if (event.getClipData().getDescription().getLabel().equals("draggedTeam")) {
                Log.d("drag", "drag " + event.getClipData().getItemAt(0).getText());
                int teamIndex = Integer.parseInt((String) event.getClipData().getItemAt(0).getText());
                interfacePresenterFromView.notifyTeamDraged(position, teamIndex);
            } else {
                Log.d("drag", "drag " + event.getClipData().getItemAt(0).getText());
                int index = Integer.parseInt((String) event.getClipData().getItemAt(0).getText());
                int positionFrom =
                        event.getClipData().getItemCount() > 1 ?
                                Integer.parseInt((String) event.getClipData().getItemAt(1).getText())
                                : -1;
                interfacePresenterFromView.notifyPlayerDraged(position, index, positionFrom);
            }
        }
        return true;
    }
}
