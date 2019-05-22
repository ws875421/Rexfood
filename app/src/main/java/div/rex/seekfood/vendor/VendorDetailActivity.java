package div.rex.seekfood.vendor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import div.rex.seekfood.R;
import div.rex.seekfood.fav_res.Fav_ResVo;
import div.rex.seekfood.main.Holder;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.member.MemberLogin;
import div.rex.seekfood.task.CommonTask;
import div.rex.seekfood.task.ImageTask;
import div.rex.seekfood.task.ImageTask2;
import div.rex.seekfood.wait_pos.ShowPos;
import div.rex.seekfood.wait_pos.WaitPosActivity;

import static div.rex.seekfood.main.Util.showToast;


public class VendorDetailActivity extends AppCompatActivity {

    private static final String TAG = "aaaaaaa";
    private List<vendorImages> vendorImagesList;
    private ImageTask vendorImageTask;
    private ImageTask2 vendorImageTask2;
    private VendorVO vendorVO;
    private TextView tv_vName, tv_detail;
    private Button addfav;
    private CommonTask addTask, initTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendordetail);
        findView();

        vendorVO = (VendorVO) this.getIntent().getSerializableExtra("vendorVO");
        if (vendorVO == null) {
            showToast(this, "查無餐廳資訊");
        }


        vendorImagesList = getvendorImagesList();

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new vendorImagesAdapter(getSupportFragmentManager(), vendorImagesList));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

//查菜單
        StringBuffer sb = new StringBuffer();
        if (Util.networkConnected(this)) {
            try {
                String vendor_no = vendorVO.getVendor_no();
                String url = Util.URL + "vendor/vendor.ad";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getmenu");
                jsonObject.addProperty("vendor_no", vendor_no);
                String jsonOut = jsonObject.toString();
                initTask = new CommonTask(url, jsonOut);
                String jsonIn = initTask.execute().get();
                Gson gson = new Gson();
                JsonArray JsonArray = gson.fromJson(jsonIn, JsonArray.class);

                int i = 0;
//                sb.append("菜單:" + "\n");
                for (JsonElement e : JsonArray) {
                    if (i == 0) {
                        sb.append("菜單:" + "\n");
                    }
                    JsonObject jsonObject2 = e.getAsJsonObject();
                    jsonObject2.get("menu_name").getAsString();
                    sb.append(jsonObject2.get("menu_name").getAsString() + "\n");
                    i++;
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        }


        tv_vName.setText(vendorVO.getV_name());
        tv_detail.setText("地址: " + vendorVO.getV_address1() + "" + vendorVO.getV_address2() + "" + vendorVO.getV_address3() + "\n"
                + "電話: (" + vendorVO.getV_n_code() + ")" + vendorVO.getV_tel() + "\n"
                + "餐廳類型: " + vendorVO.getV_type() + "\n"
                + "營業時間: " + vendorVO.getV_start_time() + " - " + vendorVO.getV_end_time() + "\n"
                + "店家特色: " + vendorVO.getV_text() + "\n" + sb.toString().trim()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        List<VendorVO> favlist = null;
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        //判斷是否登入
        boolean islogin = preferences.getBoolean("login", false);
        String mem_no = preferences.getString("mem_no", "");
        String vendor_no = vendorVO.getVendor_no();

        if (islogin && !(mem_no.isEmpty())) {

            if (Util.networkConnected(this)) {
                try {
                    String url = Util.URL + "fav_res/fav_res.ad";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "isFav");
                    jsonObject.addProperty("mem_no", mem_no);
                    jsonObject.addProperty("vendor_no", vendor_no);
                    String jsonOut = jsonObject.toString();
                    initTask = new CommonTask(url, jsonOut);
                    String jsonIn = initTask.execute().get();
                    Type listType = new TypeToken<List<Fav_ResVo>>() {
                    }.getType();
                    favlist = new Gson().fromJson(jsonIn, listType);


                    if (favlist.size() > 0) {
                        addfav.setText("取消收藏");
                    } else {

                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }
        }

    }


    private void findView() {
        tv_vName = findViewById(R.id.tv_vName);
        tv_detail = findViewById(R.id.tv_detail);
        addfav = findViewById(R.id.addfav);

    }

    //加入收藏
    public void Onaddfav(View view) {
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        //判斷是否登入
        boolean islogin = preferences.getBoolean("login", false);
        String mem_no = preferences.getString("mem_no", "");
        String vendor_no = vendorVO.getVendor_no();
        if (!islogin) {

            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
        } else if (islogin && mem_no.length() == 7) {
//已登入
            addRes(mem_no, vendor_no);
        }

    }

    private void addRes(String mem_no, String vendor_no) {

        if (Util.networkConnected(this) && addfav.getText().equals("加入收藏")) {
            String url = Util.URL + "fav_res/fav_res.ad";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "addfav");
            jsonObject.addProperty("mem_no", mem_no);
            jsonObject.addProperty("vendor_no", vendor_no);
            String jsonOut = jsonObject.toString();
            addTask = new CommonTask(url, jsonOut);
            try {
                String result = addTask.execute().get();

                if (Integer.parseInt(result) > 0) {
                    addfav.setText("取消收藏");
                } else {
                    showToast(this, "此餐廳已加入收藏");
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else if (Util.networkConnected(this) && addfav.getText().equals("取消收藏")) {

            String url = Util.URL + "fav_res/fav_res.ad";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "del");
            jsonObject.addProperty("mem_no", mem_no);
            jsonObject.addProperty("vendor_no", vendor_no);
            String jsonOut = jsonObject.toString();
            addTask = new CommonTask(url, jsonOut);
            try {
                String result = addTask.execute().get();

                if (Integer.parseInt(result) > 0) {
                    addfav.setText("加入收藏");
                } else {
                    showToast(this, "發生異常");
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }


        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

    }


    //候位
    public void OnWaitPos(View view) {

        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        //判斷是否登入
        boolean islogin = preferences.getBoolean("login", false);
        String mem_no = preferences.getString("mem_no", "");

        String vendor_no = vendorVO.getVendor_no();
        if (!islogin) {

            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
            return;
        }

        String posVendor = preferences.getString("posVendor", "");
        Integer party_size = preferences.getInt("party_size", 0);

        if (posVendor.equals(vendor_no)) {
            showToast(Holder.getContext(), "您已經在候位了");
            Intent intent = new Intent(VendorDetailActivity.this, ShowPos.class);
            intent.putExtra("vendorVO", vendorVO);
            intent.putExtra("party_size", party_size);
            finish();
            startActivity(intent);
        } else {
            finish();
            Intent intent = new Intent(VendorDetailActivity.this, WaitPosActivity.class);
            intent.putExtra("vendorVO", vendorVO);
            intent.putExtra("mem_no", mem_no);
            startActivity(intent);
        }
    }


    //登入會員提示
    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setMessage("請登入會員")
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton("登入會員", this)
                    //設定取消鍵 (negative用於取消)
                    .setNegativeButton("取消", this)
                    .create();
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(this.getContext(), MemberLogin.class);
                    getActivity().finish();
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }
    }


    private List<vendorImages> getvendorImagesList() {

        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        String url = Util.URL + "vendor/vendor.ad";
        String url2 = Util.URL + "vendor/vendor2.ad";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;

        vendorImageTask = new ImageTask(url, "vendor_no", vendorVO.getVendor_no(), imageSize);
        vendorImageTask2 = new ImageTask2(url2, "vendor_no", vendorVO.getVendor_no(), imageSize);

        try {
            bitmap = vendorImageTask.execute().get();
            bitmap2 = vendorImageTask2.execute().get();
        } catch (ExecutionException e) {
            Log.e(TAG, e.toString());
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }


        List<vendorImages> vendorImagesList = new ArrayList<>();
        vendorImagesList.add(new vendorImages(bitmap, "餐廳圖片"));
        vendorImagesList.add(new vendorImages(bitmap2, "餐廳活動"));

        return vendorImagesList;
    }

    private class vendorImagesAdapter extends FragmentPagerAdapter {
        private List<vendorImages> vendorImagesList;

        public vendorImagesAdapter(FragmentManager fm, List<vendorImages> vendorImagesList) {
            super(fm);

            this.vendorImagesList = vendorImagesList;
        }

        @Override
        public Fragment getItem(int i) {
            return vendorImagesFragment.newInstance(vendorImagesList.get(i));
        }

        @Override
        public int getCount() {
            return vendorImagesList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return vendorImagesList.get(position).getTitle();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        if (vendorImageTask2 != null) {
            vendorImageTask2.cancel(true);
        }
        if (vendorImageTask != null) {
            vendorImageTask.cancel(true);
        }

    }

}
