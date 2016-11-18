package is.handsome.labs.iotfoosball.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import is.handsome.labs.iotfoosball.R;
import is.handsome.labs.iotfoosball.models.TeamViewInfo;

class TeamsRecyclerAdapter extends RecyclerView.Adapter<TeamsRecyclerAdapter.ViewHolder>{

    private Presenter presenter;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            //player.setOnLongClickListener(new OnPlayerLongClickListener(PlayerRecyclerAdapter.ViewHolder.this, v));
            team.setOnLongClickListener(new OnTeamLongClickListener(TeamsRecyclerAdapter.ViewHolder.this, v));
        }

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.avatar1)
        ImageView avatar1;
        @BindView(R.id.avatar2)
        ImageView avatar2;
        @BindView(R.id.team)
        View team;
    }

    public TeamsRecyclerAdapter (Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void onBindViewHolder(TeamsRecyclerAdapter.ViewHolder holder, int position) {
        holder.avatar1.setImageBitmap(null);
        holder.avatar2.setImageBitmap(null);
        TeamViewInfo teamViewInfo = presenter.getTeamViewInfo(position);
        holder.name.setText(teamViewInfo.getName());
        if (!teamViewInfo.getAvatar1Url().equals("")) {
            presenter.setAvatar(holder.avatar1, teamViewInfo.getAvatar1Url());
        }
        if (!teamViewInfo.getAvatar2Url().equals("")) {
            presenter.setAvatar(holder.avatar2, teamViewInfo.getAvatar2Url());
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getTeamCount();
    }

    @Override
    public TeamsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.team_rv_item, parent, false);
        return new ViewHolder(v);
    }
}
