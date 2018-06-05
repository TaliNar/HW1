package com.example.tali.hw1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tali.hw1.R;
import com.example.tali.hw1.db.Player;

import java.util.List;


public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewScore;
        private TextView textViewAddress;

        private PlayerViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewScore = itemView.findViewById(R.id.textViewScore);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
        }
    }

    private final LayoutInflater mInflater;
    private List<Player> mPlayers; // Cached copy of players

    public PlayerListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        if (mPlayers != null) {
            Player current = mPlayers.get(position);
            holder.textViewName.setText(current.getName());
            holder.textViewScore.setText(current.getScore().toString());
            if(current.getAddress() != null) {
                holder.textViewAddress.setText(current.getAddress().getAddressLine(0));
            }else
                Log.w("PlayerListAdapter", "address is null");
        }
    }

    public void setPlayers (List < Player > players) {
            mPlayers = players;
            notifyDataSetChanged();
    }


// getItemCount() is called many times, and when it is first called,
        // mPlayers has not been updated (means initially, it's null, and we can't return null).
        @Override
        public int getItemCount () {
            if (mPlayers != null)
                return mPlayers.size();
            else return 0;
        }
}
