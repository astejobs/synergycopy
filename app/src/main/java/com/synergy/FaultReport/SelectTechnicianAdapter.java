package com.synergy.FaultReport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.synergy.R;
import com.synergy.Search.EquipmentSearchResponseforEdit;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SelectTechnicianAdapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<SelectTechnicianResponse> contacts; //data source of the list adapter
    private static final String TAG = "SearchResponseAdapter";

    //public constructor
    public SelectTechnicianAdapter(Context context, ArrayList<SelectTechnicianResponse> items) {
        this.context = context;
        this.contacts = items;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.equipment_search_row, parent, false);
        }
        // get current item to be displayed
        SelectTechnicianResponse currentItem = (SelectTechnicianResponse) getItem(position);
        // get the TextView for item name and item description
        TextView qw_id = (TextView) convertView.findViewById(R.id.textview_eq_id);
        TextView eq_name = (TextView) convertView.findViewById(R.id.textview_eq_name);
        TextView eq_code = (TextView) convertView.findViewById(R.id.textview_eq_eqcode);
        TextView to = convertView.findViewById(R.id.tokenGen);
        TextView workspaceSearchTextView = convertView.findViewById(R.id.workspace_search);

       qw_id.setText(String.valueOf(currentItem.getId()));
        eq_name.setText(currentItem.getName());
        eq_code.setText(currentItem.getUsername());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedcode = ((TextView) view.findViewById(R.id.textview_eq_eqcode)).getText().toString();
                String selectedId = ((TextView) view.findViewById(R.id.textview_eq_id)).getText().toString();

                Intent intent1 = new Intent();
                intent1.putExtra("equipment_code", selectedcode);
                Log.d(TAG, "onClick: equipmemnt code"+selectedcode);
                intent1.putExtra("equipmentId", Integer.parseInt(selectedId));
                ((Activity) context).setResult(RESULT_OK, intent1);
                ((Activity) context).finish();
            }
        });
        return convertView;
    }
}


