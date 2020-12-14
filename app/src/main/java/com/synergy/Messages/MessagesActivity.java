package com.synergy.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.Workspace.CardAdapter;
import com.synergy.Workspace.CardDetails;
import com.synergy.Workspace.WorkspaceActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends MyBaseActivity {

    private static final String TAG = "";
    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String type;
    public String dateToday = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_messages, null, false);
        drawer.addView(viewLayout, 0);
        toolbar.setTitle("Messages");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_messages);

        ArrayList<MessageCardDetails> messagesList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(MessagesActivity.this);
        recyclerView.setHasFixedSize(true);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<List<MessageResponse>> getMessageList = APIClient.getUserServices().getMessageList(token, username);
        getMessageList.enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.code() == 200) {
                    List<MessageResponse> messageResponseList = response.body();
                    for (int i = 0; i < messageResponseList.size(); i++) {

                        long dateTimeLong = messageResponseList.get(i).createdDate;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM");

                        String date = dateFormat.format(dateTimeLong);
                        String time;
                        Date today = new Date();
                        Date incomingToday = new Date();
                        try {
                            today = dateFormat.parse(dateToday);
                            incomingToday = dateFormat.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (incomingToday.before(today)) {
                            time = dateFormat.format(dateTimeLong);
                        } else time = timeFormat.format(dateTimeLong);

                        type = messageResponseList.get(i).getType();
                        messagesList.add(new MessageCardDetails(messageResponseList.get(i).title, messageResponseList.get(i).getText(), time, type));
                        mAdapter = new MessageAdapter(messagesList, MessagesActivity.this);
                    }
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Toast.makeText(MessagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}