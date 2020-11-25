package com.synergy.SearchTasks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.synergy.R;
import com.synergy.Search.EditFaultReportActivity;
import com.synergy.Search.SearchResponse;

import java.util.ArrayList;

public class SearchTaskAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<TaskSearchResponse> contacts; //data source of the list adapter
    private static final String TAG = "SearchResponseAdapter";
    private String workspaceId;


    public SearchTaskAdapter(Context context, ArrayList<TaskSearchResponse> items, String workspaceId) {
        this.context = context;
        this.workspaceId = workspaceId;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.search_row, parent, false);
        }
        SearchResponse currentItem = (SearchResponse) getItem(position);

        TextView frIdTextView = (TextView) convertView.findViewById(R.id.textView_frid);
        TextView re = (TextView) convertView.findViewById(R.id.textView_reporteddatete);
        TextView cr = (TextView) convertView.findViewById(R.id.textView_createddate);
        TextView st = (TextView) convertView.findViewById(R.id.textView_status);
        TextView bu = (TextView) convertView.findViewById(R.id.textView_building);
        TextView lo = (TextView) convertView.findViewById(R.id.textView_location);
        TextView to = convertView.findViewById(R.id.tokenGen);
        TextView workspaceSearchTextView = convertView.findViewById(R.id.workspace_search);

        String activationDate = null;
        String activationTime = null;



        frIdTextView.setText(" FrId:" + currentItem.getFrId().trim());
        re.setText(" Activation Date:" + activationDate);
        cr.setText(" Activation Time:" + activationTime);
        st.setText(" Status:" + currentItem.getStatus());
        bu.setText(" Building:" + currentItem.getBuilding());
        lo.setText(" Location:" + currentItem.getLocation());
        workspaceId = currentItem.getWorkspaceId();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String frid = ((TextView) view.findViewById(R.id.textView_frid)).getText().toString().substring(6);
                Intent intent = new Intent(context.getApplicationContext(), EditFaultReportActivity.class);
                intent.putExtra("frId", frid);
                intent.putExtra("workspaceId", workspaceId);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
