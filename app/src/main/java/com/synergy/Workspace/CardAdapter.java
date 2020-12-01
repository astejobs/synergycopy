package com.synergy.Workspace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.synergy.R;
import com.synergy.Dashboard.Dashboard;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    public CardAdapter(ArrayList<CardDetails> mCardDetails) {
        this.mCardDetails = mCardDetails;
    }

    private String variable;
    private ArrayList<CardDetails> mCardDetails;
    private static final String TAG = "CardAdapter";
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_card, parent, false);
        ViewHolder farmsViewHolder = new ViewHolder(v);
        return farmsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CardDetails currentItem = mCardDetails.get(position);

        holder.tvWorkID.setText(currentItem.getWorkspaceId());
        holder.tvDesc.setText(currentItem.getBuildingDescription().toString().toUpperCase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView workspaceTextView= holder.itemView.findViewById(R.id.worskspace_tv);
                String id=workspaceTextView.getText().toString();
                Intent intent = new Intent(v.getContext(), Dashboard.class);
                intent.putExtra("workspaceId", id);
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        return mCardDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDesc;
        public TextView tvWorkID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.worskspace_tv);
            tvWorkID = itemView.findViewById(R.id.desc_tv);
        }
    }

}
