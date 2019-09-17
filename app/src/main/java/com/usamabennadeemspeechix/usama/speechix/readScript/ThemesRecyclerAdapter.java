package com.usamabennadeemspeechix.usama.speechix.readScript;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usamabennadeemspeechix.usama.speechix.R;

import java.util.ArrayList;
import java.util.List;

public class ThemesRecyclerAdapter extends RecyclerView.Adapter<ThemesRecyclerAdapter.ViewHolder> {


    //Todo Create Themes
    public Theme[] themes = {
            new Theme("#291B2c", "#CCA969"),
            new Theme("#1A2C56", "#D1A683"),
            new Theme("#3B1313", "#A66D3F"),
            new Theme("#2A2927", "#FCCF0D"),
            new Theme("#721c47", "#D1A377"),
            new Theme("#2E3268", "#989479"),
            new Theme("#7F1518", "#B69D70"),
            new Theme("#060709", "#DEA527"),
            new Theme("#22422D", "#28B14A"),
            new Theme("#0D8580", "#EF7196")


    };


    @NonNull
    @Override
    public ThemesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThemesRecyclerAdapter.ViewHolder holder, int position) {

        String textColor = themes[position].getTextColor();
        String backgroundColor = themes[position].getBackgroundColor();

        holder.themeCardText.setTextColor(Color.parseColor(textColor));
        holder.colorLayout.setBackgroundColor(Color.parseColor(backgroundColor));
    }

    @Override
    public int getItemCount() {
        return themes.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout colorLayout;
        TextView themeCardText;

        public ViewHolder(View itemView) {
            super(itemView);
            colorLayout = itemView.findViewById(R.id.theme_card_layout);
            themeCardText = itemView.findViewById(R.id.theme_card_text);
        }
    }
}
