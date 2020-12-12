package com.synergy.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.synergy.APIClient;
import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.Workspace.CardAdapter;
import com.synergy.Workspace.CardDetails;
import com.synergy.Workspace.WorkspaceActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends MyBaseActivity {

    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_messages, null, false);
        drawer.addView(viewLayout, 0);
        toolbar.setTitle("Messages");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_messages);

        ArrayList<CardDetails> messagesList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(MessagesActivity.this);
        recyclerView.setHasFixedSize(true);

        /*Call<MessageResponse> getMessageList = APIClient.getUserServices().getMessageList(token, username);
        getMessageList.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                MessageResponse messageResponse = response.body();

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });*/


        messagesList.add(new CardDetails("messageTitle", "messageBody"));
        messagesList.add(new CardDetails("messageTitle1", "messageBody1"));
        mAdapter = new MessageAdapter(messagesList, MessagesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}