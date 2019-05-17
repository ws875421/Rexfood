package div.rex.seekfood.chat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;





/*
 * 將websocket server上的所有user以RecyclerView列出。
 * 當server上的user連線或斷線時，ChatWebSocketClient都會發LocalBroadcast，
 * 此頁的BroadcastReceiver會接收到並在RecyclerView呈現。
 */

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendsActivity";

    private RecyclerView rvFriendsOnline, rvFriendsOffLine;
    private String user;
    private List<String> friendList;
    private List<Friend_ListVo> allMyFriend;
    private List<Friend_ListVo> MyFriendOnline = new LinkedList<>();
    private List<Friend_ListVo> MyFriendOffline = new LinkedList<>();
    private LocalBroadcastManager broadcastManager;
    Friend_ListVo fnVO = null;
    private CommonTask getFriendTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerFriendStateReceiver();
        // 取得user name
        user = Connect_WebSocket.getUserName();
        // 將標題設定成user name
        //setTitle("I am " + user);


//取回我的好友
        String mem_no = Util.getSharePreAccount().getString("mem_no", "");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getFriend");
        jsonObject.addProperty("mem_no", mem_no);
        String jsonOut = jsonObject.toString();
        getFriendTask = new CommonTask(Util.URL + "friend_list/friend_list.do", jsonOut);
//        List<Friend_ListVo> vendorList = null;
        String jsonIn = null;
        try {
            jsonIn = getFriendTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<List<Friend_ListVo>>() {
        }.getType();
        allMyFriend = new Gson().fromJson(jsonIn, listType);



        // 初始化聊天清單
        friendList = new LinkedList<>();
        // 初始化RecyclerView
        rvFriendsOnline = findViewById(R.id.rvFriendsOnline);
        rvFriendsOnline.setLayoutManager(new LinearLayoutManager(this));
        rvFriendsOnline.setAdapter(new FriendAdapter(this, "on"));

        //
        rvFriendsOffLine = findViewById(R.id.rvFriendsOffLine);
        rvFriendsOffLine.setLayoutManager(new LinearLayoutManager(this));
        rvFriendsOffLine.setAdapter(new FriendAdapter(this, "off"));

        //建立連線
        Connect_WebSocket.connectServer(this, user);
    }


    // 攔截user連線或斷線的Broadcast
    private void registerFriendStateReceiver() {
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        FriendStateReceiver friendStateReceiver = new FriendStateReceiver();
        broadcastManager.registerReceiver(friendStateReceiver, openFilter);
        broadcastManager.registerReceiver(friendStateReceiver, closeFilter);
    }

    // 攔截user連線或斷線的Broadcast，並在RecyclerView呈現
    private class FriendStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

//            showToast(Holder.getContext(),message);

            State stateMessage = new Gson().fromJson(message, State.class);
            String type = stateMessage.getType();
            String friend = stateMessage.getUser();

//            showToast(Holder.getContext(),friend);


//            String mem_no = Util.getSharePreAccount().getString("mem_no", "");
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getName");
//            jsonObject.addProperty("mem_no", mem_no);
//            String jsonOut = jsonObject.toString();
//            getFriendTask = new CommonTask(Util.URL + "friend_list/friend_list.do", jsonOut);
////        List<Friend_ListVo> vendorList = null;
//            String jsonIn = null;
//            try {
//                jsonIn = getFriendTask.execute().get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            showToast(Holder.getContext(),jsonIn);




            Log.e("type friend", type + friend);
            switch (type) {
                // 有user連線
                case "open":

                    // 如果是自己連線
                    if (friend.equals(user)) {
                        // 取得server上的所有user
                        friendList = new LinkedList<>(stateMessage.getUsers());
                        // 將自己從聊天清單中移除，否則會看到自己在聊天清單上
                        friendList.remove(user);
                        MyFriendOffline = new LinkedList<>(allMyFriend);
                        for (Friend_ListVo nvo : allMyFriend) {
                            for (String str : friendList) {

                                if (nvo.getFrie_no().equals(str)) {

                                    MyFriendOnline.add(nvo);
                                    MyFriendOffline.remove(nvo);
                                }
                            }
                        }
                    }

                    else {
                        // 如果其他user連線且尚未加入聊天清單，就加上

                        for (Friend_ListVo nvo : allMyFriend) {
                            if (nvo.getFrie_no().equals(friend)) {
                                MyFriendOnline.add(nvo);
                                MyFriendOffline.remove(nvo);
                            }
                        }

//                        if (!friendList.contains(friend)) {
//                            friendList.add(friend);
//                        }
                        Connect_WebSocket.showToast(FriendsActivity.this, friend + " is online");
                    }
                    // 重刷聊天清單
                    rvFriendsOnline.getAdapter().notifyDataSetChanged();
                    rvFriendsOffLine.getAdapter().notifyDataSetChanged();


                    break;
                // 有user斷線
                case "close":
                    // 將斷線的user從聊天清單中移除
                    for (Friend_ListVo nvo : allMyFriend) {
                        if (nvo.getFrie_no().equals(friend)) {
                            MyFriendOnline.remove(nvo);
                            MyFriendOffline.add(nvo);
                        }
                    }
                    rvFriendsOnline.getAdapter().notifyDataSetChanged();
                    rvFriendsOffLine.getAdapter().notifyDataSetChanged();
                    Connect_WebSocket.showToast(FriendsActivity.this, friend + " is offline");
            }
            Log.d(TAG, message);
        }

    }


    private class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
        Context context;
        String line;

        public FriendAdapter(Context context, String line) {
            this.context = context;
            this.line = line;
        }

        class FriendViewHolder extends RecyclerView.ViewHolder {
            TextView tvFriendName;

            FriendViewHolder(View itemView) {
                super(itemView);
                tvFriendName = itemView.findViewById(R.id.tvFrinedName);
            }
        }

        @Override
        public int getItemCount() {

            if (line.equals("on")) {
                return MyFriendOnline.size();
            } else {
                return MyFriendOffline.size();
            }

        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.card_friends, parent, false);
            return new FriendViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {

            if (line.equals("on")) {
                fnVO = MyFriendOnline.get(position);
                final String friendName = fnVO.getFrie_no();
                final String friendId = fnVO.getMem_no();
                // 點選聊天清單上的user即開啟聊天頁面
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FriendsActivity.this, ChatRoomActivity.class);
                        intent.putExtra("friendName", friendName);
                        intent.putExtra("friendId", friendId);
                        intent.putExtra("title", friendName);

                        startActivity(intent);
                    }
                });


            } else {
                fnVO = MyFriendOffline.get(position);
                holder.tvFriendName.setTextColor(Color.GRAY);
            }
//            showToast(Holder.getContext(),fnVO.getFrie_no());

            fnVO.getFrie_no();
//            String mem_no = Util.getSharePreAccount().getString("mem_no", "");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getName");
            jsonObject.addProperty("mem_no",  fnVO.getFrie_no());
            String jsonOut = jsonObject.toString();
            getFriendTask = new CommonTask(Util.URL + "friend_list/friend_list.do", jsonOut);
//        List<Friend_ListVo> vendorList = null;
            String jsonIn = null;
            try {
                jsonIn = getFriendTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            holder.tvFriendName.setText(jsonIn);

        }

    }

    // Activity結束即中斷WebSocket連線
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Connect_WebSocket.disconnectServerChat();
    }


}
