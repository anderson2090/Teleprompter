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

public class TextColorRecyclerAdapter extends RecyclerView.Adapter<TextColorRecyclerAdapter.ViewHolder> {


    //Todo Create Themes
    public String[] textColors = {

            "#000000",
            "#ffc0cb",
            "#ffffff",
            "#008080",
            "#ffe4e1",
            "#ff0000",
            "#ffd700",
            "#00ffff",
            "#40e0d0",
            "#ff7373",
            "#e6e6fa",
            "#d3ffce",
            "#0000ff",
            "#ffa500",
            "#f0f8ff",
            "#b0e0e6",
            "#7fffd4",
            "#c6e2ff",
            "#faebd7",
            "#800080",
            "#eeeeee",
            "#cccccc",
            "#fa8072",
            "#ffb6c1",
            "#800000",
            "#00ff00",
            "#333333",
            "#003366",
            "#ffff00",
            "#20b2aa",
            "#c0c0c0",
            "#ffc3a0",
            "#f08080",
            "#fff68f",
            "#f6546a",
            "#468499",
            "#66cdaa",
            "#ff6666",
            "#666666",
            "#c39797",
            "#00ced1",
            "#ffdab9",
            "#ff00ff",
            "#660066",
            "#008000",
            "#088da5",
            "#f5f5f5",
            "#c0d6e4",
            "#8b0000",
            "#0e2f44",
            "#ff7f50",
            "#afeeee",
            "#808080",
            "#990000",
            "#b4eeb4",
            "#dddddd",
            "#daa520",
            "#ffff66",
            "#cbbeb5",
            "#00ff7f",
            "#f5f5dc",
            "#8a2be2",
            "#81d8d0",
            "#ff4040",
            "#b6fcd5",
            "#66cccc",
            "#3399ff",
            "#a0db8e",
            "#cc0000",
            "#794044",
            "#ccff00",
            "#000080",
            "#3b5998",
            "#999999",
            "#191970",
            "#0099cc",
            "#fef65b",
            "#ff4444",
            "#31698a",
            "#6897bb",
            "#ff1493",
            "#f7f7f7",
            "#191919",
            "#6dc066"


    };


    @NonNull
    @Override
    public TextColorRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_color_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TextColorRecyclerAdapter.ViewHolder holder, int position) {


        holder.colorLayout.setBackgroundColor(Color.parseColor(textColors[position]));
    }

    @Override
    public int getItemCount() {
        return textColors.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout colorLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            colorLayout = itemView.findViewById(R.id.text_color_layout);

        }
    }
}
