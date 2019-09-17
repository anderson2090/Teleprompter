package com.usamabennadeemspeechix.usama.speechix.scriptsMenu;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usamabennadeemspeechix.usama.speechix.R;

import java.util.ArrayList;
import java.util.List;

public class ScriptMenuRecyclerAdapter extends RecyclerView.Adapter<ScriptMenuRecyclerAdapter.ViewHolder> {

    // private String[] scripts = {"One", "Two", "Three"};
    private List<String> scripts = new ArrayList<>();

    public ScriptMenuRecyclerAdapter(List<String> scriptNames) {

        scripts = scriptNames;

        /*for (int i = 0; i <= 200; i++) {
            scripts.add(i + "");
        }*/
    }

    @NonNull
    @Override
    public ScriptMenuRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.script_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScriptMenuRecyclerAdapter.ViewHolder holder, int position) {
        holder.scriptName.setText(scripts.get(position));
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    public void updateList(List<String> list) {

        scripts = new ArrayList<>();
        scripts.addAll(list);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView scriptName;

        public ViewHolder(View itemView) {
            super(itemView);
            scriptName = itemView.findViewById(R.id.script_card_name);
        }
    }
}
