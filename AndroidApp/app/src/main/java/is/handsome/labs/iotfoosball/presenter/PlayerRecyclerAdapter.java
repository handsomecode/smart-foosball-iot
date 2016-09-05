package is.handsome.labs.iotfoosball.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import is.handsome.labs.iotfoosball.models.PlayerViewInfo;
import is.handsome.labs.iotfoosball.R;

class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            player.setOnLongClickListener(new OnPlayerLongClickListener(ViewHolder.this, v));
        }

        @BindView(R.id.nick)
        TextView nick;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.player)
        View player;
    }

    private is.handsome.labs.iotfoosball.presenter.InterfacePresenterFromView InterfacePresenterFromView;

    public PlayerRecyclerAdapter(InterfacePresenterFromView InterfacePresenterFromView) {
        this.InterfacePresenterFromView = InterfacePresenterFromView;
    }

    @Override
    public PlayerRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.player_rv_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlayerRecyclerAdapter.ViewHolder holder, int position) {
        PlayerViewInfo playerViewInfo = InterfacePresenterFromView.getPlayerViewInfoByPosition(position);
        holder.nick.setText(playerViewInfo.getNick());
        holder.score.setText(playerViewInfo.getScore());
        InterfacePresenterFromView.getImgSetterService().setImg(playerViewInfo.getAvatar(), holder.avatar);
    }

    @Override
    public int getItemCount() {
        return InterfacePresenterFromView.getPlayerCount();
    }
}
