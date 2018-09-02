package com.vidya.spectra.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vidya.spectra.R;
import com.vidya.spectra.model.Event;

import java.util.List;

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private Context mContext;
    private List<Event> eventList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_main);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_main);
        }
    }


    public MainAdapter (Context mContext, List<Event> eventList) {
        this.mContext = mContext;
        this.eventList = eventList;
    }

    @Override
    public MainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_card, parent, false);

        return new MainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MainAdapter.MyViewHolder holder, int position) {
        Event album = eventList.get(position);
        holder.title.setText(album.getName());
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
