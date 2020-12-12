package com.synergy.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.Workspace.CardDetails;

import java.util.ArrayList;

public class ChatActivity extends MyBaseActivity {

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

        toolbar.setTitle("Technician");
        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setHasFixedSize(true);
        CardDetails cardDetails = new CardDetails("Lorem ipsum, or lipsum as it is sometimes known, " +
                "is dummy text used in laying out print, graphic or web designs. The passage is attributed to an " +
                "unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus " +
                "Bonorum et Malorum for use in a type specimen book.", "Tech1");
        CardDetails cardDetails1 = new CardDetails("Lorem ipsum, or lipsum as it is sometimes known, " +
                "is dummy text used in laying out print, graphic or web designs. The passage is attributed to an " +
                "unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus " +
                "Bonorum et Malorum for use in a type specimen book.Lorem ipsum, or lipsum as it is sometimes known, \" +\n" +
                "is dummy text used in laying out print, graphic or web designs. The passage is attributed to an \" +\n" +
                " unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus \" +\n" +
                " Bonorum et Malorum for use in a type sp", "Tech1");
        CardDetails cardDetails2 = new CardDetails("Lorem ipsum, or lipsum as it is sometimes known, " +
                "is dummy text used in laying out print, graphic or web designs. The passage is attributed to an " +
                "unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus " +
                "Bonorum et Malorum for use in a type specimen book.Lorem ipsum, or lipsum as it is sometimes known, \" +\n" +
                "is dummy text used in laying out print, graphic or web designs. The passage is attributed to an \" +\n" +
                " unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus \" +\n" +
                " Bonorum et Malorum for use in a type sp", "Tech1");
        ArrayList<CardDetails> chatList = new ArrayList<>();
        chatList.add(cardDetails);
        chatList.add(cardDetails1);
        chatList.add(cardDetails2);
        chatList.add(new CardDetails("hello", "Name"));
        chatAdapter = new ChatAdapter(chatList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}