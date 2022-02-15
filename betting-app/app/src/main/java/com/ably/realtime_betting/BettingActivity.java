package com.ably.realtime_betting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.ChannelOptions;
import io.ably.lib.types.ErrorInfo;
import io.ably.lib.types.Message;
import io.ably.lib.util.Log;

public class BettingActivity extends AppCompatActivity implements ItemClickListener {

    private AblyRealtime rtConnection;
    private Channel publishChannel;
    private BettingAdaptor bettingAdaptor;
    private RecyclerView bettingRV;
    // Arraylist for storing data
    private ArrayList<BettingTile> BettingTileArrayList;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.betting_activity);
        bettingRV = findViewById(R.id.idBettingList);
        Log.e("testing", "Loaded betting activity");
        // Here we have created new array list and added data to it.
        // Likely from a local DB for production or a rest request for the state of the event
        // for this its going to be fixed to start with
        // the Ably connection will put data on top of this
        BettingTileArrayList = new ArrayList<>();
        BettingTileArrayList.add(new BettingTile("Car 1", null, 0.125f));
        BettingTileArrayList.add(new BettingTile("Car 2", null, 0.125f));
        BettingTileArrayList.add(new BettingTile("Car 3", null, 0.125f));
        BettingTileArrayList.add(new BettingTile("Car 4", null, 0.125f));
        BettingTileArrayList.add(new BettingTile("Car 5", null, 0.125f));
        BettingTileArrayList.add(new BettingTile("Car 6", null, 0.125f));
        BettingTileArrayList.add(new BettingTile("Car 7", null, 0.125f));

        // we are initializing our adapter class and passing our arraylist to it.
        bettingAdaptor = new BettingAdaptor(this, BettingTileArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        gson = new Gson();

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        bettingRV.setLayoutManager(linearLayoutManager);
        bettingRV.setAdapter(bettingAdaptor);
        bettingAdaptor.setClickListener(this);

        try {
            initAbly();
        } catch (AblyException e) {
            e.printStackTrace();
        }
    }

    //Here we are initialising the realtime listener so that we can get the live odds changes back
    //We have enabled in flight delta compression to reduce the bandwidth consumed and improve over the wire latency over mobile networks
    //We're Also entering presence on the inbound channel so we can track the user. The data field is the "page name"
    private void initAbly() throws AblyException {

        //IMPORTANT NOTE: this should be done via token auth in a real application. see https://ably.com/documentation/core-features/authentication#token-authentication
        rtConnection = new AblyRealtime("YOUR KEY HERE");
        Map<String, String> params = new HashMap<>();
        params.put("delta", "vcdiff");
        ChannelOptions options = new ChannelOptions();
        options.params = params;
        Channel channel = rtConnection.channels.get("outbound:odds:event1",options);
        channel.subscribe(new Channel.MessageListener() {
            @Override
            public void onMessage(Message message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //using a bit of reflection and RTTI we can get this inbound data into the array we need in a few simple lines
                        Type BettingTileList = new TypeToken<ArrayList<BettingTile>>(){}.getType();
                        BettingTileArrayList.clear();
                        List<BettingTile> myNewList= gson.fromJson((JsonArray)message.data, BettingTileList);
                        BettingTileArrayList.addAll(myNewList);
                        bettingAdaptor.notifyDataSetChanged();

                    }
                });
            }
        });

        publishChannel = rtConnection.channels.get("inbound:betting:catagory:username");
        //Entering presence allows the backend to know what a customer app is doing. it can be updated as an when needed.
        //with some addition messages this can give you a full analyitcs breakdown of what users are doing in your application
        publishChannel.presence.enterClient("user_ID","Car Racing Page");
    }

    //simplified Betting Function, would more properly have a dialogue to confirm.
    @Override
    public void onClick(View view, int position) {
        try {
            // This submits the bet and the odds to the channel.
            // This allows us to show odds drift + risk differences,
            // as even with the fastest odds, the internet still includes delays
            publishChannel.publish("My-User-Name", gson.toJson(BettingTileArrayList.get(position)));
        } catch (AblyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(isFinishing())
        {
            try {
                // This is part of how we catch it when users leave the betting page. presence also times out if a user just disconnects entirely
                // Additional data can be sent with this message, like time on page or anything else.
                publishChannel.presence.leave(new CompletionListener() {
                    @Override
                    public void onSuccess() {
                        // Successfully left presence
                    }

                    @Override
                    public void onError(ErrorInfo reason) {
                        // Failed some how
                    }
                });
            } catch (AblyException e) {
                e.printStackTrace();
            }
        }
    }
}