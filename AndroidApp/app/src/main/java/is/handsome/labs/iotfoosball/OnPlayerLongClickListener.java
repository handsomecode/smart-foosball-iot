package is.handsome.labs.iotfoosball;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.View;

import timber.log.Timber;

public class OnPlayerLongClickListener implements View.OnLongClickListener {
    private Integer playerIndex;
    private View draggedView;
    private PlayerRecyclerAdapter.ViewHolder viewHolder;
    private Integer includePosition;

    public OnPlayerLongClickListener(Integer index, Integer includePosition, View draggedView) {
        this.playerIndex = index;
        this.draggedView = draggedView;
        this.includePosition = includePosition;
    }

    public OnPlayerLongClickListener(PlayerRecyclerAdapter.ViewHolder viewHolder, View draggedView) {
        this.viewHolder = viewHolder;
        this.draggedView = draggedView;
    }

    @Override
    public boolean onLongClick(View view) {
        int index;
        Timber.d("Click on playerIndex = " + String.valueOf(playerIndex));
        if (viewHolder == null) {
            if (playerIndex == -1) {
                return false;
            }
            index = playerIndex;
        } else {
            index = viewHolder.getAdapterPosition();
        }
        Log.d("drag", "click on "
                + String.valueOf(index));
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(draggedView);
        ClipData.Item item = new ClipData.Item(String.valueOf(index));
        ClipData dragedPlayer = new ClipData("dragedPlayer",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                item);
        if (viewHolder == null) {
            ClipData.Item includeItem = new ClipData.Item(String.valueOf(includePosition));
            dragedPlayer.addItem(includeItem);
        }
        draggedView.startDrag(dragedPlayer, shadow, null, 0);
        Log.d("drag", "drag started on "
                + String.valueOf(index));
        return true;
    }
}
