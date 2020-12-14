package com.synergy.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.Workspace.CardDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends MyBaseActivity {

    private static final String TAG = "";
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_chat, null, false);
        drawer.addView(viewLayout, 0);
        recyclerView = findViewById(R.id.rv_chat);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        Log.d(TAG, "onCreate: " + type);

        toolbar.setTitle("Technician");
        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setHasFixedSize(true);

        ArrayList<ChatCardDetails> chatList = new ArrayList<>();

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.show();

        String dateToday = new MessagesActivity().dateToday;

        Call<List<MessageResponse>> callChat = APIClient.getUserServices().getChatList(token, username, type);
        callChat.enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.code() == 200) {
                    List<MessageResponse> messageResponseList = response.body();
                    for (int i = 0; i < messageResponseList.size(); i++) {
                        String title = messageResponseList.get(i).getTitle();
                        String body = messageResponseList.get(i).getText();

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

                        chatList.add(new ChatCardDetails(title, body, time));
                    }
                    chatAdapter = new ChatAdapter(chatList);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(ChatActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
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