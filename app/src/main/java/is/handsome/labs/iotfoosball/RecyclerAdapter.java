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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private FirebaseListPlayers mFirebaseListPlayers;
    private FirebaseImgSetter mFirebaseImgSetter;

    public RecyclerAdapter() {

    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
        holder.nick.setText(mFirebaseListPlayers.getDataList().get(position).getNick());
        String score = String.format(Locale.US, "%d:%d",
                mFirebaseListPlayers.getDataList().get(position).getWins(),
                mFirebaseListPlayers.getDataList().get(position).getLoses());
        holder.score.setText(score);
        String link = "avatars/"
                + mFirebaseListPlayers.getDataList().get(position).getNick().toLowerCase()
                + ".jpg";
        mFirebaseImgSetter.setImg(link, holder.avatar);
    }

    @Override
    public int getItemCount() {
        if (mFirebaseListPlayers == null)
            return 0;
        else
            return mFirebaseListPlayers.getDataList().size();
    }

    public void setFirebase(FirebaseListPlayers firebaseListPlayers,
            FirebaseImgSetter firebaseImgSetter){
        this.mFirebaseListPlayers = firebaseListPlayers;
        this.mFirebaseImgSetter = firebaseImgSetter;
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
