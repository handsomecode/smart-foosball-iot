package is.handsome.labs.iotfoosball;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.View;

import timber.log.Timber;

public class OnPlayerLongClickListener implements View.OnLongClickListener {
    private Integer mPlayerIndex;
    private View mDraggedView;
    private PlayerRecyclerAdapter.ViewHolder mViewHolder;
    private Integer mIncludePOsition;

    public OnPlayerLongClickListener(Integer index, Integer includePOsition, View draggedView) {
        this.mPlayerIndex = index;
        this.mDraggedView = draggedView;
        this.mIncludePOsition = includePOsition;
    }

    public OnPlayerLongClickListener(PlayerRecyclerAdapter.ViewHolder viewHolder, View draggedView) {
        this.mViewHolder = viewHolder;
        this.mDraggedView = draggedView;
    }

    @Override
    public boolean onLongClick(View view) {
        int index;
        Timber.d("Click on mPlayerIndex = " + String.valueOf(mPlayerIndex));
        if (mViewHolder == null) {
            if (mPlayerIndex == -1) {
                return false;
            }
            index = mPlayerIndex;
        } else {
            index = mViewHolder.getAdapterPosition();
        }
        Log.d("drag", "click on "
                + String.valueOf(index));
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(mDraggedView);
        ClipData.Item item = new ClipData.Item(String.valueOf(index));
        ClipData dragedPlayer = new ClipData("dragedPlayer",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                item);
        if (mViewHolder == null) {
            ClipData.Item includeItem = new ClipData.Item(String.valueOf(mIncludePOsition));
            dragedPlayer.addItem(includeItem);
        }
        mDraggedView.startDrag(dragedPlayer, shadow, null, 0);
        Log.d("drag", "drag started on "
                + String.valueOf(index));
        return true;
    }
}
