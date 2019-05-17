package div.rex.seekfood.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;

import static div.rex.seekfood.chat.Connect_WebSocket.getUserName;


public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private LocalBroadcastManager broadcastManager;
    private EditText etMessage;
    private String friendId, friendName;
    private RecyclerView rvChat;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private CommonTask getFriendTask;
    //  friendId        自己
    //  friendName      接收

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        findViews();



        LinearLayoutManager lm = new LinearLayoutManager(this);

        rvChat.setLayoutManager(lm);
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerChatReceiver();
        // 取得前頁傳來的聊天對象
        friendId = getIntent().getStringExtra("friendId");
        friendName = getIntent().getStringExtra("friendName");

//        showToast(Holder.getContext(),"friendId:"+friendId);
//        showToast(Holder.getContext(),"friendName:"+friendName);

        Connect_WebSocket.connectServer(this, getUserName());


    }

    private void findViews() {
        etMessage = findViewById(R.id.etMessage);
        rvChat = findViewById(R.id.rvChat);
    }

    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");

        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, chatFilter);

    }

    // 接收到聊天訊息會在TextView呈現
    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);

            String sender = chatMessage.getSender();
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView

            if (sender.equals(friendName)) {
                chatMessages.add(chatMessage);
                rvChat.setAdapter(new ChatMessageAdapter());
                rvChat.getAdapter().notifyDataSetChanged();
                rvChat.scrollToPosition(chatMessages.size()-1);
            }
        }
    }



    // user點擊發送訊息按鈕
    public void clickSend(View view) {
        String message = etMessage.getText().toString();
        if (message.trim().isEmpty()) {
            Connect_WebSocket.showToast(this, "請輸入訊息");
            return;
        }
        String sender = getUserName();

        // 將輸入的訊息清空
        etMessage.setText(null);

        // 將欲傳送訊息轉成JSON後送出
        ChatMessage chatMessage = new ChatMessage("chat", sender, friendName, message);

        String chatMessageJson = new Gson().toJson(chatMessage);
        Connect_WebSocket.chatWebSocketClient.send(chatMessageJson);
        chatMessages.add(chatMessage);
        rvChat.setAdapter(new ChatMessageAdapter());
        rvChat.getAdapter().notifyDataSetChanged();
        rvChat.scrollToPosition(chatMessages.size()-1);
    }


    private class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {


        class ChatMessageViewHolder extends RecyclerView.ViewHolder {
            TextView tvMessageR,tvMessageL;
            CardView cardRight,cardLeft;

            ChatMessageViewHolder(View itemView) {
                super(itemView);
                tvMessageL = itemView.findViewById(R.id.tvMessageL);
                tvMessageR = itemView.findViewById(R.id.tvMessageR);
                cardRight = itemView.findViewById(R.id.cardRight);
                cardLeft = itemView.findViewById(R.id.cardLeft);
            }
        }
        @Override
        public ChatMessageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_message,parent,false);
            return new ChatMessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
            ChatMessage ctm = chatMessages.get(position);

            if(ctm.getSender().equals(friendName)){

//
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getName");
                jsonObject.addProperty("mem_no",  friendName);
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

//


                holder.tvMessageL.setText(jsonIn+"："+ctm.getMessage());
                holder.cardRight.setVisibility(View.GONE);
                holder.cardLeft.setVisibility(View.VISIBLE);
            }else{
                holder.tvMessageR.setText("我："+ctm.getMessage());
                holder.cardRight.setVisibility(View.VISIBLE);
                holder.cardLeft.setVisibility(View.GONE);
            }



        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }


    }
}
