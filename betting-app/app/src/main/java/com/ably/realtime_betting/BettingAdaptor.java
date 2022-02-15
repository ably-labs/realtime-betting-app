package com.ably.realtime_betting;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class BettingAdaptor extends RecyclerView.Adapter<BettingAdaptor.Viewholder> {

    private Context context;
    private ArrayList<BettingTile> bettingTileArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener clickListener;

    // Constructor
    public BettingAdaptor(Context context, ArrayList<BettingTile> dataArray) {
        this.context = context;
        this.bettingTileArrayList = dataArray;
        this.context = context;
    }


    @NonNull
    @Override
    public BettingAdaptor.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.betting_tile, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BettingAdaptor.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        BettingTile model = bettingTileArrayList.get(position);
        holder.betNameTV.setText(model.getName());
        holder.betOddsTV.setText("" + model.getOdds());
        try {
            //in a more complete system this would pull the image for the tile from the URL provided
           // holder.betImageIV.setImageBitmap(BitmapFactory.decodeStream((InputStream)new URL(model.getImageURL()).getContent()));
            holder.betImageIV.setImageResource(R.drawable.car);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return bettingTileArrayList == null ? 0 : bettingTileArrayList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView betImageIV;
        private final TextView betNameTV;
        private final TextView betOddsTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            betImageIV = itemView.findViewById(R.id.idIVBetImage);
            betNameTV = itemView.findViewById(R.id.idTVBetName);
            betOddsTV = itemView.findViewById(R.id.idTVOdds);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}