package com.offlinefirebasedatabase.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.offlinefirebasedatabase.android.models.Profile;
import com.offlinefirebasedatabase.android.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Profile> profileList;

    public ProfileAdapter(Context mCtx, List<Profile> profileList) {
        this.mCtx = mCtx;
        this.profileList = profileList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_profile, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Profile t = profileList.get(position);
        holder.name.setText(t.getName());
        holder.email.setText(t.getEmail());
        holder.number.setText(t.getNumber());


        Glide.with(mCtx).load(t.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView name, email, number;
        ImageView imageView;

        public TasksViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTV);
            email = itemView.findViewById(R.id.emailTV);
            number = itemView.findViewById(R.id.numberTV);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }
}
