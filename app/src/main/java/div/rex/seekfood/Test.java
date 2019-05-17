package div.rex.seekfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class Test extends AppCompatActivity {
    private static final String TAG = "TogetherChatActivity";
//    //    private static final String SERVER_URI = "ws://10.0.2.2:8081/WebSocketChatWeb/TogetherWS/";
    private static final String SERVER_URIpos = "ws://172.20.10.3:8081/CA107G3phone/MemberWS/M000001";
////                                            ws://localhost:8081/WebSocketChatWeb/TogetherWS


    private MyWebSocketClient myWebSocketClient;
    private URI uri;
    private NotificationManager manager;

    private class MyWebSocketClient extends WebSocketClient {


        MyWebSocketClient(URI serverURI) {
            // Draft_17是連接協議，就是標準的RFC 6455（JSR356）
            super(serverURI, new Draft_17());
        }

        @Override
        public void onOpen(ServerHandshake handshakeData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeConnectStatus(true);
                }
            });
            String text = String.format(Locale.getDefault(),
                    "onOpen: Http status code = %d; status message = %s",
                    handshakeData.getHttpStatus(),
                    handshakeData.getHttpStatusMessage());

            Log.d(TAG, text);
        }

        @Override
        public void onMessage(final String message) {
            Log.d(TAG, "onMessage2: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
//                        String userName = jsonObject.get("M0000002").toString();
                        createNotification();

//                        if (userName != null) {
//                            AlertFragment alertFragment = new AlertFragment();
//                            FragmentManager fm = getSupportFragmentManager();
//                            alertFragment.show(fm, "alert");
//                        }
//                        tvMessage.append(jsonObject.toString());
//                        scrollView.fullScroll(View.FOCUS_DOWN);
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeConnectStatus(false);
                }
            });
            String text = String.format(Locale.getDefault(),
                    "code = %d, reason = %s, remote = %b",
                    code, reason, remote);
            Log.d(TAG, "onClose: " + text);
        }

        @Override
        public void onError(Exception ex) {
            Log.d(TAG, "onError: exception = " + ex.toString());
        }


    }

    private void createNotification() {
        Intent intent = new Intent(Test.this, NotiActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "從通知訊息切換過來的");
        bundle.putString("content", "老師在你背後，他很火！");
        intent.putExtras(bundle);

        /*
            Intent指定好要幹嘛後，就去做了，如startActivity(intent);
            而PendingIntent則是先把某個Intent包好，以後再去執行Intent要幹嘛
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(Test.this
                , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = "id";
        String CHANNEL_NAME = "name";



//        PendingIntent pendingIntent2 = PendingIntent.getActivity(
//                Test.this, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

//        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
//                android.R.drawable.ic_menu_share, "Go!", pendingIntent2
//        ).build();

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        Notification notification = builder
                // 訊息面板的標題
                .setContentTitle("提醒您!!候位快要到了喔~~")
                // 訊息面板的內容文字
                // 訊息的小圖示
                .setSmallIcon(R.drawable.ic_secret_notification)
                // 訊息的大圖示
                .setLargeIcon(bitmap)
                // 使用者點了之後才會執行指定的Intent
                .setContentIntent(pendingIntent)
                // 加入音效
                .setSound(soundUri)
                // 點擊後會自動移除狀態列上的通知訊息
                .setAutoCancel(true)
                // 加入狀態列下拉後的進一步操作
//                .addAction(action)
                .build();

        manager.notify(1, notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        try {
//            uri = new URI(SERVER_URI + userName);
            uri = new URI(SERVER_URIpos);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        myWebSocketClient = new MyWebSocketClient(uri);
        myWebSocketClient.connect();
    }


    /* 依照連線狀況改變按鈕enable狀態 */
    private void changeConnectStatus(boolean isConnected) {
        if (isConnected) {
//            btSend.setEnabled(true);

//            showToast(R.string.text_Connect);
        } else {
//            btSend.setEnabled(false);

//            showToast(R.string.text_Disconnect);
        }

    }




    private void showToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }
}







