package div.rex.seekfood.main;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class Util {
    // 模擬器連Tomcat

    //    public static String URL = "http://10.0.2.2:8081/BookStoreWeb/";
//    public static String URL = "http://192.168.1.92:8081/BookStoreWeb/";
//    public static String URL = "http://172.20.10.3:8081/CA107G3phone/";
    public static String URL = "http://172.20.10.3:8081/CA107G3phone/";
    public static final String SERVER_URI = "ws://172.20.10.3:8081/CA107G3phone/FriendWS/";

    public static final String SERVER_POSURI = "ws://172.20.10.3:8081/CA107G3phone/MemberWS/";
//    public static String URL = "http://10.0.2.2:8081/CA107G3phone/";
//    public static final String SERVER_URI = "ws://10.0.2.2:8081/CA107G3phone/FriendWS/";


    // 偏好設定檔案名稱
    public final static String PREF_FILE = "preference";

    public static SharedPreferences getSharePreAccount() {
        return Holder.getContext().getSharedPreferences(Util.PREF_FILE, Context.MODE_PRIVATE);
    }


    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }


    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


//            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
//                MODE_PRIVATE);
//        boolean login = preferences.getBoolean("login", false);


}
