package is.handsome.labs.iotfoosball;

import android.content.ClipData;
import android.content.ClipDescription;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Opengamer on 04.07.2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private FirebaseListPlayers firebaseListPlayers;
    private FirebaseImgStorage firebaseImgStorage;

    public RecyclerAdapter() {}

    public void setFirebase(FirebaseListPlayers firebaseListPlayers, FirebaseImgStorage firebaseImgStorage){
        this.firebaseListPlayers = firebaseListPlayers;
        this.firebaseImgStorage = firebaseImgStorage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nick)
        TextView nick;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.player)
        View player;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
        holder.nick.setText(firebaseListPlayers.getDataList().get(position).getNick());
        String score = String.format(Locale.US, "%d:%d",
                firebaseListPlayers.getDataList().get(position).getWin(),
                firebaseListPlayers.getDataList().get(position).getLose());
        holder.score.setText(score);
        holder.player.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("drag", "click on " + String.valueOf(position));
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
                ClipData.Item item = new ClipData.Item(String.valueOf(position));
                ClipData playerid = new ClipData("playerid",
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                v.startDrag(playerid, shadow, null, 0);
                return true;
            }
        });
        String link = "avatars/" + firebaseListPlayers.getDataList().get(position).getNick().toLowerCase() + ".jpg";
        firebaseImgStorage.setImg(link, holder.avatar);
    }

    @Override
    public int getItemCount() {
        if (firebaseListPlayers == null)
            return 0;
        else
            return firebaseListPlayers.getDataList().size();
    }
}
