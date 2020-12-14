package com.synergy.Search;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.synergy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;

public class SearchResponseAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<SearchResponse> contacts; //data source of the list adapter
    private static final String TAG = "SearchResponseAdapter";
    private String workspaceId;
    private double latitude,longitude;


    public SearchResponseAdapter(Context context, ArrayList<SearchResponse> items, String workspaceId,double latitude,double longitude) {
        this.context = context;
        this.workspaceId = workspaceId;
        this.contacts = items;
        this.longitude=longitude;
        this.latitude=latitude;
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
        String date = null;
        if (currentItem.getActivationTime() != null) {


            String pattern = "dd-MM-yyyy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.format(new Date());
          //  System.out.println(date);


          /*  activationDate=currentItem.getActivationTime().getDayOfMonth()+"-"+currentItem.
                    getActivationTime().getMonthValue()+"-"+currentItem.getActivationTime().getYear();

            activationTime=currentItem.getActivationTime().getHour()+":"+currentItem.getActivationTime().getMinute();*/
        }


        frIdTextView.setText(currentItem.getFrId().trim());
        re.setText(date);
        //    re.setText( activationDate +" "+ activationTime);
        //   cr.setText(" Activation Time:" + activationTime);
        st.setText(currentItem.getStatus());
        bu.setText(currentItem.getBuilding());
        lo.setText(currentItem.getLocation());
        workspaceId = currentItem.getWorkspaceId();
        longitude=currentItem.getLongitude();
        latitude=currentItem.getLatitude();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String frid = ((TextView) view.findViewById(R.id.textView_frid)).getText().toString();//.substring(6);
                Intent intent = new Intent(context.getApplicationContext(), EditFaultOnSearchActivity.class);
                intent.putExtra("frId", frid);
                intent.putExtra("longitude",currentItem.getLongitude());
                intent.putExtra("latitude",currentItem.getLatitude());
                intent.putExtra("workspaceId", workspaceId);

                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
