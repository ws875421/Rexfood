package div.rex.seekfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.concurrent.ExecutionException;

import div.rex.seekfood.fav_res.GetAllFavActivity;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.member.MemberLogin;
import div.rex.seekfood.task.ImageTask;
import div.rex.seekfood.vendor.VendorLogin;
import div.rex.seekfood.vendor.VendorlistFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = "fragment";


    private TextView tvLogName, tvLogDetal;
    private ImageView ivmember;
    private ImageTask memberImageTask, vendorImageTask;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


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

                    FragmentManager manager2 = getSupportFragmentManager();
                    FragmentTransaction transaction2 = manager2.beginTransaction();

                    VendorlistFragment vendorlist = new VendorlistFragment();
                    Bundle bundle2 = new Bundle();
                    vendorlist.setArguments(bundle2);

                    transaction2.replace(R.id.frameHome, vendorlist, TAG);
                    transaction2.commit();


                    return true;

                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, FirseActivity.class);
        startActivity(intent);
        findView();


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

            case R.id.nav_like:
                Intent intent3 = new Intent(MainActivity.this, GetAllFavActivity.class);
                startActivity(intent3);

                break;
            case R.id.nav_gallery:
                // 切換Fragment或是Activity
                break;
            case R.id.nav_manage:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //畫面返回
    @Override
    public void onResume() {
        super.onResume();


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
                    .setIcon(R.drawable.alert)
                    .setTitle("This is Dialog")
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
