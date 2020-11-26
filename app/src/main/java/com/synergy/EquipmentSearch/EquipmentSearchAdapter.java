package com.synergy.EquipmentSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.synergy.R;
import com.synergy.Workspace.CardAdapter;
import com.synergy.Workspace.CardDetails;

import java.util.ArrayList;

public class EquipmentSearchAdapter extends RecyclerView.Adapter<EquipmentSearchAdapter.MyViewHolder> {

    public ArrayList<EquipmentSearchCard> equipmentSearchCards;
    Context context;

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
        holder.statusTV.setText(String.valueOf(currentPosition.getStatus()));
        holder.buildingTV.setText(String.valueOf(currentPosition.getBuildingName()));
        holder.locationTV.setText(String.valueOf(currentPosition.getLocationName()));
        holder.scheduleTV.setText(String.valueOf(currentPosition.getScheduleDate()));
        holder.taskNumberTV.setText(currentPosition.getTask_number());
        String workspace = currentPosition.getWorkspace();
        String afterImage = currentPosition.getAfterImage();
        String beforeImage = currentPosition.getBeforeImage();
        String source = currentPosition.getSource();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PmTaskActivity.class);
                intent.putExtra("taskNumber", currentPosition.getTask_number());
                intent.putExtra("taskId", currentPosition.getTaskId());
                intent.putExtra("workspace", workspace);
                intent.putExtra("afterImage", afterImage);
                intent.putExtra("source", source);
                intent.putExtra("beforeImage", beforeImage);
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentSearchCards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView taskNumberTV, taskIdTV, statusTV, buildingTV, locationTV, scheduleTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNumberTV = itemView.findViewById(R.id.taskNumberRV);
            taskIdTV = itemView.findViewById(R.id.taskIdRV);
            statusTV = itemView.findViewById(R.id.statusRV);
            buildingTV = itemView.findViewById(R.id.buildingRV);
            locationTV = itemView.findViewById(R.id.locationRV);
            scheduleTV = itemView.findViewById(R.id.schduleDateRV);
        }
    }
}
