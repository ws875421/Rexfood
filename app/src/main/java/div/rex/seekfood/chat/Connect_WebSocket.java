package div.rex.seekfood.chat;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;


import div.rex.seekfood.main.Util;

import static div.rex.seekfood.main.Util.SERVER_URI;


public class Connect_WebSocket {
    private final static String TAG = "Util";

    public static ChatWebSocketClient chatWebSocketClient;

    // 建立ChatWebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (chatWebSocketClient == null) {
            chatWebSocketClient = new ChatWebSocketClient(uri, context);
            chatWebSocketClient.connect();      //連線
        }
    }

    // 中斷ChatWebSocket連線
    public static void disconnectServerChat() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
    }


    public static String getUserName() {


        String userName = Util.getSharePreAccount().getString("mem_no", " ");
        return userName;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }


}
