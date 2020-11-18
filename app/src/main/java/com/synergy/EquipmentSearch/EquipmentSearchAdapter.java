package com.synergy.EquipmentSearch;

import android.content.Intent;
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

public class EquipmentSearchAdapter extends RecyclerView.Adapter<EquipmentSearchAdapter.MyViewHolder> {

    public ArrayList<EquipmentSearchCard> equipmentSearchCards;

    public EquipmentSearchAdapter(ArrayList<EquipmentSearchCard> equipmentSearchCards){
        this.equipmentSearchCards = equipmentSearchCards;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.equip_search_adapter, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        EquipmentSearchCard currentPosition = equipmentSearchCards.get(position);
        holder.taskIdTV.setText(String.valueOf(currentPosition.getTaskId()));
        holder.taskNumberTV.setText(currentPosition.getTask_number());
        String workspace = currentPosition.getWorkspace();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PmTaskActivity.class);
                intent.putExtra("taskNumber", currentPosition.getTask_number());
                intent.putExtra("taskId", currentPosition.getTaskId());
                intent.putExtra("workspace", workspace);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentSearchCards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView taskNumberTV, taskIdTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNumberTV = itemView.findViewById(R.id.taskNumberRV);
            taskIdTV = itemView.findViewById(R.id.taskIdRV);
        }
    }
}
