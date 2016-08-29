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

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder>{

    private List<PlayerWithScore> mPlayerWithScores;
    private FirebaseImgSetter mFirebaseImgSetter;

    public PlayerRecyclerAdapter(List<PlayerWithScore> playerWithScores,
            FirebaseImgSetter mirebaseImgSetter) {
        this.mPlayerWithScores = playerWithScores;
        this.mFirebaseImgSetter = mirebaseImgSetter;
    }

    @Override
    public PlayerRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_rv_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlayerRecyclerAdapter.ViewHolder holder, final int position) {
        PlayerWithScore player = mPlayerWithScores.get(position);
        holder.nick.setText(player.getPlayer().getNick());
        String score = String.format(Locale.US, "%d:%d",
                player.getWins(),
                player.getLosses());
        holder.score.setText(score);
        String link = "avatars/"
                + player.getPlayer().getNick().toLowerCase()
                + ".jpg";
        mFirebaseImgSetter.setImg(link, holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mPlayerWithScores.size();
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
            player.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("drag", "click on "
                            + String.valueOf(ViewHolder.this.getAdapterPosition()));
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
                    ClipData.Item item =
                            new ClipData.Item(String.valueOf(ViewHolder.this.getAdapterPosition()));
                    ClipData playerId =
                            new ClipData("playerid",
                                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    v.startDrag(playerId, shadow, null, 0);
                    Log.d("drag", "drag started on "
                            + String.valueOf(ViewHolder.this.getAdapterPosition()));
                    return true;
                }
            });
        }
    }

}
