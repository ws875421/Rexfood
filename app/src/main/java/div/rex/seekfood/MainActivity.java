package div.rex.seekfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import div.rex.seekfood.chat.FriendsActivity;
import div.rex.seekfood.fav_res.GetAllFavActivity;
import div.rex.seekfood.main.Holder;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.member.MemberLogin;
import div.rex.seekfood.ord.FindOrdActicity;
import div.rex.seekfood.task.CommonTask;
import div.rex.seekfood.task.ImageTask;
import div.rex.seekfood.vendor.VendorLogin;
import div.rex.seekfood.vendor.VendorlistFragment;
import div.rex.seekfood.wait_pos.ShowPos;

import static div.rex.seekfood.main.Util.SERVER_POSURI;
import static div.rex.seekfood.main.Util.showToast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = "fragment";
    private final static String TAG2 = "push";

    private CommonTask isOrdTask;
    private TextView tvLogName, tvLogDetal;
    private ImageView ivmember;
    private ImageTask memberImageTask, vendorImageTask;
    private static final String PACKAGE = "com.google.zxing.client.android";

    //提醒
    private MyWebSocketClient myWebSocketClient;
    private URI uri;
    private NotificationManager manager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =
            new BottomNavigationView.OnNavigationItemSelectedListener() {


                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();

                            FragmentHome vendorhome = new FragmentHome();
                            Bundle bundle = new Bundle();
                            vendorhome.setArguments(bundle);

                            transaction.replace(R.id.frameHome, vendorhome, TAG);
                            transaction.commit();
                            return true;

                        case R.id.navigation_dashboard:

                            //收尋
//                            FragmentManager manager3 = getSupportFragmentManager();
//                            FragmentTransaction transaction3 = manager3.beginTransaction();
//
//                            VendorSearch vendorSearch = new VendorSearch();
//                            Bundle bundle3 = new Bundle();
//                            vendorSearch.setArguments(bundle3);
//
//                            transaction3.replace(R.id.frameHome2, vendorSearch, TAG);
//                            transaction3.commit();


                            //餐廳列表
                            FragmentManager manager2 = getSupportFragmentManager();
                            FragmentTransaction transaction2 = manager2.beginTransaction();

                            VendorlistFragment vendorlist = new VendorlistFragment();
                            Bundle bundle2 = new Bundle();
                            vendorlist.setArguments(bundle2);

                            transaction2.replace(R.id.frameHome, vendorlist, TAG);
                            transaction2.commit();


                            return true;

                    }
                    return false;
                }
            };
    //推播

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

            Log.d(TAG2, text);
        }

        @Override
        public void onMessage(final String message) {
            Log.d(TAG2, "onMessage2: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(message);


                        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                        String mem_no = preferences.getString("mem_no", "");
                        String posVendor = preferences.getString("posVendor", "");
                        String pushstatus = preferences.getString("pushstatus", "");
                        String notic = preferences.getString("notic", "");
                        String WGroup = preferences.getString("WGroup", "");

                        String num = jsonObject.get(mem_no).toString();

                        if ((Integer.parseInt(WGroup)) + 3 == Integer.parseInt(num)) {
                            showToast(Holder.getContext(), "因為未報到您的候位號碼自動往後3位!!");
                        }

//                        showToast(Holder.getContext(),num);
                        preferences.edit()
                                .putString("WGroup", num)       //候位號碼牌
                                .apply();

                        if (posVendor == null) {
                            return;
                        }

//                        createNotification();

                        if (num.equals(notic) && pushstatus.equals("yes")) {
                            createNotification();
                        }
                        if (Integer.parseInt(num) == 0) {
                            createNotification2();
                        }


                    } catch (JSONException e) {
                        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                        preferences.edit()
                                .putString("posVendor", "")   //候位餐廳
                                .putString("PosNum", "")       //候位號碼牌
                                .putString("WGroup", "")      //前方還有幾組
                                .putInt("party_size", 0)      //候位人數
                                .putString("pushstatus", "")
                                .putString("notic", "")
                                .apply();
                        showToast(Holder.getContext(), "因未報到您的候位被取消了!!");
                        Log.e(TAG2, e.toString());
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
            Log.d(TAG2, "onClose: " + text);
        }

        @Override
        public void onError(Exception ex) {
            Log.d(TAG2, "onError: exception = " + ex.toString());
        }
    }

    private void createNotification() {
        String CHANNEL_ID = "id";
        String CHANNEL_NAME = "name";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
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
                .setContentText("候位快要到了喔~~")
                // 訊息的小圖示
                .setSmallIcon(R.drawable.ic_secret_notification)
                // 訊息的大圖示
                .setLargeIcon(bitmap)
                // 使用者點了之後才會執行指定的Intent
//                .setContentIntent(pendingIntent)
                // 加入音效
                .setSound(soundUri)
                // 點擊後會自動移除狀態列上的通知訊息
                .setAutoCancel(true)
                // 加入狀態列下拉後的進一步操作
//                .addAction(action)
                .build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        manager.notify(1, notification);
    }

    private void createNotification2() {
        String CHANNEL_ID = "id";
        String CHANNEL_NAME = "name";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
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
                .setContentTitle("目前候位到您了! ")
                // 訊息面板的內容文字
                .setContentText("請在10分鐘內前往驗證!!")
                // 訊息的小圖示
                .setSmallIcon(R.drawable.ic_secret_notification)
                // 訊息的大圖示
                .setLargeIcon(bitmap)
                // 使用者點了之後才會執行指定的Intent
//                .setContentIntent(pendingIntent)
                // 加入音效
                .setSound(soundUri)
                // 點擊後會自動移除狀態列上的通知訊息
                .setAutoCancel(true)
                // 加入狀態列下拉後的進一步操作
//                .addAction(action)
                .build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        manager.notify(1, notification);
    }

    private void changeConnectStatus(boolean isConnected) {
        if (isConnected) {
//            btSend.setEnabled(true);

//            showToast(R.string.text_Connect);
        } else {
//            btSend.setEnabled(false);

//            showToast(R.string.text_Disconnect);
        }

    }

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, FirseActivity.class);
        startActivity(intent);
        findView();
        Holder.initial(this);


//推播
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


//        showToast(Holder.getContext(),Util.getSharePreAccount().getString("mem_name", " "));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // 讓Drawer開關出現三條線
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //######


    }

    private void findView() {

        tvLogName = findViewById(R.id.tvLogName);
        tvLogDetal = findViewById(R.id.tvLogDetal);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        NavigationView navigationView = findViewById(R.id.nav_view);
        switch (item.getItemId()) {
            case R.id.nav_member://會員登入
                Intent intent = new Intent(MainActivity.this, MemberLogin.class);
                startActivity(intent);
                break;
            case R.id.nav_memberOut://會員登出

                if (myWebSocketClient != null) {
                    myWebSocketClient.close();
                }

                preferences.edit()
                        .putBoolean("login", false)
                        .putString("mem_no", " ")
                        .putString("mem_name", " ")
                        .putString("member_accout", " ")
                        .putString("mem_gender", " ")
                        .putString("mem_mail", " ")
                        .putString("mem_id", " ")
                        .putString("mem_tel", " ")
                        .putString("mem_status", " ")
                        .putString("mem_balance", " ")
                        .putString("mem_nickname", " ")
                        .apply();

                //清除資料
                tvLogName = findViewById(R.id.tvLogName);
                tvLogDetal = findViewById(R.id.tvLogDetal);
                ivmember = findViewById(R.id.ivmember);

                //切換清單
                navigationView.getMenu().clear();
                tvLogName.setText("歡迎來到SeekFood");
                tvLogDetal.setText("請先登入會員");
                ivmember.setImageResource(R.drawable.baseline);
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                break;


            case R.id.nav_vendor://廠商登入
                Intent intent2 = new Intent(MainActivity.this, VendorLogin.class);
                startActivity(intent2);
                break;
            case R.id.nav_vendorOut://廠商登出

                preferences.edit()
                        .putBoolean("login", false)
                        .putString("vendor_no", " ")
                        .putString("v_account", " ")
                        .putString("v_name", " ")
                        .putString("v_type", " ")
                        .apply();
//
//
//
//                //清除資料
                tvLogName = findViewById(R.id.tvLogName);
                tvLogDetal = findViewById(R.id.tvLogDetal);
                ivmember = findViewById(R.id.ivmember);
//
                tvLogName.setText("歡迎來到SeekFood");
                tvLogDetal.setText("請先登入會員");
                ivmember.setImageResource(R.drawable.baseline);
                //切換清單
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                break;

            case R.id.nav_like:        //收藏
                Intent intent3 = new Intent(MainActivity.this, GetAllFavActivity.class);
                startActivity(intent3);

                break;
            case R.id.nav_chat:        //聊天

                Intent intent4 = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent4);


                // 切換Fragment或是Activity
                break;
            case R.id.nav_scanner:     //QRcode 掃描

                Intent intent5 = new Intent("com.google.zxing.client.android.SCAN");
                try {
                    startActivityForResult(intent5, 0);
                }
                // 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
                catch (ActivityNotFoundException ex) {
                    showDownloadDialog();
                }
                break;
            case R.id.nav_findorder:    //訂單查詢

                Intent intent6 = new Intent(MainActivity.this, FindOrdActicity.class);
                startActivity(intent6);

                break;
            case R.id.nav_wpos:
                Intent intent7 = new Intent(MainActivity.this, ShowPos.class);
                startActivity(intent7);
                break;


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //QR 掃描
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");

                if (Util.networkConnected(this)) {
                    String url = Util.URL + "ord/ord.do";
                    String result = null;
                    isOrdTask = new CommonTask(url, contents);
                    try {

                        result = isOrdTask.execute().get();


                        if (result.equals("true")) {
                            showToast(Holder.getContext(), "驗證成功");
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("驗證結果")
                                    .setMessage("驗證成功")
                                    .show();
                        } else {
                            showToast(Holder.getContext(), "驗證失敗");
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("驗證結果")
                                    .setMessage("驗證失敗!!")
                                    .show();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
//                    showToast(this, R.string.msg_NoNetwork);
                }


//                Intent intent6 = new Intent(MainActivity.this, scannerActivity.class);
//                intent6.putExtra("contents", contents);
//                startActivity(intent6);


            } else if (resultCode == RESULT_CANCELED) {


                showToast(Holder.getContext(), "Scan was Cancelled!");

            }
        }
    }


    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle("No Barcode Scanner Found");
        downloadDialog.setMessage("Please download and install Barcode Scanner!");
        downloadDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }


    //畫面返回
    @Override
    public void onResume() {
        super.onResume();


//----------------------------------------------------------------------
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        FragmentHome vendorlist = new FragmentHome();
        Bundle bundle = new Bundle();
        vendorlist.setArguments(bundle);

        transaction.replace(R.id.frameHome, vendorlist, TAG);
        transaction.commit();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//判斷是否登入 & 登入廠商 OR 會員
        boolean islogin = preferences.getBoolean("login", false);
        String mem_no = preferences.getString("mem_no", "");
        String vendor_no = preferences.getString("vendor_no", "");
        Bitmap bitmap = null;

        if (!islogin) {
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            try {
                tvLogName = findViewById(R.id.tvLogName);
                tvLogDetal = findViewById(R.id.tvLogDetal);
                ivmember = findViewById(R.id.ivmember);

                tvLogName.setText("歡迎來到SeekFood");
                tvLogDetal.setText("請先登入會員");
                ivmember.setImageResource(R.drawable.baseline);
            } catch (NullPointerException e) {
                return;
            }

        } else if (islogin && mem_no.length() >= 7) {
            navigationView.inflateMenu(R.menu.activity_main_login);
            String mem_name = preferences.getString("mem_name", " ");
            String mem_balance = preferences.getString("mem_balance", " ");


///推播websocket
            if (mem_no != null) {
                try {
//            uri = new URI(SERVER_URI + userName);
                    uri = new URI(SERVER_POSURI + mem_no);
                } catch (URISyntaxException e) {
                    Log.e(TAG, e.toString());
                }
                myWebSocketClient = new MyWebSocketClient(uri);
                myWebSocketClient.connect();
            }
///

            String url = Util.URL + "member/member.do";
            int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            try {

                tvLogName = findViewById(R.id.tvLogName);
                tvLogDetal = findViewById(R.id.tvLogDetal);
                ivmember = findViewById(R.id.ivmember);

                tvLogName.setText(mem_name + "   先生/小姐");
                tvLogDetal.setText("餘額:" + mem_balance + "   元");

                //會員頭像
                memberImageTask = new ImageTask(url, "mem_no", mem_no, imageSize);

                bitmap = memberImageTask.execute().get();
                if (bitmap != null) {
                    ivmember.setImageBitmap(bitmap);
                } else {
                    ivmember.setImageResource(R.drawable.default_image);
                }

            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
                return;
            }

        } else if (islogin && vendor_no.length() >= 7) {

            navigationView.inflateMenu(R.menu.activity_main_rest);
            String v_name = preferences.getString("v_name", " ");
            String v_type = preferences.getString("v_type", " ");
            String url = Util.URL + "vendor/vendor.do";
            int imageSize = getResources().getDisplayMetrics().widthPixels / 4;

            try {
                tvLogName = findViewById(R.id.tvLogName);
                tvLogDetal = findViewById(R.id.tvLogDetal);
                ivmember = findViewById(R.id.ivmember);

                tvLogName.setText("店名 :" + v_name);
                tvLogDetal.setText("餐廳類型: " + v_type);

                //廠商頭像
                vendorImageTask = new ImageTask(url, "vendor_no", vendor_no, imageSize);
                bitmap = vendorImageTask.execute().get();
                if (bitmap != null) {
                    ivmember.setImageBitmap(bitmap);
                } else {
                    ivmember.setImageResource(R.drawable.default_image);
                }

            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
                return;
            }
        }
    }


    /**
     * 離開上一頁設定
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
//                    .setIcon(R.drawable.alert)
//                    .setTitle("This is Dialog")
                    //設定訊息內容
                    .setMessage(R.string.message_alert)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.text_btYes, this)
                    //設定取消鍵 (negative用於取消)
                    .setNegativeButton(R.string.text_btNo, this)
                    .create();
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    getActivity().finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }
    }


    public static class AlertFragment2 extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
//                    .setIcon(R.drawable.alert)
//                    .setTitle("This is Dialog")
                    //設定訊息內容
                    .setMessage("驗證成功")
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton("完成", this)
                    //設定取消鍵 (negative用於取消)
//                    .setNegativeButton(R.string.text_btNo, this)
                    .create();
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    getActivity().finish();
                    break;
//                case DialogInterface.BUTTON_NEGATIVE:
//                    dialog.cancel();
//                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        if (memberImageTask != null) {
            memberImageTask.cancel(true);
        }
        if (vendorImageTask != null) {
            vendorImageTask.cancel(true);
        }

    }
}
