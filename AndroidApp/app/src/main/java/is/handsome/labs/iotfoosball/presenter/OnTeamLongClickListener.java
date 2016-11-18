package is.handsome.labs.iotfoosball.presenter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.View;

import timber.log.Timber;

public class OnTeamLongClickListener implements View.OnLongClickListener{
    private TeamsRecyclerAdapter.ViewHolder viewHolder;
    private View draggedView;

    public OnTeamLongClickListener(TeamsRecyclerAdapter.ViewHolder viewHolder, View draggedView) {
        this.viewHolder = viewHolder;
        this.draggedView = draggedView;
    }

    @Override
    public boolean onLongClick(View view) {
        int teamIndex = viewHolder.getAdapterPosition();
        Timber.d("Click on teamIndex = " + String.valueOf(teamIndex));
        Log.d("drag", "click on "
                + String.valueOf(teamIndex));
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(draggedView);
        ClipData.Item item = new ClipData.Item(String.valueOf(teamIndex));
        ClipData draggedTeam = new ClipData("draggedTeam",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                item);
        draggedView.startDrag(draggedTeam, shadow, null, 0);
        Log.d("drag", "drag started on "
                + String.valueOf(teamIndex));
        return true;
    }
}
