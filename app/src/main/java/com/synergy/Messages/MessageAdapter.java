package com.synergy.Messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.synergy.R;
import com.synergy.Workspace.CardAdapter;
import com.synergy.Workspace.CardDetails;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<CardDetails> messageList = new ArrayList<>();
    private Context context;

    public MessageAdapter(ArrayList<CardDetails> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        final CardDetails currentDetails = messageList.get(position);

        holder.mBody.setText(currentDetails.getWorkspaceId());
        holder.mTitle.setText(currentDetails.getBuildingDescription());
        holder.mTime.setText("12:12");
        holder.mImage.setImageResource(R.drawable.user_icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mBody;
        public ImageView mImage;
        public TextView mTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.message_title);
            mBody = itemView.findViewById(R.id.message_body);
            mImage = itemView.findViewById(R.id.imageView_message);
            mTime = itemView.findViewById(R.id.message_time);
        }
    }
}
