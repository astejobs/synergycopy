package com.synergy.Messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.synergy.R;
import com.synergy.Workspace.CardAdapter;
import com.synergy.Workspace.CardDetails;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<CardDetails> chatList = new ArrayList<>();

    public ChatAdapter(ArrayList<CardDetails> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyclerview, parent, false);
        ChatAdapter.ViewHolder farmsViewHolder = new ChatAdapter.ViewHolder(v);
        return farmsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CardDetails currentDetails = chatList.get(position);
        holder.chatTime.setText("09:13");
        holder.chatBody.setText(currentDetails.getBuildingDescription());
        holder.chatTitle.setText(currentDetails.getWorkspaceId());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chatTitle, chatBody, chatTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTitle = itemView.findViewById(R.id.title_chat);
            chatBody = itemView.findViewById(R.id.body_chat);
            chatTime = itemView.findViewById(R.id.time_chat);
        }
    }
}